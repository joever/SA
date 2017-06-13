/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joever
 */
public class Util {
    public final int PROCESSA = 50; //Quantidade de registros que serão processados por ciclo.
    public final int MEDIA = 3; //Número de registros para gerar a média.
    public final int DECIMAL = 100; //Duas casa decimais.
    public final int T1 = 10000; //10s entre ciclos de verificações dos tipos.
    public final int T2 = 1000; //1s entre ciclos de análise da entrada e gerar a saída.
    public final int PROXIMO = 3; //Quantidade de próximos valores iguais para avaliar o dado que está sendo processado.
    
    public ControleThread tControle;
    
    public void CarregaThreads() {
        String[] tipos = BancoUtil.cTipo();
        tControle = new ControleThread("Controle");
        tControle.start();  //Thread de controle de Tipos
        //System.out.println("Thread de Controle Inicializada!");
        
        if (tipos != null){
            for (int i=0;i<tipos.length;i++){
                String[] controle; //QUANTIDADE, PROCESSADO e PROCESSAR
                controle = BancoUtil.carregaControle(Integer.parseInt(tipos[i]));
                if ("true".equals(controle[2])){
                    ControlaTipos novoTipo = new ControlaTipos("Tipo_"+tipos[i],Integer.parseInt(tipos[i]));
                    novoTipo.start();
                    //System.out.println("Iniciado processamento do tipo: " + tipos[i]);
                }
            }
        }
    }

