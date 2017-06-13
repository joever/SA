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
public class UserDAO {
    private final Connection connection;
    
    public UserDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(User myUser) {
        if (buscaLogin(myUser.getLogin()) == 0) {
            String sql = "insert into user" +
                        " (login,password,nome,apelido,cpf,email,ultimoLogin)" +
                        " values (?,md5(?),?,?,?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){        
                stmt.setString(1, myUser.getLogin());
                stmt.setString(2, myUser.getPassword());
                stmt.setString(3, myUser.getNome());
                stmt.setString(4, myUser.getApelido());
                stmt.setString(5, myUser.getCpf());
                stmt.setString(6, myUser.getEmail());
                stmt.setString(7, myUser.getUltimoLogin());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public List<User> getUsers() {
        List<User> myUsers = new ArrayList<>();
        String sql = "select * from user";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                User myUser = new User();
                myUser.setLogin(rs.getString("login"));
                myUser.setPassword(rs.getString("password"));
                myUser.setNome(rs.getString("nome"));
                myUser.setApelido(rs.getString("apelido"));
                myUser.setCpf(rs.getString("cpf"));
                myUser.setEmail(rs.getString("email"));
                myUser.setUltimoLogin(rs.getString("ultimoLogin"));
                myUsers.add(myUser);
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myUsers;
    }
    
    public Integer buscaLogin(String login){
        String sql = "select login from user where login = '"+login+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Boolean atualiza(User myUser){
        if (buscaLogin(myUser.getLogin()) == 0) {
            String sql = "update user" +
                        " set login = ?, password = ?, nome = ?, apelido = ?, cpf = ?, email = ?" +
                        " where login = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1, myUser.getLogin());
                stmt.setString(2, myUser.getPassword());
                stmt.setString(3, myUser.getNome());
                stmt.setString(4, myUser.getApelido());
                stmt.setString(5, myUser.getCpf());
                stmt.setString(6, myUser.getEmail());
                stmt.setString(7, myUser.getLogin());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Boolean remove(String login){
        String sql = "delete from user" +
                    " where login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, login);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
