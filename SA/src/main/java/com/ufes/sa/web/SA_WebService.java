/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.web;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 *
 * @author joever
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface SA_WebService {
    @WebMethod Boolean enviaXMLCarga(String myXML);
    @WebMethod Boolean enviaListaXMLCarga(String myXML);
    @WebMethod String enviaXMLProcessar(String myXML);
    @WebMethod String getXML(Integer idRede, Integer idTM, String idUsuario, String dthrInicial, String dthrFinal, Boolean Op_QoC);
    @WebMethod String getXMLAccuracyMin(Integer idRede, Integer idTM, String idUsuario, String dthrInicial, String dthrFinal, Double AccuracyMin);
}
