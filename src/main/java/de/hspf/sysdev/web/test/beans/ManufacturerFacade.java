/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.sysdev.web.test.beans;

import de.hspf.sysdev.web.test.dao.Manufacturer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author thomas.schuster
 */
@Stateless
public class ManufacturerFacade extends AbstractFacade<Manufacturer> {

    @PersistenceContext(unitName = "de.hspf.sysdev_web-test_war_0.42PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ManufacturerFacade() {
        super(Manufacturer.class);
    }
    
}
