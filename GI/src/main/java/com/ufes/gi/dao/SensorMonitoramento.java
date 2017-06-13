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
public class SensorMonitoramento {
    private Integer idSensor;
    private String cpf;
    private Integer idTM;
    private Integer media;
    private Integer diferenca;
    private Integer idTF;

    public Integer getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Integer idSensor) {
        this.idSensor = idSensor;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getIdTM() {
        return idTM;
    }

    public void setIdTM(Integer idTM) {
        this.idTM = idTM;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public Integer getDiferenca() {
        return diferenca;
    }

    public void setDiferenca(Integer diferenca) {
        this.diferenca = diferenca;
    }

    public Integer getIdTF() {
        return idTF;
    }

    public void setIdTF(Integer idTF) {
        this.idTF = idTF;
    }
}
