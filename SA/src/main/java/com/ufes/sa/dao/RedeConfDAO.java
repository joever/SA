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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joever
 */
public class RedeConfDAO {
    private final Connection connection;
    
    public RedeConfDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public Boolean adiciona(RedeConf myRedeConf){
        String sql = "insert into redeconf" +
                    " (idrede,idtm,idusuario,a_valormedio,a_percentualaceitavel,u_tempovida,c_inf,c_sup,s_op1,s_valor1,s_op2,s_valor2,f_frequenciageracao,qoc_pa,qoc_pu,qoc_pc,qoc_ps,qoc_pf)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, myRedeConf.getIdRede());
            stmt.setInt(2, myRedeConf.getIdTM());
            stmt.setString(3, myRedeConf.getIdUsuario());
            stmt.setString(4, myRedeConf.getaValorMedio());
            stmt.setString(5, myRedeConf.getaPercentualAceitavel());
            stmt.setString(6, myRedeConf.getuTempoVida());
            stmt.setString(7, myRedeConf.getcInf());
            stmt.setString(8, myRedeConf.getcSup());
            stmt.setString(9, myRedeConf.getsOp1());
            stmt.setString(10, myRedeConf.getsValor1());
            stmt.setString(11, myRedeConf.getsOp2());
            stmt.setString(12, myRedeConf.getsValor2());
            stmt.setString(13, myRedeConf.getfFrequenciaGeracao());
            stmt.setString(14, myRedeConf.getQocPA());
            stmt.setString(15, myRedeConf.getQocPU());
            stmt.setString(16, myRedeConf.getQocPC());
            stmt.setString(17, myRedeConf.getQocPS());
            stmt.setString(18, myRedeConf.getQocPF());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public RedeConf getRedeConf(Integer idRede, Integer idTM, String idUsuario) {
        RedeConf myRedeConf = new RedeConf();
        String sql = "select * from redeconf where idrede = ? and idtm = ? and idusuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, idRede);
            stmt.setInt(2, idTM);
            stmt.setString(3, idUsuario);
            ResultSet rs = stmt.executeQuery();
            System.out.println(stmt.toString());
            while (rs.next()) {
                myRedeConf.setIdRede(rs.getInt("idrede"));
                myRedeConf.setIdTM(rs.getInt("idtm"));
                myRedeConf.setIdUsuario(rs.getString("idusuario"));
                myRedeConf.setaValorMedio(rs.getString("a_valormedio"));
                myRedeConf.setaPercentualAceitavel(rs.getString("a_percentualaceitavel"));
                myRedeConf.setuTempoVida(rs.getString("u_tempovida"));
                myRedeConf.setcInf(rs.getString("c_inf"));
                myRedeConf.setcSup(rs.getString("c_sup"));
                myRedeConf.setsOp1(rs.getString("s_op1"));
                myRedeConf.setsValor1(rs.getString("s_valor1"));
                myRedeConf.setsOp2(rs.getString("s_op2"));
                myRedeConf.setsValor2(rs.getString("s_valor2"));
                myRedeConf.setfFrequenciaGeracao(rs.getString("f_frequenciageracao"));
                myRedeConf.setQocPA(rs.getString("qoc_pa"));
                myRedeConf.setQocPU(rs.getString("qoc_pu"));
                myRedeConf.setQocPC(rs.getString("qoc_pc"));
                myRedeConf.setQocPS(rs.getString("qoc_ps"));
                myRedeConf.setQocPF(rs.getString("qoc_pf"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myRedeConf;
    }
}
