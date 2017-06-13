/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sp.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author joever
 */
@XStreamAlias("InformaçõesDaColeta")
public class SaidaRede {
    @XStreamAlias("IdRede")
    private Integer idRede;
    @XStreamAlias("NomeDaRede")
    private String nomeRede;
    @XStreamAlias("IdSensor")
    private Integer idSensor;
    @XStreamAlias("NomeDoSensor")
    private String nomeSensor;
    @XStreamAlias("IdTipoDeMonitoramento")
    private Integer idTM;
    @XStreamAlias("TipoDeMonitoramento")
    private String tipoMonitoramento;
    @XStreamAlias("IdUsuário")
    private String cpf;
    @XStreamAlias("DataHoraGeração")
    private String dthrGeracao;
    @XStreamAlias("DataHoraEnvio")
    private String dthrEnvio;
    @XStreamAlias("ValorMensurado")
    private String valor;
    @XStreamAlias("MetricaCoverge")
    private String coverage;
    @XStreamAlias("MetricaUp_To_Dateness")
    private String up_to_dateness;
    @XStreamAlias("MetricaAccuracy")
    private String accuracy;
    @XStreamAlias("MetricaFrequency")
    private String frequency;
    @XStreamAlias("MetricaSignificance")
    private String significance;
    @XStreamAlias("MetricaQoC_Geral")
    private String qoc_geral;

    public SaidaRede(){
    }
    
    public SaidaRede(Integer idRede, String nomeRede, Integer idSensor, String nomeSensor, Integer idTM, String tipoMonitoramento, String cpf, String dthrGeracao, String dthrEnvio, String valor, String coverage, String up_to_dateness, String accuracy, String frequency, String significance, String qoc_geral){
        this.idRede = idRede;
        this.nomeRede = nomeRede;
        this.idSensor = idSensor;
        this.nomeSensor = nomeSensor;
        this.idTM = idTM;
        this.tipoMonitoramento = tipoMonitoramento;
        this.cpf = cpf;
        this.dthrGeracao = dthrGeracao;
        this.dthrEnvio = dthrEnvio;
        this.valor = valor;
        this.coverage = coverage;
        this.up_to_dateness = up_to_dateness;
        this.accuracy = accuracy;
        this.frequency = frequency;
        this.significance = significance;
        this.qoc_geral = qoc_geral;
    }

    public Integer getIdRede() {
        return idRede;
    }

    public void setIdRede(Integer idRede) {
        this.idRede = idRede;
    }

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

    public String getDthrGeracao() {
        return dthrGeracao;
    }

    public void setDthrGeracao(String dthrGeracao) {
        this.dthrGeracao = dthrGeracao;
    }

    public String getDthrEnvio() {
        return dthrEnvio;
    }

    public void setDthrEnvio(String dthrEnvio) {
        this.dthrEnvio = dthrEnvio;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getIdTM() {
        return idTM;
    }

    public void setIdTM(Integer idTM) {
        this.idTM = idTM;
    }

    public String getNomeRede() {
        return nomeRede;
    }

    public void setNomeRede(String nomeRede) {
        this.nomeRede = nomeRede;
    }

    public String getNomeSensor() {
        return nomeSensor;
    }

    public void setNomeSensor(String nomeSensor) {
        this.nomeSensor = nomeSensor;
    }

    public String getTipoMonitoramento() {
        return tipoMonitoramento;
    }

    public void setTipoMonitoramento(String tipoMonitoramento) {
        this.tipoMonitoramento = tipoMonitoramento;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getUp_to_dateness() {
        return up_to_dateness;
    }

    public void setUp_to_dateness(String up_to_dateness) {
        this.up_to_dateness = up_to_dateness;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }

    public String getQoc_geral() {
        return qoc_geral;
    }

    public void setQoc_geral(String qoc_geral) {
        this.qoc_geral = qoc_geral;
    }
}