    public class ControleThread extends Thread {
        int Tempo;
        public ControleThread(String nome){
            super(nome);
            Tempo = T1; // Acada 1s executa um ciclo
        }
        public void run(){
            while (true) {
                try {
                    cTipos();
                    Thread.sleep(Tempo);
                } catch (InterruptedException ex) {
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public class ControlaTipos extends Thread {
        int Tipo, Tempo, NadaProcessado;
        String processar;
        public ControlaTipos(String nome, int cTipo){
            super(nome);
            Tipo = cTipo; //Código do Tipo que será processado.
            Tempo = T2; //Acada 2s carrega no máximo PROCESSA vezes.
            NadaProcessado = 0; //Se ficar 10 ciclos sem processar nada, controle fica falso e a thread termina.
            processar = "true"; //Se foi criada a thred é pq existem dados para serem processados.
            //System.out.println("Iniciado o processamento do tipo: " + Tipo);
        }

        public void run(){
            while ("true".equals(processar)) {
                try {
                    if (vTipo(Tipo) == false)
                        NadaProcessado++;
                    String[] controle; //QUANTIDADE, PROCESSADO e PROCESSAR
                    controle = BancoUtil.carregaControle(Tipo);
                    processar = controle[2];
                    if (NadaProcessado >= 10){
                        BancoUtil.aControle("false",Tipo,Integer.parseInt(controle[1]));
                    }
                    else{
                        Thread.sleep(Tempo);
                    }
                } catch (InterruptedException ex) {
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //System.out.println("Terminado o processamento do tipo: " + Tipo);
            return;
        }
    }

    public void cTipos() throws SQLException {
        String tipos[];
        int tipo = 0, nEntrada;
        tipos = BancoUtil.cTipo();
        if (tipos != null){ //Temos tipos para controlar?
            for (int i = 0; i < tipos.length; i++){ //Analisa Tipo por Tipo
                boolean carregar = false;
                String processar = "false";
                tipo = Integer.parseInt(tipos[i]);
                if (!BancoUtil.eControle(tipo)){ //Existe Controle para esse Tipo?
                    //Tipo novo - Inclui
                    nEntrada = BancoUtil.nLinhas("qc_entrada",tipo);
                    if (nEntrada >= MEDIA){
                        processar = "true";
                        carregar = true;
                    }
                    BancoUtil.iControle(tipo,nEntrada,0,processar,"");
                }
                else {
                    //Tipo já existe - Atualiza
                    nEntrada = BancoUtil.nLinhas("qc_entrada",tipo);
                    String[] controle = BancoUtil.carregaControle(tipo); //QUANTIDADE, PROCESSADO e PROCESSAR
                    processar = controle[2];
                    if (nEntrada > Integer.parseInt(controle[0])){
                        if ("false".equals(processar) && (nEntrada - Integer.parseInt(controle[1])) >= MEDIA){ //Só irá mudar o campo processar se ele for falso.
                            processar = "true";
                            carregar = true;
                        }
                        BancoUtil.aControle(tipo,nEntrada,processar);
                    }
                }
                if (carregar){
                    ControlaTipos novoTipo = new ControlaTipos("Tipo_"+tipo,tipo);
                    novoTipo.start();
                }
            }
        }
    }
    
    public boolean vTipo(int Tipo) throws SQLException, ParseException {
        boolean AjustadoSaida = false;
        String[] controle; //QUANTIDADE, PROCESSADO e PROCESSAR
        controle = BancoUtil.carregaControle(Tipo);
        int a = Integer.parseInt(controle[1]);
        
        String[][] dadosSaida = BancoUtil.carregaEntrada(Tipo,a,PROCESSA); //DADOS1, DADOS2, DADOS3, DATA, COVERAGE, UP_TO_DATENESS, ACCURACY, FREQUENCY, SIGNIFICANCE E QOC
        if (dadosSaida != null) {
            int fProcessar = 0; //falta processar
            String[][] DadosJaProcessados = null;
            String[] tValida = BancoUtil.carregaTipoValida(Tipo); //TIPO, VALIDA1, VALIDA2, OPCAO, F_INFERIOR, F_SUPERIOR, C_GERAR, T_VIDA, T_ATUALIZACAO, OPERADOR1, VALOR1, OPERADOR2, VALOR2, S_GERAR, P_COVERAGE, P_UP_TO_DATENESS, P_ACCURACY, P_FREQUENCY E P_SIGNIFICANCE
            if (a >= MEDIA)
                DadosJaProcessados = BancoUtil.carregaSaida(Tipo,a-MEDIA,MEDIA); //DADOS1, DADOS2, DADOS3 e DATA
            if ("%".equals(tValida[0])){
                fProcessar = aSaidaPecente(dadosSaida,DadosJaProcessados,tValida); //Ajusta Saída
                if (fProcessar == dadosSaida.length){
                    return false;
                }
                AjustadoSaida = true;
            }
            else
                return false;
            if (AjustadoSaida) {
                int removido = BancoUtil.iSaida(dadosSaida,Tipo,fProcessar);
                if (removido >= 0){
                    BancoUtil.aControle(controle[2],Tipo,a+dadosSaida.length-fProcessar-removido);
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public int aSaidaPecente(String[][] dadosSaida, String[][] dadosJaProcessados, String[] tValida) throws ParseException {
        int i = 0;
        boolean removido = false;
        if (dadosJaProcessados == null){
            dadosSaida[i][5] = Up_To_Dateness(dadosSaida[i][3],tValida[7]);
            dadosSaida[i][6] = "1";
            dadosSaida[i][7] = "1";
            dadosSaida[i][10] = "0";
            dadosSaida[i][4] = Coverage(dadosSaida[i][0],tValida[4],tValida[5],tValida[6]);
            dadosSaida[i][8] = Significance(dadosSaida[i][0],tValida[9],tValida[10],tValida[11],tValida[12],tValida[13]);
            dadosSaida[i][9] = QoS(dadosSaida[i][4],dadosSaida[i][5],dadosSaida[i][6],dadosSaida[i][7],dadosSaida[i][8],tValida[14],tValida[15],tValida[16],tValida[17],tValida[18]);
            i = 1; //Zero já está OK (Primeiro registro)
            //System.out.println("Processamento Inicial");
            while (i < dadosSaida.length){
                dadosSaida[i][5] = Up_To_Dateness(dadosSaida[i][3],tValida[7]);
                dadosSaida[i][6] = "1";
                String Anterior = null;
                int j=1;
                while (Anterior == null){
                    //System.out.println("I: "+ i + "J: " + j);
                    if ("0".equals(dadosSaida[i-j][10]))
                        Anterior = dadosSaida[i-j][3];
                    j++;
                }
                dadosSaida[i][7] = Frequency(dadosSaida[i][3],Anterior,tValida[8]);
                dadosSaida[i][10] = "0";
                if (CalculaP(dadosSaida[i][0], dadosSaida[i-1][0]) > Double.parseDouble(tValida[1])) {
                    int temp = pDiferentes(dadosSaida,i,Double.parseDouble(tValida[1]));
                    if (temp > 0) {
                        //Os próximos valores são diferentes
                        if ("0".equals(tValida[3])){
                            dadosSaida[i][0] = dadosSaida[i-1][0];
                            dadosSaida[i][6] = "0";
                            removido = false;
                        }
                        else if ("1".equals(tValida[3])){
                            dadosSaida[i][10] = "1";
                            removido = true;
                        }
                        else {
                            dadosSaida[i][6] = "0";
                            removido = false;
                        }
                    }
                    else if (temp < 0) {
                        //Não foi possível definir os próximos valores, pois eles ainda não existem
                        return dadosSaida.length - i;
                    }
                    else
                        removido = false;
                    //System.out.println("******    FIM    *******");
                }
                else
                    removido = false;
                if (!removido){
                    dadosSaida[i][4] = Coverage(dadosSaida[i][0],tValida[4],tValida[5],tValida[6]);
                    dadosSaida[i][8] = Significance(dadosSaida[i][0],tValida[9],tValida[10],tValida[11],tValida[12],tValida[13]);
                    dadosSaida[i][9] = QoS(dadosSaida[i][4],dadosSaida[i][5],dadosSaida[i][6],dadosSaida[i][7],dadosSaida[i][8],tValida[14],tValida[15],tValida[16],tValida[17],tValida[18]);
                }
                i++;
            }
            //System.out.println("Concluído Processamento Inicial");
        }
        else{
            i = 0; //Iniciar o processamento pelo primeiro registro, pois já existe histórico
            //System.out.println("Processamento em Andamento");
            while (i < dadosSaida.length){
                dadosSaida[i][10] = "0";
                dadosSaida[i][5] = Up_To_Dateness(dadosSaida[i][3],tValida[7]);
                dadosSaida[i][6] = "1";
                if (i == 0){
                    dadosSaida[i][7] = Frequency(dadosSaida[i][3],dadosJaProcessados[MEDIA-1][3],tValida[8]);
                    if (CalculaP(dadosSaida[i][0], dadosJaProcessados[MEDIA-1][0]) > Double.parseDouble(tValida[1])) {
                        int temp = pDiferentes(dadosSaida,i,Double.parseDouble(tValida[1]));
                        if (temp > 0) {
                            //Os próximos valores são diferentes
                            if ("0".equals(tValida[3])){
                                dadosSaida[i][0] = dadosJaProcessados[MEDIA-1][0];
                                dadosSaida[i][6] = "0";
                                removido = false;
                            }
                            else if ("1".equals(tValida[3])){
                                dadosSaida[i][10] = "1";
                                removido = true;
                            }
                            else {
                                dadosSaida[i][6] = "0";
                                removido = false;
                            }
                        }
                        else if (temp < 0) {
                            //Não foi possível definir os próximos valores, pois eles ainda não existem
                            return dadosSaida.length - i;
                        }
                        else
                            removido = false;
                        //System.out.println("Valor Nov: "+dadosSaida[i][0]);
                        //System.out.println("******    FIM    *******");
                    }
                    else
                        removido = false;
                    if (!removido) {
                        dadosSaida[i][4] = Coverage(dadosSaida[i][0],tValida[4],tValida[5],tValida[6]);
                        dadosSaida[i][8] = Significance(dadosSaida[i][0],tValida[9],tValida[10],tValida[11],tValida[12],tValida[13]);
                        dadosSaida[i][9] = QoS(dadosSaida[i][4],dadosSaida[i][5],dadosSaida[i][6],dadosSaida[i][7],dadosSaida[i][8],tValida[14],tValida[15],tValida[16],tValida[17],tValida[18]);
                    }
                }
                else{
                    String Anterior = null;
                    int j=1;
                    while (Anterior == null){
                        if ((i-j) < 0)
                            Anterior = dadosJaProcessados[MEDIA-1][3];
                        else if ("0".equals(dadosSaida[i-j][10]))
                            Anterior = dadosSaida[i-j][3];
                        j++;
                    }
                    dadosSaida[i][7] = Frequency(dadosSaida[i][3],Anterior,tValida[8]);
                    if (CalculaP(dadosSaida[i][0], dadosSaida[i-1][0]) > Double.parseDouble(tValida[1])) {
                        int temp = pDiferentes(dadosSaida,i,Double.parseDouble(tValida[1]));
                        if (temp > 0) {
                            //Os próximos valores são diferentes
                            if ("0".equals(tValida[3])){
                                dadosSaida[i][0] = dadosSaida[i-1][0];
                                dadosSaida[i][6] = "0";
                                removido = false;
                            }
                            else if ("1".equals(tValida[3])){
                                dadosSaida[i][10] = "1";
                                removido = true;
                            }
                            else {
                                dadosSaida[i][6] = "0";
                                removido = false;
                            }
                        }
                        else if (temp < 0) {
                            //Não foi possível definir os próximos valores, pois eles ainda não existem
                            return dadosSaida.length - i;
                        }
                        else
                            removido = false;
                        //System.out.println("******    FIM    *******");
                    }
                    else
                        removido = false;
                    if (!removido) {
                        dadosSaida[i][4] = Coverage(dadosSaida[i][0],tValida[4],tValida[5],tValida[6]);
                        dadosSaida[i][8] = Significance(dadosSaida[i][0],tValida[9],tValida[10],tValida[11],tValida[12],tValida[13]);
                        dadosSaida[i][9] = QoS(dadosSaida[i][4],dadosSaida[i][5],dadosSaida[i][6],dadosSaida[i][7],dadosSaida[i][8],tValida[14],tValida[15],tValida[16],tValida[17],tValida[18]);
                    }
                }
                i++;
            }
            //System.out.println("Concluído Processamento em Andamento");
        }
        return 0;
    }
	
    public Double CalculaP(String A, String B){
        Double vA, vB, vDif;
        vA = Double.parseDouble(A);
        vB = Double.parseDouble(B);
        if (vA > vB)
            vDif = vA-vB;
        else
            vDif = vB-vA;
        return ((vDif*100)/vB);
    }
	
    public String Media(String[][] dados, int j){
        int cont = 0;
        Double total = 0.0;
        while (j >= 0){
            total += Double.parseDouble(dados[j][0]);
            j--;
            cont++;
        }
        total = total/cont;
        total = Math.rint(total*DECIMAL)/DECIMAL;
        return total.toString();
    }

    public String Media(String[][] dados, String[][] JaProcessados, int j){
        int cont = 0;
        Double total = 0.0;
        while (j >= 0){
            total += Double.parseDouble(dados[j][0]);
            j--;
            cont++;
        }
        j = MEDIA-1;
        while (cont <= MEDIA-1 && j >= 0){
            total += Double.parseDouble(JaProcessados[j][0]);
            j--;
            cont++;
        }
        total = total/cont;
        total = Math.rint(total*DECIMAL)/DECIMAL;
        return total.toString();
    }
    
    //Verifica se os próximos valores são parecidos com o atual
    public int pDiferentes(String[][] dadosSaida, int i, double Max) {
        int q = 0;
        if ((dadosSaida.length-(i+1+PROXIMO)) >= 0){ //É necessário verificar os próximos 2 valores, variavel i inicia no zero...
            int a = i+1;
            while (a <= (i+PROXIMO)){
                if (CalculaP(dadosSaida[a][0], dadosSaida[i][0]) > Max) {
                    q++;
                    break;
                }
                a++;
            }
        }
        else {
            q--;
        }
        return q;
    }
    
    public String Coverage(String Valor, String F0, String F1, String Calcular) {
        if (Double.parseDouble(Valor) >= Double.parseDouble(F0) && Double.parseDouble(Valor) <= Double.parseDouble(F1) && "1".equals(Calcular)){
            return "1";
        }
        else
            return "0";
    }
    
    public String Up_To_Dateness(String Valor1, String Tempo_Vida) throws ParseException {
        Double tVida = Double.parseDouble(Tempo_Vida);
        if (tVida > 0){
            Date D1,D0;
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            D1 = f.parse(Valor1);
            D0 = new Date();
            Long Dif = D0.getTime() - D1.getTime();
            Double Idade = Double.parseDouble(Dif.toString()) / 1000;
             if (Idade.intValue() < tVida)
                return Double.toString(Math.rint((1 - (Idade.intValue() / tVida))*DECIMAL)/DECIMAL);
        }
        return "0";
    }
    
    public String Frequency(String Valor1, String Valor0, String Tempo_Atualizacao) throws ParseException {
        int tAtualizacao = Integer.parseInt(Tempo_Atualizacao);
        if (tAtualizacao > 0){
            Date D1,D0;
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            D1 = f.parse(Valor1);
            D0 = f.parse(Valor0);
            Long Tempo = D1.getTime() - D0.getTime();
            Tempo = Tempo / 1000;
            if (Tempo.intValue() <= tAtualizacao)
                return "1";
        }
        return "0";
    }
    
    private String Significance(String Valor, String Op1, String Valor1, String Op2, String Valor2, String Calcular) {
        if ("1".equals(Calcular)){
            Double V = Double.parseDouble(Valor), V1 = Double.parseDouble(Valor1), V2 = Double.parseDouble(Valor2);
            boolean critico1 = false;
            int temp1 = Integer.parseInt(Op1), temp2 = Integer.parseInt(Op2);
            //OP: "=" - 0, ">" - 1, "<" - 2, ">=" - 3, "<=" - 4 e "<>" - 5
            switch (temp1){
                case 0:
                    if (V == V1)
                        critico1 = true;
                    break;
                case 1:
                    if (V > V1)
                        critico1 = true;
                    break;
                case 2:
                    if (V < V1)
                        critico1 = true;
                    break;
                case 3:
                    if (V >= V1)
                        critico1 = true;
                    break;
                case 4:
                    if (V <= V1)
                        critico1 = true;
                    break;
                case 5:
                    if (V != V1)
                        critico1 = true;
                    break;
            }
            if ((temp2 == 0 && V2 != 0) || temp2 > 0){
                switch (temp2){
                    case 0:
                        if (V == V2)
                            critico1 = true;
                        break;
                    case 1:
                        if (V > V2)
                            critico1 = true;
                        break;
                    case 2:
                        if (V < V2 && critico1)
                            critico1 = true;
                        else
                            critico1 = false;
                        break;
                    case 3:
                        if (V >= V2)
                            critico1 = true;
                        break;
                    case 4:
                        if (V <= V2 && critico1)
                            critico1 = true;
                        else
                            critico1 = false;
                        break;
                    case 5:
                        if (V != V2)
                            critico1 = true;
                        break;
                }
            }
            if (critico1){
                //System.out.println("É critico!!!");
                return "1";
            }
        }
        //System.out.println("Não é critico!!!");
        return "0";
    }
    
    public String QoS(String C1, String U1, String A1, String Cm1, String S1, String PC1, String PU1, String PA1, String PCm1, String PS1) {
        Double PC = Double.parseDouble(PC1);
        Double PU = Double.parseDouble(PU1);
        Double PA = Double.parseDouble(PA1);
        Double PCm = Double.parseDouble(PCm1);
        Double PS = Double.parseDouble(PS1);
        int C = Integer.parseInt(C1);
        int A = Integer.parseInt(A1);
        int Cm = Integer.parseInt(Cm1);
        int S = Integer.parseInt(S1);
        Double U = Double.parseDouble(U1);
        return Double.toString(Math.rint(((C*PC+U*PU+A*PA+Cm*PCm+S*PS)/(PC+PU+PA+PCm+PS))*DECIMAL)/DECIMAL);
    }
    
    public String[][] remove(String[][] aux, int n) {
        int j = 0, i = 0;
        String[][] saida = new String[aux.length - 1][10];
        while (j < aux.length){
            if (j != n){
                System.arraycopy(aux[j], 0, saida[i], 0, 10);
                i++;
                j++;
            }
            else
                j++;
        }
        return saida;
    }
}