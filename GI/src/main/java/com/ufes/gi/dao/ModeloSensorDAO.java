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
public class ModeloSensorDAO {
    private final Connection connection;
    
    public ModeloSensorDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(ModeloSensor myModeloSensor) {
        if (myModeloSensor.getIdModeloSensor() != null) {
            if (!idExiste(myModeloSensor.getIdModeloSensor())) {
                String sql = "insert into modelosensor" +
                            " (idmodelosensor,nome,descricao,nsensores)" +
                            " values (?,?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myModeloSensor.getIdModeloSensor());
                    stmt.setString(2, myModeloSensor.getNome());
                    stmt.setString(3, myModeloSensor.getDescricao());
                    stmt.setInt(4, myModeloSensor.getnSensores());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(ModeloSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean idExiste(Integer myId){
        String sql = "select idmodelosensor from modelosensor where idmodelosensor = "+myId;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(ModeloSensor myModeloSensor){
        if (myModeloSensor.getIdModeloSensor() != null) {
            if (myModeloSensor.getIdModeloSensor() > 0) {
                String sql = "update modelosensor" +
                            " set nome = ?, descricao = ?, nsensores = ?" +
                            " where idmodelosensor = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, myModeloSensor.getNome());
                    stmt.setString(2, myModeloSensor.getDescricao());
                    stmt.setInt(3, myModeloSensor.getnSensores());
                    stmt.setInt(4, myModeloSensor.getIdModeloSensor());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(ModeloSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(Integer myId){
        if (myId != null) {
            if (myId > 0) {
                String sql = "delete from modelosensor" +
                            " where idmodelosensor = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myId);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(ModeloSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<ModeloSensor> getModeloSensores() {
        List<ModeloSensor> myModeloSensores = new ArrayList<>();
        String sql = "select * from modelosensor";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                ModeloSensor myModeloSensor = new ModeloSensor();
                myModeloSensor.setIdModeloSensor(rs.getInt("idmodelosensor"));
                myModeloSensor.setNome(rs.getString("nome"));
                myModeloSensor.setDescricao(rs.getString("descricao"));
                myModeloSensor.setnSensores(rs.getInt("nsensores"));
                myModeloSensores.add(myModeloSensor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myModeloSensores;
    }
    
    public Integer getProxId() {
        Integer id = 0;
        String sql = "select max(idmodelosensor) as idmodelosensor from modelosensor";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                id = rs.getInt("idmodelosensor") + 1;
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        id++;
        return id;
    }
    
    public Integer getNSensor(Integer idModeloSensor) {
        String sql = "select nsensores from modelosensor where idmodelosensor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idModeloSensor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("nsensores");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloSensorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
