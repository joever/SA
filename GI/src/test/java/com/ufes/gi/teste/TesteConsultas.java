/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.ConsultasDAO;

/**
 *
 * @author joever
 */
public class TesteConsultas {
    public static void main (String[] args) {
        ConsultasDAO myDAO = new ConsultasDAO();
        Boolean pode;
        pode = myDAO.podeAddTM(1, "08603179786");
        if (pode)
            System.out.println("Liberado");
        else
            System.out.println("Não tem mais espaço para sensores nesse Nó");
    }
}
