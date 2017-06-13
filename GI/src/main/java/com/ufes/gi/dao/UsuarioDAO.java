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
public class UsuarioDAO {
    private final Connection connection;
    
    public UsuarioDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(Usuario myUsuario) {
        if (myUsuario.getCpf() != null) {
            if (!cpfExiste(myUsuario.getCpf())) {
                String sql = "insert into usuario" +
                            " (cpf,nome,login,senha,email)" +
                            " values (?,?,?,md5(?),?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, myUsuario.getCpf());
                    stmt.setString(2, myUsuario.getNome());
                    stmt.setString(3, myUsuario.getLogin());
                    stmt.setString(4, myUsuario.getSenha());
                    stmt.setString(5, myUsuario.getEmail());
                    //System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean cpfExiste(String cpf){
        String sql = "select cpf from usuario where cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean loginExiste(String login){
        String sql = "select login from usuario where login = '"+login+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(Usuario myUsuario){
        if (myUsuario.getCpf() != null) {
            if (!myUsuario.getCpf().isEmpty()) {
                String sql = "update usuario" +
                            " set nome = ?, login = ?, senha = md5(?), email = ?" +
                            " where cpf = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, myUsuario.getNome());
                    stmt.setString(2, myUsuario.getLogin());
                    stmt.setString(3, myUsuario.getSenha());
                    stmt.setString(4, myUsuario.getEmail());
                    stmt.setString(5, myUsuario.getCpf());
                    //System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(String cpf){
        if (cpf != null) {
            String sql = "delete from usuario" +
                        " where cpf = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1, cpf);
                System.out.println(stmt.toString());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public List<Usuario> getUsuarios() {
        List<Usuario> myUsuarios = new ArrayList<>();
        String sql = "select * from usuario";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                Usuario myUsuario = new Usuario();
                myUsuario.setCpf(rs.getString("cpf"));
                myUsuario.setNome(rs.getString("nome"));
                myUsuario.setLogin(rs.getString("login"));
                myUsuario.setSenha(rs.getString("senha"));
                myUsuario.setEmail(rs.getString("email"));
                myUsuarios.add(myUsuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myUsuarios;
    }
    
    public Usuario carregaUsuario(String login, String senha){
        Usuario myUsuario = null;
        String sql = "select * from usuario where login = '"+login+"' and senha = md5('"+senha+"')";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                myUsuario = new Usuario();
                myUsuario.setCpf(rs.getString("cpf"));
                myUsuario.setNome(rs.getString("nome"));
                myUsuario.setLogin(rs.getString("login"));
                myUsuario.setSenha(rs.getString("senha"));
                myUsuario.setEmail(rs.getString("email"));
                return myUsuario;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myUsuario;
    }
    
    public Usuario buscaUsuario(String cpf){
        Usuario myUsuario = null;
        String sql = "select * from usuario where cpf = '"+cpf+"'";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                myUsuario = new Usuario();
                myUsuario.setCpf(rs.getString("cpf"));
                myUsuario.setNome(rs.getString("nome"));
                myUsuario.setLogin(rs.getString("login"));
                myUsuario.setSenha(rs.getString("senha"));
                myUsuario.setEmail(rs.getString("email"));
                return myUsuario;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myUsuario;
    }
}
