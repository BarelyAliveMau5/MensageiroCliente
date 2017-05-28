
/*
 * The MIT License
 *
 * Copyright 2017 BarelyAliveMau5.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mensageiro.socket;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensageiro.transferencia.Download;
import mensageiro.transferencia.Upload;
/**
 * @author BarelyAliveMau5
 */
public class ClienteSocket implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClienteSocket.class.getName());
    private final String usuario;            // nome do usuario atual
    private final Socket socket;
    private final ObjectInputStream entrada; // entrada dos dados vindos da rede
    private final ObjectOutputStream saida;  // saída dos dados vindos da rede
    private boolean executando;              // condição que mantem o loop que verifica os dados recebidos
    private final ArrayDeque<String> mensagens = new ArrayDeque<String>();
    private String ultimoUsuarioEntrado;
    private Upload upload;
    private String caminhoArquivoUpload;
    private Download download;
    private final static int TIMEOUT = 5000;
    // callbacks independentes de interface
    public Runnable onLoginOK;
    public Runnable onErroLogin;
    public Runnable onNovaMensagem;
    public Callable onSolicitTransfer;  // igual ao runnable mas com retorno
    public Runnable onDownloadIniciado; // se eu pudesse passar parametros, passaria o tipo de transferencia em 1 var
    public Runnable onUploadIniciado;
    public Runnable onListaUsuarios;
    public Runnable onUsuarioEntrou;
    public Runnable onUsuarioSaiu;
    public Runnable onErroConexao;
    
    /** 
     * @param addr endereço do servidor
     * @param porta porta do servidor
     * @param usuario nome que aparecerá em publico
     * @param senha se for nulo, a entrada será como convidado;
     * @param login faz o login automatico logo no construtor, caso contrario este deve ser chamado pelo metodo login
     * @throws java.io.IOException
     */
    public ClienteSocket(String addr,int porta, String usuario, char[] senha, boolean login) throws IOException {
        this.usuario = usuario;
        socket = new Socket();
        socket.connect(new InetSocketAddress(addr, porta), TIMEOUT);
        saida = new ObjectOutputStream(socket.getOutputStream());
        saida.flush();
        entrada = new ObjectInputStream(socket.getInputStream());
        if (login)
            login(senha);
        if (usuario.length() == 0)
            enviar(new Mensagem(Mensagem.Tipos.TESTE, usuario, "", ""));
    }

    public final void login(char[] senha) {  
        // o ideal seria que a senha fosse criptografada antes de virar string ou ser enviada. Ela fica visivel
        // na rede, e um Man-In-the-Middle consegue ver claramente a senha. Ou caso alguem faça dump da memória, 
        // ela estaria visivel no momento em que virou string antes de ser coletada pelo garbage collection
        if (senha != null)
            enviar(new Mensagem(Mensagem.Tipos.LOGIN, usuario, String.valueOf(senha), ""));
        else
            enviar(new Mensagem(Mensagem.Tipos.LOGIN, usuario, "", "")); // vazio = servidor
    }
    
    public String usuario(){
        return usuario;
    }
    
    public void upload (String caminhoArquivo, String destinatario) {
        caminhoArquivoUpload = caminhoArquivo;
        enviar(new Mensagem(Mensagem.Tipos.PEDIDO_TRANSFERENCIA, new File(caminhoArquivo).getName(), destinatario));
    }
    
    private void iniciarUpload(Mensagem msg) {
        try {
            String addr = msg.conteudo.split(":")[0];
            int porta = Integer.parseInt(msg.conteudo.split(":")[1]);
            upload = new Upload(addr, porta, new File(caminhoArquivoUpload));
            onUploadIniciado.run();
            upload.iniciar();
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.WARNING, "erro ao iniciar upload {0}", ex.toString());
        }
    }
    
    private void responderPedidoTransf(boolean aceitar, Mensagem msg) {
        String resp;
        if (aceitar) {
            download = new Download(new File("./" + msg.conteudo).getAbsoluteFile().toString());
            resp = String.valueOf(download.porta());
            onDownloadIniciado.run();
            download.iniciar();
        } else {
            resp = Mensagem.Resp.TRANSFERENCIA_NEGADA;
        } 
        enviar(new Mensagem(Mensagem.Tipos.RESP_TRANSFERENCIA, resp, msg.remetente));
    }
    
    private String formatarMsg(Mensagem msg) {
        if (msg.destinatario.equals(""))
            return msg.remetente + ": " + msg.conteudo;
        else if (msg.destinatario.equals(usuario))
            return msg.remetente + " [privado] : " + msg.conteudo;
        else
            LOGGER.warning("Mensagem encaminhada ao destinatario errado");
            return msg.remetente + " [wtf?] : " + msg.conteudo;
    }
    
    private String formatarMsgAnuncio(Mensagem msg, boolean entrando) {
        if (entrando) {
            return msg.conteudo + " acabou de entrar.";
        } else {
            return msg.conteudo + " acabou de sair.";
        }
    }
    
    public String proximaMsg() {
        return mensagens.poll();
    }
    
    private void novaMensagem(String msg) {
        mensagens.addLast(msg);
        tentarCallback(onNovaMensagem);
    }
    
    private void lidarComLogin(Mensagem msg) {
        try {
        if (msg.conteudo.equals(Mensagem.Resp.LOGIN_OK))
            onLoginOK.run();
        if (msg.conteudo.equals(Mensagem.Resp.LOGIN_FALHO)){
            onErroLogin.run();
            this.executando = false;
        }
        } catch (NullPointerException ex) {
            LOGGER.info("eventos onLogin não definidos");
        }
    }
    
    // gambiarra, eu sei. não são várias threads, cada chamada só armazena isso uma vez, porisso não tem problema.
    public String ultimoUsuarioEntrado() {
        return ultimoUsuarioEntrado;
    }
    
    public void logOut() {
        enviar(new Mensagem(Mensagem.Tipos.LOGOUT, ""));
        executando = false;
    }
    
    private void tentarCallback(Runnable callback) {
        try {
            callback.run();
        } catch (NullPointerException ex) { 
            LOGGER.log(Level.INFO, "evento não definido", ex);
        }
    }
    
    private void tentarRespTransferencia(Mensagem msg) {
        try {
            responderPedidoTransf((Boolean) onSolicitTransfer.call(), msg);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "erro respondendo pedido de transferencia {0}", ex.getMessage());
        }
    }
  
    public void lidarComRespostas(Mensagem msg) {
        if (msg == null)
            return;
        switch (msg.tipo()) {
            case MENSAGEM:
                novaMensagem(formatarMsg(msg));
                break;
            case LOGIN:
                lidarComLogin(msg);
                break;
            case PEDIDO_TRANSFERENCIA:
                tentarRespTransferencia(msg);
                break;
            case RESP_TRANSFERENCIA:
                if (!msg.conteudo.equals(Mensagem.Resp.TRANSFERENCIA_NEGADA))
                    iniciarUpload(msg);
                else
                    novaMensagem("transferência recusada");
                break;
            case ANUNCIAR_LOGIN:
                ultimoUsuarioEntrado = msg.conteudo;
                tentarCallback(onUsuarioEntrou);
                novaMensagem(formatarMsgAnuncio(msg, true));
                break;
            case LISTA_USUARIOS:
                ultimoUsuarioEntrado = msg.conteudo;
                tentarCallback(onListaUsuarios);
                break;
            case ANUNCIAR_LOGOUT:
                ultimoUsuarioEntrado = msg.conteudo;
                tentarCallback(onUsuarioSaiu);
                novaMensagem(formatarMsgAnuncio(msg, false));
                break;
            case REGISTRAR_USUARIO:
            case TESTE:
            case LOGOUT:
            default:
                LOGGER.severe(new AssertionError(msg.tipo().name()).toString());
        }
    }
    
    private Mensagem receber(){
        try {
            return (Mensagem) entrada.readObject();
        } catch (IOException ex) {
            executando = false;
            LOGGER.severe(ex.toString());
        } catch (ClassNotFoundException ex) {
            LOGGER.severe(ex.toString());
        }
        return null;
    }
    
    @Override
    public void run() {
        executando = true;
        while (executando)
        {
            // sockets dão block na thread até que algo chegue, porisso isso é feito numa thread separada.
            lidarComRespostas(receber());
        }
    }
    
    public final synchronized void enviar(Mensagem msg){
        try {
            saida.writeObject(msg);
            saida.flush();
            LOGGER.log(Level.FINE, "Enviado: {0}", msg.toString());
        } catch (IOException ex) {
            LOGGER.warning(ex.toString());
            if(ex.getClass().toString().equals("java.net.SocketException"));
                tentarCallback(onErroConexao);
        }
    }
}

