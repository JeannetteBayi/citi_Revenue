/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import com.mobitill.citirevenue.entity.MerchantCategory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Francis
 */
@Stateless
public class MerchantCategorySessionBean {

    @PersistenceContext(unitName = "com.mobitill_CitiRevenue_war_1PU")
    private EntityManager em;
    @Resource
    private EJBContext context;

    @EJB
    private BatchOperation bo;

    private static final Logger LOG = Logger.getLogger(MerchantCategorySessionBean.class.getName());

    public MerchantCategory createMerchantCategory(MerchantCategory mc) {
        try {
            MerchantCategory merge = em.merge(mc);
            return merge;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            throw e;
        }
    }
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void createMerchantCategory(List<MerchantCategory> mcs) {
        for (MerchantCategory mc : mcs) {
            try {
                bo.batchUpdate(mc);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
                //context.setRollbackOnly();
                break;
            }
        }

    }
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void batchUpdate(MerchantCategory mc) throws Exception {
        if (!em.createNamedQuery("MerchantCategory.findMccExist", Boolean.class).setParameter("mcc", mc.getMcc()).getSingleResult()) {
            em.persist(mc);
            em.flush();
            em.clear();
        }
        System.out.println(mc.toString());
    }

    public List<MerchantCategory> merchantList() {
        return em.createNamedQuery("MerchantCategory.findAll", MerchantCategory.class).getResultList();
    }
}
