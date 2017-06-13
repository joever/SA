/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.ui;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joever.hoffman
 */
@Entity
@Table(name = "saidarede")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SaidaredeJSF.findAll", query = "SELECT s FROM SaidaredeJSF s"),
    @NamedQuery(name = "SaidaredeJSF.findByIdsaidarede", query = "SELECT s FROM SaidaredeJSF s WHERE s.idsaidarede = :idsaidarede"),
    @NamedQuery(name = "SaidaredeJSF.findByIdrede", query = "SELECT s FROM SaidaredeJSF s WHERE s.idrede = :idrede"),
    @NamedQuery(name = "SaidaredeJSF.findByNomerede", query = "SELECT s FROM SaidaredeJSF s WHERE s.nomerede = :nomerede"),
    @NamedQuery(name = "SaidaredeJSF.findByIdsensor", query = "SELECT s FROM SaidaredeJSF s WHERE s.idsensor = :idsensor"),
    @NamedQuery(name = "SaidaredeJSF.findByNomesensor", query = "SELECT s FROM SaidaredeJSF s WHERE s.nomesensor = :nomesensor"),
    @NamedQuery(name = "SaidaredeJSF.findByIdtm", query = "SELECT s FROM SaidaredeJSF s WHERE s.idtm = :idtm"),
    @NamedQuery(name = "SaidaredeJSF.findByTipomonitoramento", query = "SELECT s FROM SaidaredeJSF s WHERE s.tipomonitoramento = :tipomonitoramento"),
    @NamedQuery(name = "SaidaredeJSF.findByIdusuario", query = "SELECT s FROM SaidaredeJSF s WHERE s.idusuario = :idusuario"),
    @NamedQuery(name = "SaidaredeJSF.findByDthrgeracao", query = "SELECT s FROM SaidaredeJSF s WHERE s.dthrgeracao = :dthrgeracao"),
    @NamedQuery(name = "SaidaredeJSF.findByDthrenvio", query = "SELECT s FROM SaidaredeJSF s WHERE s.dthrenvio = :dthrenvio"),
    @NamedQuery(name = "SaidaredeJSF.findByValormensurado", query = "SELECT s FROM SaidaredeJSF s WHERE s.valormensurado = :valormensurado"),
    @NamedQuery(name = "SaidaredeJSF.findByCoverage", query = "SELECT s FROM SaidaredeJSF s WHERE s.coverage = :coverage"),
    @NamedQuery(name = "SaidaredeJSF.findByUpToDateness", query = "SELECT s FROM SaidaredeJSF s WHERE s.upToDateness = :upToDateness"),
    @NamedQuery(name = "SaidaredeJSF.findByAccuracy", query = "SELECT s FROM SaidaredeJSF s WHERE s.accuracy = :accuracy"),
    @NamedQuery(name = "SaidaredeJSF.findByCompleteness", query = "SELECT s FROM SaidaredeJSF s WHERE s.completeness = :completeness"),
    @NamedQuery(name = "SaidaredeJSF.findBySignificance", query = "SELECT s FROM SaidaredeJSF s WHERE s.significance = :significance"),
    @NamedQuery(name = "SaidaredeJSF.findByQocGeral", query = "SELECT s FROM SaidaredeJSF s WHERE s.qocGeral = :qocGeral")})
public class SaidaredeJSF implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idsaidarede")
    private Integer idsaidarede;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idrede")
    private int idrede;
    @Size(max = 45)
    @Column(name = "nomerede")
    private String nomerede;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idsensor")
    private int idsensor;
    @Size(max = 45)
    @Column(name = "nomesensor")
    private String nomesensor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idtm")
    private int idtm;
    @Size(max = 45)
    @Column(name = "tipomonitoramento")
    private String tipomonitoramento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "idusuario")
    private String idusuario;
    @Size(max = 45)
    @Column(name = "dthrgeracao")
    private String dthrgeracao;
    @Size(max = 45)
    @Column(name = "dthrenvio")
    private String dthrenvio;
    @Size(max = 45)
    @Column(name = "valormensurado")
    private String valormensurado;
    @Size(max = 45)
    @Column(name = "coverage")
    private String coverage;
    @Size(max = 45)
    @Column(name = "up_to_dateness")
    private String upToDateness;
    @Size(max = 45)
    @Column(name = "accuracy")
    private String accuracy;
    @Size(max = 45)
    @Column(name = "completeness")
    private String completeness;
    @Size(max = 45)
    @Column(name = "significance")
    private String significance;
    @Size(max = 45)
    @Column(name = "qoc_geral")
    private String qocGeral;

    public SaidaredeJSF() {
    }

    public SaidaredeJSF(Integer idsaidarede) {
        this.idsaidarede = idsaidarede;
    }

    public SaidaredeJSF(Integer idsaidarede, int idrede, int idsensor, int idtm, String idusuario) {
        this.idsaidarede = idsaidarede;
        this.idrede = idrede;
        this.idsensor = idsensor;
        this.idtm = idtm;
        this.idusuario = idusuario;
    }

    public Integer getIdsaidarede() {
        return idsaidarede;
    }

    public void setIdsaidarede(Integer idsaidarede) {
        this.idsaidarede = idsaidarede;
    }

    public int getIdrede() {
        return idrede;
    }

    public void setIdrede(int idrede) {
        this.idrede = idrede;
    }

    public String getNomerede() {
        return nomerede;
    }

    public void setNomerede(String nomerede) {
        this.nomerede = nomerede;
    }

    public int getIdsensor() {
        return idsensor;
    }

    public void setIdsensor(int idsensor) {
        this.idsensor = idsensor;
    }

    public String getNomesensor() {
        return nomesensor;
    }

    public void setNomesensor(String nomesensor) {
        this.nomesensor = nomesensor;
    }

    public int getIdtm() {
        return idtm;
    }

    public void setIdtm(int idtm) {
        this.idtm = idtm;
    }

    public String getTipomonitoramento() {
        return tipomonitoramento;
    }

    public void setTipomonitoramento(String tipomonitoramento) {
        this.tipomonitoramento = tipomonitoramento;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getDthrgeracao() {
        return dthrgeracao;
    }

    public void setDthrgeracao(String dthrgeracao) {
        this.dthrgeracao = dthrgeracao;
    }

    public String getDthrenvio() {
        return dthrenvio;
    }

    public void setDthrenvio(String dthrenvio) {
        this.dthrenvio = dthrenvio;
    }

    public String getValormensurado() {
        return valormensurado;
    }

    public void setValormensurado(String valormensurado) {
        this.valormensurado = valormensurado;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getUpToDateness() {
        return upToDateness;
    }

    public void setUpToDateness(String upToDateness) {
        this.upToDateness = upToDateness;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getCompleteness() {
        return completeness;
    }

    public void setCompleteness(String completeness) {
        this.completeness = completeness;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }

    public String getQocGeral() {
        return qocGeral;
    }

    public void setQocGeral(String qocGeral) {
        this.qocGeral = qocGeral;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsaidarede != null ? idsaidarede.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaidaredeJSF)) {
            return false;
        }
        SaidaredeJSF other = (SaidaredeJSF) object;
        if ((this.idsaidarede == null && other.idsaidarede != null) || (this.idsaidarede != null && !this.idsaidarede.equals(other.idsaidarede))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ufes.sa.ui.SaidaredeJSF[ idsaidarede=" + idsaidarede + " ]";
    }
    
}
