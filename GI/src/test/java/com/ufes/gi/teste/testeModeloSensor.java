/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.ModeloSensor;
import com.ufes.gi.dao.ModeloSensorDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeModeloSensor {
    public static void main (String[] args) {
        cadastraModeloSensor(1,"Nó Sensor","Nó sensor que permite acoplar apenas 1 (um) tipo de sensor",1);
        cadastraModeloSensor(2,"Nó Sensor","Nó sensor que permite acoplar até 2 (dois) tipo de sensor",2);
        imprimeListaModeloSensor();
        atualizaModeloSensor(1,"Nó Sensor [1]","Nó sensor que permite acoplar apenas 1 (um) tipo de sensor",1);
        atualizaModeloSensor(2,"Nó Sensor [2]","Nó sensor que permite acoplar até 2 (dois) tipos de sensores",2);
        //apagaModeloSensor(1);
        //apagaModeloSensor(2);
        imprimeListaModeloSensor();
    }
    
    public static void cadastraModeloSensor(Integer idModeloSensor, String nome, String descricao, Integer nSensores){
        ModeloSensor mySensor = new ModeloSensor();
        mySensor.setIdModeloSensor(idModeloSensor);
        mySensor.setNome(nome);
        mySensor.setDescricao(descricao);
        mySensor.setnSensores(nSensores);
        ModeloSensorDAO myDao = new ModeloSensorDAO();
        if (myDao.adiciona(mySensor))
            System.out.println("Cadastro com sucesso");
        else
            System.out.println("Erro ao cadastrar sensor");
    }
    
    public static void atualizaModeloSensor(Integer idModeloSensor, String nome, String descricao, Integer nSensores){
        ModeloSensor mySensor = new ModeloSensor();
        mySensor.setIdModeloSensor(idModeloSensor);
        mySensor.setNome(nome);
        mySensor.setDescricao(descricao);
        mySensor.setnSensores(nSensores);
        ModeloSensorDAO myDao = new ModeloSensorDAO();
        if (myDao.atualiza(mySensor))
            System.out.println("Cadastro atualizado com sucesso");
        else
            System.out.println("Erro ao atualizar cadastro sensor");
    }
    
    public static void apagaModeloSensor(Integer idModeloSensor){
        ModeloSensorDAO myDao = new ModeloSensorDAO();
        if (myDao.deleta(idModeloSensor))
            System.out.println("Cadastro apagado com sucesso");
        else
            System.out.println("Erro ao apagar cadastro sensor");
    }
    
    public static void imprimeListaModeloSensor(){
        ModeloSensorDAO myDAO = new ModeloSensorDAO();
        List<ModeloSensor> mySensores = myDAO.getModeloSensores();
        mySensores.stream().forEach((mySensor) -> {
            imprimeModeloSensor(mySensor);
        });
    }
    
    public static void imprimeModeloSensor(ModeloSensor mySensor){
        System.out.println("ID: "+ mySensor.getIdModeloSensor());
        System.out.println("Nome: "+ mySensor.getNome());
        System.out.println("Descrição: "+ mySensor.getDescricao());
        System.out.println("Número de Sensores: "+ mySensor.getnSensores());
    }
}
