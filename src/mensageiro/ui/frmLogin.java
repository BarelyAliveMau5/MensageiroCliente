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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import mensageiro.socket.ClienteSocket;

/**
 *
 * @author BarelyAliveMau5
 */
public class frmLogin extends javax.swing.JFrame {
    private ClienteSocket clienteSocket;
    /**
     * Creates new form frmLogin
     */
    public frmLogin() {
        initComponents();
        this.setLocationRelativeTo(null); // centralizar janela
        panelConta.setVisible(false);
        lblTesteConexao.setVisible(false);
        //imageLbl.setVisible(true);
        //this.add(imageLbl, BorderLayout.CENTER);
    }

    /**
     * código gerado pelo netbeans, não mexa
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lbl_titulo = new javax.swing.JLabel();
        panel_login = new javax.swing.JPanel();
        panelApelido = new javax.swing.JPanel();
        lbl_apelido = new javax.swing.JLabel();
        txtApelido = new javax.swing.JTextField();
        btnEntrarConvidado = new javax.swing.JButton();
        panelConta = new javax.swing.JPanel();
        lbl_senha = new javax.swing.JLabel();
        btnEntrarConta = new javax.swing.JButton();
        lbl_usuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        panel_convidado = new javax.swing.JPanel();
        chkConvidado = new javax.swing.JCheckBox();
        panel_servidor = new javax.swing.JPanel();
        lblTesteConexao = new javax.swing.JLabel();
        txtServidor = new javax.swing.JTextField();
        btnTestarConexao = new javax.swing.JButton();
        lbl_servidor1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Mensageiro");
        setResizable(false);

        lbl_titulo.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Mensageiro");
        lbl_titulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        panel_login.setLayout(new javax.swing.OverlayLayout(panel_login));

        panelApelido.setLayout(new java.awt.GridBagLayout());

        lbl_apelido.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_apelido.setText("Apelido:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 155;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        panelApelido.add(lbl_apelido, gridBagConstraints);

        txtApelido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtApelido.setText("teste");
        txtApelido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtApelidoKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 63;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 0, 20);
        panelApelido.add(txtApelido, gridBagConstraints);

        btnEntrarConvidado.setText("Entrar");
        btnEntrarConvidado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarConvidadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 12, 20);
        panelApelido.add(btnEntrarConvidado, gridBagConstraints);

        panel_login.add(panelApelido);

        panelConta.setLayout(new java.awt.GridBagLayout());

        lbl_senha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_senha.setText("Senha:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 155;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        panelConta.add(lbl_senha, gridBagConstraints);

        btnEntrarConta.setText("Entrar");
        btnEntrarConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarContaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(12, 20, 12, 20);
        panelConta.add(btnEntrarConta, gridBagConstraints);

        lbl_usuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_usuario.setText("Usuário:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 155;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        panelConta.add(lbl_usuario, gridBagConstraints);

        txtUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuario.setText("teste");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 63;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 0, 20);
        panelConta.add(txtUsuario, gridBagConstraints);

        txtSenha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSenha.setText("teste");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 63;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 0, 20);
        panelConta.add(txtSenha, gridBagConstraints);

        panel_login.add(panelConta);

        chkConvidado.setSelected(true);
        chkConvidado.setText("Entrar como convidado");
        chkConvidado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConvidadoActionPerformed(evt);
            }
        });
        panel_convidado.add(chkConvidado);

        panel_servidor.setLayout(new java.awt.GridBagLayout());

        lblTesteConexao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTesteConexao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mensageiro/ui/load.gif"))); // NOI18N
        lblTesteConexao.setText("Testando conexão...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 20);
        panel_servidor.add(lblTesteConexao, gridBagConstraints);

        txtServidor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtServidor.setText("localhost:13000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 0, 20);
        panel_servidor.add(txtServidor, gridBagConstraints);

        btnTestarConexao.setText("Testar conexão");
        btnTestarConexao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestarConexaoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 12, 20);
        panel_servidor.add(btnTestarConexao, gridBagConstraints);

        lbl_servidor1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_servidor1.setText("Servidor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 155;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        panel_servidor.add(lbl_servidor1, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_login, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_servidor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_convidado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_servidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_login, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_convidado, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // evita que o usuario dispare varias threads
    private void setAtoresEnabled(boolean estado) {
        btnEntrarConta.setEnabled(estado);
        btnEntrarConvidado.setEnabled(estado);
        btnTestarConexao.setEnabled(estado);
        txtApelido.setEnabled(estado);
        txtSenha.setEnabled(estado);
        txtServidor.setEnabled(estado);
        txtUsuario.setEnabled(estado);
    }
    
    private void mostrarChat() {
        frmChat chat = new frmChat(this, clienteSocket);
        chat.setVisible(true);
        this.setVisible(false);
    }
    
    private void definirCallbackLoginOK() {
        clienteSocket.onLoginOK = new Runnable() {
            @Override
            public void run() {
                lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("accept.png")));
                lblTesteConexao.setText("Sucesso");
                mostrarChat();
            }
        };
    }

    private void definirCallbackLoginFalho() {
        clienteSocket.onErroLogin = new Runnable() {
            @Override
            public void run() {
                lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("error.png")));
                lblTesteConexao.setText("Login falho");
                setAtoresEnabled(true);
            }
        };
    }
    
    private void definirCallbacks() {
        definirCallbackLoginFalho();
        definirCallbackLoginOK();
    }
    
    private void login(String usuario, char[] senha) {
        try {
            lblTesteConexao.setVisible(true);
            lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("load.gif")));
            lblTesteConexao.setText("Conectando...");
            clienteSocket = new ClienteSocket(serverAddr(), serverPort(), usuario, senha, true);
            definirCallbacks(); 
            clienteSocket.run();
            //lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("accept.png")));
            //lblTesteConexao.setText("Sucesso");
            //mostrarChat();
        } catch (IOException ex) {
            lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("error.png")));
            lblTesteConexao.setText("Conexão falha");
            Logger.getGlobal().log(Level.INFO, ex.toString());
            ///mostrarChat();
        } 
    }
     
    private void loginConvidado(String usuario) {
        login(usuario, null);
    }
    
    private void btnEntrarConvidadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarConvidadoActionPerformed
        Thread loginConvidado = new Thread(new Runnable() { 
            @Override
            public void run() {
                loginConvidado(txtApelido.getText());
                setAtoresEnabled(true);
            }
        });
        setAtoresEnabled(false);
        loginConvidado.start();
    }//GEN-LAST:event_btnEntrarConvidadoActionPerformed

    private void btnEntrarContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarContaActionPerformed
        Thread ContaConvidado = new Thread(new Runnable() { 
            @Override
            public void run() {
                login(txtUsuario.getText(), txtSenha.getPassword());
                setAtoresEnabled(true);
            }
        });
        setAtoresEnabled(false);
        ContaConvidado.start();
    }//GEN-LAST:event_btnEntrarContaActionPerformed

    private void chkConvidadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConvidadoActionPerformed

        panelApelido.setVisible(chkConvidado.isSelected());
        panelConta.setVisible(!chkConvidado.isSelected());
    }//GEN-LAST:event_chkConvidadoActionPerformed

    private int serverPort() {
       try {
            return Integer.valueOf(txtServidor.getText().split(":")[1]);
       } catch (NumberFormatException ex) {
           Logger.getGlobal().log(Level.INFO, ex.toString());
       } catch (Exception ex) {
           Logger.getGlobal().log(Level.WARNING, ex.toString(), ex);
       }
       return 0;
    }
    
    private String serverAddr() {
        try {
            return txtServidor.getText().split(":")[0];
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.WARNING, "Erro ao interpretar texto", ex);
        }
        return "";
    }         
    
    private void btnTestarConexaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestarConexaoActionPerformed
        Thread testeConexao = new Thread(new Runnable() {
            @Override
            public void run() {
                setAtoresEnabled(false);
                lblTesteConexao.setVisible(true);
                lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("load.gif")));
                lblTesteConexao.setText("Testando conexão... ");
                try {
                    ClienteSocket teste = new ClienteSocket(serverAddr(), serverPort(), "", null, false);
                    lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("accept.png")));
                    lblTesteConexao.setText("Sucesso");
                } catch (IOException ex) {
                    lblTesteConexao.setIcon(new ImageIcon(this.getClass().getResource("error.png")));
                    lblTesteConexao.setText("Conexão falha");
                    Logger.getGlobal().log(Level.INFO, ex.toString());
                } 
                setAtoresEnabled(true);
            }
        });
        testeConexao.start();
    }//GEN-LAST:event_btnTestarConexaoActionPerformed

    private void txtApelidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApelidoKeyPressed
        // mais uma pequena gambiarra
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            btnEntrarConvidadoActionPerformed( new java.awt.event.ActionEvent((Object) evt, 1, "none"));
        }
    }//GEN-LAST:event_txtApelidoKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrarConta;
    private javax.swing.JButton btnEntrarConvidado;
    private javax.swing.JButton btnTestarConexao;
    private javax.swing.JCheckBox chkConvidado;
    private javax.swing.JLabel lblTesteConexao;
    private javax.swing.JLabel lbl_apelido;
    private javax.swing.JLabel lbl_senha;
    private javax.swing.JLabel lbl_servidor1;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JLabel lbl_usuario;
    private javax.swing.JPanel panelApelido;
    private javax.swing.JPanel panelConta;
    private javax.swing.JPanel panel_convidado;
    private javax.swing.JPanel panel_login;
    private javax.swing.JPanel panel_servidor;
    private javax.swing.JTextField txtApelido;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtServidor;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
