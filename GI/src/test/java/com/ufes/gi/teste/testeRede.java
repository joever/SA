/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.Rede;
import com.ufes.gi.dao.RedeDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeRede {
    public static void main (String[] args) {
        cadastraRede(1,"08603179786","UFES PPGI","Monitoramento de temperatura","Vitória-ES","");
        imprimeListaRede();
        atualizaRede(1,"08603179786","UFES PPGI","Monitoramento de temperatura do campos UFES Centro de Informática","Vitória-ES","");
        //apagaRede(1,"08603179786");
        imprimeListaRede();
    }
    
    public static void cadastraRede(Integer idRede, String cpf, String nome, String descricao, String localizacao, String Destino){
        Rede myRede = new Rede();
        myRede.setIdRede(idRede);
        myRede.setCpf(cpf);
        myRede.setNome(nome);
        myRede.setDescricao(descricao);
        myRede.setLocalizacao(localizacao);
        myRede.setDestino(Destino);
        RedeDAO myDao = new RedeDAO();
        if (myDao.adiciona(myRede))
            System.out.println("Cadastro com sucesso");
        else
            System.out.println("Erro ao cadastrar rede");
    }
    
    public static void atualizaRede(Integer idRede, String cpf, String nome, String descricao, String localizacao, String Destino){
        Rede myRede = new Rede();
        myRede.setIdRede(idRede);
        myRede.setCpf(cpf);
        myRede.setNome(nome);
        myRede.setDescricao(descricao);
        myRede.setLocalizacao(localizacao);
        myRede.setDestino(Destino);
        RedeDAO myDao = new RedeDAO();
        if (myDao.atualiza(myRede))
            System.out.println("Cadastro atualizado com sucesso");
        else
            System.out.println("Erro ao atualizar cadastro rede");
    }
    
    public static void apagaRede(Integer idRede, String cpf){
        RedeDAO myDao = new RedeDAO();
        if (myDao.deleta(idRede,cpf))
            System.out.println("Cadastro apagado com sucesso");
        else
            System.out.println("Erro ao apagar cadastro rede");
    }
    
    public static void imprimeListaRede(){
        RedeDAO myDAO = new RedeDAO();
        List<Rede> myRedes = myDAO.getRedes();
        myRedes.stream().forEach((myRede) -> {
            imprimeRede(myRede);
        });
    }
    
    public static void imprimeRede(Rede myRede){
        System.out.println("ID: "+ myRede.getIdRede());
        System.out.println("CPF: "+ myRede.getCpf());
        System.out.println("Nome: "+ myRede.getNome());
        System.out.println("Descrição: "+ myRede.getDescricao());
        System.out.println("Localização: "+ myRede.getLocalizacao());
        System.out.println("Destino: "+ myRede.getDestino());
    }    
}
