/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.dao.Usuario;
import com.ufes.gi.dao.UsuarioDAO;
import java.util.List;

/**
 *
 * @author joever
 */
public class testeUsuario {
    public static void main (String[] args) {
        UsuarioDAO myDao = new UsuarioDAO();
        Usuario myUsuario = myDao.carregaUsuario("joever","teste");
        if (myUsuario == null)
            System.out.println("Não foi possível identificar o usuário");
        else
            imprimeUsuario(myUsuario);
        //cadastraUsuario("08603179786","Joéver Silva Hoffman","joever","teste","joeversh@gmail.com");
        //imprimeListaUsuario();
    }
    
    public static void cadastraUsuario(String cpf, String nome, String login, String senha, String email){
        Usuario myUsuario = new Usuario();
        myUsuario.setCpf(cpf);
        myUsuario.setNome(nome);
        myUsuario.setLogin(login);
        myUsuario.setSenha(senha);
        myUsuario.setEmail(email);
        UsuarioDAO myDao = new UsuarioDAO();
        if (!myDao.loginExiste(myUsuario.getLogin())){
            if (myDao.adiciona(myUsuario))
                System.out.println("Cadastro com sucesso");
            else
                System.out.println("Erro ao cadastrar usuário");
        }
    }
    
    public static void imprimeListaUsuario(){
        UsuarioDAO myUsuarioDAO = new UsuarioDAO();
        List<Usuario> myUsuarios = myUsuarioDAO.getUsuarios();
        for (Usuario myUsuario : myUsuarios){
            System.out.println("CPF: "+ myUsuario.getCpf());
            System.out.println("Nome: "+ myUsuario.getNome());
            System.out.println("Login: "+ myUsuario.getLogin());
            System.out.println("Senha: "+ myUsuario.getSenha());
            System.out.println("E-Mail: "+ myUsuario.getEmail());
        }
    }
    
    public static void imprimeUsuario(Usuario myUsuario){
        System.out.println("CPF: "+ myUsuario.getCpf());
        System.out.println("Nome: "+ myUsuario.getNome());
        System.out.println("Login: "+ myUsuario.getLogin());
        System.out.println("Senha: "+ myUsuario.getSenha());
        System.out.println("E-Mail: "+ myUsuario.getEmail());
    }
}
