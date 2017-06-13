/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.dao;

import com.ufes.sa.jdbc.ConnectionFactory;
import com.ufes.sa.xml.SaidaRede;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joever.hoffman
 */
public class SaidaRedeDAO {
    private final Connection connection;
    
    public SaidaRedeDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public Boolean adiciona(SaidaRede mySaidaRede){
        String sql = "insert into saidarede" +
                    " (idrede,nomerede,idsensor,nomesensor,idtm,tipomonitoramento,idusuario,dthrgeracao,dthrenvio,valormensurado,coverage,up_to_dateness,accuracy,frequency,significance,qoc_geral)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, mySaidaRede.getIdRede());
            stmt.setString(2, mySaidaRede.getNomeRede());
            stmt.setInt(3, mySaidaRede.getIdSensor());
            stmt.setString(4, mySaidaRede.getNomeSensor());
            stmt.setInt(5, mySaidaRede.getIdTM());
            stmt.setString(6, mySaidaRede.getTipoMonitoramento());
            stmt.setString(7, mySaidaRede.getCpf());
            stmt.setString(8, mySaidaRede.getDthrGeracao());
            stmt.setString(9, mySaidaRede.getDthrEnvio());
            stmt.setString(10, mySaidaRede.getValor());
            stmt.setString(11, mySaidaRede.getCoverage());
            stmt.setString(12, mySaidaRede.getUp_to_dateness());
            stmt.setString(13, mySaidaRede.getAccuracy());
            stmt.setString(14, mySaidaRede.getFrequency());
            stmt.setString(15, mySaidaRede.getSignificance());
            stmt.setString(16, mySaidaRede.getQoc_geral());
            System.out.println(stmt.toString());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<SaidaRede> getSaidasRede(Integer idRede, Integer idTM, String idUsuario, String dthrInicial, String dthrFinal) {
        List<SaidaRede> mySaidasRede = new ArrayList<>();
        String sql = "select * from saidarede where idrede = ? and idtm = ? and idusuario = ? and dthrGeracao >= ? and dthrGeracao <= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setInt(2, idTM);
            stmt.setString(3, idUsuario);
            stmt.setString(4, dthrInicial);
            stmt.setString(5, dthrFinal);
            ResultSet rs = stmt.executeQuery();
            System.out.println(stmt.toString());
            while (rs.next()) {
                SaidaRede mySaidaRede = new SaidaRede();
                mySaidaRede.setIdRede(rs.getInt("idrede"));
                mySaidaRede.setNomeRede(rs.getString("nomeRede"));
                mySaidaRede.setIdSensor(rs.getInt("idSensor"));
                mySaidaRede.setNomeSensor(rs.getString("nomeSensor"));
                mySaidaRede.setIdTM(rs.getInt("idTM"));
                mySaidaRede.setTipoMonitoramento(rs.getString("tipoMonitoramento"));
                mySaidaRede.setCpf(rs.getString("idusuario"));
                mySaidaRede.setDthrGeracao(rs.getString("dthrGeracao"));
                mySaidaRede.setDthrEnvio(rs.getString("dthrEnvio"));
                mySaidaRede.setValor(rs.getString("valormensurado"));
                mySaidaRede.setCoverage(rs.getString("coverage"));
                mySaidaRede.setUp_to_dateness(rs.getString("up_to_dateness"));
                mySaidaRede.setAccuracy(rs.getString("accuracy"));
                mySaidaRede.setFrequency(rs.getString("frequency"));
                mySaidaRede.setSignificance(rs.getString("significance"));
                mySaidaRede.setQoc_geral(rs.getString("qoc_geral"));
                mySaidasRede.add(mySaidaRede);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaidaRedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mySaidasRede;
    }
}
