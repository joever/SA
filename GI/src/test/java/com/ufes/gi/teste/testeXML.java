/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.teste;

import com.ufes.gi.xml.SaidaRede;
import com.ufes.gi.xml.SaidaRedeXML;
import java.text.SimpleDateFormat;

/**
 *
 * @author joever
 */
public class testeXML {
    
    public static void main(String[] args) {
        String path = "C:\\tmp\\";
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(s.format(s.getCalendar().getTime()));
        SaidaRede mySR = new SaidaRede(1,"",1,"",1,"","08603179786",s.format(s.getCalendar().getTime()),"","30.5","","","","","","");
        SaidaRedeXML myXML = new SaidaRedeXML();
        myXML.GeraXML(mySR, path);
    }

}
