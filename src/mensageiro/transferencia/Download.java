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
package mensageiro.transferencia;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BarelyAliveMau5
 */
public class Download extends TransferenciaBase {
    // todo: testar
    private String salvarEm;
    private int porta;
    private InputStream entrada;
    private FileOutputStream saida;
    private static final String CONCLUIDO = "Downloaad concluido";
    private static final String BAIXANDO = "Baixando..";

    public Download(String salvarEm, boolean autoIniciar) {
        super();
        try {
            server = new ServerSocket(0);
            porta = server.getLocalPort();
            this.salvarEm = salvarEm;
            if (autoIniciar)
                iniciar();
        }
        catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }
    
    public int porta() {
        return porta;
    }
    
    private void finalizar() {
        try {
            if(saida != null)
                saida.close();
            if(entrada != null)
                entrada.close();
            if(socket != null)
                socket.close();
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.WARNING, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            executando = true;
            socket = server.accept();
            Logger.getGlobal().log(Level.INFO, "Download: {0}", socket.getRemoteSocketAddress());
            entrada = socket.getInputStream();
            saida = new FileOutputStream(salvarEm);
            
            byte[] buffer = new byte[tamanhoBuffer];
            status = Download.BAIXANDO;
            
            int count;
            while((count = entrada.read(buffer)) >= 0){
                saida.write(buffer, 0, count);
                bytesTransferidos += count;
                if (!executando) {
                    cancelar();
                    saida.flush();
                    return;
                }
            }
            
            saida.flush();
            status = Download.CONCLUIDO;
            finalizar();
        } 
        catch (IOException ex) {
            status = Download.INTERROMPIDO;
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
        executando = false;
    }
    
}
