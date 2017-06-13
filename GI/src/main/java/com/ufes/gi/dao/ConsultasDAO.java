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
 * @author joever
 */
public class ConsultasDAO {
    private final Connection connection;
    
    public ConsultasDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public Boolean podeAddTM(Integer idSensor, String cpf){
        String sql = "select count(a.idsensor) as qtTM, nsensores as nsensores from sensormonitoramento a, sensor b, modelosensor c "
                + "where a.idsensor = b.idsensor and a.cpf = b.cpf and b.idmodelosensor = c.idmodelosensor "
                + "and a.idsensor = ? and a.cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql);){
            stmt.setInt(1, idSensor);
            stmt.setString(2, cpf);
            System.out.println(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer qtTM, nSensor;
                qtTM = rs.getInt("qtTM");
                nSensor = rs.getInt("nsensores");
                System.out.println(qtTM+" .:. "+nSensor);
                if (qtTM >= nSensor)
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SensorMonitoramentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
