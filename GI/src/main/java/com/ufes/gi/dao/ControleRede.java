/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.dao;

/**
 *
 * @author joever.hoffman
 */
public class ControleRede {
    private Integer idRede;
    private String cpf;
    private Integer situacao; //0 - Parado ou 1 - Ativa
    private Integer acaoParar; //0 - Normal ou 1 - Parar

    public Integer getIdRede() {
        return idRede;
    }

    public void setIdRede(Integer idrede) {
        this.idRede = idrede;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public Integer getAcaoParar() {
        return acaoParar;
    }

    public void setAcaoParar(Integer acaoParar) {
        this.acaoParar = acaoParar;
    }
}
