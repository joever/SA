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
public class TipoMonitoramentoDAO {
    private final Connection connection;
    
    public TipoMonitoramentoDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public boolean adiciona(TipoMonitoramento myTipoMonitoramento) {
        if (myTipoMonitoramento.getCodigo() != null) {
            if (!codigoExiste(myTipoMonitoramento.getCodigo())) {
                String sql = "insert into tipomonitoramento" +
                            " (codigo,nome,descricao)" +
                            " values (?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myTipoMonitoramento.getCodigo());
                    stmt.setString(2, myTipoMonitoramento.getNome());
                    stmt.setString(3, myTipoMonitoramento.getDescricao());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean codigoExiste(Integer myCodigo){
        String sql = "select codigo from tipomonitoramento where codigo = "+myCodigo;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean atualiza(TipoMonitoramento myTipoMonitoramento){
        if (myTipoMonitoramento.getCodigo() != null) {
            if (myTipoMonitoramento.getCodigo() > 0) {
                String sql = "update tipomonitoramento" +
                            " set nome = ?, descricao = ?" +
                            " where codigo = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setString(1, myTipoMonitoramento.getNome());
                    stmt.setString(2, myTipoMonitoramento.getDescricao());
                    stmt.setInt(3, myTipoMonitoramento.getCodigo());
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public Boolean deleta(Integer myCodigo){
        if (myCodigo != null) {
            if (myCodigo > 0) {
                String sql = "delete from tipomonitoramento" +
                            " where codigo = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, myCodigo);
                    System.out.println(stmt.toString());
                    stmt.execute();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(TipoMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public List<TipoMonitoramento> getTiposMonitoramento() {
        List<TipoMonitoramento> myTiposMonitoramento = new ArrayList<>();
        String sql = "select * from tipomonitoramento";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                TipoMonitoramento myTipoMonitoramento = new TipoMonitoramento();
                myTipoMonitoramento.setCodigo(rs.getInt("codigo"));
                myTipoMonitoramento.setNome(rs.getString("nome"));
                myTipoMonitoramento.setDescricao(rs.getString("descricao"));
                myTiposMonitoramento.add(myTipoMonitoramento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myTiposMonitoramento;
    }
    
    public Integer getProxId() {
        Integer id = 0;
        String sql = "select max(codigo) as codigo from tipomonitoramento";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                id = rs.getInt("codigo") + 1;
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        id++;
        return id;
    }
    
    public String getNome(Integer myCodigo) {
        String sql = "select nome from tipomonitoramento where codigo = "+myCodigo;
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
