/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.teste;

import com.ufes.sa.dao.User;
import com.ufes.sa.dao.UserDAO;
import com.ufes.sa.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author joever
 */
public class testeCadastro {
    public static void main (String[] args) {
        try (Connection connection = new ConnectionFactory().getConnection()) {
            User myUser = new User();
            myUser.setNome("Joyce Moreira de Oliveira Hoffman");
            myUser.setApelido("Joycinha");
            myUser.setCpf("104.192.123-87");
            myUser.setEmail("joycemoliveira@hotmail.com");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            myUser.setUltimoLogin(s.format(c.getTime()));
            UserDAO db = new UserDAO();
            if (db.adiciona(myUser)) {
                System.out.println("Cadastro gravado com sucesso!");
            }
            else
                System.out.println("Erro ao gravar cadastro!");
        } catch (SQLException e) {
            System.out.println("Não foi possível estabelecer uma conexão com o banco de dados.");
            throw new RuntimeException(e);
        }
    }
}
