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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joever.hoffman
 */
public class ControleRedeDAO {
    private final Connection connection;
    
    public ControleRedeDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(ControleRede myControleRede) {
        if (myControleRede.getIdRede() != null) {
            String sql = "insert into controlerede" +
                        " (idrede,cpf,situacao,acaoparar)" +
                        " values (?,?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, myControleRede.getIdRede());
                stmt.setString(2, myControleRede.getCpf());
                stmt.setInt(3, myControleRede.getSituacao());
                stmt.setInt(4, myControleRede.getAcaoParar());
                System.out.println(stmt.toString());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(ControleRedeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Boolean atualiza(ControleRede myControleRede){
        if (myControleRede.getIdRede() != null) {
            String sql = "update controlerede" +
                        " set situacao = ?, acaoparar = ?" +
                        " where idrede = ? and cpf = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, myControleRede.getSituacao());
                stmt.setInt(2, myControleRede.getAcaoParar());
                stmt.setInt(3, myControleRede.getIdRede());
                stmt.setString(4, myControleRede.getCpf());
                System.out.println(stmt.toString());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public ControleRede getRede(Integer idRede, String CPF) {
        ControleRede myControleRede = null;
        String sql = "select * from controlerede where idrede = ? and cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setString(2, CPF);
            ResultSet rs = stmt.executeQuery();
            myControleRede = new ControleRede();
            while (rs.next()) {
                myControleRede.setIdRede(rs.getInt("idrede"));
                myControleRede.setCpf(rs.getString("cpf"));
                myControleRede.setSituacao(rs.getInt("situacao"));
                myControleRede.setAcaoParar(rs.getInt("acaoparar"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myControleRede;
    }
    
    public Integer getAcaoParar(Integer idRede, String CPF) {
        String sql = "select acaoparar from controlerede where idrede = ? and cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setString(2, CPF);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("acaoparar");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Boolean atualizaRedes(Integer situacao, Integer acaoParar){
            String sql = "update controlerede" +
                        " set situacao = ?, acaoparar = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, situacao);
                stmt.setInt(2, acaoParar);
                System.out.println(stmt.toString());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        return false;
    }
}
