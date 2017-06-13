/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.ui.rede;

import com.ufes.gi.dao.Rede;
import com.ufes.gi.dao.RedeDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author joever
 */
public class ConsultaRede extends javax.swing.JInternalFrame {
    List<Rede> myRedes = new ArrayList<>();
    Integer indice = 0;
    /**
     * Creates new form cadastraTS
     */
    public ConsultaRede() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        txtDescricao = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        listaRede = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtLocalizacao = new javax.swing.JTextField();
        txtDestino = new javax.swing.JTextField();
        txtCpf = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCaminhoXML = new javax.swing.JTextField();

        setClosable(true);
        setTitle("Consulta/Altera/Exclui Tipo de Monitoramento");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jLabel1.setText("Código: ");

        jLabel2.setText("Nome:");

        jLabel3.setText("Descricão: ");

        jButton1.setText("Atualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Lista: ");

        listaRede.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaRedeItemStateChanged(evt);
            }
        });

        jButton2.setText("Excluir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("CPF: ");

        jLabel6.setText("Localização: ");

        jLabel7.setText("Destino: ");

        try {
            txtCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel8.setText("Caminho geração do XML [Usar \\\\]: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescricao)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listaRede, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLocalizacao))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDestino)))
                        .addGap(10, 10, 10))))
            .addGroup(layout.createSequentialGroup()
                .addGap(165, 165, 165)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 172, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCaminhoXML)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(listaRede, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtLocalizacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCaminhoXML, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void atualizarRede(Rede myTM){
        RedeDAO myDAO = new RedeDAO();
        if (myDAO.atualiza(myTM)){
            indice = listaRede.getSelectedIndex();
            JOptionPane.showMessageDialog(rootPane, "Rede atualizada com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            listaRede.removeAllItems();
            formInternalFrameOpened(null);
            txtNome.grabFocus();
        }
        else
            JOptionPane.showMessageDialog(rootPane, "Falha ao atualizar rede!", "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void excluiRede(Integer myCodigo, String myCPF){
        RedeDAO myDAO = new RedeDAO();
        if (myDAO.deleta(myCodigo,myCPF)){
            indice = 0;
            JOptionPane.showMessageDialog(rootPane, "Rede excluída com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            listaRede.removeAllItems();
            formInternalFrameOpened(null);
        }
        else
            JOptionPane.showMessageDialog(rootPane, "Falha ao excluir rede!", "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String cpf = txtCpf.getText();
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        if (txtNome.getText().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Favor informar o Nome para Rede.", "Aviso", WIDTH);
            txtNome.grabFocus();
        }
        else {
            Rede newRede = new Rede();
            newRede.setIdRede(Integer.parseInt(txtCodigo.getText()));
            newRede.setCpf(cpf);
            newRede.setNome(txtNome.getText());
            newRede.setDescricao(txtDescricao.getText());
            newRede.setLocalizacao(txtLocalizacao.getText());
            newRede.setDestino(txtDestino.getText());
            newRede.setCaminhoXML(txtCaminhoXML.getText());
            atualizarRede(newRede);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        RedeDAO myDAO = new RedeDAO();
        myRedes = myDAO.getRedes();
        myRedes.stream().forEach((myRede) -> {
            listaRede.addItem(myRede.getNome());
        });
        listaRede.setSelectedIndex(indice);
        txtCodigo.setEditable(false);
        txtCpf.setEditable(false);
        txtCodigo.setText(myRedes.get(indice).getIdRede().toString());
        txtCpf.setText(myRedes.get(indice).getCpf());
        txtNome.setText(myRedes.get(indice).getNome());
        txtDescricao.setText(myRedes.get(indice).getDescricao());
        txtLocalizacao.setText(myRedes.get(indice).getLocalizacao());
        txtDestino.setText(myRedes.get(indice).getDestino());
        txtCaminhoXML.setText(myRedes.get(indice).getCaminhoXML());
    }//GEN-LAST:event_formInternalFrameOpened

    private void listaRedeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaRedeItemStateChanged
        // TODO add your handling code here:
        myRedes.stream().forEach((myRede) -> {
            if (myRede.getNome().equals(listaRede.getItemAt(listaRede.getSelectedIndex()))) {
                txtCodigo.setText(myRede.getIdRede().toString());
                txtCpf.setText(myRede.getCpf());
                txtNome.setText(myRede.getNome());
                txtDescricao.setText(myRede.getDescricao());
                txtLocalizacao.setText(myRede.getLocalizacao());
                txtDestino.setText(myRede.getDestino());
                txtCaminhoXML.setText(myRede.getCaminhoXML());
            }
        });
    }//GEN-LAST:event_listaRedeItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String cpf = txtCpf.getText();
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        int resposta;
        resposta = JOptionPane.showConfirmDialog(rootPane, "Confirma a exclusão da Rede "+txtNome.getText()+"?","Excluir Tipo Sensor?",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta == JOptionPane.YES_NO_OPTION) {
            excluiRede(Integer.parseInt(txtCodigo.getText()),cpf);
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JComboBox<String> listaRede;
    private javax.swing.JTextField txtCaminhoXML;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JFormattedTextField txtCpf;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtLocalizacao;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}