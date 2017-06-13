/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.web;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joever
 */
public class SA_WebServiceTest {
    
    public SA_WebServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of enviaXMLCarga method, of class SA_WebService.
     */
    @org.junit.Test
    public void testEnviaXMLCarga() {
        System.out.println("enviaXMLCarga");
        String myXML = "";
        SA_WebServiceImpl instance = new SA_WebServiceImpl();
        Boolean expResult = null;
        Boolean result = instance.enviaXMLCarga(myXML);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of enviaXMLCargaComMetricas method, of class SA_WebService.
     */
    @org.junit.Test
    public void testEnviaXMLCargaComMetricas() {
        System.out.println("enviaXMLCargaComMetricas");
        String myXML = "";
        SA_WebServiceImpl instance = new SA_WebServiceImpl();
        Boolean expResult = null;
        Boolean result = instance.enviaListaXMLCarga(myXML);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of enviaXMLProcessar method, of class SA_WebService.
     */
    @org.junit.Test
    public void testEnviaXMLProcessar() {
        System.out.println("enviaXMLProcessar");
        String myXML = "";
        SA_WebServiceImpl instance = new SA_WebServiceImpl();
        Boolean expResult = null;
        String result = instance.enviaXMLProcessar(myXML);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
