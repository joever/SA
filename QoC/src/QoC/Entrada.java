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
@Table(name = "qc_entrada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entrada.findAll", query = "SELECT e FROM Entrada e"),
    @NamedQuery(name = "Entrada.findByIndice", query = "SELECT e FROM Entrada e WHERE e.indice = :indice"),
    @NamedQuery(name = "Entrada.findByDado1", query = "SELECT e FROM Entrada e WHERE e.dado1 = :dado1"),
    @NamedQuery(name = "Entrada.findByDado2", query = "SELECT e FROM Entrada e WHERE e.dado2 = :dado2"),
    @NamedQuery(name = "Entrada.findByDado3", query = "SELECT e FROM Entrada e WHERE e.dado3 = :dado3"),
    @NamedQuery(name = "Entrada.findByQcDataEntrada", query = "SELECT e FROM Entrada e WHERE e.qcDataEntrada = :qcDataEntrada")})
public class Entrada implements Serializable {
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
    @Basic(optional = false)
    @Column(name = "qc_data_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date qcDataEntrada;
    @JoinColumn(name = "tipo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Tipo tipo;

    public Entrada() {
    }

    public Entrada(Integer indice) {
        this.indice = indice;
    }

    public Entrada(Integer indice, Date qcDataEntrada) {
        this.indice = indice;
        this.qcDataEntrada = qcDataEntrada;
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

    public Date getQcDataEntrada() {
        return qcDataEntrada;
    }

    public void setQcDataEntrada(Date qcDataEntrada) {
        this.qcDataEntrada = qcDataEntrada;
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
        if (!(object instanceof Entrada)) {
            return false;
        }
        Entrada other = (Entrada) object;
        if ((this.indice == null && other.indice != null) || (this.indice != null && !this.indice.equals(other.indice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QoC.Entrada[ indice=" + indice + " ]";
    }
    
}
