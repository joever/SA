package QoC;


import static java.awt.image.ImageObserver.WIDTH;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joever
 */
public class QoC extends javax.swing.JFrame {

    public boolean logado = false, parar = true, bloquear = false;
    public String vInformacoes[] = null, vGateway[] = null, vAplicacao[] = null, sTipo[][] = null, lTValida[][] = null, ListTipo[][] = null, ListOrigem[][] = null, ListTipoValida[][] = null, Tipos[][] = null, Versao = "002", Build = "005";
    public Integer vCodigo[] = null, contSimuladores=0;
    public ChartPanel cPanelEntrada = null;
    public ChartPanel cPanelSaida = null;
    public ChartPanel cPanelMetricas = null;
    public tAtualizaGrafico AtualizaGrafico;
    public String login = "";
    ProcessBuilder Processa = new ProcessBuilder("java","-jar", "C:\\Users\\Joever\\Documents\\Projeto\\Aplicacoes\\Controle\\dist\\Controle.jar");
    Process iProcessa;
    Process[] Simuladores;
    /**
     * Creates new form QoC
     */    
    public QoC() {
        initComponents();
        logado = false;
    }

    public void sessao() {
        boolean liberado = false;
        String senha = new String(cSenha.getPassword()).trim();
        if (bloquear){
            if (login.compareTo(this.cLogin.getText()) != 0){
                JOptionPane.showMessageDialog(rootPane, "Já existe o usuário "+login+" logado no sistema!", "Erro no Login", WIDTH);
            }
            else{
                liberado = true;
            }
        }
        else {
            login = this.cLogin.getText();
            liberado = true;
        }
            
        if (liberado) {
            if (BancoUtil.login(login, senha, rootPane)){
                bloquear = false;
                logado = true;
                tLogin.setVisible(false);
                this.setExtendedState(this.MAXIMIZED_BOTH);
                this.pack();
                this.setTitle("Qualidade de Contexto"+" (Usuário: "+login.toUpperCase()+") - Versão: "+Versao+" Build "+Build);
                this.setVisible(true);
            }
            else {
                cLogin.selectAll();
                cLogin.grabFocus();
            }
        }
    }
    
    private void SelecionaOrigem() {
        if (cOrigemNome.getSelectedIndex() >= 0) {
                cOrigemCodigo.setText(vCodigo[cOrigemNome.getSelectedIndex()].toString());
                vOrigemCodigo.setText(vCodigo[cOrigemNome.getSelectedIndex()].toString());
                vOrigemNome.setText((String) cOrigemNome.getSelectedItem());
                vOrigemInformacoes.setText(vInformacoes[cOrigemNome.getSelectedIndex()]);
                vOrigemGateway.setText(vGateway[cOrigemNome.getSelectedIndex()]);
                vOrigemAplicacao.setText(vAplicacao[cOrigemNome.getSelectedIndex()]);
            }
    }
    
    private void Carrega(JComboBox DadosTipo, String[][] sTipo) {
        if (sTipo.length > 0){
            DadosTipo.removeAllItems();
            for (int i=0;i<sTipo.length;i++){
                DadosTipo.addItem(sTipo[i][0]+" - "+sTipo[i][1]+" - "+sTipo[i][2]);
            }
        }
    }
    
    public class tAtualizaGrafico extends Thread {
        public tAtualizaGrafico(String nome){
            super(nome);
        }
        public void run(){
            while (!parar) {
                try {
                    if (((!"".equals(qRegistro.getText().trim()) && OpRegistros.isSelected()) || OpPeriodo.isSelected()) && DadosTipo.getSelectedIndex() > -1){
                        if (aSaida.isSelected()){
                            if (OpSaida.isSelected() && tRelatorioSaida.isVisible()){
                                if (OpRegistros.isSelected())
                                    cPanelSaida = Util.criaGrafico(pGraficoSaida,Integer.parseInt(qRegistro.getText()),"qc_saida","Saída",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,null);
                                else if (OpPeriodo.isSelected()){
                                    String[] Periodo = new String[2];
                                    Periodo[0] = DtIni.getText();
                                    Periodo[1] = DtFim.getText();
                                    cPanelSaida = Util.criaGrafico(pGraficoSaida,Integer.parseInt(qRegistro.getText()),"qc_saida","Saída",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,Periodo);
                                }
                            }
                        }
                        if (aEntrada.isSelected()) {
                            if (OpEntrada.isSelected() && tRelatorioEntrada.isVisible()){
                                if (OpRegistros.isSelected())
                                    cPanelEntrada = Util.criaGrafico(pGraficoEntrada,Integer.parseInt(qRegistro.getText()),"qc_entrada","Entrada",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,null);
                                else if (OpPeriodo.isSelected()){
                                    String[] Periodo = new String[2];
                                    Periodo[0] = DtIni.getText();
                                    Periodo[1] = DtFim.getText();
                                    cPanelEntrada = Util.criaGrafico(pGraficoEntrada,Integer.parseInt(qRegistro.getText()),"qc_entrada","Entrada",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,Periodo);
                                }
                            }
                        }
                        if (aMetricas.isSelected()) {
                            if ((C.isSelected() || U.isSelected() || A.isSelected() || F.isSelected() || S.isSelected() || Q.isSelected()) && tRelatorioMetricas.isVisible()){
                                boolean metricas[] = new boolean[6];
                                metricas[0] = C.isSelected();
                                metricas[1] = U.isSelected();
                                metricas[2] = A.isSelected();
                                metricas[3] = F.isSelected();
                                metricas[4] = S.isSelected();
                                metricas[5] = Q.isSelected();
                                if (OpRegistros.isSelected())
                                    cPanelMetricas = Util.criaGraficoMetricas(pGraficoMetricas,Integer.parseInt(qRegistro.getText()),"qc_saida","Medidas da QoC",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,metricas,null);
                                else if (OpPeriodo.isSelected()){
                                    String[] Periodo = new String[2];
                                    Periodo[0] = DtIni.getText();
                                    Periodo[1] = DtFim.getText();
                                    cPanelMetricas = Util.criaGraficoMetricas(pGraficoMetricas,Integer.parseInt(qRegistro.getText()),"qc_saida","Medidas da QoC",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,metricas,Periodo);
                                }
                            }
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    return;
                } catch (ParseException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tLogin = new javax.swing.JFrame();
        cLogin = new javax.swing.JTextField();
        cSenha = new javax.swing.JPasswordField();
        bLogar = new javax.swing.JButton();
        bFechar = new javax.swing.JButton();
        tTitulo = new javax.swing.JLabel();
        tUsuario = new javax.swing.JLabel();
        tSenha = new javax.swing.JLabel();
        Desktop = new javax.swing.JDesktopPane();
        tSobre = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        textoSobre = new javax.swing.JTextPane();
        tOrigem = new javax.swing.JInternalFrame();
        jLabel1 = new javax.swing.JLabel();
        cOrigemCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bOrigemSalvar = new javax.swing.JButton();
        bOrigemDeletar = new javax.swing.JButton();
        bOrigemCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        vOrigemCodigo = new javax.swing.JTextField();
        vOrigemNome = new javax.swing.JTextField();
        vOrigemInformacoes = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        vOrigemGateway = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        vOrigemAplicacao = new javax.swing.JTextField();
        cOrigemNome = new javax.swing.JComboBox();
        bOrigemNovo = new javax.swing.JButton();
        tRelatorioEntrada = new javax.swing.JInternalFrame();
        pGraficoEntrada = new javax.swing.JPanel();
        tRelatorioSaida = new javax.swing.JInternalFrame();
        pGraficoSaida = new javax.swing.JPanel();
        tControlaRelatorio = new javax.swing.JInternalFrame();
        bAtualizar = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        C = new javax.swing.JCheckBox();
        U = new javax.swing.JCheckBox();
        F = new javax.swing.JCheckBox();
        A = new javax.swing.JCheckBox();
        S = new javax.swing.JCheckBox();
        Q = new javax.swing.JCheckBox();
        aMetricas = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        OpEntrada = new javax.swing.JCheckBox();
        aEntrada = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        OpSaida = new javax.swing.JCheckBox();
        aSaida = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        OpRegistros = new javax.swing.JCheckBox();
        OpPeriodo = new javax.swing.JCheckBox();
        DtIni = new javax.swing.JFormattedTextField();
        DtFim = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        DadosTipo = new javax.swing.JComboBox();
        qRegistro = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        tGrafico = new javax.swing.JComboBox();
        tTipoValida = new javax.swing.JInternalFrame();
        jLabel6 = new javax.swing.JLabel();
        lTipoValida = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        vTipoValidaCodigo = new javax.swing.JTextField();
        vTipoValida = new javax.swing.JTextField();
        vTipoValidaDescricao = new javax.swing.JTextField();
        bTipoValidaNovo = new javax.swing.JButton();
        bTipoValidaSalvar = new javax.swing.JButton();
        bTipoValidaDeletar = new javax.swing.JButton();
        bTipoValidaCancelar = new javax.swing.JButton();
        tTipo = new javax.swing.JInternalFrame();
        jLabel13 = new javax.swing.JLabel();
        lTipo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        vTipoCodigo = new javax.swing.JTextField();
        vTipoDescricao = new javax.swing.JTextField();
        vTipoValida1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lTipoOrigem = new javax.swing.JComboBox();
        lTipoTValida = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        vTipoValida2 = new javax.swing.JTextField();
        vAQoC = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        vCInferior = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        vCSuperior = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        vCPeso = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        vCGerar = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        vUTempo = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        vUPeso = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        vAPeso = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        vFTempo = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        vFPeso = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        vSOpcao1 = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        vSValor1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        vSOpcao2 = new javax.swing.JComboBox();
        vSValor2 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        vSPeso = new javax.swing.JTextField();
        vSGerar = new javax.swing.JCheckBox();
        jLabel42 = new javax.swing.JLabel();
        vTipoOpcao = new javax.swing.JComboBox();
        bTipoNovo = new javax.swing.JButton();
        bTipoSalvar = new javax.swing.JButton();
        bTipoDeletar = new javax.swing.JButton();
        bTipoCancelar = new javax.swing.JButton();
        tRelatorioMetricas = new javax.swing.JInternalFrame();
        pGraficoMetricas = new javax.swing.JPanel();
        tResumoMetricas = new javax.swing.JInternalFrame();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        CP = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        AP = new javax.swing.JLabel();
        FP = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        SP = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        Status = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        Processamento = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        DtIniResumo = new javax.swing.JFormattedTextField();
        DtFimResumo = new javax.swing.JFormattedTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        ListaTipoResumo = new javax.swing.JComboBox();
        mPrincipal = new javax.swing.JMenuBar();
        mCadastro = new javax.swing.JMenu();
        cTipoValida = new javax.swing.JMenuItem();
        cOrigem = new javax.swing.JMenuItem();
        cTipo = new javax.swing.JMenuItem();
        bDesligar = new javax.swing.JMenuItem();
        bSair = new javax.swing.JMenuItem();
        mProcessa = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        pEntradaIniciar = new javax.swing.JMenuItem();
        pEntradaParar = new javax.swing.JMenuItem();
        mRelatorios = new javax.swing.JMenu();
        rEntrada = new javax.swing.JMenuItem();
        rSaida = new javax.swing.JMenuItem();
        rMetricas = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        mUsuario = new javax.swing.JMenu();
        cPerfil = new javax.swing.JMenuItem();
        cUsuario = new javax.swing.JMenuItem();
        mAjuda = new javax.swing.JMenu();
        bAjuda = new javax.swing.JMenuItem();
        bSobre = new javax.swing.JMenuItem();
        mSair = new javax.swing.JMenu();

        tLogin.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        tLogin.setTitle("Qualidade de Contexto (Login)");
        tLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tLogin.setIconImages(null);
        tLogin.setLocationByPlatform(true);
        tLogin.setName("Tela Login"); // NOI18N
        tLogin.setPreferredSize(new java.awt.Dimension(250, 180));
        tLogin.setResizable(false);
        tLogin.setType(java.awt.Window.Type.UTILITY);
        tLogin.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                tLoginWindowActivated(evt);
            }
        });

        cLogin.setName("f_usuario"); // NOI18N
        cLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cLoginKeyPressed(evt);
            }
        });

