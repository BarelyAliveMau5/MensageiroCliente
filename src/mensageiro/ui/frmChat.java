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
package mensageiro.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mensageiro.socket.ClienteSocket;
import mensageiro.socket.Mensagem;

/**
 * @author user
 * o código está mal estruturado aqui pois tive que me apressar em 200% o tempo de desenvolvimento, perdão
 */
public final class frmChat extends JFrame {
    private ClienteSocket clienteSocket;  // isso quebra a programação funcional por permitir muitos estados
    private DefaultListModel model;
    private JFrame janelaAnterior;
    /**
     * define a ultima janela, isso DEVE ser chamado logo após instanciar a janela
     * @param prev janela anterior
     */
    private void setJanelaAnterior(JFrame prev) {
        janelaAnterior = prev;
    }
 
    public frmChat(JFrame prev, ClienteSocket cliente) {
        this();
        this.clienteSocket = cliente;
        definirCallbackNovaMensagem();
        definirCallbackUsuarioEntrou();
        definirCallbackUsuarioSaiu();
        definirCallbackErroConexao();
        definirCallbackSolicitTransfer();
        definirCallbackDownloadIniciado();
        definirCallbackUploadIniciado();
        lblNome.setText(clienteSocket.usuario() + ":");
        setJanelaAnterior(prev);
    }
    
    public frmChat() {
        initComponents();
        this.setLocationRelativeTo(null);  // centralizar janela
        panelTransferencia.setVisible(false);
    }
        
    private void setNumeroPessoas(int num) {
        lblPessoas.setText(String.valueOf(num));
    }
    
    private void setNumeroMensagens(int num) {
        lblMensagens.setText(String.valueOf(num));
    }
    
    // <editor-fold defaultstate="collapsed" desc="Callbacks">   
    private void definirCallbackNovaMensagem() {
        clienteSocket.onNovaMensagem = new Runnable() {
            @Override
            public void run() {
                String msg;
                do {
                    msg = clienteSocket.proximaMsg();
                    if (msg != null)
                        txtMensagens.append(msg + "\n");
                } while (msg != null);
                setNumeroMensagens(txtMensagens.getText().split("\n").length);
            }
        };
    }
        
    private void definirCallbackUsuarioEntrou() {
        clienteSocket.onUsuarioEntrou = clienteSocket.onListaUsuarios = new Runnable() {
            @Override
            public void run() {
                model.addElement(clienteSocket.ultimoUsuarioEntrado());
                setNumeroPessoas(model.size());
            }
        };
    }
    
    private void definirCallbackUsuarioSaiu() {
        clienteSocket.onUsuarioSaiu = new Runnable() {
            @Override
            public void run() {
                model.removeElement(clienteSocket.ultimoUsuarioEntrado());
                setNumeroPessoas(model.size());
            }
        };
    }
    
