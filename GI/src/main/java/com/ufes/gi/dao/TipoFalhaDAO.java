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
public class TipoFalhaDAO {
    private final Connection connection;
    
    public TipoFalhaDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(TipoFalha myTipofalha) {
        if (myTipofalha.getIdTipoFalha() != null) {
            if (!idExiste(myTipofalha.getIdTipoFalha())) {
                String sql = "insert into tipofalha" +
                            " (idtipofalha,nome,descricao,valor1,valor2,valor3)" +
                            " values (?,?,?,?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myTipofalha.getIdTipoFalha());
                    stmt.setString(2, myTipofalha.getNome());
                    stmt.setString(3, myTipofalha.getDescricao());
                    stmt.setInt(4, myTipofalha.getValor1());
                    stmt.setInt(5, myTipofalha.getValor2());
                    stmt.setInt(6, myTipofalha.getValor3());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean idExiste(Integer myId){
        String sql = "select idtipofalha from tipofalha where idtipofalha = "+myId;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(TipoFalha myTipoFalha){
        if (myTipoFalha.getIdTipoFalha() != null) {
            if (myTipoFalha.getIdTipoFalha() > 0) {
                String sql = "update tipofalha" +
                            " set nome = ?, descricao = ?, valor1 = ?, valor2 = ?, valor3 = ?" +
                            " where idtipofalha = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, myTipoFalha.getNome());
                    stmt.setString(2, myTipoFalha.getDescricao());
                    stmt.setInt(3, myTipoFalha.getValor1());
                    stmt.setInt(4, myTipoFalha.getValor2());
                    stmt.setInt(5, myTipoFalha.getValor3());
                    stmt.setInt(6, myTipoFalha.getIdTipoFalha());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(Integer myId){
        if (myId != null) {
            if (myId > 0) {
                String sql = "delete from tipofalha" +
                            " where idtipofalha = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myId);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<TipoFalha> getTiposFalhas() {
        List<TipoFalha> getTiposFalhas = new ArrayList<>();
        String sql = "select * from tipofalha";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                TipoFalha myTipoFalha = new TipoFalha();
                myTipoFalha.setIdTipoFalha(rs.getInt("idtipofalha"));
                myTipoFalha.setNome(rs.getString("nome"));
                myTipoFalha.setDescricao(rs.getString("descricao"));
                myTipoFalha.setValor1(rs.getInt("valor1"));
                myTipoFalha.setValor2(rs.getInt("valor2"));
                myTipoFalha.setValor3(rs.getInt("valor3"));
                getTiposFalhas.add(myTipoFalha);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getTiposFalhas;
    }
    
    public Integer getProxId() {
        Integer id = 0;
        String sql = "select max(idtipofalha) as idtipofalha from tipofalha";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                id = rs.getInt(1) + 1;
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        id++;
        return id;
    }

    public String getNome(Integer idTF) {
        String sql = "select nome from tipofalha where idtipofalha = "+idTF;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoFalhaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
