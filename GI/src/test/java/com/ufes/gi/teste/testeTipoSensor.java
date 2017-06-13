/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.TipoSensor;
import com.ufes.gi.dao.TipoSensorDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeTipoSensor {
    public static void main (String[] args) {
        cadastraTipoSensor(1,"Gateway","Gateway com conexão Wireless");
        cadastraTipoSensor(2,"Gateway","Gateway com conexão USB");
        cadastraTipoSensor(3,"Nó Sensor","Nó Sensor");
        imprimeListaTipoSensor();
    }
    
    public static void cadastraTipoSensor(Integer idTipoSensor, String nome, String descricao){
        TipoSensor myTipoSensor = new TipoSensor();
        myTipoSensor.setIdTipoSensor(idTipoSensor);
        myTipoSensor.setNome(nome);
        myTipoSensor.setDescricao(descricao);
        TipoSensorDAO myDao = new TipoSensorDAO();
        if (myDao.adiciona(myTipoSensor))
            System.out.println("Cadastro com sucesso");
        else
            System.out.println("Erro ao cadastrar tipoSensor");
    }
    
    public static void imprimeListaTipoSensor(){
        TipoSensorDAO myDAO = new TipoSensorDAO();
        List<TipoSensor> myTiposSensor = myDAO.getTiposSensor();
        myTiposSensor.stream().forEach((myTipoSensor) -> {
            imprimeTipoSensor(myTipoSensor);
        });
    }
    
    public static void imprimeTipoSensor(TipoSensor myTipoSensor){
        System.out.println("ID: "+ myTipoSensor.getIdTipoSensor());
        System.out.println("Nome: "+ myTipoSensor.getNome());
        System.out.println("Descrição: "+ myTipoSensor.getDescricao());
    }
}
