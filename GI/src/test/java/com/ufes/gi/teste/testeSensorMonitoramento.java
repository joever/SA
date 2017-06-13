/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.SensorMonitoramento;
import com.ufes.gi.dao.SensorMonitoramentoDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeSensorMonitoramento {
    public static void main (String[] args) {
        cadastraSM(1,"08603179786",2,400,10);
        imprimeListaSM();
        atualizaSM(1,"08603179786",2,400,1);
        //apagaSM();
        imprimeListaSM();
    }
    
    public static void cadastraSM(Integer idSensor, String cpf, Integer idTM, Integer media, Integer diferenca){
        SensorMonitoramento mySM = new SensorMonitoramento();
        mySM.setIdSensor(idSensor);
        mySM.setCpf(cpf);
        mySM.setIdTM(idTM);
        mySM.setMedia(media);
        mySM.setDiferenca(diferenca);
        SensorMonitoramentoDAO myDao = new SensorMonitoramentoDAO();
        if (myDao.adiciona(mySM))
            System.out.println("Cadastro com sucesso");
        else
            System.out.println("Erro ao cadastrar sensorMonitoramento");
    }
    
    public static void atualizaSM(Integer idSensor, String cpf, Integer idTM, Integer media, Integer diferenca){
        SensorMonitoramento mySM = new SensorMonitoramento();
        mySM.setIdSensor(idSensor);
        mySM.setCpf(cpf);
        mySM.setIdTM(idTM);
        mySM.setMedia(media);
        mySM.setDiferenca(diferenca);
        SensorMonitoramentoDAO myDao = new SensorMonitoramentoDAO();
        if (myDao.atualiza(mySM))
            System.out.println("Cadastro atualizado com sucesso");
        else
            System.out.println("Erro ao atualizar cadastro sensorMonitoramento");
    }
    
    public static void apagaSM(Integer idSensor, String cpf, Integer idTM, Integer idTF){
        SensorMonitoramentoDAO myDao = new SensorMonitoramentoDAO();
        if (myDao.deleta(idSensor,cpf,idTM, idTF))
            System.out.println("Cadastro apagado com sucesso");
        else
            System.out.println("Erro ao apagar cadastro sensorMonitoramento");
    }
    
    public static void imprimeListaSM(){
        SensorMonitoramentoDAO myDAO = new SensorMonitoramentoDAO();
        List<SensorMonitoramento> mySMs = myDAO.getSMs();
        mySMs.stream().forEach((mySM) -> {
            imprimeSM(mySM);
        });
    }
    
    public static void imprimeSM(SensorMonitoramento mySM){
        System.out.println("ID Sensor: "+ mySM.getIdSensor());
        System.out.println("CPF: "+ mySM.getCpf());
        System.out.println("Tipo Monitoramento: " + mySM.getIdTM());
        System.out.println("Valor Médio Gerado: "+ mySM.getMedia());
        System.out.println("Diferença Máxima Possível: "+ mySM.getDiferenca());
    }
}
