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

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BarelyAliveMau5
 */
public abstract class TransferenciaBase implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Download.class.getName());
    // todo: testar
    protected ServerSocket server;
    protected Socket socket;
    protected String status;
    protected boolean executando;    
    protected static final String INTERROMPIDO = "Interrompido";
    protected static final String CANCELADO = "Cancelado pelo usuario";
    protected static final String PARADO = "Parado";
    protected int tamanhoArquivo, 
                  bytesTransferidos,
                  tamanhoBuffer;
 
    public TransferenciaBase() {
        tamanhoBuffer = 1024; // 1kb
        bytesTransferidos = 0;
    }
    
    public synchronized void setBuffer(int buffer) {
        tamanhoBuffer = buffer;
        status = TransferenciaBase.PARADO;
    }
    
    public String status() {
        return status;
    }
    
    public String progresso() {
        return String.valueOf(bytesTransferidos / 1024) 
                + "/" + String.valueOf(tamanhoArquivo / 1024) 
                + " kB [" + String.valueOf(bytesTransferidos / tamanhoArquivo * 100) + "%]";
    }
    
    public void cancelar() {
        if (executando) {
            executando = false;
        } else {
            LOGGER.warning("Tentativa de cancelar transferencia não iniciado");
        }
        status = TransferenciaBase.CANCELADO;
    }
    
    public final void iniciar() {
        if (!executando) {
            Thread exec = new Thread(this);
            exec.start();
        } else {
            LOGGER.warning("Aviso: Tentativa de iniciação de transferencia já iniciado");
        }
    }
}
