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

import java.io.Serializable;

public class Mensagem implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private static final String OK = "\u0001";
    private static final String FAIL = "\u0000";
    
    public class Resp {
        public static final String LOGIN_OK = OK;
        public static final String LOGIN_FAIL = FAIL;
        public static final String TRANSFERENCIA_OK = OK;
        public static final String TRANSFERENCIA_FAIL = FAIL;
        public static final String REGISTRAR_OK = OK;
        public static final String REGISTRAR_FAIL = FAIL;
    }
    
    public enum Tipos {
        MENSAGEM,
        LOGIN,
        LOGOUT,
        SAIDA,
        PEDIR_TRANSFERENCIA,
        TRANSFERIR,
        REGISTRAR_USUARIO,
        RESULT_NOVO_USUARIO,
        ANUNCIAR_NOVO_USUARIO,
        ANUNCIAR_SAIDA_USUARIO,
        TESTE,
    }
    
    public String remetente, conteudo, destinatario;
    private final Tipos tipo;

    public Tipos tipo() {
        return tipo;
    }
    
    public Mensagem(Tipos tipo, String remetente, String conteudo, String destinatario){
        this.tipo = tipo; 
        this.remetente = remetente; 
        this.conteudo = conteudo; 
        this.destinatario = destinatario;
    }
    
    @Override
    public String toString(){
        return "{tipo='" + tipo + "'" + 
                ", remetente='" + remetente + "'" + 
                ", conteudo='" + conteudo + "'" + 
                ", destinatario='" + destinatario+"'}";
    }
}
