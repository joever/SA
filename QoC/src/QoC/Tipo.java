/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QoC;

import static QoC.BancoUtil.Conexao;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "qc_tipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipo.findAll", query = "SELECT t FROM Tipo t"),
    @NamedQuery(name = "Tipo.findByCodigo", query = "SELECT t FROM Tipo t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "Tipo.findByDescricao", query = "SELECT t FROM Tipo t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "Tipo.findByValida1", query = "SELECT t FROM Tipo t WHERE t.valida1 = :valida1"),
    @NamedQuery(name = "Tipo.findByValida2", query = "SELECT t FROM Tipo t WHERE t.valida2 = :valida2")})
public class Tipo implements Serializable {
    private static final long serialVersionUID = 1L;
    final static String tabela = "qc_tipo";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "valida1")
    private Integer valida1;
    @Column(name = "valida2")
    private Integer valida2;
    @JoinColumn(name = "tipo_valida", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TipoValida tipoValida;
    @JoinColumn(name = "origem", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Origem origem;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private Collection<Saida> saidaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private Collection<Entrada> entradaCollection;

    public Tipo() {
    }

    public Tipo(Integer codigo) {
        this.codigo = codigo;
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

    public Integer getValida1() {
        return valida1;
    }

    public void setValida1(Integer valida1) {
        this.valida1 = valida1;
    }

    public Integer getValida2() {
        return valida2;
    }

    public void setValida2(Integer valida2) {
        this.valida2 = valida2;
    }

    public TipoValida getTipoValida() {
        return tipoValida;
    }

    public void setTipoValida(TipoValida tipoValida) {
        this.tipoValida = tipoValida;
    }

    public Origem getOrigem() {
        return origem;
    }

    public void setOrigem(Origem origem) {
        this.origem = origem;
    }

    @XmlTransient
    public Collection<Saida> getSaidaCollection() {
        return saidaCollection;
    }

    public void setSaidaCollection(Collection<Saida> saidaCollection) {
        this.saidaCollection = saidaCollection;
    }

    @XmlTransient
    public Collection<Entrada> getEntradaCollection() {
        return entradaCollection;
    }

    public void setEntradaCollection(Collection<Entrada> entradaCollection) {
        this.entradaCollection = entradaCollection;
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
        if (!(object instanceof Tipo)) {
            return false;
        }
        Tipo other = (Tipo) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QoC.Tipo[ codigo=" + codigo + " ]";
    }
    
    public static String[][] Carrega(JComboBox lTipo) {
        Connection conn = null;
        String sql = null;
        String[][] dados;
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT A.CODIGO, "
                    + "A.ORIGEM, "
                    + "A.TIPO_VALIDA, "
                    + "A.DESCRICAO, "
                    + "A.VALIDA1, "
                    + "A.VALIDA2, "
                    + "A.OPCAO, "
                    + "A.F_INFERIOR, "
                    + "A.F_SUPERIOR, "
                    + "A.T_VIDA, "
                    + "A.T_ATUALIZACAO, "
                    + "A.OPERADOR1, "
                    + "A.VALOR1, "
                    + "A.OPERADOR2, "
                    + "A.VALOR2, "
                    + "A.P_COVERAGE, "
                    + "A.P_UP_TO_DATENESS, "
                    + "A.P_ACCURACY, "
                    + "A.P_FREQUENCY, "
                    + "A.P_SIGNIFICANCE, "
                    + "A.C_GERAR, "
                    + "A.S_GERAR, "
                    + "B.DESCRICAO, "
                    + "B.INFORMACOES, "
                    + "C.TIPO, "
                    + "C.DESCRICAO "
                    + "FROM qc_tipo A, qc_origem B, qc_tipo_valida C "
                    + "WHERE A.ORIGEM = B.CODIGO "
                    + "AND A.TIPO_VALIDA = C.CODIGO order by A.CODIGO;";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                dados = new String[i][22];
                rs.beforeFirst();
                i = 0;
                while(rs.next()){
                    dados[i][0] = Integer.toString(rs.getInt(1));//CODIGO
                    dados[i][1] = Integer.toString(rs.getInt(2));//ORIGEM
                    dados[i][2] = Integer.toString(rs.getInt(3));//TIPO_VALIDA
                    dados[i][3] = rs.getString(4);//DESCRICAO
                    dados[i][4] = Double.toString(rs.getDouble(5));//VALIDA1
                    dados[i][5] = Double.toString(rs.getDouble(6));//VALIDA2
                    dados[i][6] = Integer.toString(rs.getInt(7));//OPCAO
                    dados[i][7] = Integer.toString(rs.getInt(8));//F_INFERIOR
                    dados[i][8] = Integer.toString(rs.getInt(9));//F_SUPERIOR
                    dados[i][9] = Integer.toString(rs.getInt(10));//T_VIDA
                    dados[i][10] = Integer.toString(rs.getInt(11));//T_ATUALIZACAO
                    dados[i][11] = rs.getString(12);//OPERADOR1
                    dados[i][12] = Integer.toString(rs.getInt(13));//VALOR1
                    dados[i][13] = rs.getString(14);//OPERADOR2
                    dados[i][14] = Integer.toString(rs.getInt(15));//VALOR2
                    dados[i][15] = Double.toString(rs.getDouble(16));//P_COVERAGE
                    dados[i][16] = Double.toString(rs.getDouble(17));//P_UP_TO_DATENESS
                    dados[i][17] = Double.toString(rs.getDouble(18));//P_ACCURACY
                    dados[i][18] = Double.toString(rs.getDouble(19));//P_FREQUENCY
                    dados[i][19] = Double.toString(rs.getDouble(20));//P_SIGNIFICANCE
                    dados[i][20] = Integer.toString(rs.getInt(21));
                    dados[i][21] = Integer.toString(rs.getInt(22));
                    lTipo.addItem(dados[i][0]+"  "+dados[i][3]);
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
    
    public static String[][] cOrigem(JComboBox lTipoOrigem) {
        Connection conn = null;
        String sql = null;
        String[][] dados;
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "select codigo, descricao, informacoes from qc_origem;";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                dados = new String[i][3];
                rs.beforeFirst();
                i = 0;
                while(rs.next()){
                    dados[i][0] = Integer.toString(rs.getInt("codigo"));
                    dados[i][1] = rs.getString("descricao");
                    dados[i][2] = rs.getString("informacoes");
                    lTipoOrigem.addItem(dados[i][1]+"  "+dados[i][2]);
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

    public static String[][] cTipoValida(JComboBox lTipoTValida) {
        Connection conn = null;
        String sql = null;
        String[][] dados;
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT CODIGO, TIPO, DESCRICAO FROM qc_tipo_valida;";
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
                    lTipoTValida.addItem(dados[i][1]+"  "+dados[i][2]);
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
    
    public static boolean Deleta(int Codigo) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "delete from "+tabela+" where codigo = ?;";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Codigo);

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
    
    static boolean Verifica(int Codigo) {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "SELECT COUNT(1) MAXIMO FROM qc_entrada WHERE TIPO = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Codigo);
            ResultSet rs = stm.executeQuery();
            rs.next();
            if (rs.getInt("MAXIMO") > 0)
                return false;
            else {
                stm = null;
                String sql1 = "SELECT COUNT(1) MAXIMO FROM qc_saida WHERE TIPO = ?;";
                stm = conn.prepareStatement(sql1);
                stm.setInt(1, Codigo);
                rs = stm.executeQuery();
                rs.next();
                if (rs.getInt("MAXIMO") > 0)
                    return false;
                else
                    return true;
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
        return true;
    }
    
    public static boolean Atualiza(String[] aTipo) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "UPDATE "+tabela+" "
                    + "SET ORIGEM = ?, "
                    + "TIPO_VALIDA = ?, "
                    + "DESCRICAO = ?, "
                    + "VALIDA1 = ?, "
                    + "VALIDA2 = ?, "
                    + "OPCAO = ?, "
                    + "F_INFERIOR = ?, "
                    + "F_SUPERIOR = ?, "
                    + "T_VIDA = ?, "
                    + "T_ATUALIZACAO = ?, "
                    + "OPERADOR1 = ?, "
                    + "VALOR1 = ?, "
                    + "OPERADOR2 = ?, "
                    + "VALOR2 = ?, "
                    + "P_COVERAGE = ?, "
                    + "P_UP_TO_DATENESS = ?, "
                    + "P_ACCURACY = ?, "
                    + "P_FREQUENCY = ?, "
                    + "P_SIGNIFICANCE = ?, "
                    + "C_GERAR = ?, "
                    + "S_GERAR = ? "
                    + "WHERE CODIGO = ?;";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Integer.parseInt(aTipo[1]));
            stm.setInt(2, Integer.parseInt(aTipo[2]));
            stm.setString(3, aTipo[3]);
            stm.setDouble(4, Double.parseDouble(aTipo[4]));
            stm.setDouble(5, Double.parseDouble(aTipo[5]));
            stm.setInt(6, Integer.parseInt(aTipo[6]));
            stm.setInt(7, Integer.parseInt(aTipo[7]));
            stm.setInt(8, Integer.parseInt(aTipo[8]));
            stm.setInt(9, Integer.parseInt(aTipo[9]));
            stm.setInt(10, Integer.parseInt(aTipo[10]));
            stm.setInt(11, Integer.parseInt(aTipo[11]));
            stm.setInt(12, Integer.parseInt(aTipo[12]));
            stm.setInt(13, Integer.parseInt(aTipo[13]));
            stm.setInt(14, Integer.parseInt(aTipo[14]));
            stm.setDouble(15, Double.parseDouble(aTipo[15]));
            stm.setDouble(16, Double.parseDouble(aTipo[16]));
            stm.setDouble(17, Double.parseDouble(aTipo[17]));
            stm.setDouble(18, Double.parseDouble(aTipo[18]));
            stm.setDouble(19, Double.parseDouble(aTipo[19]));
            stm.setInt(20, Integer.parseInt(aTipo[20]));
            stm.setInt(21, Integer.parseInt(aTipo[21]));
            stm.setInt(22, Integer.parseInt(aTipo[0]));

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

    public static boolean Incluir(int cOrigem, int cTipoValida, String Descricao, double Valida1, double Valida2, int Opcao, int CInferior, int CSuperior, double CPeso, int UTempo, double UPeso, double APeso, int CmTempo, double CmPeso, int SOpcao1, int SOpcao2, int SValor1, int SValor2, double SPeso, int CGerar, int SGerar) throws SQLException {
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "insert into "+tabela+" "
                    + "(origem,"
                    + "tipo_valida,"
                    + "descricao,"
                    + "valida1,"
                    + "valida2,"
                    + "opcao,"
                    + "f_inferior,"
                    + "f_superior,"
                    + "t_vida,"
                    + "t_atualizacao,"
                    + "operador1,"
                    + "valor1,"
                    + "operador2,"
                    + "valor2,"
                    + "p_coverage,"
                    + "p_up_to_dateness,"
                    + "p_accuracy,"
                    + "p_frequency,"
                    + "p_significance,"
                    + "c_gerar,"
                    + "s_gerar)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1,cOrigem);
            stm.setInt(2,cTipoValida);
            stm.setString(3,Descricao);
            stm.setDouble(4, Valida1);
            stm.setDouble(5, Valida2);
            stm.setInt(6,Opcao);
            stm.setInt(7,CInferior);
            stm.setInt(8,CSuperior);
            stm.setInt(9,UTempo);
            stm.setInt(10,CmTempo);
            stm.setInt(11,SOpcao1);
            stm.setInt(12,SValor1);
            stm.setInt(13,SOpcao2);
            stm.setInt(14,SValor2);
            stm.setDouble(15,CPeso);
            stm.setDouble(16,UPeso);
            stm.setDouble(17,APeso);
            stm.setDouble(18,CmPeso);
            stm.setDouble(19,SPeso);
            stm.setInt(20,CGerar);
            stm.setInt(21,SGerar);

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
