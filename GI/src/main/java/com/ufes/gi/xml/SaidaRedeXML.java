/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joever
 */
public class SaidaRedeXML {
    public void GeraXML(SaidaRede mySR, String path){
        Calendar envio = Calendar.getInstance();

        XStream stream = new XStream(new Xpp3Driver());
        stream.autodetectAnnotations(true);
        
        String nomeArquivo = path+"GI;"+mySR.getIdRede()+";"+mySR.getIdSensor()+";"+envio.getTimeInMillis()+".xml";
        
        salvarArquivo(stream.toXML(mySR), nomeArquivo);
    }
    
    private void salvarArquivo(String documento, String file) {
        File path = new File(file);
        try {
            PrintWriter writer = new PrintWriter(path);
            writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>");           
            writer.println(documento);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaidaRedeXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
