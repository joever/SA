/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.web;

import static org.testng.Assert.*;

/**
 *
 * @author joever
 */
public class SA_WebServiceNGTest {
    
    public SA_WebServiceNGTest() {
    }

    @org.testng.annotations.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.testng.annotations.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.testng.annotations.BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @org.testng.annotations.AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Teste de método enviaXMLCarga, da classe SA_WebService.
     */
    @org.testng.annotations.Test
    public void testEnviaXMLCarga() {
        System.out.println("enviaXMLCarga");
        String myXML = "";
        SA_WebServiceImpl instance = new SA_WebServiceImpl();
        Boolean expResult = null;
        Boolean result = instance.enviaXMLCarga(myXML);
        assertEquals(result, expResult);
        // TODO verifica o código de teste gerado e remove a chamada default para falha.
        //fail("O caso de teste \u00e9 um prot\u00f3tipo.");
    }

    /**
     * Teste de método enviaXMLCargaComMetricas, da classe SA_WebService.
     */
    @org.testng.annotations.Test
    public void testEnviaXMLCargaComMetricas() {
        System.out.println("enviaXMLCargaComMetricas");
        String myXML = "";
        SA_WebServiceImpl instance = new SA_WebServiceImpl();
        Boolean expResult = null;
        Boolean result = instance.enviaListaXMLCarga(myXML);
        assertEquals(result, expResult);
        // TODO verifica o código de teste gerado e remove a chamada default para falha.
        //fail("O caso de teste \u00e9 um prot\u00f3tipo.");
    }

    /**
     * Teste de método enviaXMLProcessar, da classe SA_WebService.
     */
    @org.testng.annotations.Test
    public void testEnviaXMLProcessar() {
        System.out.println("enviaXMLProcessar");
        String myXML = "";
        SA_WebServiceImpl instance = new SA_WebServiceImpl();
        Boolean expResult = null;
        String result = instance.enviaXMLProcessar(myXML);
        assertEquals(result, expResult);
        // TODO verifica o código de teste gerado e remove a chamada default para falha.
        //fail("O caso de teste \u00e9 um prot\u00f3tipo.");
    }
    
}
