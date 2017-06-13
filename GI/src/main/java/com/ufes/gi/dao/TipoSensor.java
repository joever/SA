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
public class TipoSensor {
    private Integer idTipoSensor;
    private String nome;
    private String descricao;

    public Integer getIdTipoSensor() {
        return idTipoSensor;
    }

    public void setIdTipoSensor(Integer idTipoSensor) {
        this.idTipoSensor = idTipoSensor;
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
}
