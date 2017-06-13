/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QoC;

import static QoC.BancoUtil.Conexao;
import static QoC.Origem.tabela;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Joever
 */
@Entity
@Table(name = "qc_tipo_valida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoValida.findAll", query = "SELECT t FROM TipoValida t"),
    @NamedQuery(name = "TipoValida.findByCodigo", query = "SELECT t FROM TipoValida t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TipoValida.findByTipo", query = "SELECT t FROM TipoValida t WHERE t.tipo = :tipo"),
    @NamedQuery(name = "TipoValida.findByDescricao", query = "SELECT t FROM TipoValida t WHERE t.descricao = :descricao")})
public class TipoValida implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoValida")
    private Collection<Tipo> tipoCollection;
    
    final static String tabela = "qc_tipo_valida";

    public TipoValida() {
    }

    public TipoValida(Integer codigo) {
        this.codigo = codigo;
    }

    public TipoValida(Integer codigo, String tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<Tipo> getTipoCollection() {
        return tipoCollection;
    }

    public void setTipoCollection(Collection<Tipo> tipoCollection) {
        this.tipoCollection = tipoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoValida)) {
            return false;
        }
        TipoValida other = (TipoValida) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QoC.TipoValida[ codigo=" + codigo + " ]";
    }
    public static String[][] Carrega(JComboBox lTipoValida) {
        Connection conn = null;
        String sql = null;
        String[][] dados;
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT CODIGO, TIPO, DESCRICAO FROM "+ tabela +";";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                dados = new String[i][3];
                rs.beforeFirst();
                i = 0;
                while(rs.next()){
                    dados[i][0] = Integer.toString(rs.getInt("CODIGO"));
                    dados[i][1] = rs.getString("TIPO");
                    dados[i][2] = rs.getString("DESCRICAO");
                    lTipoValida.addItem(dados[i][0]+"  "+dados[i][1]+"  "+dados[i][2]);
                    i++;
                }
                return dados;
            }
        }
        catch(SQLException e)
        {
            System.out.println(sql);
            e.printStackTrace();
        }
        finally
        {
           try
           {
              conn.close();
           }
           catch(SQLException onConClose)
           {
               JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão!", "Erro", WIDTH);
               onConClose.printStackTrace();
           }
        }
        return null;
    }
    
    public static boolean Deleta(int cTipoValida) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "delete from "+tabela+" where codigo = ?;";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, cTipoValida);

            if (!stm.execute()) {
                conn.commit();
                return true;
            }
        }
        catch(SQLException e)
        {
            conn.rollback();
            JOptionPane.showMessageDialog(null, "Erro no SQL. Código: "+e.getErrorCode()+" Descrição: "+ e.getSQLState(), "Erro", WIDTH);
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Não foi possível se conectar ao banco de dados!","Erro", WIDTH);
            }
            e.printStackTrace();
        }
        finally
        {
           try
           {
              conn.close();
           }
           catch(SQLException onConClose)
           {
               JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão!", "Erro", WIDTH);
               onConClose.printStackTrace();
           }
        }
        return false;
    }
    
    public static boolean Verifica(int cTipoValida) {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "SELECT COUNT(1) MAXIMO FROM qc_tipo WHERE TIPO_VALIDA = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, cTipoValida);
            ResultSet rs = stm.executeQuery();
            rs.next();
            if (rs.getInt("MAXIMO") > 0)
                return false;
            else
                return true;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
           try
           {
              conn.close();
           }
           catch(SQLException onConClose)
           {
               JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão!", "Erro", WIDTH);
               onConClose.printStackTrace();
           }
        }
        return true;
    }
    
    public static boolean Atualiza(String[] TipoValida) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "update "+tabela+" set tipo = ?, descricao = ? where codigo = ?;";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, TipoValida[1]);
            stm.setString(2, TipoValida[2]);
            stm.setInt(3, Integer.parseInt(TipoValida[0]));

            if (!stm.execute()) {
                conn.commit();
                return true;
            }
        }
        catch(SQLException e)
        {
            conn.rollback();
            JOptionPane.showMessageDialog(null, "Erro no SQL. Código: "+e.getErrorCode()+" Descrição: "+ e.getSQLState(), "Erro", WIDTH);
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Não foi possível se conectar ao banco de dados!","Erro", WIDTH);
            }
            e.printStackTrace();
        }
        finally
        {
           try
           {
              conn.close();
           }
           catch(SQLException onConClose)
           {
               JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão!", "Erro", WIDTH);
               onConClose.printStackTrace();
           }
        }
        return false;
    }
    
    public static boolean Incluir(String Tipo, String Descricao) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "insert into "+tabela+" (tipo,descricao)values(?,?);";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, Tipo);
            stm.setString(2, Descricao);

            if (!stm.execute()) {
                conn.commit();
                return true;
            }
        }
        catch(SQLException e)
        {
            conn.rollback();
            JOptionPane.showMessageDialog(null, "Erro no SQL. Código: "+e.getErrorCode()+" Descrição: "+ e.getSQLState(), "Erro", WIDTH);
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Não foi possível se conectar ao banco de dados!","Erro", WIDTH);
            }
            e.printStackTrace();
        }
        finally
        {
           try
           {
              conn.close();
           }
           catch(SQLException onConClose)
           {
               JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão!", "Erro", WIDTH);
               onConClose.printStackTrace();
           }
        }
        return false;
    }
}
