/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QoC;

import static QoC.BancoUtil.*;
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
@Table(name = "qc_origem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Origem.findAll", query = "SELECT o FROM Origem o"),
    @NamedQuery(name = "Origem.findByCodigo", query = "SELECT o FROM Origem o WHERE o.codigo = :codigo"),
    @NamedQuery(name = "Origem.findByDescricao", query = "SELECT o FROM Origem o WHERE o.descricao = :descricao"),
    @NamedQuery(name = "Origem.findByInformacoes", query = "SELECT o FROM Origem o WHERE o.informacoes = :informacoes")})
public class Origem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "informacoes")
    private String informacoes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "origem")
    private Collection<Tipo> tipoCollection;
    
    final static String tabela = "qc_origem";

    public Origem() {
    }

    public Origem(Integer codigo) {
        this.codigo = codigo;
    }

    public Origem(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getInformacoes() {
        return informacoes;
    }

    public void setInformacoes(String informacoes) {
        this.informacoes = informacoes;
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
        if (!(object instanceof Origem)) {
            return false;
        }
        Origem other = (Origem) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QoC.Origem[ codigo=" + codigo + " ]";
    }
    
    public static void Dados(String Informacoes[], Integer Codigo[], JComboBox Origem,String Gateway[],String Aplicacao[]) {
        Connection conn = null;
        int n = 0;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "select codigo, descricao, informacoes, gateway, aplicacao from "+tabela+";";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                rs.beforeFirst();
                while(rs.next()){
                    Codigo[n] = rs.getInt("codigo");
                    Informacoes[n] = rs.getString("informacoes");
                    Gateway[n] = rs.getString("gateway");
                    Aplicacao[n] = rs.getString("aplicacao");
                    Origem.addItem(rs.getString("descricao"));
                    n++;
                }
            }
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
    }
    
    public static int nLinhas() {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "select * from "+tabela+";";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.last();
            return rs.getRow();
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
        return 0;
    }

    public static boolean Inclui(String nome, String informacoes, String gateway, String aplicacao) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "insert into "+tabela+" (descricao,informacoes,gateway,aplicacao)values(?,?,?,?);";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, nome);
            stm.setString(2, informacoes);
            stm.setString(3, gateway);
            stm.setString(4, aplicacao);

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

    public static boolean Deleta(int codigo) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "delete from "+tabela+" where codigo = ?;";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, codigo);

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

    public static boolean Verifica(int codigo) {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "select * from qc_tipo where origem = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, codigo);
            ResultSet rs = stm.executeQuery();
            rs.last();
            if (rs.getRow() > 0)
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
        return false;
    }
    
    public static boolean Alterar(int codigo, String nome, String informacoes, String gateway, String aplicacao) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "update "+tabela+" set descricao = ?, informacoes = ?, gateway = ?, aplicacao = ? where codigo = ?;";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, nome);
            stm.setString(2, informacoes);
            stm.setString(3, gateway);
            stm.setString(4, aplicacao);
            stm.setInt(5, codigo);

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
