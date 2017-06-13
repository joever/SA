
package controle;

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
import javax.swing.JOptionPane;

/**
 *
 * @author Joéver Silva Hoffman
 * Data: 22/06/2013
 * Classe útil aos acessos ao banco de dados.
 *
 */
public class BancoUtil {
    /**
    @Método: Conexao;
    Entrada: Não possui;
    Retorno: Conexão com o banco;
    Descrição: Conecta ao banco de dados mydvt001 utilizando o usuário contexto;
    */
    public static Connection Conexao () throws SQLException {
        final String driver = "com.mysql.jdbc.Driver";
        final String url = "jdbc:mysql://localhost:3306/mydvt001";

        try {
                Class.forName(driver);
                return (DriverManager.getConnection(url,"contexto","contexto"));
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
            }
        return null;
    }
    /**
    @Método: nLinhas;
    Entrada: Nome da Tabela
    Retorno: Número de linhas;
    Descrição: Executa uma consulta na base de dados para buscar a quantidade de linhas da tabela que foi passada como parâmetro;
    */
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
    /**
    @Método: nLinhas;
    Entrada: Nome da Tabela e código do tipo
    Retorno: Número de linhas;
    Descrição: Executa uma consulta na base de dados para buscar a quantidade de linhas referente ao códico do tipo;
    */
    public static int nLinhas(String nTabela, int cTipo) {
        Connection conn = null;
        try {
            conn = Conexao();
                
            PreparedStatement stm = null;
            String sql = "SELECT COUNT(1) MAXIMO FROM "+nTabela+" WHERE TIPO = ?;";
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
    /**
    @Método: cTipo;
    Entrada: Não possui;
    Retorno: Uma String com todos os códigos dos tipos existentes;
    Descrição: Executa uma consulta na base de dados buscando todos os tipos cadastrados na tabela qc_tipo;
    */
    public static String[] cTipo() {
        Connection conn = null;
        int n = 0;
        String sql = null;
        String[]dados;
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT CODIGO FROM qc_tipo;";
            stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                dados = new String[i];
                rs.beforeFirst();
                while(rs.next()){
                    dados[n] = Integer.toString(rs.getInt("CODIGO"));
                    n++;
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
    /**
    @Método: eControle;
    Entrada: Código d Tipo;
    Retorno: true ou false;
    Descrição: Verifica se o código do Tipo já existe na tabela de controle qc_controle;
    */
    public static boolean eControle(int tipo) {
        if (nLinhas("qc_controle",tipo) > 0)
            return true;
        else
            return false;
    }
    /**
    @Método: iControle;
    Entrada: Código do Tipo, Quantidade de registros existentes na tabela qc_entrada para o tipo, Número de registros já processados, É para processar ou não (true ou false), Observações;
    Retorno: true ou false;
    Descrição: Insere uma nova linha na tabela qc_controle e retorna (true ou false) referente a execução do SQL;
    */
    public static boolean iControle(int tipo, int nEntrada, int processado, String processar, String obs) throws SQLException {
        Connection conn = null;

        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "insert into qc_controle (tipo,quantidade,processado,processar,obs)values(?,?,?,?,?);";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, tipo);
            stm.setInt(2, nEntrada);
            stm.setInt(3, processado);
            stm.setString(4, processar);
            stm.setString(5, obs);

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
    /**
    @Método: aControle;
    Entrada: Código do Tipo, quantidade de registros, número de registros já processados, String informando se é para continuar processando ou não (true ou false);
    Retorno: não há retorno;
    Descrição: Atualiza os campos quantidade, processado e processar da tabela qc_controle para o código Tipo passado;
    */
    public static void aControle(int tipo, int Quantidade, int Processado, String Processar) throws SQLException {
        Connection conn = null;

        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "update qc_controle set quantidade = ?, processado = ?, processar = ? where tipo = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Quantidade);
            stm.setInt(2, Processado);
            stm.setString(3,Processar);
            stm.setInt(4, tipo);
            
            if (!stm.execute()) {
                conn.commit();
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
    }
    /**
    @Método: aControle;
    Entrada: Código do Tipo, quantidade de registros e String informando se é para continuar processando ou não (true ou false);
    Retorno: não há retorno;
    Descrição: Atualiza os campos quantidade e processar da tabela qc_controle para o código Tipo passado;
    */
    public static void aControle(int tipo, int Quantidade, String Processar) throws SQLException {
        Connection conn = null;

        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "update qc_controle set quantidade = ?, processar = ? where tipo = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Quantidade);
            stm.setString(2,Processar);
            stm.setInt(3, tipo);
            
            if (!stm.execute()) {
                conn.commit();
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
    }
    /**
    @Método: aControle;
    Entrada: String informando se é para continuar processando ou não (true ou false), Código do Tipo e número de itens já processados;
    Retorno: não há retorno;
    Descrição: Atualiza os campos processado e processar da tabela qc_controle para o código Tipo passado;
    */
    public static void aControle(String Processar, int tipo, int Processado) throws SQLException {
        Connection conn = null;

        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;
            
            String sql = "update qc_controle set processado = ?, processar = ? where tipo = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Processado);
            stm.setString(2,Processar);
            stm.setInt(3, tipo);
            
            if (!stm.execute()) {
                conn.commit();
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
    }
    /**
    @Método: carregaControle;
    Entrada: Código do Tipo;
    Retorno: String com as informações de controle para o código do tipo passado;
    Descrição: Executa uma consulta na base de dados e retorna as informações de controle para o código do tipo passado.;
    */
    public static String[] carregaControle(int Tipo) {
        Connection conn = null;
        String sql = null;
        String[] dados = new String[3];
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT QUANTIDADE, PROCESSADO, PROCESSAR FROM qc_controle WHERE TIPO = ?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Tipo);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                dados[0] = Integer.toString(rs.getInt("QUANTIDADE"));
                dados[1] = Integer.toString(rs.getInt("PROCESSADO"));
                dados[2] = rs.getString("PROCESSAR");
            }
            return dados;
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
    /**
    @Método: carregaEntrada;
    Entrada: Código do Tipo, registro inicial e quantidade de registro;
    Retorno: String com as informações do dado de entrada (qc_entrada);
    Descrição: Executa uma consulta na base de dados e retorna uma String:
    String[][0] = DADO1
    String[][1] = DADO2
    String[][2] = DADO3
    String[][3] = DATA
    */
    public static String[][] carregaEntrada(int Tipo, int minimo, int maximo) {
        Connection conn = null;
        String sql = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int n = 0;
        String[][] dados;
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT DADO1, DADO2, DADO3, DATA FROM qc_entrada WHERE TIPO = ? limit ?,?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Tipo);
            stm.setInt(2, minimo);
            stm.setInt(3, maximo);
            ResultSet rs = stm.executeQuery();
            rs.last();
            int i = rs.getRow();
            if (i > 0) {
                dados = new String[i][11];
                rs.beforeFirst();
                while(rs.next()){
                    dados[n][0] = rs.getString("DADO1");
                    dados[n][1] = rs.getString("DADO2");
                    dados[n][2] = rs.getString("DADO3");
                    dados[n][3] = f.format(rs.getTimestamp("DATA"));
                    n++;
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
    /**
    @Método: carregaTipoValida;
    Entrada: Código do Tipo;
    Retorno: String com as informações do tipo de dados que está sendo processado;
    Descrição: Executa uma consulta na base de dados e retorna as informações do tipo de dados que será processado:
    String[][0] = TIPO
    String[][1] = VALIDA1
    String[][2] = VALIDA2
    */
    public static String[] carregaTipoValida(int Tipo) {
        Connection conn = null;
        String sql1 = null;
        String[] tValida = new String[19];
        try {
            conn = Conexao();
            PreparedStatement stm;
            
            sql1 = "SELECT B.TIPO TIPO, "
                    + "A.VALIDA1 VALIDA1, "
                    + "A.VALIDA2 VALIDA2, "
                    + "A.OPCAO OPCAO, "
                    + "A.F_INFERIOR F_INFERIOR, "
                    + "A.F_SUPERIOR F_SUPERIOR, "
                    + "A.C_GERAR C_GERAR, "
                    + "A.T_VIDA T_VIDA, "
                    + "A.T_ATUALIZACAO T_ATUALIZACAO, "
                    + "A.OPERADOR1 OPERADOR1, "
                    + "A.VALOR1 VALOR1, "
                    + "A.OPERADOR2 OPERADOR2, "
                    + "A.VALOR2 VALOR2, "
                    + "A.S_GERAR S_GERAR, "
                    + "A.P_COVERAGE P_COVERAGE, "
                    + "A.P_UP_TO_DATENESS P_UP_TO_DATENESS, "
                    + "A.P_ACCURACY P_ACCURACY, "
                    + "A.P_FREQUENCY P_FREQUENCY, "
                    + "A.P_SIGNIFICANCE P_SIGNIFICANCE "
                    + "FROM qc_tipo A, qc_tipo_valida B "
                    + "WHERE A.CODIGO = ? AND "
                    + "A.TIPO_VALIDA = B.CODIGO;";
            stm = conn.prepareStatement(sql1);
            stm.setInt(1, Tipo);
            ResultSet rs1 = stm.executeQuery();
            rs1.next();
            tValida[0] = rs1.getString(1);
            tValida[1] = Double.toString(rs1.getDouble(2));
            tValida[2] = Double.toString(rs1.getDouble(3));
            tValida[3] = Integer.toString(rs1.getInt(4));
            tValida[4] = Integer.toString(rs1.getInt(5));
            tValida[5] = Integer.toString(rs1.getInt(6));
            tValida[6] = Integer.toString(rs1.getInt(7));
            tValida[7] = Integer.toString(rs1.getInt(8));
            tValida[8] = Integer.toString(rs1.getInt(9));
            tValida[9] = Integer.toString(rs1.getInt(10));
            tValida[10] = Integer.toString(rs1.getInt(11));
            tValida[11] = Integer.toString(rs1.getInt(12));
            tValida[12] = Integer.toString(rs1.getInt(13));
            tValida[13] = Integer.toString(rs1.getInt(14));
            tValida[14] = Double.toString(rs1.getDouble(15));
            tValida[15] = Double.toString(rs1.getDouble(16));
            tValida[16] = Double.toString(rs1.getDouble(17));
            tValida[17] = Double.toString(rs1.getDouble(18));
            tValida[18] = Double.toString(rs1.getDouble(19));
            return tValida;
        }
        catch(SQLException e)
        {
            System.out.println(sql1);
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
    /**
    @Método: carregaSaida;
    Entrada: Código do Tipo, registro inicial e quantidade de registro;
    Retorno: String com as informações do dado de saída (qc_saida);
    Descrição: Executa uma consulta na base de dados e retorna uma String:
    String[][0] = DADO1
    String[][1] = DADO2
    String[][2] = DADO3
    */
    public static String[][] carregaSaida(int Tipo, int minimo, int maximo) {
        Connection conn = null;
        String sql = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int n = 0;
        String[][] dados;
        try {
            conn = Conexao();
            PreparedStatement stm = null;
            sql = "SELECT DADO1, DADO2, DADO3, DATA_ENTRADA FROM qc_saida WHERE TIPO = ? limit ?,?;";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, Tipo);
            stm.setInt(2, minimo);
            stm.setInt(3, maximo);
            ResultSet rs = stm.executeQuery();
            dados = new String[maximo][4];
            rs.beforeFirst();
            while(rs.next()){
                dados[n][0] = rs.getString("DADO1");
                dados[n][1] = rs.getString("DADO2");
                dados[n][2] = rs.getString("DADO3");
                dados[n][3] = f.format(rs.getTimestamp("DATA_ENTRADA"));
                n++;
            }
            return dados;
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
    /**
    @Método: iSaida;
    Entrada: String com os dados a serem gravados e o código do tipo;
    Retorno: true ou false
    Descrição: Realiza insert na tabela qc_entrada com os dados que estão na String:
    String[][0] = DADO1
    String[][1] = DADO2
    String[][2] = DADO3
    String[][3] = DATA
    */
    public static boolean iSaida(String[][] dadosSaida, int Tipo) throws SQLException {
        Date data = new Date();
        SimpleDateFormat DataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean valida = true;
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;

            int i = 0;
            while (i < dadosSaida.length && valida) {
                String sql = "insert into qc_saida (tipo,dado1,dado2,dado3,data_entrada,data_processado)values(?,?,?,?,?,?);";

                stm = conn.prepareStatement(sql);
                stm.setInt(1, Tipo);
                stm.setString(2, dadosSaida[i][0]);
                stm.setString(3, dadosSaida[i][1]);
                stm.setString(4, dadosSaida[i][2]);
                stm.setString(5, dadosSaida[i][3]);
                stm.setString(6, DataTime.format(data));

                if (stm.execute()) { //Se apresentar algum problema ao incluir o registro, sai e executa rollback.
                    valida = false;
                }
                i++;
            }
            if (valida){
                conn.commit();
                return true;
            }
            else{
                conn.rollback();
                return false;
            }
        }
        catch(SQLException e)
        {
            conn.rollback();
            JOptionPane.showMessageDialog(null, "Erro no SQL. Código: "+e.getErrorCode()+" Descrição: "+ e.getSQLState(), "Erro", WIDTH);
            e.printStackTrace();
			return false;
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
    
    /**
    @Método: iSaida;
    Entrada: String com os dados a serem gravados e o código do tipo;
    Retorno: true ou false
    Descrição: Realiza insert na tabela qc_entrada com os dados que estão na String até um certo ponto:
    String[][0] = DADO1
    String[][1] = DADO2
    String[][2] = DADO3
    String[][3] = DATA
    */
    public static int iSaida(String[][] dadosSaida, int Tipo, int Falta) throws SQLException {
        int removido = 0;
        Date data = new Date();
        SimpleDateFormat DataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean valida = true;
        Connection conn = null;
        
        try {
            conn = Conexao();
            conn.setAutoCommit(false);
            
            PreparedStatement stm;

            int i = 0;
            while (i < (dadosSaida.length - Falta) && valida) {
                if ("0".equals(dadosSaida[i][10])){
                    String sql = "insert into qc_saida ("
                            + "tipo,"
                            + "dado1,"
                            + "dado2,"
                            + "dado3,"
                            + "data_entrada,"
                            + "data_processado,"
                            + "coverage,"
                            + "up_to_dateness,"
                            + "accuracy,"
                            + "frequency,"
                            + "significance,"
                            + "qoc_geral)"
                            + "values(?,?,?,?,?,?,?,?,?,?,?,?);";

                    stm = conn.prepareStatement(sql);
                    stm.setInt(1, Tipo);
                    stm.setString(2, dadosSaida[i][0]);
                    stm.setString(3, dadosSaida[i][1]);
                    stm.setString(4, dadosSaida[i][2]);
                    stm.setString(5, dadosSaida[i][3]);
                    stm.setString(6, DataTime.format(data));
                    stm.setInt(7, Integer.parseInt(dadosSaida[i][4]));
                    stm.setDouble(8, Double.parseDouble(dadosSaida[i][5]));
                    stm.setInt(9, Integer.parseInt(dadosSaida[i][6]));
                    stm.setInt(10, Integer.parseInt(dadosSaida[i][7]));
                    stm.setInt(11, Integer.parseInt(dadosSaida[i][8]));
                    stm.setDouble(12, Double.parseDouble(dadosSaida[i][9]));

                    if (stm.execute()) { //Se apresentar algum problema ao incluir o registro, sai e executa rollback.
                        valida = false;
                    }
                }
                else
                    removido++;
                i++;
            }
            if (valida){
                conn.commit();
                return removido;
            }
            else{
                conn.rollback();
                return -1;
            }
        }
        catch(SQLException e)
        {
            conn.rollback();
            JOptionPane.showMessageDialog(null, "Erro no SQL. Código: "+e.getErrorCode()+" Descrição: "+ e.getSQLState(), "Erro", WIDTH);
            e.printStackTrace();
            return -1;
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
        return -1;
    }
}
