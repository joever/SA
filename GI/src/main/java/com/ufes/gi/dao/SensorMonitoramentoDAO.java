/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.dao;

import com.ufes.gi.jdbc.ConnectionFactory;
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
 * @author joever
 */
public class SensorMonitoramentoDAO {
    private final Connection connection;
    
    public SensorMonitoramentoDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(SensorMonitoramento mySM) {
        if (mySM.getIdSensor() != null) {
            if (!idExiste(mySM.getIdSensor(),mySM.getCpf(),mySM.getIdTM())) {
                String sql = "insert into sensormonitoramento" +
                            " (idsensor,cpf,idtm,media,diferenca,idtf)" +
                            " values (?,?,?,?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, mySM.getIdSensor());
                    stmt.setString(2, mySM.getCpf());
                    stmt.setInt(3, mySM.getIdTM());
                    stmt.setInt(4, mySM.getMedia());
                    stmt.setInt(5, mySM.getDiferenca());
                    stmt.setInt(6, mySM.getIdTF());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(SensorMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean idExiste(Integer idSensor, String cpf, Integer idTM){
        String sql = "select idsensor from sensormonitoramento where idsensor = "+idSensor+" and cpf = '"+cpf+"' and idtm = "+idTM;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SensorMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(SensorMonitoramento mySM){
        if (mySM.getIdSensor() != null) {
            if (mySM.getIdSensor() > 0) {
                String sql = "update sensormonitoramento" +
                            " set media = ?, diferenca = ?, idtf = ?" +
                            " where idsensor = ? and cpf = ? and idtm = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, mySM.getMedia());
                    stmt.setInt(2, mySM.getDiferenca());
                    stmt.setInt(3, mySM.getIdTF());
                    stmt.setInt(4, mySM.getIdSensor());
                    stmt.setString(5, mySM.getCpf());
                    stmt.setInt(6, mySM.getIdTM());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(SensorMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(Integer idSensor, String cpf, Integer idTM, Integer idTF){
        if (idSensor != null && cpf != null && idTM != null) {
            if (idSensor > 0 && idTM > 0 && !cpf.isEmpty()) {
                String sql = "delete from sensormonitoramento" +
                            " where idsensor = ? and cpf = ? and idtm = ? and idtf = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, idSensor);
                    stmt.setString(2, cpf);
                    stmt.setInt(3, idTM);
                    stmt.setInt(4, idTF);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(SensorMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<SensorMonitoramento> getSMs() {
        List<SensorMonitoramento> mySMs = new ArrayList<>();
        String sql = "select * from sensormonitoramento";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                SensorMonitoramento mySM = new SensorMonitoramento();
                mySM.setIdSensor(rs.getInt("idsensor"));
                mySM.setCpf(rs.getString("cpf"));
                mySM.setIdTM(rs.getInt("idtm"));
                mySM.setMedia(rs.getInt("media"));
                mySM.setDiferenca(rs.getInt("diferenca"));
                mySM.setIdTF(rs.getInt("idtf"));
                mySMs.add(mySM);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SensorMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mySMs;
    }
    
    public List<SensorMonitoramento> getSM(Integer idSensor, String cpf) {
        List<SensorMonitoramento> mySMs = new ArrayList<>();
        String sql = "select * from sensormonitoramento where idsensor = ? and cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idSensor);
            stmt.setString(2, cpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SensorMonitoramento mySM = new SensorMonitoramento();
                mySM.setIdSensor(rs.getInt("idsensor"));
                mySM.setCpf(rs.getString("cpf"));
                mySM.setIdTM(rs.getInt("idtm"));
                mySM.setMedia(rs.getInt("media"));
                mySM.setDiferenca(rs.getInt("diferenca"));
                mySM.setIdTF(rs.getInt("idtf"));
                mySMs.add(mySM);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SensorMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mySMs;
    }
}
