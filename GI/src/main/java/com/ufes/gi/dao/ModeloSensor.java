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
public class ModeloSensor {
    private Integer idModeloSensor;
    private String nome;
    private String descricao;
    private Integer nSensores;

    public Integer getIdModeloSensor() {
        return idModeloSensor;
    }

    public void setIdModeloSensor(Integer idModeloSensor) {
        this.idModeloSensor = idModeloSensor;
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

    public Integer getnSensores() {
        return nSensores;
    }

    public void setnSensores(Integer nSensores) {
        this.nSensores = nSensores;
    }
}
