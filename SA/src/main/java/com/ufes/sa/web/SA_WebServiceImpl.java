/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.web;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import com.ufes.sa.dao.RedeConf;
import com.ufes.sa.dao.RedeConfDAO;
import com.ufes.sa.dao.SaidaRedeDAO;
import com.ufes.sa.xml.SaidaRede;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

/**
 *
 * @author joever
 */
@WebService(serviceName = "SA_WebService",endpointInterface = "com.ufes.sa.web.SA_WebService")
public class SA_WebServiceImpl implements SA_WebService {
    private final int NUM_DIF_PROX = 3; //Número de registros para gerar a média.
    public final int DECIMAL = 100; //Duas casa decimais.

    @Override
    public Boolean enviaXMLCarga(String myXML) {
        //System.out.println(myXML);
        if (myXML.isEmpty())
            return null;
        else {
            XStream stream = new XStream(new Xpp3Driver());
            stream.autodetectAnnotations(true);
            stream.alias("InformaçõesDaColeta", SaidaRede.class);
            SaidaRede mySaidaRede = (SaidaRede) stream.fromXML(myXML);
            SaidaRedeDAO myDAO = new SaidaRedeDAO();
            myDAO.adiciona(mySaidaRede);
            //System.out.println("XML Carregado na BD");
            return true;
        }
    }

    @Override
    public Boolean enviaListaXMLCarga(String myXML) {
        if (myXML.isEmpty())
            return null;
        else {
            XStream stream = new XStream(new Xpp3Driver());
            stream.autodetectAnnotations(true);
            stream.alias("InformaçõesDaColeta", SaidaRede.class);
            List<SaidaRede> mySaidasRede = (List<SaidaRede>) stream.fromXML(myXML);
            SaidaRedeDAO myDAO = new SaidaRedeDAO();
            for (SaidaRede mySaidaRede : mySaidasRede)
                myDAO.adiciona(mySaidaRede);
            return true;
        }
    }

    @Override
    public String enviaXMLProcessar(String myXML) {
        if (!myXML.isEmpty()){
            XStream stream = new XStream(new Xpp3Driver());
            stream.autodetectAnnotations(true);
            stream.alias("InformaçõesDaColeta", SaidaRede.class);
            List<SaidaRede> mySRs = (List<SaidaRede>) stream.fromXML(myXML);
            String xml = processa(mySRs, 0.0);
            return xml;
        }else
            return null;
    }
    
    @Override
    public String getXML(Integer idRede, Integer idTM, String idUsuario, String dthrInicial, String dthrFinal, Boolean Op_QoC){
        String xml;
        SaidaRedeDAO myDAO = new SaidaRedeDAO();
        List<SaidaRede> mySRs = myDAO.getSaidasRede(idRede, idTM, idUsuario, dthrInicial, dthrFinal);
        if (Op_QoC)
            xml = processa(mySRs, 0.0);
        else {
            XStream stream = new XStream(new Xpp3Driver());
            stream.autodetectAnnotations(true);
            xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n".concat(stream.toXML(mySRs));
        }
        return xml;
    }
    
    @Override
    public String getXMLAccuracyMin(Integer idRede, Integer idTM, String idUsuario, String dthrInicial, String dthrFinal, Double AccuracyMin){
        String xml;
        SaidaRedeDAO myDAO = new SaidaRedeDAO();
        List<SaidaRede> mySRs = myDAO.getSaidasRede(idRede, idTM, idUsuario, dthrInicial, dthrFinal);
        xml = processa(mySRs, AccuracyMin);
        return xml;
    }
    
