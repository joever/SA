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
public class TipoSensorDAO {
    private final Connection connection;
    
    public TipoSensorDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(TipoSensor myTipoSensor) {
        if (myTipoSensor.getIdTipoSensor() != null) {
            if (!idExiste(myTipoSensor.getIdTipoSensor())) {
                String sql = "insert into tiposensor" +
                            " (idtiposensor,nome,descricao)" +
                            " values (?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myTipoSensor.getIdTipoSensor());
                    stmt.setString(2, myTipoSensor.getNome());
                    stmt.setString(3, myTipoSensor.getDescricao());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean idExiste(Integer myId){
        String sql = "select idtiposensor from tiposensor where idtiposensor = "+myId;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(TipoSensor myTipoSensor){
        if (myTipoSensor.getIdTipoSensor() != null) {
            if (myTipoSensor.getIdTipoSensor() > 0) {
                String sql = "update tiposensor" +
                            " set nome = ?, descricao = ?" +
                            " where idtiposensor = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, myTipoSensor.getNome());
                    stmt.setString(2, myTipoSensor.getDescricao());
                    stmt.setInt(3, myTipoSensor.getIdTipoSensor());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(Integer myId){
        if (myId != null) {
            if (myId > 0) {
                String sql = "delete from tiposensor" +
                            " where idtiposensor = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myId);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<TipoSensor> getTiposSensor() {
        List<TipoSensor> myTiposSensor = new ArrayList<>();
        String sql = "select * from tiposensor";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                TipoSensor myTipoSensor = new TipoSensor();
                myTipoSensor.setIdTipoSensor(rs.getInt("idtiposensor"));
                myTipoSensor.setNome(rs.getString("nome"));
                myTipoSensor.setDescricao(rs.getString("descricao"));
                myTiposSensor.add(myTipoSensor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myTiposSensor;
    }
    
    public Integer getProxId() {
        Integer id = 0;
        String sql = "select max(idtiposensor) as idtiposensor from tiposensor";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                id = rs.getInt("idtiposensor") + 1;
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        id++;
        return id;
    }
}
