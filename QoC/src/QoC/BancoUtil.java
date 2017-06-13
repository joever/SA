package QoC;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
                ex.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(QoC.class.getName()).log(Level.SEVERE, null, ex);
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

    public static String[][] cTabela(String nTabela, int cTipo, int nRegistro) {
        Connection conn = null;
        String sql = null;
        int n = 0, minimo = 0, maximo;
        String[][] dados;
        maximo = nLinhas(nTabela,cTipo);
        if (maximo > 0) {
            if ((maximo - nRegistro) > 0)
                minimo = maximo - nRegistro;
            maximo = nRegistro;
            try {
                conn = Conexao();
                PreparedStatement stm = null;
                sql = "SELECT INDICE, DADO1, DADO2, DADO3 FROM "+ nTabela +" WHERE TIPO = ? limit ?,?;";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, cTipo);
                stm.setInt(2, minimo);
                stm.setInt(3, maximo);
                ResultSet rs = stm.executeQuery();
                rs.last();
                int i = rs.getRow();
                if (i > 0) {
                    dados = new String[i][4];
                    rs.beforeFirst();
                    while(rs.next()){
                        dados[n][0] = Integer.toString(rs.getInt("INDICE"));
                        dados[n][1] = rs.getString("DADO1");
                        dados[n][2] = rs.getString("DADO2");
                        dados[n][3] = rs.getString("DADO3");
                        n++;
                    }
                    return dados;
                }
            }
            catch(SQLException e)
            {
                System.out.println(sql);
                System.out.println(nTabela);
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
        return null;
    }
    
    public static String[][] cTabela(String nTabela, int cTipo, int nRegistro, String Periodo[]) throws ParseException {
        Connection conn = null;
        String sql = null;
        int n = 0;
        String[][] dados;
        SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar dtINI = Calendar.getInstance();
        dtINI.setTime(f1.parse(Periodo[0]));
        Calendar dtFIM = Calendar.getInstance();
        dtFIM.setTime(f1.parse(Periodo[1]));
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            if ("qc_entrada".equals(nTabela))
                sql = "SELECT INDICE, DADO1, DADO2, DADO3 FROM "+ nTabela +" WHERE TIPO = ? AND DATA >= DATE_FORMAT(?,'%Y-%m-%d %T') AND DATA <= DATE_FORMAT(?,'%Y-%m-%d %T');";
            else
                sql = "SELECT INDICE, DADO1, DADO2, DADO3 FROM "+ nTabela +" WHERE TIPO = ? AND DATA_ENTRADA >= DATE_FORMAT(?,'%Y-%m-%d %T') AND DATA_ENTRADA <= DATE_FORMAT(?,'%Y-%m-%d %T');";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, cTipo);
            stm.setString(2, f2.format(dtINI.getTime()));
            stm.setString(3, f2.format(dtFIM.getTime()));
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                dados = new String[i][4];
                rs.beforeFirst();
                while(rs.next()){
                    dados[n][0] = Integer.toString(rs.getInt("INDICE"));
                    dados[n][1] = rs.getString("DADO1");
                    dados[n][2] = rs.getString("DADO2");
                    dados[n][3] = rs.getString("DADO3");
                    n++;
                }
                return dados;
            }
        }
        catch(SQLException e)
        {
            System.out.println(sql);
            System.out.println(nTabela);
            System.out.println(f2.format(dtINI.getTime()));
            System.out.println(f2.format(dtFIM.getTime()));
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
    
    public static String[][] cTabelaMetricas(String nTabela, int cTipo, int nRegistro) {
        Connection conn = null;
        String sql = null;
        int n = 0, minimo = 0, maximo;
        String[][] dados;
        maximo = nLinhas(nTabela,cTipo);
        if (maximo > 0) {
            if ((maximo - nRegistro) > 0)
                minimo = maximo - nRegistro;
            maximo = nRegistro;
            try {
                conn = Conexao();
                PreparedStatement stm = null;
                sql = "SELECT COVERAGE, UP_TO_DATENESS, ACCURACY, FREQUENCY, SIGNIFICANCE, QOC_GERAL FROM "+ nTabela +" WHERE TIPO = ? limit ?,?;";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, cTipo);
                stm.setInt(2, minimo);
                stm.setInt(3, maximo);
                ResultSet rs = stm.executeQuery();
                rs.last();
                int i = rs.getRow();
                if (i > 0) {
                    dados = new String[i][6];
                    rs.beforeFirst();
                    while(rs.next()){
                        dados[n][0] = Integer.toString(rs.getInt("COVERAGE"));
                        dados[n][1] = Double.toString(rs.getDouble("UP_TO_DATENESS"));
                        dados[n][2] = Integer.toString(rs.getInt("ACCURACY"));
                        dados[n][3] = Integer.toString(rs.getInt("FREQUENCY"));
                        dados[n][4] = Integer.toString(rs.getInt("SIGNIFICANCE"));
                        dados[n][5] = Double.toString(rs.getDouble("QOC_GERAL"));
                        n++;
                    }
                    return dados;
                }
            }
            catch(SQLException e)
            {
                System.out.println(sql);
                System.out.println(nTabela);
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
        return null;
    }
    
    public static String[][] cTabelaMetricas(String nTabela, int cTipo, int nRegistro, String Periodo[]) throws ParseException {
        Connection conn = null;
        String sql = null;
        int n = 0;
        String[][] dados;
        try {
            SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar dtINI = Calendar.getInstance();
            dtINI.setTime(f1.parse(Periodo[0]));
            Calendar dtFIM = Calendar.getInstance();
            dtFIM.setTime(f1.parse(Periodo[1]));
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT COVERAGE, UP_TO_DATENESS, ACCURACY, FREQUENCY, SIGNIFICANCE, QOC_GERAL FROM "+ nTabela +" WHERE TIPO = ? AND DATA_ENTRADA >= DATE_FORMAT(?,'%Y-%m-%d %T') AND DATA_ENTRADA <= DATE_FORMAT(?,'%Y-%m-%d %T');";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, cTipo);
            stm.setString(2, f2.format(dtINI.getTime()));
            stm.setString(3, f2.format(dtFIM.getTime()));
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                dados = new String[i][6];
                rs.beforeFirst();
                while(rs.next()){
                    dados[n][0] = Integer.toString(rs.getInt("COVERAGE"));
                    dados[n][1] = Double.toString(rs.getDouble("UP_TO_DATENESS"));
                    dados[n][2] = Integer.toString(rs.getInt("ACCURACY"));
                    dados[n][3] = Integer.toString(rs.getInt("FREQUENCY"));
                    dados[n][4] = Integer.toString(rs.getInt("SIGNIFICANCE"));
                    dados[n][5] = Double.toString(rs.getDouble("QOC_GERAL"));
                    n++;
                }
                return dados;
            }
        }
        catch(SQLException e)
        {
            System.out.println(sql);
            System.out.println(nTabela);
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
    
    public static int nLinhas(String nTabela) {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "SELECT COUNT(1) MAXIMO FROM "+nTabela+";";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return rs.getInt("MAXIMO");
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
    
    public static int nLinhas(String nTabela, int cTipo) {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "SELECT COUNT(1) MAXIMO FROM "+nTabela+" where TIPO = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, cTipo);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return rs.getInt("MAXIMO");
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

    public static String[][] sTipo() {
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

    public static String[][] cTabela(String nTabela, int cTipo, int nRegistro, boolean b) {
        Connection conn = null;
        String sql1 = null, sql2 = null, indice = "";
        int n, minimo = 0,maximo;
        String[][] dados;
        maximo = nLinhas(nTabela,cTipo);
        if (maximo > 0) {
            if ((maximo - nRegistro) > 0)
                minimo = maximo - nRegistro;
            maximo = nRegistro;
            try {
                conn = Conexao();
                PreparedStatement stm;
                sql1 = "SELECT INDICE FROM "+ nTabela +" WHERE TIPO = ? LIMIT ?,?;";
                stm = conn.prepareStatement(sql1);
                stm.setInt(1, cTipo);
                stm.setInt(2, minimo);
                stm.setInt(3, maximo);
                ResultSet rs = stm.executeQuery();
                rs.last();
                int i = rs.getRow();
                rs.beforeFirst();
                n = 0;
                while(rs.next()){
                    indice += rs.getInt("INDICE");
                    n++;
                    if (n < i)
                        indice += ",";
                }
                
                sql2 = "SELECT DADO1, Count(*) TOTAL FROM "+ nTabela +" WHERE TIPO = ? and INDICE in ("+ indice +") group by DADO1";
                stm = conn.prepareStatement(sql2);
                stm.setInt(1, cTipo);
                ResultSet rs1 = stm.executeQuery();
                rs1.last();
                i = rs1.getRow();
                if (i > 0) {
                    dados = new String[i][2];
                    rs1.beforeFirst();
                    n = 0;
                    Double x;
                    while(rs1.next()){
                        x = Double.parseDouble(rs1.getString("DADO1"));
                        dados[n][0] = Integer.toString(x.intValue());
                        dados[n][1] = Integer.toString(rs1.getInt("TOTAL"));
                        n++;
                    }
                    return dados;
                }
            }
            catch(SQLException e)
            {
                System.out.println(sql1);
                System.out.println(sql2);
                System.out.println(nTabela);
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
        return null;
    }
    
    public static String[][] cTabela(String nTabela, int cTipo, int nRegistro, boolean b, String Periodo []) throws ParseException {
        Connection conn = null;
        String sql1 = null, sql2 = null, indice = "";
        int n, minimo = 0,maximo;
        String[][] dados;
        maximo = nLinhas(nTabela,cTipo);
        if (maximo > 0) {
            if ((maximo - nRegistro) > 0)
                minimo = maximo - nRegistro;
            maximo = nRegistro;
            try {
                SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar dtINI = Calendar.getInstance();
                dtINI.setTime(f1.parse(Periodo[0]));
                Calendar dtFIM = Calendar.getInstance();
                dtFIM.setTime(f1.parse(Periodo[1]));
                conn = Conexao();
                PreparedStatement stm;
                if ("qc_entrada".equals(nTabela))
                    sql1 = "SELECT INDICE FROM "+ nTabela +" WHERE TIPO = ? AND DATA >= DATE_FORMAT(?,'%Y-%m-%d %T') AND DATA <= DATE_FORMAT(?,'%Y-%m-%d %T');";
                else
                    sql1 = "SELECT INDICE FROM "+ nTabela +" WHERE TIPO = ? AND DATA_ENTRADA >= DATE_FORMAT(?,'%Y-%m-%d %T') AND DATA_ENTRADA <= DATE_FORMAT(?,'%Y-%m-%d %T');";
                stm = conn.prepareStatement(sql1);
                stm.setInt(1, cTipo);
                stm.setString(2, f2.format(dtINI.getTime()));
                stm.setString(3, f2.format(dtFIM.getTime()));
                ResultSet rs = stm.executeQuery();
                rs.last();
                int i = rs.getRow();
                rs.beforeFirst();
                n = 0;
                while(rs.next()){
                    indice += rs.getInt("INDICE");
                    n++;
                    if (n < i)
                        indice += ",";
                }
                
                sql2 = "SELECT DADO1, Count(*) TOTAL FROM "+ nTabela +" WHERE TIPO = ? and INDICE in ("+ indice +") group by DADO1";
                stm = conn.prepareStatement(sql2);
                stm.setInt(1, cTipo);
                ResultSet rs1 = stm.executeQuery();
                rs1.last();
                i = rs1.getRow();
                if (i > 0) {
                    dados = new String[i][2];
                    rs1.beforeFirst();
                    n = 0;
                    Double x;
                    while(rs1.next()){
                        x = Double.parseDouble(rs1.getString("DADO1"));
                        dados[n][0] = Integer.toString(x.intValue());
                        dados[n][1] = Integer.toString(rs1.getInt("TOTAL"));
                        n++;
                    }
                    return dados;
                }
            }
            catch(SQLException e)
            {
                System.out.println(sql1);
                System.out.println(sql2);
                System.out.println(nTabela);
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
        return null;
    }

    public static int Metricas(String Campo, int Tipo, int i, String Periodo[]) throws ParseException {
        Connection conn = null;
        String sql = null;
        SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar dtINI = Calendar.getInstance();
        dtINI.setTime(f1.parse(Periodo[0]));
        Calendar dtFIM = Calendar.getInstance();
        dtFIM.setTime(f1.parse(Periodo[1]));
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT count(*) FROM qc_saida WHERE TIPO = ? AND "+Campo+" = ? AND DATA_ENTRADA >= DATE_FORMAT(?,'%Y-%m-%d %T') AND DATA_ENTRADA <= DATE_FORMAT(?,'%Y-%m-%d %T');";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Tipo);
            stm.setInt(2,i);
            stm.setString(3, f2.format(dtINI.getTime()));
            stm.setString(4, f2.format(dtFIM.getTime()));
            ResultSet rs = stm.executeQuery();
            rs.next();
            return rs.getInt(1);
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
        return 0;
    }
    
    public static int Status(String Tabela, int Tipo) throws ParseException {
        Connection conn = null;
        String sql = null;
        SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar dtFIM = Calendar.getInstance();
        dtFIM.setTime(new Date());
        dtFIM.add(Calendar.SECOND,-Tempo(Tipo)*2);
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            if ("qc_entrada".equals(Tabela))
                sql = "SELECT COUNT(*) FROM "+Tabela+" WHERE TIPO = ? AND DATA >= DATE_FORMAT(?,'%Y-%m-%d %T');";
            else
                sql = "SELECT COUNT(*) FROM "+Tabela+" WHERE TIPO = ? AND DATA_ENTRADA >= DATE_FORMAT(?,'%Y-%m-%d %T');";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Tipo);
            stm.setString(2, f2.format(dtFIM.getTime()));
            ResultSet rs = stm.executeQuery();
            rs.next();
            return rs.getInt(1);
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
        return 0;
    }

    private static int Tempo(int Tipo) {
        Connection conn = null;
        String sql = null;
        try {
            conn = Conexao();
            PreparedStatement stm;
            
            sql = "SELECT A.T_ATUALIZACAO FROM QC_TIPO A WHERE A.CODIGO = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Tipo);
            ResultSet rs = stm.executeQuery();
            int temp = 0;
            while (rs.next()){
                temp = rs.getInt(1);
            }
            return temp;
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
        return 0;
    }
}