        cSenha.setName("f_senha"); // NOI18N
        cSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cSenhaKeyPressed(evt);
            }
        });

        bLogar.setText("Logar");
        bLogar.setName("b_logar"); // NOI18N
        bLogar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLogarActionPerformed(evt);
            }
        });
        bLogar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bLogarKeyPressed(evt);
            }
        });

        bFechar.setText("Fechar");
        bFechar.setName("b_fechar"); // NOI18N
        bFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFecharActionPerformed(evt);
            }
        });
        bFechar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bFecharKeyPressed(evt);
            }
        });

        tTitulo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tTitulo.setText("Favor informar usuário e senha.");
        tTitulo.setToolTipText("");
        tTitulo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tTitulo.setAutoscrolls(true);
        tTitulo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tTitulo.setName("l_texto"); // NOI18N

        tUsuario.setText("Usuário:");
        tUsuario.setName("l_usuario"); // NOI18N

        tSenha.setText("Senha:");
        tSenha.setName("l_senha"); // NOI18N

        javax.swing.GroupLayout tLoginLayout = new javax.swing.GroupLayout(tLogin.getContentPane());
        tLogin.getContentPane().setLayout(tLoginLayout);
        tLoginLayout.setHorizontalGroup(
            tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tLoginLayout.createSequentialGroup()
                .addComponent(tTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
            .addGroup(tLoginLayout.createSequentialGroup()
                .addGroup(tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tLoginLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(bLogar)
                        .addGap(18, 18, 18)
                        .addComponent(bFechar))
                    .addGroup(tLoginLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tSenha)
                            .addComponent(tUsuario))
                        .addGap(10, 10, 10)
                        .addGroup(tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cLogin)
                            .addComponent(cSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tLoginLayout.setVerticalGroup(
            tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tLoginLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(tTitulo)
                .addGap(18, 18, 18)
                .addGroup(tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tUsuario)
                    .addComponent(cLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tSenha)
                    .addComponent(cSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(tLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bLogar)
                    .addComponent(bFechar))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        tLogin.getAccessibleContext().setAccessibleDescription("");
        tLogin.getAccessibleContext().setAccessibleParent(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Qualidade de Contexto");
        setName("Tela_login"); // NOI18N

        Desktop.setBackground(new java.awt.Color(240, 240, 240));
        Desktop.setForeground(new java.awt.Color(240, 240, 240));
        Desktop.setToolTipText("");
        Desktop.setAlignmentX(0.0F);
        Desktop.setAlignmentY(0.0F);

        tSobre.setClosable(true);
        tSobre.setIconifiable(true);
        tSobre.setTitle("Sobre");
        tSobre.setPreferredSize(new java.awt.Dimension(300, 200));
        tSobre.setVisible(false);
        tSobre.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                tSobreInternalFrameActivated(evt);
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
                tSobreInternalFrameOpened(evt);
            }
        });

        textoSobre.setEditable(false);
        textoSobre.setContentType("text/html"); // NOI18N
        textoSobre.setText("<p align=justify>&nbsp;&nbsp;&nbsp;Sistema de Gerenciamento de Qualidade de Contexto (SGQoC) foi desenvolvido para ler os arquivos XML gerados pelas redes de sensores sem fio e adicionar a esses arquivos métricas de qualidade de contexto.</p>\n\n<p align=left>Autor: <b>Joéver Silva Hoffman</b><br>\n\n<p align=center>Vitória, ES<br>\n2016</p>");
        textoSobre.setOpaque(false);
        jScrollPane1.setViewportView(textoSobre);

        javax.swing.GroupLayout tSobreLayout = new javax.swing.GroupLayout(tSobre.getContentPane());
        tSobre.getContentPane().setLayout(tSobreLayout);
        tSobreLayout.setHorizontalGroup(
            tSobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tSobreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );
        tSobreLayout.setVerticalGroup(
            tSobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        Desktop.add(tSobre);
        tSobre.setBounds(0, 0, 60, 60);

        tOrigem.setClosable(true);
        tOrigem.setIconifiable(true);
        tOrigem.setTitle("Cadastro Origem");
        tOrigem.setVisible(false);

        jLabel1.setText("Código: ");

        cOrigemCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cOrigemCodigoKeyPressed(evt);
            }
        });

        jLabel2.setText("Nome: ");

        bOrigemSalvar.setText("Salvar");
        bOrigemSalvar.setPreferredSize(new java.awt.Dimension(75, 23));
        bOrigemSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOrigemSalvarActionPerformed(evt);
            }
        });

        bOrigemDeletar.setText("Deletar");
        bOrigemDeletar.setPreferredSize(new java.awt.Dimension(75, 23));
        bOrigemDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOrigemDeletarActionPerformed(evt);
            }
        });

        bOrigemCancelar.setText("Cancelar");
        bOrigemCancelar.setName(""); // NOI18N
        bOrigemCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOrigemCancelarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(" Origem "));

        jLabel3.setText("Código: ");

        jLabel4.setText("Nome:");

        jLabel5.setText("Informações: ");

        vOrigemCodigo.setEditable(false);
        vOrigemCodigo.setEnabled(false);

        jLabel52.setText("IP Gateway:");

        jLabel53.setText("Aplicação:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vOrigemNome))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vOrigemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 245, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vOrigemInformacoes))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vOrigemGateway))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vOrigemAplicacao)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(vOrigemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(vOrigemNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(vOrigemInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(vOrigemGateway, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(vOrigemAplicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cOrigemNome.setMaximumRowCount(5);
        cOrigemNome.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cOrigemNomeItemStateChanged(evt);
            }
        });

        bOrigemNovo.setText("Novo");
        bOrigemNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOrigemNovoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tOrigemLayout = new javax.swing.GroupLayout(tOrigem.getContentPane());
        tOrigem.getContentPane().setLayout(tOrigemLayout);
        tOrigemLayout.setHorizontalGroup(
            tOrigemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tOrigemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tOrigemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tOrigemLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tOrigemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bOrigemDeletar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bOrigemSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bOrigemCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bOrigemNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tOrigemLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cOrigemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cOrigemNome, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tOrigemLayout.setVerticalGroup(
            tOrigemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tOrigemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tOrigemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tOrigemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(cOrigemCodigo)
                        .addComponent(jLabel2))
                    .addComponent(cOrigemNome))
                .addGroup(tOrigemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tOrigemLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(bOrigemNovo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bOrigemSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bOrigemDeletar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bOrigemCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                        .addGap(165, 165, 165))
                    .addGroup(tOrigemLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        bOrigemSalvar.setSize(85, 35);
        bOrigemDeletar.setSize(85, 35);
        bOrigemCancelar.setSize(85, 35);
        cOrigemNome.setSize(273, 20);
        bOrigemNovo.setSize(85, 35);

        Desktop.add(tOrigem);
        tOrigem.setBounds(30, 20, 490, 400);
        tOrigem.setSize(500, 310);

        tRelatorioEntrada.setClosable(true);
        tRelatorioEntrada.setIconifiable(true);
        tRelatorioEntrada.setMaximizable(true);
        tRelatorioEntrada.setResizable(true);
        tRelatorioEntrada.setTitle("Relatório de Entrada");
        tRelatorioEntrada.setPreferredSize(new java.awt.Dimension(400, 340));
        tRelatorioEntrada.setVisible(false);
        tRelatorioEntrada.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                tRelatorioEntradaInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        tRelatorioEntrada.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tRelatorioEntradaComponentResized(evt);
            }
        });

        javax.swing.GroupLayout pGraficoEntradaLayout = new javax.swing.GroupLayout(pGraficoEntrada);
        pGraficoEntrada.setLayout(pGraficoEntradaLayout);
        pGraficoEntradaLayout.setHorizontalGroup(
            pGraficoEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        pGraficoEntradaLayout.setVerticalGroup(
            pGraficoEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tRelatorioEntradaLayout = new javax.swing.GroupLayout(tRelatorioEntrada.getContentPane());
        tRelatorioEntrada.getContentPane().setLayout(tRelatorioEntradaLayout);
        tRelatorioEntradaLayout.setHorizontalGroup(
            tRelatorioEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGraficoEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tRelatorioEntradaLayout.setVerticalGroup(
            tRelatorioEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGraficoEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Desktop.add(tRelatorioEntrada);
        tRelatorioEntrada.setBounds(110, 70, 400, 340);

        tRelatorioSaida.setClosable(true);
        tRelatorioSaida.setIconifiable(true);
        tRelatorioSaida.setMaximizable(true);
        tRelatorioSaida.setResizable(true);
        tRelatorioSaida.setTitle("Relatório de Saída");
        tRelatorioSaida.setPreferredSize(new java.awt.Dimension(400, 340));
        tRelatorioSaida.setVisible(false);
        tRelatorioSaida.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                tRelatorioSaidaInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        tRelatorioSaida.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tRelatorioSaidaComponentResized(evt);
            }
        });

        javax.swing.GroupLayout pGraficoSaidaLayout = new javax.swing.GroupLayout(pGraficoSaida);
        pGraficoSaida.setLayout(pGraficoSaidaLayout);
        pGraficoSaidaLayout.setHorizontalGroup(
            pGraficoSaidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        pGraficoSaidaLayout.setVerticalGroup(
            pGraficoSaidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tRelatorioSaidaLayout = new javax.swing.GroupLayout(tRelatorioSaida.getContentPane());
        tRelatorioSaida.getContentPane().setLayout(tRelatorioSaidaLayout);
        tRelatorioSaidaLayout.setHorizontalGroup(
            tRelatorioSaidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGraficoSaida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tRelatorioSaidaLayout.setVerticalGroup(
            tRelatorioSaidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGraficoSaida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Desktop.add(tRelatorioSaida);
        tRelatorioSaida.setBounds(110, 70, 400, 340);

        tControlaRelatorio.setClosable(true);
        tControlaRelatorio.setIconifiable(true);
        tControlaRelatorio.setTitle("Filtro do Relatório");
        tControlaRelatorio.setMinimumSize(new java.awt.Dimension(320, 316));
        tControlaRelatorio.setName(""); // NOI18N
        tControlaRelatorio.setVisible(false);
        tControlaRelatorio.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                tControlaRelatorioInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        bAtualizar.setText("Atualizar");
        bAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAtualizarActionPerformed(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Métricas"));

        C.setText("Coverage");
        C.setEnabled(false);

        U.setText("Up_To_Dateness");
        U.setEnabled(false);

        F.setText("Frequency");
        F.setEnabled(false);

        A.setText("Accuracy");
        A.setEnabled(false);

        S.setText("Significance");
        S.setEnabled(false);

        Q.setSelected(true);
        Q.setText("QoC Geral");
        Q.setEnabled(false);

        aMetricas.setText("Atualização Automática");
        aMetricas.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aMetricas)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(F)
                            .addComponent(C))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(U)
                            .addComponent(S))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Q)
                            .addComponent(A))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(C)
                    .addComponent(U)
                    .addComponent(A))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(F)
                    .addComponent(S)
                    .addComponent(Q))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aMetricas))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados de Entrada"));

        OpEntrada.setSelected(true);
        OpEntrada.setText("Visualizar?");
        OpEntrada.setEnabled(false);

        aEntrada.setText("Atualização Automática");
        aEntrada.setEnabled(false);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OpEntrada)
                    .addComponent(aEntrada)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(OpEntrada)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aEntrada))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados de Saída"));

        OpSaida.setSelected(true);
        OpSaida.setText("Visualizar?");
        OpSaida.setEnabled(false);

        aSaida.setText("Atualização Automática");
        aSaida.setEnabled(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OpSaida)
                    .addComponent(aSaida)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(OpSaida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aSaida))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro"));
        jPanel12.setPreferredSize(new java.awt.Dimension(328, 92));

        OpRegistros.setSelected(true);
        OpRegistros.setText("Número de Registros");

        OpPeriodo.setText("Período");

        try {
            DtIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/#### ##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            DtFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/#### ##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel9.setText("Tipo:");

        qRegistro.setText("100");

        jLabel7.setText("À");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DadosTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(OpRegistros)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(qRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(OpPeriodo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DtIni, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DtFim, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OpRegistros)
                    .addComponent(qRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OpPeriodo)
                    .addComponent(DtIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DtFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(DadosTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo do Gráfico"));

        tGrafico.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Coluna", "Linha", "Pizza" }));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tGrafico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout tControlaRelatorioLayout = new javax.swing.GroupLayout(tControlaRelatorio.getContentPane());
        tControlaRelatorio.getContentPane().setLayout(tControlaRelatorioLayout);
        tControlaRelatorioLayout.setHorizontalGroup(
            tControlaRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tControlaRelatorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tControlaRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(bAtualizar, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tControlaRelatorioLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 336, Short.MAX_VALUE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        tControlaRelatorioLayout.setVerticalGroup(
            tControlaRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tControlaRelatorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tControlaRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );

        Desktop.add(tControlaRelatorio);
        tControlaRelatorio.setBounds(0, 0, 420, 515);

        tTipoValida.setClosable(true);
        tTipoValida.setIconifiable(true);
        tTipoValida.setTitle("Cadastro do Tipo de Validação");
        tTipoValida.setVisible(false);
        tTipoValida.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                tTipoValidaInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jLabel6.setText("Listagem:");

        lTipoValida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lTipoValida.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lTipoValidaItemStateChanged(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(" Tipo Valida"));

        jLabel10.setText("Código: ");

        jLabel11.setText("Tipo:");

        jLabel12.setText("Descrição:");

        vTipoValidaCodigo.setEditable(false);
        vTipoValidaCodigo.setEnabled(false);

        bTipoValidaNovo.setText("Novo");
        bTipoValidaNovo.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoValidaNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoValidaNovoActionPerformed(evt);
            }
        });

        bTipoValidaSalvar.setText("Salvar");
        bTipoValidaSalvar.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoValidaSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoValidaSalvarActionPerformed(evt);
            }
        });

        bTipoValidaDeletar.setText("Deletar");
        bTipoValidaDeletar.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoValidaDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoValidaDeletarActionPerformed(evt);
            }
        });

        bTipoValidaCancelar.setText("Cancelar");
        bTipoValidaCancelar.setName(""); // NOI18N
        bTipoValidaCancelar.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoValidaCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoValidaCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoValidaDescricao))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoValidaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoValida))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bTipoValidaNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bTipoValidaSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bTipoValidaDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bTipoValidaCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(vTipoValidaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(vTipoValida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(vTipoValidaDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bTipoValidaNovo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTipoValidaSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTipoValidaDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTipoValidaCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        bOrigemNovo.setSize(85, 35);
        bOrigemSalvar.setSize(85, 35);
        bOrigemDeletar.setSize(85, 35);
        bOrigemCancelar.setSize(85, 35);

        javax.swing.GroupLayout tTipoValidaLayout = new javax.swing.GroupLayout(tTipoValida.getContentPane());
        tTipoValida.getContentPane().setLayout(tTipoValidaLayout);
        tTipoValidaLayout.setHorizontalGroup(
            tTipoValidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tTipoValidaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tTipoValidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tTipoValidaLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addGroup(tTipoValidaLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTipoValida, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tTipoValidaLayout.setVerticalGroup(
            tTipoValidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tTipoValidaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(tTipoValidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lTipoValida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        Desktop.add(tTipoValida);
        tTipoValida.setBounds(130, 80, 430, 220);

        tTipo.setClosable(true);
        tTipo.setIconifiable(true);
        tTipo.setTitle("Cadastro de Tipo");
        tTipo.setMinimumSize(new java.awt.Dimension(695, 455));
        tTipo.setName(""); // NOI18N
        tTipo.setPreferredSize(new java.awt.Dimension(695, 455));
        tTipo.setVisible(false);
        tTipo.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                tTipoInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jLabel13.setText("Listagem:");

        lTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lTipoItemStateChanged(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(" Tipo"));

        jLabel14.setText("Código: ");

        jLabel15.setText("Descrição:");

        jLabel16.setText("Valida1:");

        vTipoCodigo.setEditable(false);
        vTipoCodigo.setEnabled(false);

        jLabel17.setText("Origem:");

        jLabel18.setText("Tipo Valida:");

        lTipoOrigem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lTipoTValida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel19.setText("Valida2:");

        vAQoC.setToolTipText("");

        jLabel20.setText("Limite Inferior:");

        jLabel21.setText("Limite Superior:");

        jLabel22.setText("Peso:");

        jLabel33.setText("Descrição: Indica se os dados que estão sendo recebidos estão dentro da faixa definida abaixo:");

        vCGerar.setText("Gerar essa avaliação da QoC?");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(vCSuperior)
                            .addComponent(vCPeso, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addComponent(vCInferior))
                        .addGap(76, 76, 76)
                        .addComponent(vCGerar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(vCInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(vCSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vCGerar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(vCPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        vAQoC.addTab("Coverage", jPanel4);

        jLabel23.setText("Tempo de Vida:");

        jLabel24.setText("Peso:");

        jLabel35.setText("Descrição: Indica a idade da informação tomando como base o seu tempo de vida definido abaixo:");

        jLabel36.setText("Obs.: Se o tempo de vida não for definido, essa avaliação da QoC não será gerada.");
        jLabel36.setToolTipText("");

        jLabel44.setText("(em segundos)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel24))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(vUTempo)
                                    .addComponent(vUPeso, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel44))
                            .addComponent(jLabel36))
                        .addGap(0, 129, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(vUTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(vUPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel36)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        vAQoC.addTab("Up_To_Dateness", jPanel5);

        jLabel25.setText("Peso:");

        jLabel37.setText("Descrição: Indica se a informação que está sendo recebida está correta ou não. Para essa avaliação são");

        jLabel38.setText("utilizadas as informações (Tipo Valida, Valida1, Valida2 e Ação).");

        jLabel46.setText("Obs.: Essa avaliação de QoC sempre é gerada.");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vAPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel46))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(vAPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel46)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        vAQoC.addTab("Accuracy", jPanel6);

        jLabel27.setText("Tempo entre medições:");

        jLabel28.setText("Peso:");

        jLabel26.setText("Descrição: Informa se o dado existia no momento da coleta utilizando o tempo entre medições:");

        jLabel39.setText("Obs.: Se o tempo entre medições não for definido, essa avaliação de QoC não será gerada.");

        jLabel45.setText("(em segundos)");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(vFTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(vFPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel45))
                            .addComponent(jLabel39))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(vFTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addGap(9, 9, 9)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(vFPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel39)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        vAQoC.addTab("Frequency", jPanel7);

        jLabel29.setText("Primeiro Operador:");

        vSOpcao1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=", ">", "<", ">=", "<=", "<>" }));

        jLabel30.setText("Valor:");

        jLabel31.setText("Segundo Operador:");

        jLabel32.setText("Valor:");
        jLabel32.setToolTipText("");

        vSOpcao2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=", ">", "<", ">=", "<=", "<>" }));

        jLabel40.setText("Descrição: Indica se a informação recebida é crítica ou não dependendo das parametrizações abaixo:");

        jLabel43.setText("Peso:");

        vSGerar.setText("Gerar essa avaliação da QoC?");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel31)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(vSOpcao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vSValor1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(vSOpcao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vSValor2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel43)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vSPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(vSGerar)
                        .addGap(22, 22, 22)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(vSOpcao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(vSValor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(vSOpcao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(vSValor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vSGerar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(vSPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        vAQoC.addTab("Significance", jPanel8);

        jLabel42.setText("Ação:");

        vTipoOpcao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Substituir a informação atual pela anterior", "Rejeitar a informação", "Manter a informação errada" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vAQoC)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTipoTValida, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTipoOrigem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoDescricao))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoValida1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoValida2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vTipoOpcao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lTipoOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lTipoTValida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(vTipoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(vTipoDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(vTipoValida2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel42)
                        .addComponent(vTipoOpcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(vTipoValida1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vAQoC, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bTipoNovo.setText("Novo");
        bTipoNovo.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoNovoActionPerformed(evt);
            }
        });

        bTipoSalvar.setText("Salvar");
        bTipoSalvar.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoSalvarActionPerformed(evt);
            }
        });

        bTipoDeletar.setText("Deletar");
        bTipoDeletar.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoDeletarActionPerformed(evt);
            }
        });

        bTipoCancelar.setText("Cancelar");
        bTipoCancelar.setName(""); // NOI18N
        bTipoCancelar.setPreferredSize(new java.awt.Dimension(85, 25));
        bTipoCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTipoCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tTipoLayout = new javax.swing.GroupLayout(tTipo.getContentPane());
        tTipo.getContentPane().setLayout(tTipoLayout);
        tTipoLayout.setHorizontalGroup(
            tTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tTipoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tTipoLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 70, Short.MAX_VALUE))
                    .addGroup(tTipoLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(tTipoLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(bTipoNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bTipoSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bTipoDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bTipoCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tTipoLayout.setVerticalGroup(
            tTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tTipoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tTipoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bTipoNovo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTipoSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTipoDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTipoCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        bOrigemNovo.setSize(85, 35);
        bOrigemSalvar.setSize(85, 35);
        bOrigemDeletar.setSize(85, 35);
        bOrigemCancelar.setSize(85, 35);

        Desktop.add(tTipo);
        tTipo.setBounds(0, 0, 695, 455);

        tRelatorioMetricas.setClosable(true);
        tRelatorioMetricas.setIconifiable(true);
        tRelatorioMetricas.setMaximizable(true);
        tRelatorioMetricas.setResizable(true);
        tRelatorioMetricas.setTitle("Relatório de Medidas da QoC");
        tRelatorioMetricas.setPreferredSize(new java.awt.Dimension(400, 340));
        tRelatorioMetricas.setVisible(false);
        tRelatorioMetricas.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                tRelatorioMetricasInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        tRelatorioMetricas.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tRelatorioMetricasComponentResized(evt);
            }
        });

        javax.swing.GroupLayout pGraficoMetricasLayout = new javax.swing.GroupLayout(pGraficoMetricas);
        pGraficoMetricas.setLayout(pGraficoMetricasLayout);
        pGraficoMetricasLayout.setHorizontalGroup(
            pGraficoMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        pGraficoMetricasLayout.setVerticalGroup(
            pGraficoMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tRelatorioMetricasLayout = new javax.swing.GroupLayout(tRelatorioMetricas.getContentPane());
        tRelatorioMetricas.getContentPane().setLayout(tRelatorioMetricasLayout);
        tRelatorioMetricasLayout.setHorizontalGroup(
            tRelatorioMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGraficoMetricas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tRelatorioMetricasLayout.setVerticalGroup(
            tRelatorioMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pGraficoMetricas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Desktop.add(tRelatorioMetricas);
        tRelatorioMetricas.setBounds(110, 70, 400, 340);

        tResumoMetricas.setClosable(true);
        tResumoMetricas.setIconifiable(true);
        tResumoMetricas.setTitle("Resumo das Métrias");
        tResumoMetricas.setToolTipText("");
        tResumoMetricas.setMinimumSize(new java.awt.Dimension(338, 325));
        tResumoMetricas.setPreferredSize(new java.awt.Dimension(310, 355));
        tResumoMetricas.setVisible(false);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Métricas / Informações"));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Coverage:");
        jLabel8.setToolTipText("");

        CP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CP.setForeground(new java.awt.Color(0, 0, 255));
        CP.setText("0");
        CP.setToolTipText("");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel34.setText("Accuracy:");
        jLabel34.setToolTipText("");

        AP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        AP.setForeground(new java.awt.Color(0, 0, 255));
        AP.setText("0");
        AP.setToolTipText("");

        FP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        FP.setForeground(new java.awt.Color(0, 0, 255));
        FP.setText("0");
        FP.setToolTipText("");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel41.setText("Frequency:");
        jLabel41.setToolTipText("");

        SP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        SP.setForeground(new java.awt.Color(0, 0, 255));
        SP.setText("0");
        SP.setToolTipText("");

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel47.setText("Significance:");
        jLabel47.setToolTipText("");

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel48.setText("Rede:");
        jLabel48.setToolTipText("");

        Status.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Status.setForeground(new java.awt.Color(0, 0, 255));
        Status.setText("-");
        Status.setToolTipText("");

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel49.setText("Processamento:");
        jLabel49.setToolTipText("");

        Processamento.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Processamento.setForeground(new java.awt.Color(0, 0, 255));
        Processamento.setText("-");
        Processamento.setToolTipText("");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CP))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AP))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FP))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SP))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Status))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Processamento)))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(CP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(AP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(FP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(SP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(Status))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(Processamento))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Atualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        try {
            DtIniResumo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/#### ##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            DtFimResumo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/#### ##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel50.setText("À");

        jLabel51.setText("Período:");

        ListaTipoResumo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout tResumoMetricasLayout = new javax.swing.GroupLayout(tResumoMetricas.getContentPane());
        tResumoMetricas.getContentPane().setLayout(tResumoMetricasLayout);
        tResumoMetricasLayout.setHorizontalGroup(
            tResumoMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tResumoMetricasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DtIniResumo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DtFimResumo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(tResumoMetricasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51))
            .addGroup(tResumoMetricasLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jButton1))
            .addGroup(tResumoMetricasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tResumoMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ListaTipoResumo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        tResumoMetricasLayout.setVerticalGroup(
            tResumoMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tResumoMetricasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ListaTipoResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tResumoMetricasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DtIniResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DtFimResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        Desktop.add(tResumoMetricas);
        tResumoMetricas.setBounds(0, 0, 310, 355);

        mCadastro.setText("Cadastro");

        cTipoValida.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        cTipoValida.setText("Tipo Valida");
        cTipoValida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cTipoValidaActionPerformed(evt);
            }
        });
        mCadastro.add(cTipoValida);

        cOrigem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        cOrigem.setText("Origem");
        cOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cOrigemActionPerformed(evt);
            }
        });
        mCadastro.add(cOrigem);

        cTipo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        cTipo.setText("Tipo");
        cTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cTipoActionPerformed(evt);
            }
        });
        mCadastro.add(cTipo);

        bDesligar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        bDesligar.setText("Bloquear");
        bDesligar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDesligarActionPerformed(evt);
            }
        });
        mCadastro.add(bDesligar);

        bSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        bSair.setText("Sair");
        bSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSairActionPerformed(evt);
            }
        });
        mCadastro.add(bSair);

        mPrincipal.add(mCadastro);

        mProcessa.setText("Processa");

        jMenu1.setText("Entrada");

        pEntradaIniciar.setText("Iniciar");
        pEntradaIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pEntradaIniciarActionPerformed(evt);
            }
        });
        jMenu1.add(pEntradaIniciar);

        pEntradaParar.setText("Parar");
        pEntradaParar.setEnabled(false);
        pEntradaParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pEntradaPararActionPerformed(evt);
            }
        });
        jMenu1.add(pEntradaParar);

        mProcessa.add(jMenu1);

        mPrincipal.add(mProcessa);

        mRelatorios.setText("Relatórios");

        rEntrada.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        rEntrada.setText("Entrada");
        rEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rEntradaActionPerformed(evt);
            }
        });
        mRelatorios.add(rEntrada);

        rSaida.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        rSaida.setText("Saída");
        rSaida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSaidaActionPerformed(evt);
            }
        });
        mRelatorios.add(rSaida);

        rMetricas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        rMetricas.setText("Medidas da QoC");
        rMetricas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rMetricasActionPerformed(evt);
            }
        });
        mRelatorios.add(rMetricas);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Resumo das Métricas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mRelatorios.add(jMenuItem1);

        mPrincipal.add(mRelatorios);

        mUsuario.setText("Usuário");

        cPerfil.setText("Cadastrar Perfil");
        mUsuario.add(cPerfil);

        cUsuario.setText("Cadastrar Usuário");
        mUsuario.add(cUsuario);

        mPrincipal.add(mUsuario);

        mAjuda.setText("Ajuda");

        bAjuda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        bAjuda.setText("Ajuda");
        mAjuda.add(bAjuda);

        bSobre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        bSobre.setText("Sobre");
        bSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSobreActionPerformed(evt);
            }
        });
        mAjuda.add(bSobre);

        mPrincipal.add(mAjuda);

        mSair.setText("Sair");
        mSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mSairMouseClicked(evt);
            }
        });
        mPrincipal.add(mSair);

        setJMenuBar(mPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 582, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cLoginKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            if (cLogin.getText().length() == 0) {
                JOptionPane.showMessageDialog(rootPane, "Favor informar o usuário!", "Aviso", WIDTH);
            }
            else {
                if (cSenha.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(rootPane, "Favor informar a senha!", "Aviso", WIDTH);
                    cSenha.grabFocus();
                }
                else {
                    sessao();
                }
            }
        }
        else if (evt.getKeyCode() == 27) {
            bFecharActionPerformed(null);
        }
    }//GEN-LAST:event_cLoginKeyPressed

    private void cSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cSenhaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            if (cSenha.getPassword().length == 0) {
                JOptionPane.showMessageDialog(rootPane, "Favor informar a senha!", "Aviso", WIDTH);
            }
            else {
                if (cLogin.getText().length() == 0) {
                    JOptionPane.showMessageDialog(rootPane, "Favor informar o usuário!", "Aviso", WIDTH);
                    cLogin.grabFocus();
                }
                else {
                    sessao();
                }
            }
        }
        else if (evt.getKeyCode() == 27) {
            bFecharActionPerformed(null);
        }
    }//GEN-LAST:event_cSenhaKeyPressed

    private void bLogarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLogarActionPerformed
        if (cLogin.getText().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Favor informar o usuário!", "Aviso", WIDTH);
            cLogin.grabFocus();
        }
        else {
            if (cSenha.getPassword().length == 0) {
                JOptionPane.showMessageDialog(rootPane, "Favor informar a senha!", "Aviso", WIDTH);
                cSenha.grabFocus();
            }
            else {
                sessao();
            }
        }
    }//GEN-LAST:event_bLogarActionPerformed

    private void bFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFecharActionPerformed
        tLogin.dispose();
        this.dispose();
    }//GEN-LAST:event_bFecharActionPerformed

    private void tLoginWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_tLoginWindowActivated
        // TODO add your handling code here:
        int x = (int) this.getLocation().x + (this.getWidth()/2);
        int y = (int) this.getLocation().y + (this.getHeight()/2);
        tLogin.setLocationRelativeTo(this);
        tLogin.setSize(250, 190);
        tLogin.setLocation(x - (tLogin.getWidth()/2), y - (tLogin.getHeight()/2));
        cSenha.setText(null);
    }//GEN-LAST:event_tLoginWindowActivated

    private void bDesligarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDesligarActionPerformed
        // TODO add your handling code here:
        logado = false;
        this.setVisible(false);
        tLogin.setVisible(true);
        bloquear = true;
    }//GEN-LAST:event_bDesligarActionPerformed

    private void bSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSairActionPerformed
        // TODO add your handling code here:
        int resposta;
        resposta = JOptionPane.showConfirmDialog(null, "Confirma a saída do sistema?","Sair?",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta == JOptionPane.YES_NO_OPTION) {
            if (tLogin.isEnabled()) {
                tLogin.removeAll();
                tLogin.dispose();
            }
            if (this.isEnabled()) {
                this.removeAll();
                this.dispose();
            }
            if (AtualizaGrafico != null){
                if (AtualizaGrafico.getState() != Thread.State.NEW) {
                    AtualizaGrafico.interrupt();
                    parar = true;
                }
            }
            if (iProcessa != null)
                iProcessa.destroy();
            if (Simuladores != null){
                for (int i=0;i<Simuladores.length;i++)
                    Simuladores[i].destroy();
            }
        }
    }//GEN-LAST:event_bSairActionPerformed

    private void mSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mSairMouseClicked
        // TODO add your handling code here:
        bSairActionPerformed(null);
    }//GEN-LAST:event_mSairMouseClicked

    private void cOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cOrigemActionPerformed
        // TODO add your handling code here:
        try {
            //Caso já foi fechado, ativa e inclui no Desktop.
            if (tOrigem.isClosed()) {
                tOrigem.setClosed(false);
                Desktop.add(tOrigem);
            }
            //Apagando dados do formulário
            if (!bOrigemNovo.isEnabled()) {
                bOrigemNovo.setEnabled(true);
                bOrigemDeletar.setEnabled(true);
            }
            vOrigemCodigo.setText("");
            vOrigemNome.setText("");
            vOrigemInformacoes.setText("");
            cOrigemNome.removeAllItems();
            //Lendo dados do banco
            int numero = Origem.nLinhas();
            vCodigo = new Integer[numero];
            vInformacoes = new String[numero];
            vGateway = new String[numero];
            vAplicacao = new String[numero];
            Origem.Dados(vInformacoes, vCodigo, cOrigemNome, vGateway, vAplicacao);
            //Atualizando a tela
            tOrigem.moveToFront();
            if (!tOrigem.isVisible())
                Util.centralizar(tOrigem, this.Desktop);
            tOrigem.setVisible(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cOrigemActionPerformed

    private void tSobreInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tSobreInternalFrameOpened
        // TODO add your handling code here:
        tSobre.setSize(300, 220);
    }//GEN-LAST:event_tSobreInternalFrameOpened

    private void bSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSobreActionPerformed
        // TODO add your handling code here:
        try {
            if (tSobre.isClosed()) {
                tSobre.setClosed(false);
                Desktop.add(tSobre);
            }
            tSobre.moveToFront();
            tSobre.setVisible(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bSobreActionPerformed

    private void tSobreInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tSobreInternalFrameActivated
        // TODO add your handling code here:
        Util.centralizar(tSobre, this.Desktop);
    }//GEN-LAST:event_tSobreInternalFrameActivated

    private void bLogarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bLogarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 32) {
            bLogarActionPerformed(null);
        }
        else if (evt.getKeyCode() == 27) {
            bFecharActionPerformed(null);
        }
    }//GEN-LAST:event_bLogarKeyPressed

    private void bFecharKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bFecharKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 32 || evt.getKeyCode() == 32) {
            bFecharActionPerformed(null);
        }
    }//GEN-LAST:event_bFecharKeyPressed

    private void rEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rEntradaActionPerformed
        // TODO add your handling code here:
        try {
            //Caso já foi fechado, ativa e inclui no Desktop.
            if (tControlaRelatorio.isClosed()) {
                tControlaRelatorio.setClosed(false);
                Desktop.add(tControlaRelatorio);
            }
            tControlaRelatorio.moveToFront();
            if (tRelatorioEntrada.isClosed()) {
                tRelatorioEntrada.setClosed(false);
                Desktop.add(tRelatorioEntrada);
            }
            OpEntrada.setEnabled(true);
            aEntrada.setEnabled(true);
            tRelatorioEntrada.moveToFront();
            if (!tRelatorioEntrada.isVisible()){
                if (!tRelatorioSaida.isVisible() && !tRelatorioMetricas.isVisible()){
                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Calendar dt = Calendar.getInstance();
                    dt.setTime(new Date());
                    DtFim.setText(f.format(dt.getTime()));
                    dt.add(Calendar.DAY_OF_YEAR,-1);
                    DtIni.setText(f.format(dt.getTime()));
                    sTipo = BancoUtil.sTipo();
                    Carrega(DadosTipo,sTipo);
                    parar = false;
                    AtualizaGrafico = new tAtualizaGrafico("Controle");
                    AtualizaGrafico.start();  //Thread de controle de tipos
                }
                Util.centralizar(tRelatorioEntrada, this.Desktop);
                if (OpRegistros.isSelected())
                    cPanelEntrada = Util.criaGrafico(pGraficoEntrada,Integer.parseInt(qRegistro.getText()),"qc_entrada","Entrada",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,null);
                else if (OpPeriodo.isSelected()){
                    String[] Periodo = new String[2];
                    Periodo[0] = DtIni.getText();
                    Periodo[1] = DtFim.getText();
                    cPanelEntrada = Util.criaGrafico(pGraficoEntrada,Integer.parseInt(qRegistro.getText()),"qc_entrada","Entrada",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,Periodo);
                }
                tControlaRelatorio.setVisible(true);
                tRelatorioEntrada.setVisible(true);
            }
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rEntradaActionPerformed

    private void tRelatorioEntradaComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tRelatorioEntradaComponentResized
        // TODO add your handling code here:
        if (tRelatorioEntrada.isVisible() && cPanelEntrada != null)
            Util.tamanhoGrafico(pGraficoEntrada,cPanelEntrada);
    }//GEN-LAST:event_tRelatorioEntradaComponentResized

    private void tRelatorioEntradaInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tRelatorioEntradaInternalFrameClosing
        // TODO add your handling code here:
        cPanelEntrada = null;
        OpEntrada.setEnabled(false);
        aEntrada.setEnabled(false);
        if (!tRelatorioSaida.isVisible() && !tRelatorioMetricas.isVisible()) {
            tControlaRelatorio.setVisible(false);
            parar = true;
        }
    }//GEN-LAST:event_tRelatorioEntradaInternalFrameClosing

    private void tRelatorioSaidaInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tRelatorioSaidaInternalFrameClosing
        // TODO add your handling code here:
        cPanelSaida = null;
        OpSaida.setEnabled(false);
        aSaida.setEnabled(false);
        if (!tRelatorioEntrada.isVisible() && !tRelatorioMetricas.isVisible()) {
            tControlaRelatorio.setVisible(false);
            parar = true;
        }
    }//GEN-LAST:event_tRelatorioSaidaInternalFrameClosing

    private void tRelatorioSaidaComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tRelatorioSaidaComponentResized
        // TODO add your handling code here:
        if (tRelatorioSaida.isVisible() && cPanelSaida != null)
            Util.tamanhoGrafico(pGraficoSaida,cPanelSaida);
    }//GEN-LAST:event_tRelatorioSaidaComponentResized

    private void rSaidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSaidaActionPerformed
        // TODO add your handling code here:
        try {
            //Caso já foi fechado, ativa e inclui no Desktop.
            if (tControlaRelatorio.isClosed()) {
                tControlaRelatorio.setClosed(false);
                Desktop.add(tControlaRelatorio);
            }
            tControlaRelatorio.moveToFront();
            if (tRelatorioSaida.isClosed()) {
                tRelatorioSaida.setClosed(false);
                Desktop.add(tRelatorioSaida);
            }
            OpSaida.setEnabled(true);
            aSaida.setEnabled(true);
            tRelatorioSaida.moveToFront();
            if (!tRelatorioSaida.isVisible()){
                if (!tRelatorioEntrada.isVisible() && !tRelatorioMetricas.isVisible()){
                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Calendar dt = Calendar.getInstance();
                    dt.setTime(new Date());
                    DtFim.setText(f.format(dt.getTime()));
                    dt.add(Calendar.DAY_OF_YEAR,-1);
                    DtIni.setText(f.format(dt.getTime()));
                    sTipo = BancoUtil.sTipo();
                    Carrega(DadosTipo,sTipo);
                    parar = false;
                    AtualizaGrafico = new tAtualizaGrafico("Controle");
                    AtualizaGrafico.start();  //Thread de controle de tipos
                }
                Util.centralizar(tRelatorioSaida, this.Desktop);
                if (OpRegistros.isSelected())
                    cPanelSaida = Util.criaGrafico(pGraficoSaida,Integer.parseInt(qRegistro.getText()),"qc_saida","Saída",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,null);
                else if (OpPeriodo.isSelected()){
                    String[] Periodo = new String[2];
                    Periodo[0] = DtIni.getText();
                    Periodo[1] = DtFim.getText();
                    cPanelSaida = Util.criaGrafico(pGraficoSaida,Integer.parseInt(qRegistro.getText()),"qc_saida","Saída",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,Periodo);
                }
                tControlaRelatorio.setVisible(true);
                tRelatorioSaida.setVisible(true);
            }
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rSaidaActionPerformed

    private void bAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAtualizarActionPerformed
        // TODO add your handling code here:
        if (((!"".equals(qRegistro.getText().trim()) && OpRegistros.isSelected()) || OpPeriodo.isSelected()) && DadosTipo.getSelectedIndex() > -1){
            if (OpSaida.isSelected() && tRelatorioSaida.isVisible()){
                if (OpRegistros.isSelected())
                    try {
                    cPanelSaida = Util.criaGrafico(pGraficoSaida,Integer.parseInt(qRegistro.getText()),"qc_saida","Saída",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,null);
                } catch (ParseException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
                else if (OpPeriodo.isSelected()){
                    String[] Periodo = new String[2];
                    Periodo[0] = DtIni.getText();
                    Periodo[1] = DtFim.getText();
                    try {
                        cPanelSaida = Util.criaGrafico(pGraficoSaida,Integer.parseInt(qRegistro.getText()),"qc_saida","Saída",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,Periodo);
                    } catch (ParseException ex) {
                        Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (OpEntrada.isSelected() && tRelatorioEntrada.isVisible()){
                if (OpRegistros.isSelected())
                    try {
                    cPanelEntrada = Util.criaGrafico(pGraficoEntrada,Integer.parseInt(qRegistro.getText()),"qc_entrada","Entrada",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,null);
                } catch (ParseException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
                else if (OpPeriodo.isSelected()){
                    String[] Periodo = new String[2];
                    Periodo[0] = DtIni.getText();
                    Periodo[1] = DtFim.getText();
                    try {
                        cPanelEntrada = Util.criaGrafico(pGraficoEntrada,Integer.parseInt(qRegistro.getText()),"qc_entrada","Entrada",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,Periodo);
                    } catch (ParseException ex) {
                        Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if ((C.isSelected() || U.isSelected() || A.isSelected() || F.isSelected() || S.isSelected() || Q.isSelected()) && tRelatorioMetricas.isVisible()){
                try {
                    boolean metricas[] = new boolean[6];
                    metricas[0] = C.isSelected();
                    metricas[1] = U.isSelected();
                    metricas[2] = A.isSelected();
                    metricas[3] = F.isSelected();
                    metricas[4] = S.isSelected();
                    metricas[5] = Q.isSelected();
                    if (OpRegistros.isSelected())
                        cPanelMetricas = Util.criaGraficoMetricas(pGraficoMetricas,Integer.parseInt(qRegistro.getText()),"qc_saida","Medidas da QoC",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,metricas,null);
                    else if (OpPeriodo.isSelected()){
                        String[] Periodo = new String[2];
                        Periodo[0] = DtIni.getText();
                        Periodo[1] = DtFim.getText();
                        cPanelMetricas = Util.criaGraficoMetricas(pGraficoMetricas,Integer.parseInt(qRegistro.getText()),"qc_saida","Medidas da QoC",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,metricas,Periodo);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_bAtualizarActionPerformed

    private void pEntradaIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pEntradaIniciarActionPerformed
        // TODO add your handling code here:
        pEntradaIniciar.setEnabled(false);
        pEntradaParar.setEnabled(true);
        Processa.redirectErrorStream(true);
        try {
            iProcessa = Processa.start();
        } catch (IOException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pEntradaIniciarActionPerformed

    private void pEntradaPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pEntradaPararActionPerformed
        // TODO add your handling code here:
        pEntradaIniciar.setEnabled(true);
        pEntradaParar.setEnabled(false);
        iProcessa.destroy();
        iProcessa = null;
    }//GEN-LAST:event_pEntradaPararActionPerformed

    private void bTipoValidaNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoValidaNovoActionPerformed
        // TODO add your handling code here:
        bTipoValidaNovo.setEnabled(false);
        bTipoValidaDeletar.setEnabled(false);
        vTipoValidaCodigo.setText("");
        vTipoValida.setText("");
        vTipoValidaDescricao.setText("");
        vTipoValida.grabFocus();
    }//GEN-LAST:event_bTipoValidaNovoActionPerformed

    private void bTipoValidaSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoValidaSalvarActionPerformed
        // TODO add your handling code here:
        if (vTipoValida.getText().length() > 0){
            if (bTipoValidaNovo.isEnabled()){ 
                try {
                    //Atualizando
                    int i = lTipoValida.getSelectedIndex();
                    lTValida[i][1] = vTipoValida.getText();
                    lTValida[i][2] = vTipoValidaDescricao.getText();
                    if (TipoValida.Atualiza(lTValida[i])){
                        lTipoValida.removeItemAt(i);
                        lTipoValida.insertItemAt(lTValida[i][0]+"  "+lTValida[i][1]+"  "+lTValida[i][2], i);
                        lTipoValida.setSelectedIndex(i);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else { 
                try {
                    //Incluir
                    if (TipoValida.Incluir(vTipoValida.getText(),vTipoValidaDescricao.getText())){
                        lTValida = null;
                        lTipoValida.removeAllItems();
                        lTValida = TipoValida.Carrega(lTipoValida);
                        lTipoValida.setSelectedIndex(lTValida.length-1);
                        vTipoValidaCodigo.setText(lTValida[lTValida.length-1][0]);
                        vTipoValida.setText(lTValida[lTValida.length-1][1]);
                        vTipoValidaDescricao.setText(lTValida[lTValida.length-1][2]);
                        bTipoValidaNovo.setEnabled(true);
                        bTipoValidaDeletar.setEnabled(true);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Favor informar o Tipo!", "Aviso", WIDTH);
            vTipoValida.grabFocus();
        }
    }//GEN-LAST:event_bTipoValidaSalvarActionPerformed

    private void bTipoValidaDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoValidaDeletarActionPerformed
        // TODO add your handling code here:
        int resposta;
        if (vTipoValidaCodigo.getText().length() > 0){
            if (TipoValida.Verifica(Integer.parseInt(vTipoValidaCodigo.getText()))){
                resposta = JOptionPane.showConfirmDialog(null, "Deletar o Tipo Valida?","Apagar?",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (resposta == JOptionPane.YES_NO_OPTION) {
                    try {
                        if (TipoValida.Deleta(Integer.parseInt(vTipoValidaCodigo.getText()))) {
                            lTValida = null;
                            vTipoValidaCodigo.setText("");
                            vTipoValida.setText("");
                            vTipoValidaDescricao.setText("");
                            lTipoValida.removeAllItems();
                            //Carregando os dados da tabela
                            lTValida = TipoValida.Carrega(lTipoValida);
                            if (lTValida != null){
                                vTipoValidaCodigo.setText(lTValida[0][0]);
                                vTipoValida.setText(lTValida[0][1]);
                                vTipoValidaDescricao.setText(lTValida[0][2]);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(rootPane, "Não é possível excluir o Tipo Valida. Já existe vinculo dele com pelo menos um Tipo!", "Aviso", WIDTH);
            }
        }
    }//GEN-LAST:event_bTipoValidaDeletarActionPerformed

    private void bTipoValidaCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoValidaCancelarActionPerformed
        // TODO add your handling code here:
        if (!bTipoValidaNovo.isEnabled()) {
            bTipoValidaNovo.setEnabled(true);
            bTipoValidaDeletar.setEnabled(true);
            vTipoValidaCodigo.setText(lTValida[lTipoValida.getSelectedIndex()][0]);
            vTipoValida.setText(lTValida[lTipoValida.getSelectedIndex()][1]);
            vTipoValidaDescricao.setText(lTValida[lTipoValida.getSelectedIndex()][2]);
        }
        else if (!vTipoValida.getText().equals(lTValida[lTipoValida.getSelectedIndex()][1]) || !vTipoValidaDescricao.getText().equals(lTValida[lTipoValida.getSelectedIndex()][2])){
            vTipoValidaCodigo.setText(lTValida[lTipoValida.getSelectedIndex()][0]);
            vTipoValida.setText(lTValida[lTipoValida.getSelectedIndex()][1]);
            vTipoValidaDescricao.setText(lTValida[lTipoValida.getSelectedIndex()][2]);
        }
        else{
            tTipoValida.dispose();
            lTValida = null;
        }
    }//GEN-LAST:event_bTipoValidaCancelarActionPerformed

    private void cTipoValidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cTipoValidaActionPerformed
        // TODO add your handling code here:
        try {
            //Caso já foi fechado, ativa e inclui no Desktop.
            if (tTipoValida.isClosed()) {
                tTipoValida.setClosed(false);
                Desktop.add(tTipoValida);
            }
            //Apagando dados do formulário
            if (!bTipoValidaNovo.isEnabled()) {
                bTipoValidaNovo.setEnabled(true);
                bTipoValidaDeletar.setEnabled(true);
            }
            //Limpando os dados do formulário
            vTipoValidaCodigo.setText("");
            vTipoValida.setText("");
            vTipoValidaDescricao.setText("");
            lTipoValida.removeAllItems();
            
            //Carregando os dados da tabela
            lTValida = TipoValida.Carrega(lTipoValida);
            if (lTValida != null){
                vTipoValidaCodigo.setText(lTValida[0][0]);
                vTipoValida.setText(lTValida[0][1]);
                vTipoValidaDescricao.setText(lTValida[0][2]);
            }
            //Atualizando a tela
            tTipoValida.moveToFront();
            if (!tTipoValida.isVisible())
                Util.centralizar(tTipoValida, this.Desktop);
            tTipoValida.setVisible(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cTipoValidaActionPerformed

    private void lTipoValidaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lTipoValidaItemStateChanged
        // TODO add your handling code here:
        if (lTValida != null && lTipoValida.getSelectedIndex() > -1){
            if (!bTipoValidaNovo.isEnabled()) {
                bTipoValidaNovo.setEnabled(true);
                bTipoValidaDeletar.setEnabled(true);
            }
            vTipoValidaCodigo.setText(lTValida[lTipoValida.getSelectedIndex()][0]);
            vTipoValida.setText(lTValida[lTipoValida.getSelectedIndex()][1]);
            vTipoValidaDescricao.setText(lTValida[lTipoValida.getSelectedIndex()][2]);
        }
    }//GEN-LAST:event_lTipoValidaItemStateChanged

    private void bTipoCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoCancelarActionPerformed
        // TODO add your handling code here:
        
        int i = lTipo.getSelectedIndex();
        if (!bTipoNovo.isEnabled()) {
            bTipoNovo.setEnabled(true);
            bTipoDeletar.setEnabled(true);
            Util.Seleciona(ListOrigem,lTipoOrigem,ListTipoValida,lTipoTValida,ListTipo[i]);
            vTipoCodigo.setText(ListTipo[i][0]);
            vTipoDescricao.setText(ListTipo[i][3]);
            vTipoValida1.setText(ListTipo[i][4]);
            vTipoValida2.setText(ListTipo[i][5]);
            vTipoOpcao.setSelectedIndex(Integer.parseInt(ListTipo[i][6]));
            vCInferior.setText(ListTipo[i][7]);
            vCSuperior.setText(ListTipo[i][8]);
            vCPeso.setText(ListTipo[i][15]);
            vUTempo.setText(ListTipo[i][9]);
            vUPeso.setText(ListTipo[i][16]);
            vAPeso.setText(ListTipo[i][17]);
            vFTempo.setText(ListTipo[i][10]);
            vFPeso.setText(ListTipo[i][18]);
            vSOpcao1.setSelectedIndex(Integer.parseInt(ListTipo[i][11]));
            vSOpcao2.setSelectedIndex(Integer.parseInt(ListTipo[i][13]));
            vSValor1.setText(ListTipo[i][12]);
            vSValor2.setText(ListTipo[i][14]);
            vSPeso.setText(ListTipo[i][19]);
            vCGerar.setSelected(ListTipo[i][20].equals("1")?true: false);
            vSGerar.setSelected(ListTipo[i][21].equals("1")?true: false);
        }
        else if (!vTipoDescricao.getText().equals(ListTipo[i][3])
                || !vTipoValida1.getText().equals(ListTipo[i][4])
                || !vTipoValida2.getText().equals(ListTipo[i][5])
                || !ListOrigem[lTipoOrigem.getSelectedIndex()][0].equals(ListTipo[i][1])
                || !ListTipoValida[lTipoTValida.getSelectedIndex()][0].equals(ListTipo[i][2])
                || vTipoOpcao.getSelectedIndex() != Integer.parseInt(ListTipo[i][6])
                || !vCInferior.getText().equals(ListTipo[i][7])
                || !vCSuperior.getText().equals(ListTipo[i][8])
                || !vCPeso.getText().equals(ListTipo[i][15])
                || !vUTempo.getText().equals(ListTipo[i][9])
                || !vUPeso.getText().equals(ListTipo[i][16])
                || !vAPeso.getText().equals(ListTipo[i][17])
                || !vFTempo.getText().equals(ListTipo[i][10])
                || !vFPeso.getText().equals(ListTipo[i][18])
                || vSOpcao1.getSelectedIndex() != Integer.parseInt(ListTipo[i][11])
                || vSOpcao2.getSelectedIndex() != Integer.parseInt(ListTipo[i][13])
                || !vSValor1.getText().equals(ListTipo[i][12])
                || !vSValor2.getText().equals(ListTipo[i][14])
                || !vSPeso.getText().equals(ListTipo[i][19])
                || !ListTipo[i][20].equals(vCGerar.isSelected()?"1": "0")
                || !ListTipo[i][21].equals(vSGerar.isSelected()?"1": "0")){
            Util.Seleciona(ListOrigem,lTipoOrigem,ListTipoValida,lTipoTValida,ListTipo[i]);
            vTipoCodigo.setText(ListTipo[i][0]);
            vTipoDescricao.setText(ListTipo[i][3]);
            vTipoValida1.setText(ListTipo[i][4]);
            vTipoValida2.setText(ListTipo[i][5]);
            vTipoOpcao.setSelectedIndex(Integer.parseInt(ListTipo[i][6]));
            vCInferior.setText(ListTipo[i][7]);
            vCSuperior.setText(ListTipo[i][8]);
            vCPeso.setText(ListTipo[i][15]);
            vUTempo.setText(ListTipo[i][9]);
            vUPeso.setText(ListTipo[i][16]);
            vAPeso.setText(ListTipo[i][17]);
            vFTempo.setText(ListTipo[i][10]);
            vFPeso.setText(ListTipo[i][18]);
            vSOpcao1.setSelectedIndex(Integer.parseInt(ListTipo[i][11]));
            vSOpcao2.setSelectedIndex(Integer.parseInt(ListTipo[i][13]));
            vSValor1.setText(ListTipo[i][12]);
            vSValor2.setText(ListTipo[i][14]);
            vSPeso.setText(ListTipo[i][19]);
            vCGerar.setSelected(ListTipo[i][20].equals("1")?true: false);
            vSGerar.setSelected(ListTipo[i][21].equals("1")?true: false);
        }
        else{
            tTipo.dispose();
            ListTipo = null;
            ListOrigem = null;
            ListTipoValida = null;
        }
    }//GEN-LAST:event_bTipoCancelarActionPerformed

    private void bTipoDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoDeletarActionPerformed
        // TODO add your handling code here:
        int resposta;
        if (vTipoCodigo.getText().length() > 0){
            if (Tipo.Verifica(Integer.parseInt(vTipoCodigo.getText()))){
                resposta = JOptionPane.showConfirmDialog(null, "Deletar o Tipo?","Apagar?",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (resposta == JOptionPane.YES_NO_OPTION) {
                    try {
                        if (Tipo.Deleta(Integer.parseInt(vTipoCodigo.getText()))) {
                            ListTipo = null;
                            vTipoCodigo.setText("");
                            vTipoDescricao.setText("");
                            vTipoValida1.setText("");
                            vTipoValida2.setText("");
                            vTipoOpcao.setSelectedIndex(0);
                            vCInferior.setText("");
                            vCSuperior.setText("");
                            vCPeso.setText("");
                            vUTempo.setText("");
                            vUPeso.setText("");
                            vAPeso.setText("");
                            vFTempo.setText("");
                            vFPeso.setText("");
                            vSOpcao1.setSelectedIndex(0);
                            vSOpcao2.setSelectedIndex(0);
                            vSValor1.setText("");
                            vSValor2.setText("");
                            vSPeso.setText("");
                            vCGerar.setSelected(false);
                            vSGerar.setSelected(false);
                            lTipo.removeAllItems();

                            //Carregando os dados da tabela
                            ListTipo = Tipo.Carrega(lTipo);
                            if (ListTipo != null){
                                Util.Seleciona(ListOrigem,lTipoOrigem,ListTipoValida,lTipoTValida,ListTipo[lTipo.getSelectedIndex()]);
                                vTipoCodigo.setText(ListTipo[0][0]);
                                vTipoDescricao.setText(ListTipo[0][3]);
                                vTipoValida1.setText(ListTipo[0][4]);
                                vTipoValida2.setText(ListTipo[0][5]);
                                vTipoOpcao.setSelectedIndex(Integer.parseInt(ListTipo[0][6]));
                                vCInferior.setText(ListTipo[0][7]);
                                vCSuperior.setText(ListTipo[0][8]);
                                vCPeso.setText(ListTipo[0][15]);
                                vUTempo.setText(ListTipo[0][9]);
                                vUPeso.setText(ListTipo[0][16]);
                                vAPeso.setText(ListTipo[0][17]);
                                vFTempo.setText(ListTipo[0][10]);
                                vFPeso.setText(ListTipo[0][18]);
                                vSOpcao1.setSelectedIndex(Integer.parseInt(ListTipo[0][11]));
                                vSOpcao2.setSelectedIndex(Integer.parseInt(ListTipo[0][13]));
                                vSValor1.setText(ListTipo[0][12]);
                                vSValor2.setText(ListTipo[0][14]);
                                vSPeso.setText(ListTipo[0][19]);
                                vCGerar.setSelected(ListTipo[0][20].equals("1")?true: false);
                                vSGerar.setSelected(ListTipo[0][21].equals("1")?true: false);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(rootPane, "Não é possível excluir o Tipo. Já existe vinculo dele com dados de entrada e saída!", "Aviso", WIDTH);
            }
        }
    }//GEN-LAST:event_bTipoDeletarActionPerformed

    private void bTipoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoSalvarActionPerformed
        // TODO add your handling code here:
        if (vTipoDescricao.getText().length() > 0 && vTipoValida1.getText().length() > 0){
            if (bTipoNovo.isEnabled()){ 
                try {
                    //Atualizando
                    int i = lTipo.getSelectedIndex();
                    ListTipo[i][1] = ListOrigem[lTipoOrigem.getSelectedIndex()][0];
                    ListTipo[i][2] = ListTipoValida[lTipoTValida.getSelectedIndex()][0];
                    ListTipo[i][3] = vTipoDescricao.getText();
                    ListTipo[i][4] = vTipoValida1.getText();
                    ListTipo[i][5] = vTipoValida2.getText();
                    ListTipo[i][6] = Integer.toString(vTipoOpcao.getSelectedIndex());
                    ListTipo[i][7] = vCInferior.getText();
                    ListTipo[i][8] = vCSuperior.getText();
                    ListTipo[i][15] = vCPeso.getText();
                    ListTipo[i][9] = vUTempo.getText();
                    ListTipo[i][16] = vUPeso.getText();
                    ListTipo[i][17] = vAPeso.getText();
                    ListTipo[i][10] = vFTempo.getText();
                    ListTipo[i][18] = vFPeso.getText();
                    ListTipo[i][11] = Integer.toString(vSOpcao1.getSelectedIndex());
                    ListTipo[i][13] = Integer.toString(vSOpcao2.getSelectedIndex());
                    ListTipo[i][12] = vSValor1.getText();
                    ListTipo[i][14] = vSValor2.getText();
                    ListTipo[i][19] = vSPeso.getText();
                    ListTipo[i][20] = vCGerar.isSelected()?"1":"0";
                    ListTipo[i][21] = vSGerar.isSelected()?"1":"0";
                    if (Tipo.Atualiza(ListTipo[i])){
                        lTipo.removeItemAt(i);
                        lTipo.insertItemAt(ListTipo[i][0]+"  "+ListTipo[i][3], i);
                        lTipo.setSelectedIndex(i);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else { 
                try {
                    //Incluir
                    if (vTipoValida2.getText().length() == 0)
                        vTipoValida2.setText("0.0"); //Valor padrão para o campo sem valor.
                    if (Tipo.Incluir(Integer.parseInt(ListOrigem[lTipoOrigem.getSelectedIndex()][0]),
                                     Integer.parseInt(ListTipoValida[lTipoTValida.getSelectedIndex()][0]),
                                     vTipoDescricao.getText(),Double.parseDouble(vTipoValida1.getText()),
                                     Double.parseDouble(vTipoValida2.getText()),
                                     vTipoOpcao.getSelectedIndex(),
                                     Integer.parseInt(vCInferior.getText()),
                                     Integer.parseInt(vCSuperior.getText()),
                                     Double.parseDouble(vCPeso.getText()),
                                     Integer.parseInt(vUTempo.getText()),
                                     Double.parseDouble(vUPeso.getText()),
                                     Double.parseDouble(vAPeso.getText()),
                                     Integer.parseInt(vFTempo.getText()),
                                     Double.parseDouble(vFPeso.getText()),
                                     vSOpcao1.getSelectedIndex(),
                                     vSOpcao2.getSelectedIndex(),
                                     Integer.parseInt(vSValor1.getText()),
                                     Integer.parseInt(vSValor2.getText()),
                                     Double.parseDouble(vSPeso.getText()),
                                     vCGerar.isSelected()?1: 0,
                                     vSGerar.isSelected()?1:0
                                     )){
                        ListTipo = null;
                        lTipo.removeAllItems();

                        //Carregando os dados da tabela
                    ListTipo = Tipo.Carrega(lTipo);
                        lTipo.setSelectedIndex(ListTipo.length-1);
                        bTipoNovo.setEnabled(true);
                        bTipoDeletar.setEnabled(true);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Favor preencher todos os campos!", "Aviso", WIDTH);
            vTipoDescricao.grabFocus();
        }
    }//GEN-LAST:event_bTipoSalvarActionPerformed

    private void bTipoNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTipoNovoActionPerformed
        // TODO add your handling code here:
        bTipoNovo.setEnabled(false);
        bTipoDeletar.setEnabled(false);
        vTipoCodigo.setText("");
        vTipoDescricao.setText("");
        vTipoValida1.setText("");
        vTipoValida2.setText("");
        vTipoOpcao.setSelectedIndex(0);
        vCInferior.setText("0");
        vCSuperior.setText("0");
        vCPeso.setText("1.0");
        vUTempo.setText("0");
        vUPeso.setText("1.0");
        vAPeso.setText("1.0");
        vFTempo.setText("0");
        vFPeso.setText("1.0");
        vSOpcao1.setSelectedIndex(0);
        vSOpcao2.setSelectedIndex(0);
        vSValor1.setText("0");
        vSValor2.setText("0");
        vSPeso.setText("1.0");
        vCGerar.setSelected(false);
        vSGerar.setSelected(false);
        vTipoDescricao.grabFocus();
    }//GEN-LAST:event_bTipoNovoActionPerformed

    private void lTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lTipoItemStateChanged
        // TODO add your handling code here:
        if (ListTipo != null && ListOrigem != null && ListTipoValida != null && lTipo.getSelectedIndex() > -1){
            if (!bTipoNovo.isEnabled()) {
                bTipoNovo.setEnabled(true);
                bTipoDeletar.setEnabled(true);
            }
            Util.Seleciona(ListOrigem,lTipoOrigem,ListTipoValida,lTipoTValida,ListTipo[lTipo.getSelectedIndex()]);
            vTipoCodigo.setText(ListTipo[lTipo.getSelectedIndex()][0]);
            vTipoDescricao.setText(ListTipo[lTipo.getSelectedIndex()][3]);
            vTipoValida1.setText(ListTipo[lTipo.getSelectedIndex()][4]);
            vTipoValida2.setText(ListTipo[lTipo.getSelectedIndex()][5]);
            vTipoOpcao.setSelectedIndex(Integer.parseInt(ListTipo[lTipo.getSelectedIndex()][6]));
            vCInferior.setText(ListTipo[lTipo.getSelectedIndex()][7]);
            vCSuperior.setText(ListTipo[lTipo.getSelectedIndex()][8]);
            vCPeso.setText(ListTipo[lTipo.getSelectedIndex()][15]);
            vUTempo.setText(ListTipo[lTipo.getSelectedIndex()][9]);
            vUPeso.setText(ListTipo[lTipo.getSelectedIndex()][16]);
            vAPeso.setText(ListTipo[lTipo.getSelectedIndex()][17]);
            vFTempo.setText(ListTipo[lTipo.getSelectedIndex()][10]);
            vFPeso.setText(ListTipo[lTipo.getSelectedIndex()][18]);
            vSOpcao1.setSelectedIndex(Integer.parseInt(ListTipo[lTipo.getSelectedIndex()][11]));
            vSOpcao2.setSelectedIndex(Integer.parseInt(ListTipo[lTipo.getSelectedIndex()][13]));
            vSValor1.setText(ListTipo[lTipo.getSelectedIndex()][12]);
            vSValor2.setText(ListTipo[lTipo.getSelectedIndex()][14]);
            vSPeso.setText(ListTipo[lTipo.getSelectedIndex()][19]);
            vCGerar.setSelected(ListTipo[lTipo.getSelectedIndex()][20].equals("1")?true: false);
            vSGerar.setSelected(ListTipo[lTipo.getSelectedIndex()][21].equals("1")?true: false);
        }
    }//GEN-LAST:event_lTipoItemStateChanged

    private void cTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cTipoActionPerformed
        // TODO add your handling code here:
        try {
            //Caso já foi fechado, ativa e inclui no Desktop.
            if (tTipo.isClosed()) {
                tTipo.setClosed(false);
                Desktop.add(tTipo);
            }
            //Apagando dados do formulário
            if (!bTipoNovo.isEnabled()) {
                bTipoNovo.setEnabled(true);
                bTipoDeletar.setEnabled(true);
            }
            //Limpando os dados do formulário
            vTipoCodigo.setText("");
            vTipoDescricao.setText("");
            vTipoValida1.setText("");
            vTipoValida2.setText("");
            lTipo.removeAllItems();
            lTipoOrigem.removeAllItems();
            lTipoTValida.removeAllItems();
            
            //Carregando os dados da tabela
            ListTipo = Tipo.Carrega(lTipo);
            ListOrigem = Tipo.cOrigem(lTipoOrigem);
            ListTipoValida = Tipo.cTipoValida(lTipoTValida);
            if (ListTipo != null){
                Util.Seleciona(ListOrigem,lTipoOrigem,ListTipoValida,lTipoTValida,ListTipo[lTipo.getSelectedIndex()]);
                vTipoCodigo.setText(ListTipo[0][0]);
                vTipoDescricao.setText(ListTipo[0][3]);
                vTipoValida1.setText(ListTipo[0][4]);
                vTipoValida2.setText(ListTipo[0][5]);
                vTipoOpcao.setSelectedIndex(Integer.parseInt(ListTipo[0][6]));
                vCInferior.setText(ListTipo[0][7]);
                vCSuperior.setText(ListTipo[0][8]);
                vCPeso.setText(ListTipo[0][15]);
                vUTempo.setText(ListTipo[0][9]);
                vUPeso.setText(ListTipo[0][16]);
                vAPeso.setText(ListTipo[0][17]);
                vFTempo.setText(ListTipo[0][10]);
                vFPeso.setText(ListTipo[0][18]);
                vSOpcao1.setSelectedIndex(Integer.parseInt(ListTipo[0][11]));
                vSOpcao2.setSelectedIndex(Integer.parseInt(ListTipo[0][13]));
                vSValor1.setText(ListTipo[0][12]);
                vSValor2.setText(ListTipo[0][14]);
                vSPeso.setText(ListTipo[0][19]);
                vCGerar.setSelected(ListTipo[0][20].equals("1")?true: false);
                vSGerar.setSelected(ListTipo[0][21].equals("1")?true: false);
            }
            //Atualizando a tela
            tTipo.moveToFront();
            if (!tTipo.isVisible())
                Util.centralizar(tTipo, this.Desktop);
            tTipo.setVisible(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cTipoActionPerformed

    private void tTipoInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tTipoInternalFrameClosing
        // TODO add your handling code here:
        ListTipo = null;
        ListOrigem = null;
        ListTipoValida = null;
    }//GEN-LAST:event_tTipoInternalFrameClosing

    private void tTipoValidaInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tTipoValidaInternalFrameClosing
        // TODO add your handling code here:
        lTValida = null;
    }//GEN-LAST:event_tTipoValidaInternalFrameClosing

    private void tControlaRelatorioInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tControlaRelatorioInternalFrameClosing
        // TODO add your handling code here:
        if (tRelatorioEntrada.isVisible()) {
            tRelatorioEntrada.dispose();
            cPanelSaida = null;
            OpEntrada.setEnabled(false);
            aEntrada.setEnabled(false);
        }
        if (tRelatorioSaida.isVisible()) {
            tRelatorioSaida.dispose();
            cPanelSaida = null;
            OpSaida.setEnabled(false);
            aSaida.setEnabled(false);
        }
        if (tRelatorioMetricas.isVisible()) {
            tRelatorioMetricas.dispose();
            cPanelMetricas = null;
            C.setEnabled(false);
            U.setEnabled(false);
            A.setEnabled(false);
            F.setEnabled(false);
            S.setEnabled(false);
            Q.setEnabled(false);
            aMetricas.setEnabled(false);
        }
        tControlaRelatorio.setVisible(false);
        parar = true;
    }//GEN-LAST:event_tControlaRelatorioInternalFrameClosing

    private void tRelatorioMetricasInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_tRelatorioMetricasInternalFrameClosing
        // TODO add your handling code here:
        cPanelMetricas = null;
        C.setEnabled(false);
        U.setEnabled(false);
        A.setEnabled(false);
        F.setEnabled(false);
        S.setEnabled(false);
        Q.setEnabled(false);
        aMetricas.setEnabled(false);
        if (!tRelatorioSaida.isVisible() && !tRelatorioEntrada.isVisible()) {
            tControlaRelatorio.setVisible(false);
            parar = true;
        }
    }//GEN-LAST:event_tRelatorioMetricasInternalFrameClosing

    private void tRelatorioMetricasComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tRelatorioMetricasComponentResized
        // TODO add your handling code here:
        if (tRelatorioMetricas.isVisible() && cPanelMetricas != null)
            Util.tamanhoGrafico(pGraficoMetricas,cPanelMetricas);
    }//GEN-LAST:event_tRelatorioMetricasComponentResized

    private void rMetricasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rMetricasActionPerformed
        // TODO add your handling code here:
        try {
            //Caso já foi fechado, ativa e inclui no Desktop.
            if (tControlaRelatorio.isClosed()) {
                tControlaRelatorio.setClosed(false);
                Desktop.add(tControlaRelatorio);
            }
            tControlaRelatorio.moveToFront();
            if (tRelatorioMetricas.isClosed()) {
                tRelatorioMetricas.setClosed(false);
                Desktop.add(tRelatorioMetricas);
            }
            C.setEnabled(true);
            U.setEnabled(true);
            A.setEnabled(true);
            F.setEnabled(true);
            S.setEnabled(true);
            Q.setEnabled(true);
            aMetricas.setEnabled(true);
            tRelatorioMetricas.moveToFront();
            if (!tRelatorioMetricas.isVisible()){
                if (!tRelatorioEntrada.isVisible() && !tRelatorioSaida.isVisible()){
                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Calendar dt = Calendar.getInstance();
                    dt.setTime(new Date());
                    DtFim.setText(f.format(dt.getTime()));
                    dt.add(Calendar.DAY_OF_YEAR,-1);
                    DtIni.setText(f.format(dt.getTime()));
                    sTipo = BancoUtil.sTipo();
                    Carrega(DadosTipo,sTipo);
                    parar = false;
                    AtualizaGrafico = new tAtualizaGrafico("Controle");
                    AtualizaGrafico.start();  //Thread de controle de tipos
                }
                Util.centralizar(tRelatorioMetricas, this.Desktop);
                boolean metricas[] = new boolean[6];
                metricas[0] = C.isSelected();
                metricas[1] = U.isSelected();
                metricas[2] = A.isSelected();
                metricas[3] = F.isSelected();
                metricas[4] = S.isSelected();
                metricas[5] = Q.isSelected();
                if (OpRegistros.isSelected())
                    cPanelMetricas = Util.criaGraficoMetricas(pGraficoMetricas,Integer.parseInt(qRegistro.getText()),"qc_saida","Medidas da QoC",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,metricas,null);
                else if (OpPeriodo.isSelected()){
                    String[] Periodo = new String[2];
                    Periodo[0] = DtIni.getText();
                    Periodo[1] = DtFim.getText();
                    cPanelMetricas = Util.criaGraficoMetricas(pGraficoMetricas,Integer.parseInt(qRegistro.getText()),"qc_saida","Medidas da QoC",Integer.parseInt(sTipo[DadosTipo.getSelectedIndex()][0]),sTipo[DadosTipo.getSelectedIndex()][1],tGrafico.getSelectedIndex()+1,metricas,Periodo);
                }
                tControlaRelatorio.setVisible(true);
                tRelatorioMetricas.setVisible(true);
            }
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rMetricasActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        try {
            if (tResumoMetricas.isClosed()) {
                tResumoMetricas.setClosed(false);
                Desktop.add(tResumoMetricas);
            }
            tResumoMetricas.moveToFront();
            tResumoMetricas.setVisible(true);
            Tipos = BancoUtil.sTipo();
            if (Tipos != null){
                Carrega(ListaTipoResumo,Tipos);
                int t1 = 0,t2 = 0;
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Calendar dt = Calendar.getInstance();
                dt.setTime(new Date());
                DtFimResumo.setText(f.format(dt.getTime()));
                dt.add(Calendar.DAY_OF_YEAR,-1);
                DtIniResumo.setText(f.format(dt.getTime()));
                String[] Periodo = new String[2];
                Periodo[0] = DtIniResumo.getText();
                Periodo[1] = DtFimResumo.getText();
                t1 = BancoUtil.Metricas("coverage",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("coverage",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    CP.setText(((t2*100)/(t1+t2))+"%");
                else
                    CP.setText("0");
                t1 = BancoUtil.Metricas("accuracy",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("accuracy",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    AP.setText(((t2*100)/(t1+t2))+"%");
                else
                    AP.setText("0");
                t1 = BancoUtil.Metricas("frequency",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("frequency",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    FP.setText(((t2*100)/(t1+t2))+"%");
                else
                    FP.setText("0");
                t1 = BancoUtil.Metricas("significance",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("significance",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    SP.setText(((t2*100)/(t1+t2))+"%");
                else
                    SP.setText("0");
                int temp;
                temp = BancoUtil.Status("qc_entrada", Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]));
                if (temp > 0){
                    Status.setForeground(new java.awt.Color(0, 0, 255));
                    Status.setText("Ativa");
                }
                else{
                    Status.setForeground(new java.awt.Color(255, 0, 0));
                    Status.setText("Inativa");
                }
                temp = BancoUtil.Status("qc_saida", Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]));
                if (temp > 0){
                    Processamento.setForeground(new java.awt.Color(0, 0, 255));
                    Processamento.setText("Ligado");
                }
                else{
                    Processamento.setForeground(new java.awt.Color(255, 0, 0));
                    Processamento.setText("Desligado");
                }
            }
        } catch (PropertyVetoException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (Tipos != null){
            try {
                int t1 = 0,t2 = 0;
                String[] Periodo = new String[2];
                Periodo[0] = DtIniResumo.getText();
                Periodo[1] = DtFimResumo.getText();
                t1 = BancoUtil.Metricas("coverage",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("coverage",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    CP.setText(((t2*100)/(t1+t2))+"%");
                else
                    CP.setText("0");
                t1 = BancoUtil.Metricas("accuracy",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("accuracy",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    AP.setText(((t2*100)/(t1+t2))+"%");
                else
                    AP.setText("0");
                t1 = BancoUtil.Metricas("frequency",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("frequency",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    FP.setText(((t2*100)/(t1+t2))+"%");
                else
                    FP.setText("0");
                t1 = BancoUtil.Metricas("significance",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),0,Periodo);
                t2 = BancoUtil.Metricas("significance",Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]),1,Periodo);
                if (t1 > 0 || t2 > 0)
                    SP.setText(((t2*100)/(t1+t2))+"%");
                else
                    SP.setText("0");
                int temp;
                temp = BancoUtil.Status("qc_entrada", Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]));
                if (temp > 0){
                    Status.setForeground(new java.awt.Color(0, 0, 255));
                    Status.setText("Ativa");
                }
                else{
                    Status.setForeground(new java.awt.Color(255, 0, 0));
                    Status.setText("Inativa");
                }
                temp = BancoUtil.Status("qc_saida", Integer.parseInt(Tipos[ListaTipoResumo.getSelectedIndex()][0]));
                if (temp > 0){
                    Processamento.setForeground(new java.awt.Color(0, 0, 255));
                    Processamento.setText("Ligado");
                }
                else{
                    Processamento.setForeground(new java.awt.Color(255, 0, 0));
                    Processamento.setText("Desligado");
                }
            } catch (ParseException ex) {
                Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void bOrigemNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOrigemNovoActionPerformed
        // TODO add your handling code here:
        bOrigemNovo.setEnabled(false);
        bOrigemDeletar.setEnabled(false);
        vOrigemCodigo.setText("");
        vOrigemNome.setText("");
        vOrigemInformacoes.setText("");
        vOrigemGateway.setText("");
        vOrigemAplicacao.setText("");
        vOrigemNome.grabFocus();
    }//GEN-LAST:event_bOrigemNovoActionPerformed

    private void cOrigemNomeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cOrigemNomeItemStateChanged
        // TODO add your handling code here:
        SelecionaOrigem();
        if (!bOrigemNovo.isEnabled()) {
            bOrigemNovo.setEnabled(true);
            bOrigemDeletar.setEnabled(true);
        }
    }//GEN-LAST:event_cOrigemNomeItemStateChanged

    private void bOrigemCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOrigemCancelarActionPerformed
        // TODO add your handling code here:
        if (!bOrigemNovo.isEnabled()) {
            bOrigemNovo.setEnabled(true);
            bOrigemDeletar.setEnabled(true);
            SelecionaOrigem();
        }
        else
        tOrigem.dispose();
    }//GEN-LAST:event_bOrigemCancelarActionPerformed

    private void bOrigemDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOrigemDeletarActionPerformed
        // TODO add your handling code here:
        int resposta;
        if (Origem.Verifica(Integer.parseInt(vOrigemCodigo.getText()))) {
            resposta = JOptionPane.showConfirmDialog(null, "Deletar a Origem?","Apagar?",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (resposta == JOptionPane.YES_NO_OPTION) {
                try {
                    if (Origem.Deleta(Integer.parseInt(vOrigemCodigo.getText()))) {
                        int numero = Origem.nLinhas();
                        vCodigo = new Integer[numero];
                        vInformacoes = new String[numero];
                        cOrigemNome.removeAllItems();
                        vGateway = new String[numero];
                        vAplicacao = new String[numero];
                        Origem.Dados(vInformacoes, vCodigo, cOrigemNome, vGateway, vAplicacao);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(rootPane, "Não é possível excluir a Origem, pois existem tipos vinculados a ela!", "Aviso", WIDTH);
        }
    }//GEN-LAST:event_bOrigemDeletarActionPerformed

    private void bOrigemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOrigemSalvarActionPerformed
        // TODO add your handling code here:
        int n;
        int numero = Origem.nLinhas();
        if (!bOrigemNovo.isEnabled() && vOrigemNome.getText().length() > 0) {
            bOrigemNovo.setEnabled(true);
            bOrigemDeletar.setEnabled(true);
            try {
                if (Origem.Inclui(vOrigemNome.getText(),vOrigemInformacoes.getText(),vOrigemGateway.getText(),vOrigemAplicacao.getText())) {
                    numero = Origem.nLinhas();
                    n = cOrigemNome.getItemCount();
                    vCodigo = new Integer[numero];
                    vInformacoes = new String[numero];
                    cOrigemNome.removeAllItems();
                    vGateway = new String[numero];
                    vAplicacao = new String[numero];
                    Origem.Dados(vInformacoes, vCodigo, cOrigemNome, vGateway, vAplicacao);
                    cOrigemNome.setSelectedIndex(n);
                }
            } catch (SQLException ex) {
                Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (bOrigemNovo.isEnabled() && vOrigemNome.getText().length() > 0){
            try {
                if (Origem.Alterar(Integer.parseInt(vOrigemCodigo.getText()),vOrigemNome.getText(),vOrigemInformacoes.getText(),vOrigemGateway.getText(),vOrigemAplicacao.getText())) {
                    n = cOrigemNome.getSelectedIndex();
                    vCodigo = new Integer[numero];
                    vInformacoes = new String[numero];
                    cOrigemNome.removeAllItems();
                    vGateway = new String[numero];
                    vAplicacao = new String[numero];
                    Origem.Dados(vInformacoes, vCodigo, cOrigemNome, vGateway, vAplicacao);
                    cOrigemNome.setSelectedIndex(n);
                }
            } catch (SQLException ex) {
                Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            JOptionPane.showMessageDialog(rootPane, "Favor informar o nome da Origem!", "Aviso", WIDTH);
            vOrigemNome.grabFocus();
        }
    }//GEN-LAST:event_bOrigemSalvarActionPerformed

    private void cOrigemCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cOrigemCodigoKeyPressed
        // TODO add your handling code here:
        int n;
        if (evt.getKeyCode() == 10) {
            n = Util.Busca(Integer.parseInt(cOrigemCodigo.getText()),vCodigo);
            if (n >= 0){
                cOrigemNome.setSelectedIndex(n);
                SelecionaOrigem();
                if (!bOrigemNovo.isEnabled()) {
                    bOrigemNovo.setEnabled(true);
                    bOrigemDeletar.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_cOrigemCodigoKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QoC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        final QoC principal = new QoC();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                principal.tLogin.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox A;
    private javax.swing.JLabel AP;
    private javax.swing.JCheckBox C;
    private javax.swing.JLabel CP;
    private javax.swing.JComboBox DadosTipo;
    private javax.swing.JDesktopPane Desktop;
    private javax.swing.JFormattedTextField DtFim;
    private javax.swing.JFormattedTextField DtFimResumo;
    private javax.swing.JFormattedTextField DtIni;
    private javax.swing.JFormattedTextField DtIniResumo;
    private javax.swing.JCheckBox F;
    private javax.swing.JLabel FP;
    private javax.swing.JComboBox ListaTipoResumo;
    private javax.swing.JCheckBox OpEntrada;
    private javax.swing.JCheckBox OpPeriodo;
    private javax.swing.JCheckBox OpRegistros;
    private javax.swing.JCheckBox OpSaida;
    private javax.swing.JLabel Processamento;
    private javax.swing.JCheckBox Q;
    private javax.swing.JCheckBox S;
    private javax.swing.JLabel SP;
    private javax.swing.JLabel Status;
    private javax.swing.JCheckBox U;
    private javax.swing.JCheckBox aEntrada;
    private javax.swing.JCheckBox aMetricas;
    private javax.swing.JCheckBox aSaida;
    private javax.swing.JMenuItem bAjuda;
    private javax.swing.JButton bAtualizar;
    private javax.swing.JMenuItem bDesligar;
    private javax.swing.JButton bFechar;
    private javax.swing.JButton bLogar;
    private javax.swing.JButton bOrigemCancelar;
    private javax.swing.JButton bOrigemDeletar;
    private javax.swing.JButton bOrigemNovo;
    private javax.swing.JButton bOrigemSalvar;
    private javax.swing.JMenuItem bSair;
    private javax.swing.JMenuItem bSobre;
    private javax.swing.JButton bTipoCancelar;
    private javax.swing.JButton bTipoDeletar;
    private javax.swing.JButton bTipoNovo;
    private javax.swing.JButton bTipoSalvar;
    private javax.swing.JButton bTipoValidaCancelar;
    private javax.swing.JButton bTipoValidaDeletar;
    private javax.swing.JButton bTipoValidaNovo;
    private javax.swing.JButton bTipoValidaSalvar;
    private javax.swing.JTextField cLogin;
    private javax.swing.JMenuItem cOrigem;
    private javax.swing.JTextField cOrigemCodigo;
    private javax.swing.JComboBox cOrigemNome;
    private javax.swing.JMenuItem cPerfil;
    private javax.swing.JPasswordField cSenha;
    private javax.swing.JMenuItem cTipo;
    private javax.swing.JMenuItem cTipoValida;
    private javax.swing.JMenuItem cUsuario;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox lTipo;
    private javax.swing.JComboBox lTipoOrigem;
    private javax.swing.JComboBox lTipoTValida;
    private javax.swing.JComboBox lTipoValida;
    private javax.swing.JMenu mAjuda;
    private javax.swing.JMenu mCadastro;
    private javax.swing.JMenuBar mPrincipal;
    private javax.swing.JMenu mProcessa;
    private javax.swing.JMenu mRelatorios;
    private javax.swing.JMenu mSair;
    private javax.swing.JMenu mUsuario;
    private javax.swing.JMenuItem pEntradaIniciar;
    private javax.swing.JMenuItem pEntradaParar;
    private javax.swing.JPanel pGraficoEntrada;
    private javax.swing.JPanel pGraficoMetricas;
    private javax.swing.JPanel pGraficoSaida;
    private javax.swing.JTextField qRegistro;
    private javax.swing.JMenuItem rEntrada;
    private javax.swing.JMenuItem rMetricas;
    private javax.swing.JMenuItem rSaida;
    private javax.swing.JInternalFrame tControlaRelatorio;
    private javax.swing.JComboBox tGrafico;
    private javax.swing.JFrame tLogin;
    private javax.swing.JInternalFrame tOrigem;
    private javax.swing.JInternalFrame tRelatorioEntrada;
    private javax.swing.JInternalFrame tRelatorioMetricas;
    private javax.swing.JInternalFrame tRelatorioSaida;
    private javax.swing.JInternalFrame tResumoMetricas;
    private javax.swing.JLabel tSenha;
    private javax.swing.JInternalFrame tSobre;
    private javax.swing.JInternalFrame tTipo;
    private javax.swing.JInternalFrame tTipoValida;
    private javax.swing.JLabel tTitulo;
    private javax.swing.JLabel tUsuario;
    private javax.swing.JTextPane textoSobre;
    private javax.swing.JTextField vAPeso;
    private javax.swing.JTabbedPane vAQoC;
    private javax.swing.JCheckBox vCGerar;
    private javax.swing.JTextField vCInferior;
    private javax.swing.JTextField vCPeso;
    private javax.swing.JTextField vCSuperior;
    private javax.swing.JTextField vFPeso;
    private javax.swing.JTextField vFTempo;
    private javax.swing.JTextField vOrigemAplicacao;
    private javax.swing.JTextField vOrigemCodigo;
    private javax.swing.JTextField vOrigemGateway;
    private javax.swing.JTextField vOrigemInformacoes;
    private javax.swing.JTextField vOrigemNome;
    private javax.swing.JCheckBox vSGerar;
    private javax.swing.JComboBox vSOpcao1;
    private javax.swing.JComboBox vSOpcao2;
    private javax.swing.JTextField vSPeso;
    private javax.swing.JTextField vSValor1;
    private javax.swing.JTextField vSValor2;
    private javax.swing.JTextField vTipoCodigo;
    private javax.swing.JTextField vTipoDescricao;
    private javax.swing.JComboBox vTipoOpcao;
    private javax.swing.JTextField vTipoValida;
    private javax.swing.JTextField vTipoValida1;
    private javax.swing.JTextField vTipoValida2;
    private javax.swing.JTextField vTipoValidaCodigo;
    private javax.swing.JTextField vTipoValidaDescricao;
    private javax.swing.JTextField vUPeso;
    private javax.swing.JTextField vUTempo;
    // End of variables declaration//GEN-END:variables


}
