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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BarelyAliveMau5
 */
public class Upload extends TransferenciaBase {
    private static final Logger LOGGER = Logger.getLogger(Download.class.getName());
    // todo: testar
    private FileInputStream entrada;
    private OutputStream saida;
    private File arquivo;
    private static final String CONCLUIDO = "Upload concluido";
    private static final String SUBINDO = "Fazendo upload...";
    
    public Upload(String addr, int port, File arquivo){
        super();
        try {
            this.arquivo = arquivo;
            socket = new Socket(InetAddress.getByName(addr), port);
            saida = socket.getOutputStream();
            entrada = new FileInputStream(arquivo);
            tamanhoArquivo = (int) entrada.getChannel().size();
        } 
        catch (IOException ex) {
            LOGGER.severe(ex.toString());
        }
    }
        
    public String arquivo() {
        return arquivo.getAbsolutePath();
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
            LOGGER.warning(ex.toString());
        }
    }

    @Override
    public void run() {
        try {       
            executando = true;
            byte[] buffer = new byte[tamanhoBuffer];
            int count;
            status = Upload.SUBINDO;
            while((count = entrada.read(buffer)) >= 0){
                bytesTransferidos += count;
                saida.write(buffer, 0, count);
                if (!executando) {
                    cancelar();
                    saida.flush();
                    return;
                }
            }
            saida.flush();
            
            status = Upload.CONCLUIDO;
            
            finalizar();
        }
        catch (IOException ex) {
            status = Upload.INTERROMPIDO;
            LOGGER.severe(ex.toString());
        } 
    }
}
