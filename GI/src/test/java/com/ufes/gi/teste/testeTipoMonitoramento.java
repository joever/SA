/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.TipoMonitoramento;
import com.ufes.gi.dao.TipoMonitoramentoDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeTipoMonitoramento {
    public static void main (String[] args) {
        cadastraTipoMonitoramento(1,"Temperatura","Mede a temperatura do ambiente em ºC");
        cadastraTipoMonitoramento(2,"Luminosidade","Mede a luminosidade do ambiente em Lumis");
        imprimeListaTipoMonitoramento();
        atualizaTipoMonitoramento(1,"Temperatura [ºC]","Mede a temperatura do ambiente em ºC");
        atualizaTipoMonitoramento(2,"Luminosidade [Lumen]","Mede a luminosidade do ambiente em Lumen");
        //apagaTipoMonitoramento(1);
        //apagaTipoMonitoramento(2);
        imprimeListaTipoMonitoramento();
    }
    
    public static void cadastraTipoMonitoramento(Integer codigo, String nome, String descricao){
        TipoMonitoramento myTipoMonitoramento = new TipoMonitoramento();
        myTipoMonitoramento.setCodigo(codigo);
        myTipoMonitoramento.setNome(nome);
        myTipoMonitoramento.setDescricao(descricao);
        TipoMonitoramentoDAO myDao = new TipoMonitoramentoDAO();
        if (myDao.adiciona(myTipoMonitoramento))
            System.out.println("Cadastro com sucesso");
        else
            System.out.println("Erro ao cadastrar tipomonitoramento");
    }
    
    public static void atualizaTipoMonitoramento(Integer codigo, String nome, String descricao){
        TipoMonitoramento myTipoMonitoramento = new TipoMonitoramento();
        myTipoMonitoramento.setCodigo(codigo);
        myTipoMonitoramento.setNome(nome);
        myTipoMonitoramento.setDescricao(descricao);
        TipoMonitoramentoDAO myDao = new TipoMonitoramentoDAO();
        if (myDao.atualiza(myTipoMonitoramento))
            System.out.println("Cadastro atualizado com sucesso");
        else
            System.out.println("Erro ao atualizar cadastro tipomonitoramento");
    }
    
    public static void apagaTipoMonitoramento(Integer codigo){
        TipoMonitoramentoDAO myDao = new TipoMonitoramentoDAO();
        if (myDao.deleta(codigo))
            System.out.println("Cadastro apagado com sucesso");
        else
            System.out.println("Erro ao apagar cadastro tipomonitoramento");
    }
    
    public static void imprimeListaTipoMonitoramento(){
        TipoMonitoramentoDAO myDAO = new TipoMonitoramentoDAO();
        List<TipoMonitoramento> myTiposMonitoramento = myDAO.getTiposMonitoramento();
        myTiposMonitoramento.stream().forEach((myTipoMonitoramento) -> {
            imprimeTipoMonitoramento(myTipoMonitoramento);
        });
    }
    
    public static void imprimeTipoMonitoramento(TipoMonitoramento myTipoMonitoramento){
        System.out.println("Codigo: "+ myTipoMonitoramento.getCodigo());
        System.out.println("Nome: "+ myTipoMonitoramento.getNome());
        System.out.println("Descrição: "+ myTipoMonitoramento.getDescricao());
    }
}