    private String processa (List<SaidaRede> mySRs, Double AccuracyMin){
        RedeConfDAO myDAO = new RedeConfDAO();
        RedeConf myRC = myDAO.getRedeConf(mySRs.get(0).getIdRede(), mySRs.get(0).getIdTM(), mySRs.get(0).getCpf());
        System.out.println("Valor Médio: "+myRC.getaValorMedio());
        String valorAnterior = myRC.getaValorMedio(), DtHrAnteriorGeracao = "";
        Double pValorRede = Double.parseDouble(myRC.getaPercentualAceitavel());
        String tempoDeVida = myRC.getuTempoVida();
        String rAccuracy = "0.0";
        String rUpToDateness = "";
        String rCoverage = "", lInf = myRC.getcInf(), lSup = myRC.getcSup();
        String rSignificance = "", Op1 = myRC.getsOp1(), Valor1 = myRC.getsValor1(), Op2 = myRC.getsOp2(), Valor2 = myRC.getsValor2();
        String rFrequency = "", freqGeracao = myRC.getfFrequenciaGeracao();
        String pAccuracy = myRC.getQocPA(), pUpToDateness = myRC.getQocPU(), pCoverage = myRC.getQocPC(), pSignificance = myRC.getQocPS(), pFrequency = myRC.getQocPF();
        String rQoS = "";
        Integer i = 0;
        Boolean removeItem = false;
        List<SaidaRede> newSRs = new ArrayList<>();
        for (SaidaRede mySR : mySRs){
            if (DtHrAnteriorGeracao.isEmpty())
                DtHrAnteriorGeracao = mySR.getDthrGeracao();
            // Calculo da Accuracy
            if (pValorRede > 0.0) {
                Double pDifValorElemento = calculaPercentualDiferenca(mySR.getValor(),valorAnterior);
                if (pDifValorElemento > pValorRede)
                    if (verificaProxPerDif(mySRs,i+1,i+NUM_DIF_PROX,pValorRede)) //Verifica próximos percentuais de diferenca (Lista, Indice inicial, Número de elementos)
                        if ((pDifValorElemento-pValorRede) > 100){
                            rAccuracy = "0.0";
                            if (AccuracyMin > 0.0)
                                removeItem = true;
                        }
                        else {
                            Double tempAccuracy = Math.rint((1 - ((pDifValorElemento-pValorRede)/100))*DECIMAL)/DECIMAL;
                            rAccuracy = Double.toString(tempAccuracy);
                            if (AccuracyMin > tempAccuracy)
                                removeItem = true;
                        }
                    else
                        rAccuracy = "1.0";
                else
                    rAccuracy = "1.0";
            }
            // Fim Calculo da Accuracy

            // Calculo do Up_To_Dateness
            try {
                if (!tempoDeVida.isEmpty())
                    rUpToDateness = Up_To_Dateness(mySR.getDthrGeracao(),tempoDeVida);
            } catch (ParseException ex) {
                Logger.getLogger(SA_WebServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Fim Calculo do Up_To_Dateness

            // Calculo do Coverage
            if (!lInf.isEmpty() && !lSup.isEmpty())
                rCoverage = Coverage(mySR.getValor(), lInf, lSup);
            // Fim Calculo do Coverage

            // Calculo da Significance
            if (!Op1.isEmpty() && !Valor1.isEmpty())
                rSignificance = Significance(mySR.getValor(), Op1, Valor1, Op2, Valor2);
            // Fim Calculo da Significance
            
            // Calculo da Frequency
            try {
                if (!freqGeracao.isEmpty())
                    rFrequency = Frequency(mySR.getDthrGeracao(), DtHrAnteriorGeracao, freqGeracao);
            } catch (ParseException ex) {
                Logger.getLogger(SA_WebServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Fim Calculo da Frequency

            // Calculo da Significance
            if (!rCoverage.isEmpty()  || !rUpToDateness.isEmpty() || !rAccuracy.isEmpty() || !rFrequency.isEmpty() || !rSignificance.isEmpty())
                rQoS = QoS(rCoverage,rUpToDateness,rAccuracy,rFrequency,rSignificance,pCoverage,pUpToDateness,pAccuracy,pFrequency,pSignificance);
            // Fim Calculo da Significance

            if (!removeItem) {
                valorAnterior = mySR.getValor();
                DtHrAnteriorGeracao = mySR.getDthrGeracao();
                removeItem = false;
                mySR.setAccuracy(rAccuracy);
                mySR.setUp_to_dateness(rUpToDateness);
                mySR.setCoverage(rCoverage);
                mySR.setSignificance(rSignificance);
                mySR.setFrequency(rFrequency);
                mySR.setQoc_geral(rQoS);
                newSRs.add(mySR);
            }
            i++;
        }
        XStream stream = new XStream(new Xpp3Driver());
        String xml;
        stream.autodetectAnnotations(true);
        xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n".concat(stream.toXML(newSRs));
        return xml;
    }
    
    private Double calculaPercentualDiferenca(String v1, String v2){ // V1 valor Atual e V2 valor anterior já validado.
        Double dif, numV1, numV2;
        numV1 = Double.parseDouble(v1);
        numV2 = Double.parseDouble(v2);
        if (numV1 > numV2)
            dif = numV1 - numV2;
        else
            dif = numV2 - numV1;
        return ((dif*100)/numV2);
    }
    
    // Se estiver ok, retorna false
    private Boolean verificaProxPerDif(List<SaidaRede> mySRs, Integer i, Integer limite, Double pValorRede){
        Boolean resultado = false;
        Integer nValidado = 0;
        for (int j = i; j <= limite; j++){
            if (j > (mySRs.size()-1)){
                if (nValidado == 0)
                    resultado = true;
                break;
            }
            Double pDifValorElemento = calculaPercentualDiferenca(mySRs.get(j).getValor(),mySRs.get(j-1).getValor());
            if (pDifValorElemento > pValorRede){
                resultado = true;
                break;
            }
            nValidado++;
        }
        return resultado;
    }
    
    private String Up_To_Dateness(String Valor1, String Tempo_Vida) throws ParseException {
        Double tVida = Double.parseDouble(Tempo_Vida);
        if (tVida > 0){
            Date D1,D0;
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            D1 = f.parse(Valor1);
            D0 = new Date();
            Long Dif = D0.getTime() - D1.getTime();
            Double Idade = Double.parseDouble(Dif.toString()) / 1000;
             if (Idade.intValue() < tVida)
                return Double.toString(Math.rint((1 - (Idade.intValue() / tVida))*DECIMAL)/DECIMAL);
        }
        return "0";
    }
    
    public String Coverage(String Valor, String F0, String F1) {
        Double vValor, vF0, vF1;
        vValor = Double.parseDouble(Valor);
        vF0 = Double.parseDouble(F0);
        vF1 = Double.parseDouble(F1);
        if (vValor >= vF0 && vValor <= vF1)
            return "1";
        else {
            if (vValor < vF0) {
                Double dif = Math.abs(vF0 - vValor);
                dif = dif/vF0;
                if (dif > 1)
                    return "0";
                else
                    return Double.toString(Math.rint((1 - dif)*DECIMAL)/DECIMAL);
            }
            else {
                Double dif = Math.abs(vValor - vF1);
                dif = dif/vF0;
                if (dif > 1)
                    return "0";
                else
                    return Double.toString(Math.rint((1 - dif)*DECIMAL)/DECIMAL);
            }
        }
    }
    
    private String Frequency(String Valor1, String Valor0, String Tempo_Atualizacao) throws ParseException {
        int tAtualizacao = Integer.parseInt(Tempo_Atualizacao);
        if (tAtualizacao > 0){
            Date D1,D0;
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            D1 = f.parse(Valor1);
            D0 = f.parse(Valor0);
            Long Tempo = D1.getTime() - D0.getTime();
            Tempo = Tempo / 1000;
            if (Tempo.intValue() <= tAtualizacao)
                return "1";
        }
        return "0";
    }
    
    private String Significance(String Valor, String Op1, String Valor1, String Op2, String Valor2) {
        Double V = Double.parseDouble(Valor), V1 = Double.parseDouble(Valor1), V2 = 0.0;
        boolean critico1 = false;
        int temp1 = Integer.parseInt(Op1);
        int temp2 = 0;
        if (!Op2.isEmpty()){
            V2 = Double.parseDouble(Valor2);
            temp2 = Integer.parseInt(Op2);
        }
        //OP: "=" - 0, ">" - 1, "<" - 2, ">=" - 3, "<=" - 4 e "<>" - 5
        switch (temp1){
            case 0:
                if (Objects.equals(V, V1))
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
                if (!Objects.equals(V, V1))
                    critico1 = true;
                break;
        }
        if (!Op2.isEmpty())
            if ((temp2 == 0 && V2 != 0) || temp2 > 0){
                switch (temp2){
                    case 0:
                        if (Objects.equals(V, V2))
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
                        if (!Objects.equals(V, V2))
                            critico1 = true;
                        break;
                }
            }
        if (critico1){
            //System.out.println("É critico!!!");
            return "1";
        }
        //System.out.println("Não é critico!!!");
        return "0";
    }
    
    private String QoS(String C1, String U1, String A1, String F1, String S1, String PC1, String PU1, String PA1, String PF1, String PS1) {
        Double PC = Double.parseDouble(PC1);
        Double PU = Double.parseDouble(PU1);
        Double PA = Double.parseDouble(PA1);
        Double PF = Double.parseDouble(PF1);
        Double PS = Double.parseDouble(PS1);
        Double C;
        Double A;
        Double F;
        Double S;
        Double U;
        if (C1.isEmpty())
            C = 0.0;
        else
            C = Double.parseDouble(C1);
        if (A1.isEmpty())
            A = 0.0;
        else
            A = Double.parseDouble(A1);
        if (F1.isEmpty())
            F = 0.0;
        else
            F = Double.parseDouble(F1);
        if (S1.isEmpty())
            S = 0.0;
        else
            S = Double.parseDouble(S1);
        if (U1.isEmpty())
            U = 0.0;
        else
            U = Double.parseDouble(U1);
        return Double.toString(Math.rint(((C*PC+U*PU+A*PA+F*PF+S*PS)/(PC+PU+PA+PF+PS))*DECIMAL)/DECIMAL);
    }
}
