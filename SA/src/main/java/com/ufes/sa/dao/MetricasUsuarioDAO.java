/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.dao;

import com.ufes.sa.jdbc.ConnectionFactory;
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
public class MetricasUsuarioDAO {
    private final Connection connection;
    
    public MetricasUsuarioDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public Boolean adiciona(MetricasUsuario myMetricasUsuario){
        if (buscaCodigo(myMetricasUsuario.getIdUser(),myMetricasUsuario.getCodigoMetrica()) == 0){
            String sql = "insert into metricasusuario" +
                        " (idUser,codigoMetrica,codigoTipoAvaliacao,valida1,valida2,formula)" +
                        " values (?,?,?,?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, myMetricasUsuario.getIdUser());
                stmt.setInt(2, myMetricasUsuario.getCodigoMetrica());
                stmt.setInt(3, myMetricasUsuario.getCodigoTipoAvaliacao());
                stmt.setString(4, myMetricasUsuario.getValida1());
                stmt.setString(5, myMetricasUsuario.getValida2());
                stmt.setString(6, myMetricasUsuario.getFormula());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Integer buscaCodigo(Integer myIdUser, Integer myCodigoMetrica){
        String sql = "select idUser from metricasusuario where idUser = "+myIdUser+" and codigoMetrica = "+myCodigoMetrica;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getInt("idUser");
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Boolean atualiza(MetricasUsuario myMetricasUsuario){
        if (buscaCodigo(myMetricasUsuario.getIdUser(),myMetricasUsuario.getCodigoMetrica()) > 0){
            String sql = "update metricasusuario" +
                        " set codigoTipoAvaliacao = ?, valida1 = ?, valida2 = ?, formula = ?" +
                        " where idUser = ? and codigoMetrica = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, myMetricasUsuario.getCodigoTipoAvaliacao());
                stmt.setString(2, myMetricasUsuario.getValida1());
                stmt.setString(3, myMetricasUsuario.getValida2());
                stmt.setString(4, myMetricasUsuario.getFormula());
                stmt.setInt(5, myMetricasUsuario.getIdUser());
                stmt.setInt(6, myMetricasUsuario.getCodigoMetrica());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Boolean remove(Integer myIdUser, Integer myCodigoMetrica){
        String sql = "delete from metricasusuario" +
                    " where idUser = ? and codigoMetrica = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, myIdUser);
            stmt.setInt(2, myCodigoMetrica);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<MetricasUsuario> getMetricasUsuario() {
        List<MetricasUsuario> myMetricasUsuario = new ArrayList<>();
        String sql = "select * from metricas";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                MetricasUsuario myMetricaUsuario = new MetricasUsuario();
                myMetricaUsuario.setIdUser(rs.getInt("idUser"));
                myMetricaUsuario.setCodigoMetrica(rs.getInt("codigoMetrica"));
                myMetricaUsuario.setCodigoTipoAvaliacao(rs.getInt("codigoTipoAvaliacao"));
                myMetricaUsuario.setValida1(rs.getString("valida1"));
                myMetricaUsuario.setValida2(rs.getString("valida2"));
                myMetricaUsuario.setFormula(rs.getString("formula"));
                myMetricasUsuario.add(myMetricaUsuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myMetricasUsuario;
    }
}
