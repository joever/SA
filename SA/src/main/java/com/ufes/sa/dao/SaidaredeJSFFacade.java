/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.sa.dao;

import com.ufes.sa.ui.SaidaredeJSF;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author joever.hoffman
 */
@Stateless
public class SaidaredeJSFFacade extends AbstractFacade<SaidaredeJSF> {

    @PersistenceContext(unitName = "com.ufes_SA_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SaidaredeJSFFacade() {
        super(SaidaredeJSF.class);
    }
    
}
