/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.teste;

import com.ufes.sa.dao.User;
import com.ufes.sa.dao.UserDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeListContato {
    public static void main (String[] args) {
        UserDAO myUserDAO = new UserDAO();
        List<User> myUsers = myUserDAO.getUsers();
        for (User myUser : myUsers){
            System.out.println("Login: "+ myUser.getLogin());
            System.out.println("Nome: "+ myUser.getNome());
            System.out.println("Apelido: "+ myUser.getApelido());
            System.out.println("CPF: "+ myUser.getCpf());
            System.out.println("E-Mail: "+ myUser.getEmail());
            System.out.println("Último Login: "+ myUser.getUltimoLogin());
        }
    }
}
