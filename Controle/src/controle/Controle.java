package controle;

import java.sql.SQLException;

/**
 *
 * @author Joéver Silva Hoffman
 * Data: 22/06/2013
 * Classe que realiza o controle no processamento dos dados que estão sendo incluídos nas tabelas:
 * qc_tipo, qc_tipovalida, qc_entrada, qc_saida e qc_controle
 * São criadas threads para cada código de tipo existente na tabela qc_tipo para realizar o processamento desses dados
 *
 */
public class Controle {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        Util tControle = new Util();
        tControle.CarregaThreads();
    }
}