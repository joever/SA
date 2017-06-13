/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.teste;

import com.ufes.sa.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author joever
 */
public class testeConexao {
    public static void main (String[] args) {
        try (Connection connection = new ConnectionFactory().getConnection()) {
            System.out.println("Conexão Aberta");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Não foi possível estabelecer uma conexão com o banco de dados.");
            throw new RuntimeException(e);
        }
    }
}
