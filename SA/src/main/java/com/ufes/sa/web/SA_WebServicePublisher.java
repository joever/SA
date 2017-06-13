/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.web;

import javax.xml.ws.Endpoint;

/**
 *
 * @author joever
 */
public class SA_WebServicePublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9876/com.ufes.sa.web", new SA_WebServiceImpl());
    }
}
