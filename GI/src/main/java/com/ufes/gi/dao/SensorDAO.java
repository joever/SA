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
public class SensorDAO {
    private final Connection connection;
    
    public SensorDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(Sensor mySensor) {
        if (mySensor.getIdSensor() != null) {
            if (!idExiste(mySensor.getIdSensor(),mySensor.getCpf())) {
                String sql = "insert into sensor" +
                            " (idsensor,nome,cpf,idtiposensor,idmodelosensor)" +
                            " values (?,?,?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, mySensor.getIdSensor());
                    stmt.setString(2, mySensor.getNome());
                    stmt.setString(3, mySensor.getCpf());
                    stmt.setInt(4, mySensor.getIdTipoSensor());
                    stmt.setInt(5, mySensor.getIdModeloSensor());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(SensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean idExiste(Integer myId, String cpf){
        String sql = "select idsensor from sensor where idsensor = "+myId+" and cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(Sensor mySensor){
        if (mySensor.getIdSensor() != null) {
            if (mySensor.getIdSensor() > 0) {
                String sql = "update sensor" +
                            " set nome = ?, idtiposensor = ?, idmodelosensor = ?" +
                            " where idsensor = ? and cpf = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, mySensor.getNome());
                    stmt.setInt(2, mySensor.getIdTipoSensor());
                    stmt.setInt(3, mySensor.getIdModeloSensor());
                    stmt.setInt(4, mySensor.getIdSensor());
                    stmt.setString(5, mySensor.getCpf());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(SensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(Integer myId, String cpf){
        if (myId != null) {
            if (myId > 0) {
                String sql = "delete from sensor" +
                            " where idsensor = ? and cpf = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myId);
                    stmt.setString(2, cpf);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(SensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<Sensor> getSensores() {
        List<Sensor> mySensores = new ArrayList<>();
        String sql = "select * from sensor";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                Sensor mySensor = new Sensor();
                mySensor.setIdSensor(rs.getInt("idsensor"));
                mySensor.setNome(rs.getString("nome"));
                mySensor.setCpf(rs.getString("cpf"));
                mySensor.setIdTipoSensor(rs.getInt("idtiposensor"));
                mySensor.setIdModeloSensor(rs.getInt("idmodelosensor"));
                mySensores.add(mySensor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mySensores;
    }
    
    public Sensor getSensor(Integer idSensor, String CPF) {
        Sensor mySensor = new Sensor();
        String sql = "select * from sensor where idsensor = ? and cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idSensor);
            stmt.setString(2, CPF);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mySensor.setIdSensor(rs.getInt("idsensor"));
                mySensor.setNome(rs.getString("nome"));
                mySensor.setCpf(rs.getString("cpf"));
                mySensor.setIdTipoSensor(rs.getInt("idtiposensor"));
                mySensor.setIdModeloSensor(rs.getInt("idmodelosensor"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mySensor;
    }
    
    public Integer getProxId() {
        Integer id = 0;
        String sql = "select max(idsensor) as idsensor from sensor";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                id = rs.getInt("idsensor") + 1;
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        id++;
        return id;
    }

    public String getNome(Integer idSensor, String cpf) {
        String sql = "select nome from sensor where idsensor = "+idSensor+" and cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Integer getIdTS(Integer idSensor, String cpf) {
        String sql = "select idtiposensor from sensor where idsensor = "+idSensor+" and cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getInt("idtiposensor");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Integer getIdMS(Integer idSensor, String cpf) {
        String sql = "select idmodelosensor from sensor where idsensor = "+idSensor+" and cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getInt("idmodelosensor");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
