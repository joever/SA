/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.app1;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import com.ufes.app1.dao.SaidaRedeDAO;
import com.ufes.app1.xml.SaidaRede;
import java.util.List;

/**
 *
 * @author joever
 */
public class EnviaXML {
    public static void main(String[] args) {
        SAWebService_Service myWS = new SAWebService_Service();
        SAWebService SA = myWS.getSAWebServiceImplPort();
        SaidaRedeDAO myDAO = new SaidaRedeDAO();
        List<SaidaRede> listXML = myDAO.getSaidasRede(2, 1, "08603179786");
        XStream stream = new XStream(new Xpp3Driver());
        String xml;
        stream.autodetectAnnotations(true);
        xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n".concat(stream.toXML(listXML));
        System.out.println("XML Carregado");
        System.out.println(xml);
        String newXML = SA.enviaXMLProcessar(xml);
        System.out.println("XML Processado");
        System.out.println(newXML);
    }
}
