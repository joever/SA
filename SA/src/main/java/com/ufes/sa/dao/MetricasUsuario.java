/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.dao;

/**
 *
 * @author joever
 */
public class MetricasUsuario {
    private Integer idUser;
    private Integer codigoMetrica;
    private Integer codigoTipoAvaliacao;
    private String valida1;
    private String valida2;
    private String formula;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getCodigoMetrica() {
        return codigoMetrica;
    }

    public void setCodigoMetrica(Integer codigoMetrica) {
        this.codigoMetrica = codigoMetrica;
    }

    public Integer getCodigoTipoAvaliacao() {
        return codigoTipoAvaliacao;
    }

    public void setCodigoTipoAvaliacao(Integer codigoTipoAvaliacao) {
        this.codigoTipoAvaliacao = codigoTipoAvaliacao;
    }

    public String getValida1() {
        return valida1;
    }

    public void setValida1(String valida1) {
        this.valida1 = valida1;
    }

    public String getValida2() {
        return valida2;
    }

    public void setValida2(String valida2) {
        this.valida2 = valida2;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
