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
public class TipoDeAvaliacaoDAO {
    private final Connection connection;
    
    public TipoDeAvaliacaoDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public Boolean adiciona(TipoDeAvaliacao myTipoAvaliacao){
        if (buscaCodigo(myTipoAvaliacao.getCodigo()) == 0){
            String sql = "insert into tipodeavaliacao" +
                        " (codigo,tipo,descricao)" +
                        " values (?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, myTipoAvaliacao.getCodigo());
                stmt.setString(2, myTipoAvaliacao.getTipo());
                stmt.setString(3, myTipoAvaliacao.getDescricao());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Integer buscaCodigo(Integer myCodigo){
        String sql = "select codigo from tipodeavaliacao where codigo = "+myCodigo;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Boolean atualiza(TipoDeAvaliacao myTipoAvaliacao){
        if (buscaCodigo(myTipoAvaliacao.getCodigo()) > 0){
            String sql = "update tipodeavaliacao" +
                        " set tipo = ?, descricao = ?" +
                        " where codigo = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1, myTipoAvaliacao.getTipo());
                stmt.setString(2, myTipoAvaliacao.getDescricao());
                stmt.setInt(3, myTipoAvaliacao.getCodigo());
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public Boolean remove(Integer myCodigo){
        String sql = "delete from tipodeavaliacao" +
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
    
    public List<TipoDeAvaliacao> getTipoDeAvaliacao() {
        List<TipoDeAvaliacao> myTiposDeAvaliacao = new ArrayList<>();
        String sql = "select * from tipodeavaliacao";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                TipoDeAvaliacao myTipoDeAvaliacao = new TipoDeAvaliacao();
                myTipoDeAvaliacao.setCodigo(rs.getInt("codigo"));
                myTipoDeAvaliacao.setTipo(rs.getString("tipo"));
                myTipoDeAvaliacao.setDescricao(rs.getString("descricao"));
                myTiposDeAvaliacao.add(myTipoDeAvaliacao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myTiposDeAvaliacao;
    }
}