    private void definirCallbackErroConexao() {
        clienteSocket.onErroConexao = new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Houve um problema na conexão, faça o login novamente", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                voltarTelaLogin();
            }
        };
    }
    
    private void atualizarProgressoTransferencia() {
        pbProgresso.setValue(clienteSocket.porcentagemTransferencia());
        lblProgresso.setText(clienteSocket.progressoTransferencia());
    }

    private void iniciarAtualizador() {
        final Timer timer = new Timer();

        timer.schedule( new TimerTask() {
            @Override
            public void run() {
               atualizarProgressoTransferencia();
               if (!clienteSocket.transferenciaAtiva()) {
                   panelTransferencia.setVisible(true);
                   timer.cancel();
               }
            }
         }, 0, 100);
    }
    
    private void definirCallbackDownloadIniciado() {
        clienteSocket.onDownloadIniciado = new Runnable() {
            @Override
            public void run() {
                panelTransferencia.setVisible(true);
                lblTransferindo.setText("Fazendo download:");
                iniciarAtualizador();
            }
        };
    }

    private void definirCallbackUploadIniciado() {
        clienteSocket.onUploadIniciado = new Runnable() {
            @Override
            public void run() {
                panelTransferencia.setVisible(true);
                lblTransferindo.setText("Fazendo upload:");
                iniciarAtualizador();
            }
        };
    }

    private void definirCallbackSolicitTransfer() {
        clienteSocket.onSolicitTransfer = new Callable() {
            @Override
            public Object call() throws Exception {
                return (Boolean) (JOptionPane.showConfirmDialog(null, "aceitar arquivo?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
            }
        };
    }
// </editor-fold>        
    
    private void enviarMensagem(String msg) {
        String destinatario;
        // não permitir envio de mensagem privada a si mesmo
        destinatario = lstUsuarios.getSelectedValue() != null ? lstUsuarios.getSelectedValue().toString() : "";
        if (destinatario.equals(clienteSocket.usuario()))
            destinatario = "";
        else
            // outra gambiarra, isso só é enviado, não recebido, porisso tenho que adicionar manualmente a msg
            if (!destinatario.equals(""))
                txtMensagens.append(clienteSocket.usuario() + " > " + destinatario + ": "+ msg + "\n"); 
        clienteSocket.enviar(new Mensagem(Mensagem.Tipos.MENSAGEM, clienteSocket.usuario(), msg, destinatario));
    }
    
    private void voltarTelaLogin() {
        janelaAnterior.setVisible(true);
        this.dispose();
    }
    
    private void logoff() {
        clienteSocket.logOut();
        voltarTelaLogin();
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMensagens = new javax.swing.JTextArea();
        lbl_mensagens = new javax.swing.JLabel();
        lblMensagens = new javax.swing.JLabel();
        btnTransferir = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstUsuarios = new javax.swing.JList();
        lblPessoas = new javax.swing.JLabel();
        lbl_pessoas = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtMensagem = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();
        lblNome = new javax.swing.JLabel();
        panelTransferencia = new javax.swing.JPanel();
        lblTransferindo = new javax.swing.JLabel();
        pbProgresso = new javax.swing.JProgressBar();
        btnPararTransf = new javax.swing.JButton();
        lblProgresso = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Mensageiro");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtMensagens.setColumns(20);
        txtMensagens.setLineWrap(true);
        txtMensagens.setRows(5);
        txtMensagens.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMensagensMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txtMensagens);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 6, 6);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        lbl_mensagens.setText("Mensagens:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 6, 6);
        jPanel1.add(lbl_mensagens, gridBagConstraints);

        lblMensagens.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 6, 6);
        jPanel1.add(lblMensagens, gridBagConstraints);

        btnTransferir.setText("Enviar arquivo");
        btnTransferir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        jPanel1.add(btnTransferir, gridBagConstraints);

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        jPanel1.add(btnLogout, gridBagConstraints);

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        jPanel1.add(btnLimpar, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        lstUsuarios.setModel((model = new DefaultListModel()));
        jScrollPane2.setViewportView(lstUsuarios);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 12);
        jPanel2.add(jScrollPane2, gridBagConstraints);

        lblPessoas.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 12);
        jPanel2.add(lblPessoas, gridBagConstraints);

        lbl_pessoas.setText("Pessoas:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 0);
        jPanel2.add(lbl_pessoas, gridBagConstraints);

        txtMensagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMensagemKeyPressed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        lblNome.setText("Usuario:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMensagem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(txtMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNome, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTransferencia.setPreferredSize(new java.awt.Dimension(100, 49));

        lblTransferindo.setText("Transferindo:");

        btnPararTransf.setText("Parar");
        btnPararTransf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPararTransfActionPerformed(evt);
            }
        });

        lblProgresso.setText("0/0 kb [0%]");

        javax.swing.GroupLayout panelTransferenciaLayout = new javax.swing.GroupLayout(panelTransferencia);
        panelTransferencia.setLayout(panelTransferenciaLayout);
        panelTransferenciaLayout.setHorizontalGroup(
            panelTransferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransferenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTransferindo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProgresso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPararTransf)
                .addContainerGap())
        );
        panelTransferenciaLayout.setVerticalGroup(
            panelTransferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransferenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pbProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelTransferenciaLayout.createSequentialGroup()
                        .addGroup(panelTransferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPararTransf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addComponent(lblTransferindo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(panelTransferencia, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        logoff();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        enviarMensagem(txtMensagem.getText());
        txtMensagem.setText(""); // não existe metodo clear wtf
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void txtMensagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMensagemKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            enviarMensagem(txtMensagem.getText());
            txtMensagem.setText(""); // não existe metodo clear wtf
        }
    }//GEN-LAST:event_txtMensagemKeyPressed

    private void txtMensagensMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMensagensMouseClicked
        lstUsuarios.clearSelection();
    }//GEN-LAST:event_txtMensagensMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        clienteSocket.logOut();
    }//GEN-LAST:event_formWindowClosing

    private void btnTransferirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferirActionPerformed
        String destinatario= lstUsuarios.getSelectedValue() != null ? lstUsuarios.getSelectedValue().toString() : null;
        if (destinatario != null && !destinatario.equals(clienteSocket.usuario())) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                clienteSocket.upload(fc.getSelectedFile().getAbsolutePath(), destinatario); 
            }
        }
    }//GEN-LAST:event_btnTransferirActionPerformed

    private void btnPararTransfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPararTransfActionPerformed
        clienteSocket.pararTransferencia();
        panelTransferencia.setVisible(false);
    }//GEN-LAST:event_btnPararTransfActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        txtMensagens.setText("");
    }//GEN-LAST:event_btnLimparActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPararTransf;
    private javax.swing.JButton btnTransferir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMensagens;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPessoas;
    private javax.swing.JLabel lblProgresso;
    private javax.swing.JLabel lblTransferindo;
    private javax.swing.JLabel lbl_mensagens;
    private javax.swing.JLabel lbl_pessoas;
    public javax.swing.JList lstUsuarios;
    private javax.swing.JPanel panelTransferencia;
    private javax.swing.JProgressBar pbProgresso;
    private javax.swing.JTextField txtMensagem;
    private javax.swing.JTextArea txtMensagens;
    // End of variables declaration//GEN-END:variables
}
