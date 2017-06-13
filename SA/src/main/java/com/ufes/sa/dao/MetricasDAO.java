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
public class MetricasDAO {
    private final Connection connection;
    
    public MetricasDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public Boolean adiciona(Metricas myMetricas){
        if (buscaCodigo(myMetricas.getCodigo()) == 0){
            String sql = "insert into metricas" +
                        " (codigo,tipo,descricao)" +
                        " values (?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, myMetricas.getCodigo());
                stmt.setString(2, myMetricas.getTipo());
                stmt.setString(3, myMetricas.getDescricao());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Integer buscaCodigo(Integer myCodigo){
        String sql = "select codigo from metricas where codigo = "+myCodigo;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Boolean atualiza(Metricas myMetricas){
        if (buscaCodigo(myMetricas.getCodigo()) > 0){
            String sql = "update metricas" +
                        " set tipo = ?, descricao = ?" +
                        " where codigo = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1, myMetricas.getTipo());
                stmt.setString(2, myMetricas.getDescricao());
                stmt.setInt(3, myMetricas.getCodigo());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Boolean remove(Integer myCodigo){
        String sql = "delete from metricas" +
                    " where codigo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, myCodigo);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<Metricas> getMetricas() {
        List<Metricas> myMetricas = new ArrayList<>();
        String sql = "select * from metricas";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                Metricas myMetrica = new Metricas();
                myMetrica.setCodigo(rs.getInt("codigo"));
                myMetrica.setTipo(rs.getString("tipo"));
                myMetrica.setDescricao(rs.getString("descricao"));
                myMetricas.add(myMetrica);
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myMetricas;
    }
}
