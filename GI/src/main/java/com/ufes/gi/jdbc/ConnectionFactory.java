/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author joever
 */
public class ConnectionFactory {
    public Connection getConnection() {
        final String url = "jdbc:mysql://localhost:3306/mygi001?autoReconnect=true&useSSL=false";
        try {
            return DriverManager.getConnection(url,"contexto","contexto");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
