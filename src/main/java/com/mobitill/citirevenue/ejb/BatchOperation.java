/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import com.mobitill.citirevenue.entity.MerchantCategory;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Francis
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BatchOperation {

    @PersistenceContext(unitName = "com.mobitill_CitiRevenue_war_1PU")
    private EntityManager em;

    @Resource
    private UserTransaction tx;
    
    public void batchUpdate(MerchantCategory mc) throws Exception {
        tx.begin();
        if (!em.createNamedQuery("MerchantCategory.findMccExist", Boolean.class).setParameter("mcc", mc.getMcc()).getSingleResult()) {
            em.persist(mc);
        }
        System.out.println(mc.toString());
        tx.commit();
    }
}
