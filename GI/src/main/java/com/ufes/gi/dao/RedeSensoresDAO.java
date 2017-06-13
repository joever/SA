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
public class RedeSensoresDAO {
    private final Connection connection;
    
    public RedeSensoresDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(RedeSensores myRedeSensores) {
        if (myRedeSensores.getIdRedeSensor() != null) {
            String sql = "insert into redesensores" +
                        " (idredesensor,idrede,cpf,idsensor,unidades,bateriainicial,tempoativo,gastobateria,tempocoleta)" +
                        " values (?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, myRedeSensores.getIdRedeSensor());
                stmt.setInt(2, myRedeSensores.getIdRede());
                stmt.setString(3, myRedeSensores.getCpf());
                stmt.setInt(4, myRedeSensores.getIdSensor());
                stmt.setInt(5, myRedeSensores.getUnidades());
                stmt.setInt(6, myRedeSensores.getBateriaInicial());
                stmt.setInt(7, myRedeSensores.getTempoAtivo());
                stmt.setDouble(8, myRedeSensores.getGastoBateria());
                stmt.setInt(9, myRedeSensores.getTempoColeta());
                System.out.println(stmt.toString());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Boolean idExiste(Integer myId, Integer myIdRede, String cpf){
        String sql = "select idredesensor from redesensores where idredesensor = "+myId+" and idrede = "+myIdRede+" and cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(RedeSensores myRedeSensores){
        if (myRedeSensores.getIdRedeSensor() != null) {
            if (myRedeSensores.getIdRedeSensor() > 0 && idExiste(myRedeSensores.getIdRedeSensor(),myRedeSensores.getIdRede(),myRedeSensores.getCpf())) {
                String sql = "update redesensores" +
                            " set unidades = ?, bateriainicial = ?, tempoativo = ?, gastobateria = ?, tempocoleta = ?" +
                            " where idRedeSensor = ? and idrede = ? and cpf = ? and idSensor = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myRedeSensores.getUnidades());
                    stmt.setInt(2, myRedeSensores.getBateriaInicial());
                    stmt.setInt(3, myRedeSensores.getTempoAtivo());
                    stmt.setDouble(4, myRedeSensores.getGastoBateria());
                    stmt.setInt(5, myRedeSensores.getTempoColeta());
                    stmt.setInt(6, myRedeSensores.getIdRedeSensor());
                    stmt.setInt(7, myRedeSensores.getIdRede());
                    stmt.setString(8, myRedeSensores.getCpf());
                    stmt.setInt(9, myRedeSensores.getIdSensor());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean zeraTempoAtivo(Integer idRede, String cpf){
        String sql = "update redesensores" +
                    " set tempoativo = 0" +
                    " where idrede = ? and cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setString(2, cpf);
            System.out.println(stmt.toString());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean deleta(Integer myId, Integer idRede, String cpf, Integer idSensor){
        if (myId != null) {
            if (myId > 0) {
                String sql = "delete from redesensores" +
                            " where idredesensor = ? and idrede = ? and cpf = ? and idSensor = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myId);
                    stmt.setInt(2, idRede);
                    stmt.setString(3, cpf);
                    stmt.setInt(4, idSensor);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<RedeSensores> getRedesSensores() {
        List<RedeSensores> myRedesSensores = new ArrayList<>();
        String sql = "select * from redesensores order by idrede asc";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                RedeSensores myRedeSensores = new RedeSensores();
                myRedeSensores.setIdRedeSensor(rs.getInt("idredesensor"));
                myRedeSensores.setIdRede(rs.getInt("idrede"));
                myRedeSensores.setCpf(rs.getString("cpf"));
                myRedeSensores.setIdSensor(rs.getInt("idsensor"));
                myRedeSensores.setUnidades(rs.getInt("unidades"));
                myRedeSensores.setBateriaInicial(rs.getInt("bateriainicial"));
                myRedeSensores.setTempoAtivo(rs.getInt("tempoativo"));
                myRedeSensores.setGastoBateria(rs.getDouble("gastobateria"));
                myRedeSensores.setTempoColeta(rs.getInt("tempocoleta"));
                myRedesSensores.add(myRedeSensores);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myRedesSensores;
    }
    
    public List<RedeSensores> getRedesSensores(Integer idRede, String cpf) {
        List<RedeSensores> myRedesSensores = new ArrayList<>();
        String sql = "select * from redesensores where idrede = ? and cpf = ? order by idrede asc";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setString(2, cpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RedeSensores myRedeSensores = new RedeSensores();
                myRedeSensores.setIdRedeSensor(rs.getInt("idredesensor"));
                myRedeSensores.setIdRede(rs.getInt("idrede"));
                myRedeSensores.setCpf(rs.getString("cpf"));
                myRedeSensores.setIdSensor(rs.getInt("idsensor"));
                myRedeSensores.setUnidades(rs.getInt("unidades"));
                myRedeSensores.setBateriaInicial(rs.getInt("bateriainicial"));
                myRedeSensores.setTempoAtivo(rs.getInt("tempoativo"));
                myRedeSensores.setGastoBateria(rs.getDouble("gastobateria"));
                myRedeSensores.setTempoColeta(rs.getInt("tempocoleta"));
                myRedesSensores.add(myRedeSensores);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myRedesSensores;
    }
    
    public Integer getProxId() {
        Integer id = 0;
        String sql = "select max(idredesensor) as idredesensor from redesensores";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                id = rs.getInt("idredesensor") + 1;
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeSensoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        id++;
        return id;
    }
}
