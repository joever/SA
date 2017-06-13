package simulador;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import static simulador.BancoUtil.Conexao;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;

/**
 *
 * @author Joever
 */
public class BancoUtil {
    
    public static Connection Conexao () throws SQLException {
        final String driver = "com.mysql.jdbc.Driver";
        final String url = "jdbc:mysql://localhost:3306/mydvt001";
        try {
            Class.forName(driver);
            return (DriverManager.getConnection(url,"contexto","contexto"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BancoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static boolean login(String usuario, String senha, JRootPane rootPane) {

        Connection conn = null;
        
        try {
            conn = Conexao();

            PreparedStatement stm;
            
            String sql = "SELECT count(1) cont FROM qc_usuario where login = ? and senha = MD5(?);";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, usuario);
            stm.setString(2, senha);

            ResultSet rs;
            rs = stm.executeQuery();

            rs.next();
            if(rs.getInt("cont") > 0){
                return true;
            }
            else {
                JOptionPane.showMessageDialog(rootPane, "Usuário ou senha inválida!", "Erro no Login", WIDTH);
                return false;
            }
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(rootPane, "Erro de SQL. Código: "+e.getErrorCode()+" Descrição: "+ e.getSQLState(), "Erro", WIDTH);
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            if (conn == null) {
                JOptionPane.showMessageDialog(rootPane, "Não foi possível se conectar ao banco de dados!","Erro", WIDTH);
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
               JOptionPane.showMessageDialog(rootPane, "Erro ao fechar a conexão!", "Erro", WIDTH);
               onConClose.printStackTrace();
           }
        }
        return false;
    }
    
    public static String ConsultaTabela (String tabela) throws SQLException
    {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "SELECT * FROM "+tabela+" LIMIT 20";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            String sql1 = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?";
            stm = conn.prepareStatement(sql1);
            stm.setString(1, tabela);
            ResultSet rs1 = stm.executeQuery();
            rs1.last();
            int j = rs1.getRow();
            int i = 0;
            String coluna[];
            coluna = new String[j];
            String tipo[];
            tipo = new String[j];
            rs1.beforeFirst();
            while (rs1.next()) {
                coluna[i] = rs1.getString("COLUMN_NAME");
                tipo[i] = rs1.getString("DATA_TYPE");
                i++;
            }
            rs.last();
            if (rs.getRow() > 0) {
                String texto = "";
                rs.beforeFirst();
                texto = "#"+tabela+"#\n";
                while(rs.next()){
                    for (i = 0; i < j; i++){
                        switch (tipo[i]) {
                            case "int":
                                texto += coluna[i] + ": " + Integer.toString(rs.getInt(coluna[i]));
                                break;
                            case "varchar":
                                texto += coluna[i] + ": " + rs.getString(coluna[i]);
                                break;
                            case "datetime":
                                texto += coluna[i] + ": " + rs.getDate(coluna[i]);
                                break;
                            default:
                                break;
                        }
                        texto += " ";
                    }
                    texto += "\n";
                }
                return texto;
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
        return "";
    }
    
    public static boolean iEntrada(int tipo, String valor1, Date dTime) throws SQLException {
        SimpleDateFormat DataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;

            String sql = "insert into qc_entrada (tipo,dado1,data)values(?,?,?);";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, tipo);
            stm.setString(2, valor1);
            stm.setString(3, DataTime.format(dTime));

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

    static String[][] sTipo() {
        /*
         * Retorna String[][]
         * String[0][0] = Código do Tipo
         * String[0][1] = Descrição do Tipo
         * String[0][2] = Descrição Origem
        */
        Connection conn = null;
        String sql = null;
        try {
            conn = Conexao();
            PreparedStatement stm;
            
            sql = "SELECT a.CODIGO CODIGO, a.DESCRICAO DESCRICAO1, b.DESCRICAO DESCRICAO2 FROM qc_tipo a, qc_origem b WHERE a.ORIGEM = b.CODIGO ORDER BY a.ORIGEM, a.CODIGO;";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0){
                String[][] dValida = new String[i][3];
                i = 0;
                rs.beforeFirst();
                while (rs.next()){
                    dValida[i][0] = Integer.toString(rs.getInt("CODIGO"));
                    dValida[i][1] = rs.getString("DESCRICAO1");
                    dValida[i][2] = rs.getString("DESCRICAO2");
                    i++;
                }
                return dValida;
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
}
