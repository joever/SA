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
public class RedeDAO {
    private final Connection connection;
    
    public RedeDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(Rede myRede) {
        if (myRede.getIdRede() != null) {
            if (!idExiste(myRede.getIdRede(),myRede.getCpf()) && cpfExite(myRede.getCpf())) {
                String sql = "insert into rede" +
                            " (idrede,cpf,nome,descricao,localizacao,destino,caminhoXML)" +
                            " values (?,?,?,?,?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myRede.getIdRede());
                    stmt.setString(2, myRede.getCpf());
                    stmt.setString(3, myRede.getNome());
                    stmt.setString(4, myRede.getDescricao());
                    stmt.setString(5, myRede.getLocalizacao());
                    stmt.setString(6, myRede.getDestino());
                    stmt.setString(7, myRede.getCaminhoXML());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean cpfExite(String cpf){
        String sql = "select cpf from usuario where cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean idExiste(Integer myId, String cpf){
        String sql = "select idrede from rede where idrede = "+myId+" and cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(Rede myRede){
        if (myRede.getIdRede() != null) {
            if (myRede.getIdRede() > 0 && idExiste(myRede.getIdRede(),myRede.getCpf())) {
                String sql = "update rede" +
                            " set nome = ?, descricao = ?, localizacao = ?, destino = ?, caminhoXML = ?" +
                            " where idrede = ? and cpf = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, myRede.getNome());
                    stmt.setString(2, myRede.getDescricao());
                    stmt.setString(3, myRede.getLocalizacao());
                    stmt.setString(4, myRede.getDestino());
                    stmt.setString(5, myRede.getCaminhoXML());
                    stmt.setInt(6, myRede.getIdRede());
                    stmt.setString(7, myRede.getCpf());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(Integer myId, String cpf){
        if (myId != null) {
            if (myId > 0) {
                String sql = "delete from rede" +
                            " where idrede = ? and cpf = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myId);
                    stmt.setString(2, cpf);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<Rede> getRedes() {
        List<Rede> myRedes = new ArrayList<>();
        String sql = "select * from rede";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                Rede myRede = new Rede();
                myRede.setIdRede(rs.getInt("idrede"));
                myRede.setCpf(rs.getString("cpf"));
                myRede.setNome(rs.getString("nome"));
                myRede.setDescricao(rs.getString("descricao"));
                myRede.setLocalizacao(rs.getString("localizacao"));
                myRede.setDestino(rs.getString("destino"));
                myRede.setCaminhoXML(rs.getString("caminhoXML"));
                myRedes.add(myRede);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myRedes;
    }
    
    public Rede getRede(Integer idRede, String CPF) {
        Rede myRede = new Rede();
        String sql = "select * from rede where idrede = ? and cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setString(2, CPF);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                myRede.setIdRede(rs.getInt("idrede"));
                myRede.setCpf(rs.getString("cpf"));
                myRede.setNome(rs.getString("nome"));
                myRede.setDescricao(rs.getString("descricao"));
                myRede.setLocalizacao(rs.getString("localizacao"));
                myRede.setDestino(rs.getString("destino"));
                myRede.setCaminhoXML(rs.getString("caminhoXML"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myRede;
    }
    
    public Integer getProxId() {
        Integer id = 0;
        String sql = "select max(idrede) as idrede from rede";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                id = rs.getInt("idrede") + 1;
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        id++;
        return id;
    }
    
    public String getNomeRede(Integer idRede, String CPF) {
        String nome = null;
        String sql = "select nome from rede where idrede = ? and cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setString(2, CPF);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                nome = rs.getString("nome");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nome;
    }
}
