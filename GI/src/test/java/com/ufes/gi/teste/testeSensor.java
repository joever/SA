/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.Sensor;
import com.ufes.gi.dao.SensorDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeSensor {
    public static void main (String[] args) {
        cadastraSensor(2,"08603179786",1,1);
        imprimeListaSensor();
        atualizaSensor(2,"08603179786",2,1);
        //apagaSensor(2,"08603179786");
        imprimeListaSensor();
    }
    
    public static void cadastraSensor(Integer idSensor, String cpf, Integer idTipoSensor, Integer idModeloSensor){
        Sensor mySensor = new Sensor();
        mySensor.setIdSensor(idSensor);
        mySensor.setCpf(cpf);
        mySensor.setIdTipoSensor(idTipoSensor);
        mySensor.setIdModeloSensor(idModeloSensor);
        SensorDAO myDao = new SensorDAO();
        if (myDao.adiciona(mySensor))
            System.out.println("Cadastro com sucesso");
        else
            System.out.println("Erro ao cadastrar sensor");
    }
    
    public static void atualizaSensor(Integer idSensor, String cpf, Integer idTipoSensor, Integer idModeloSensor){
        Sensor mySensor = new Sensor();
        mySensor.setIdSensor(idSensor);
        mySensor.setCpf(cpf);
        mySensor.setIdTipoSensor(idTipoSensor);
        mySensor.setIdModeloSensor(idModeloSensor);
        SensorDAO myDao = new SensorDAO();
        if (myDao.atualiza(mySensor))
            System.out.println("Cadastro atualizado com sucesso");
        else
            System.out.println("Erro ao atualizar cadastro sensor");
    }
    
    public static void apagaSensor(Integer idSensor, String cpf){
        SensorDAO myDao = new SensorDAO();
        if (myDao.deleta(idSensor,cpf))
            System.out.println("Cadastro apagado com sucesso");
        else
            System.out.println("Erro ao apagar cadastro sensor");
    }
    
    public static void imprimeListaSensor(){
        SensorDAO myDAO = new SensorDAO();
        List<Sensor> mySensores = myDAO.getSensores();
        mySensores.stream().forEach((mySensor) -> {
            imprimeSensor(mySensor);
        });
    }
    
    public static void imprimeSensor(Sensor mySensor){
        System.out.println("ID: "+ mySensor.getIdSensor());
        System.out.println("CPF: "+ mySensor.getCpf());
        System.out.println("Tipo Sensor: " + mySensor.getIdTipoSensor());
        System.out.println("Modelo Sensor: "+ mySensor.getIdModeloSensor());
    }
}
