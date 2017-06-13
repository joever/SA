package com.ufes.sp;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import com.ufes.sp.xml.SaidaRede;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joever
 */
public class BuscaXML {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        final String dir = "C:\\saida\\";
        final String dirProc = dir+"proc\\";
        SimpleDateFormat formatDtHr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SAWebService_Service myWS = new SAWebService_Service();
        SAWebService SA = myWS.getSAWebServiceImplPort();
        while (true){
            File arquivos[];
            File arqDiretorio = new File(dir);
            arquivos = arqDiretorio.listFiles();
            for (File arquivo : arquivos) {
                if (arquivo.isFile()) {
                    System.out.println(arquivo.getName());
                    try (FileReader reader = new FileReader(arquivo); BufferedReader in = new BufferedReader(reader)) {
                        SaidaRede listXML;
                        listXML = getXML(in);
                        Calendar dthrEnvio = Calendar.getInstance();
                        listXML.setDthrEnvio(formatDtHr.format(dthrEnvio.getTime()));
                        XStream stream = new XStream(new Xpp3Driver());
                        String xml;
                        stream.autodetectAnnotations(true);
                        xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n".concat(stream.toXML(listXML));
                        SA.enviaXMLCarga(xml);
                    }
                    Path source = Paths.get(dir + arquivo.getName());
                    Path destination = Paths.get(dirProc + arquivo.getName());
                    Files.copy(source, destination);
                    Files.delete(source);
                }
            }
            try { Thread.sleep (1000); } catch (InterruptedException ex) {}
        }
    }
    
    public static SaidaRede getXML(Reader fonte){
        XStream stream = new XStream(new Xpp3Driver());
        stream.autodetectAnnotations(true);
        stream.alias("InformaçõesDaColeta", SaidaRede.class);
        return (SaidaRede) stream.fromXML(fonte);
    }
}
