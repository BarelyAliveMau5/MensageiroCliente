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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteSocket implements Runnable {
    private final String usuario;            // nome do usuario atual
    private final int porta;                 // porta do server
    private final String serverAddr;         // endereço do server
    private final Socket socket;
    private final ObjectInputStream entrada; // entrada dos dados vindos da rede
    private final ObjectOutputStream saida;  // saída dos dados vindos da rede
    private boolean executando;              // condição que mantem o loop que verifica os dados recebidos
    private ArrayDeque<String> mensagens;
    private final static int TIMEOUT = 5000;
    
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
        serverAddr = addr;
        this.porta = porta;
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
            enviar(new Mensagem(Mensagem.Tipos.LOGIN, usuario, "a", "a"));
    }
    
    private String formatarMsg(Mensagem msg) {
        return Mensagem.Respostas.LOGIN_OK.toString();
    }
    
    private Mensagem receber(){
        try {
            return (Mensagem) entrada.readObject();
        } catch (IOException ex) {
            executando = false;
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void resolverLogin(Mensagem msg) {
        if (msg.conteudo.equals(Mensagem.Respostas.LOGIN_OK.toString()))
            mensagens.add("Logado com sucesso");
    }
    
    public String popMsg() {
        if (!mensagens.isEmpty())
            return mensagens.pop();
        else
            return null;
    }
    
    public void lidarComRespostas(Mensagem msg) {
        switch (msg.tipo()) {
            case MENSAGEM:
                break;
            case LOGIN:
                resolverLogin(msg);
                break;
            case LOGOUT:
                break;
            case SAIDA:
                break;
            case PEDIR_TRANSFERENCIA:
                break;
            case TRANSFERIR:
                break;
            case REGISTRAR_USUARIO:
                break;
            case RESULT_NOVO_USUARIO:
                break;
            case ANUNCIAR_NOVO_USUARIO:
                break;
            case ANUNCIAR_SAIDA_USUARIO:
                break;
            case TESTE:
                break;
            default:
                Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, 
                                                                    new AssertionError(msg.tipo().name()));
        }
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
            System.out.print(msg);
            saida.writeObject(msg);
            saida.flush();
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.INFO, "Enviado: {0}", msg.toString());
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
