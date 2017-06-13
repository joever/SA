/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.RedeSensores;
import com.ufes.gi.dao.RedeSensoresDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeRedeSensor {
    public static void main (String[] args) {
        cadastraRedeSensores(1,1,"08603179786",1,1,100,0,0.0,1);
        imprimeListaRedeSensores();
        atualizaRedeSensores(1,1,"08603179786",1,1,100,0,0.0,10);
        //apagaRedeSensores(1,"08603179786");
        imprimeListaRedeSensores();
    }
    
    public static void cadastraRedeSensores(Integer idRedeSensor, Integer idRede, String cpf, Integer idSensor, Integer unidades, Integer bateriaInicial, Integer tempoAtivo, Double gastoBateria, Integer tempoColeta){
        RedeSensores myRedeSensores = new RedeSensores();
        myRedeSensores.setIdRedeSensor(idRedeSensor);
        myRedeSensores.setIdRede(idRede);
        myRedeSensores.setCpf(cpf);
        myRedeSensores.setIdSensor(idSensor);
        myRedeSensores.setUnidades(unidades);
        myRedeSensores.setBateriaInicial(bateriaInicial);
        myRedeSensores.setTempoAtivo(tempoAtivo);
        myRedeSensores.setGastoBateria(gastoBateria);
        myRedeSensores.setTempoColeta(tempoColeta);
        RedeSensoresDAO myDao = new RedeSensoresDAO();
        if (myDao.adiciona(myRedeSensores))
            System.out.println("Cadastro com sucesso");
        else
            System.out.println("Erro ao cadastrar redeSensores");
    }
    
    public static void atualizaRedeSensores(Integer idRedeSensor, Integer idRede, String cpf, Integer idSensor, Integer unidades, Integer bateriaInicial, Integer tempoAtivo, Double gastoBateria, Integer tempoColeta){
        RedeSensores myRedeSensores = new RedeSensores();
        myRedeSensores.setIdRedeSensor(idRedeSensor);
        myRedeSensores.setIdRede(idRede);
        myRedeSensores.setCpf(cpf);
        myRedeSensores.setIdSensor(idSensor);
        myRedeSensores.setUnidades(unidades);
        myRedeSensores.setBateriaInicial(bateriaInicial);
        myRedeSensores.setTempoAtivo(tempoAtivo);
        myRedeSensores.setGastoBateria(gastoBateria);
        myRedeSensores.setTempoColeta(tempoColeta);
        RedeSensoresDAO myDao = new RedeSensoresDAO();
        if (myDao.atualiza(myRedeSensores))
            System.out.println("Cadastro atualizado com sucesso");
        else
            System.out.println("Erro ao atualizar cadastro redeSensores");
    }
    
    public static void apagaRedeSensores(Integer idRedeSensor, Integer idRede, String cpf, Integer idSensor){
        RedeSensoresDAO myDao = new RedeSensoresDAO();
        if (myDao.deleta(idRedeSensor,idRede,cpf,idSensor))
            System.out.println("Cadastro apagado com sucesso");
        else
            System.out.println("Erro ao apagar cadastro redeSensores");
    }
    
    public static void imprimeListaRedeSensores(){
        RedeSensoresDAO myDAO = new RedeSensoresDAO();
        List<RedeSensores> myRedes = myDAO.getRedesSensores();
        myRedes.stream().forEach((myRede) -> {
            imprimeRedeSensores(myRede);
        });
    }
    
    public static void imprimeRedeSensores(RedeSensores myRedeSensores){
        System.out.println("ID: "+ myRedeSensores.getIdRedeSensor());
        System.out.println("ID Rede: "+ myRedeSensores.getIdRede());
        System.out.println("CPF: "+ myRedeSensores.getCpf());
        System.out.println("ID Sensor: "+ myRedeSensores.getIdSensor());
        System.out.println("Unidades: "+ myRedeSensores.getUnidades());
        System.out.println("Bateria Inicial: "+ myRedeSensores.getBateriaInicial());
        System.out.println("Tempo Ativo: "+ myRedeSensores.getTempoAtivo());
        System.out.println("Gasto Bateria: "+ myRedeSensores.getGastoBateria());
        System.out.println("Tempo Coleta: "+ myRedeSensores.getTempoColeta());
    }
}
