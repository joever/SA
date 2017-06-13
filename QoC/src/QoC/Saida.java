/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QoC;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joever
 */
@Entity
@Table(name = "qc_saida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Saida.findAll", query = "SELECT s FROM Saida s"),
    @NamedQuery(name = "Saida.findByIndice", query = "SELECT s FROM Saida s WHERE s.indice = :indice"),
    @NamedQuery(name = "Saida.findByDado1", query = "SELECT s FROM Saida s WHERE s.dado1 = :dado1"),
    @NamedQuery(name = "Saida.findByDado2", query = "SELECT s FROM Saida s WHERE s.dado2 = :dado2"),
    @NamedQuery(name = "Saida.findByDado3", query = "SELECT s FROM Saida s WHERE s.dado3 = :dado3"),
    @NamedQuery(name = "Saida.findByDataEntrada", query = "SELECT s FROM Saida s WHERE s.dataEntrada = :dataEntrada"),
    @NamedQuery(name = "Saida.findByDataProcessado", query = "SELECT s FROM Saida s WHERE s.dataProcessado = :dataProcessado")})
public class Saida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "indice")
    private Integer indice;
    @Column(name = "dado1")
    private String dado1;
    @Column(name = "dado2")
    private String dado2;
    @Column(name = "dado3")
    private String dado3;
    @Column(name = "data_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;
    @Column(name = "data_processado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataProcessado;
    @JoinColumn(name = "tipo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Tipo tipo;

    public Saida() {
    }

    public Saida(Integer indice) {
        this.indice = indice;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getDado1() {
        return dado1;
    }

    public void setDado1(String dado1) {
        this.dado1 = dado1;
    }

    public String getDado2() {
        return dado2;
    }

    public void setDado2(String dado2) {
        this.dado2 = dado2;
    }

    public String getDado3() {
        return dado3;
    }

    public void setDado3(String dado3) {
        this.dado3 = dado3;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Date getDataProcessado() {
        return dataProcessado;
    }

    public void setDataProcessado(Date dataProcessado) {
        this.dataProcessado = dataProcessado;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indice != null ? indice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Saida)) {
            return false;
        }
        Saida other = (Saida) object;
        if ((this.indice == null && other.indice != null) || (this.indice != null && !this.indice.equals(other.indice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QoC.Saida[ indice=" + indice + " ]";
    }
    
}
