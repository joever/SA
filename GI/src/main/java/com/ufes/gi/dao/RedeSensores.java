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
public class RedeSensores {
    private Integer idRedeSensor;
    private Integer idRede;
    private String cpf;
    private Integer idSensor;
    private Integer unidades;
    private Integer bateriaInicial;
    private Integer tempoAtivo;
    private Double gastoBateria;
    private Integer tempoColeta;

    public Integer getIdRedeSensor() {
        return idRedeSensor;
    }

    public void setIdRedeSensor(Integer idRedeSensor) {
        this.idRedeSensor = idRedeSensor;
    }

    public Integer getIdRede() {
        return idRede;
    }

    public void setIdRede(Integer idRede) {
        this.idRede = idRede;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Integer idSensor) {
        this.idSensor = idSensor;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public Integer getBateriaInicial() {
        return bateriaInicial;
    }

    public void setBateriaInicial(Integer bateriaInicial) {
        this.bateriaInicial = bateriaInicial;
    }

    public Integer getTempoAtivo() {
        return tempoAtivo;
    }

    public void setTempoAtivo(Integer tempoAtivo) {
        this.tempoAtivo = tempoAtivo;
    }

    public Double getGastoBateria() {
        return gastoBateria;
    }

    public void setGastoBateria(Double gastoBateria) {
        this.gastoBateria = gastoBateria;
    }

    public Integer getTempoColeta() {
        return tempoColeta;
    }

    public void setTempoColeta(Integer tempoColeta) {
        this.tempoColeta = tempoColeta;
    }
}
