/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.dao;

/**
 *
 * @author joever
 */
public class TipoFalha {
    private Integer idTipoFalha;
    private String nome;
    private String descricao;
    private Integer valor1;
    private Integer valor2;
    private Integer valor3;

    public Integer getIdTipoFalha() {
        return idTipoFalha;
    }

    public void setIdTipoFalha(Integer idTipoFalha) {
        this.idTipoFalha = idTipoFalha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getValor1() {
        return valor1;
    }

    public void setValor1(Integer valor1) {
        this.valor1 = valor1;
    }

    public Integer getValor2() {
        return valor2;
    }

    public void setValor2(Integer valor2) {
        this.valor2 = valor2;
    }

    public Integer getValor3() {
        return valor3;
    }

    public void setValor3(Integer valor3) {
        this.valor3 = valor3;
    }
}
