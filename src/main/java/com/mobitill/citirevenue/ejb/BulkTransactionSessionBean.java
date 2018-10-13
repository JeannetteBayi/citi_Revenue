/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import com.mobitill.citirevenue.entity.Merchant;
import com.mobitill.citirevenue.entity.Transactions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Francis
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BulkTransactionSessionBean {
    @PersistenceContext(unitName = "com.mobitill_CitiRevenue_war_1PU")
    private EntityManager em;
    @Resource
    private EJBContext context;
    @Resource
    private UserTransaction tx;
    @EJB
    private UtilitySessionBean usb;
    private static final Logger LOG = Logger.getLogger(BulkTransactionSessionBean.class.getName());
    
    public Response saveBulkTransaction(List<Transactions> list) {
        if (list == null) {
            return usb.errorResponse("missing information");
        }
        Map<String, Object> response = new HashMap<>();
        for (Transactions trans : list) {
            try {
                Boolean transExist = em.createNamedQuery("Transactions.findByReceiptnumber", Boolean.class)
                        .setParameter("receiptnumber", trans.getReceiptnumber())
                        .setParameter("pin", trans.getMerchantpin())
                        .getSingleResult();
                if (transExist) {
                    LOG.log(Level.WARNING, "duplicate record");
                    continue;
                }
                Boolean deviceExist = em.createNamedQuery("Taxregister.existByDeviceid", Boolean.class)
                        .setParameter("deviceid", trans.getDeviceserial())
                        .getSingleResult();
                if (!deviceExist) {
                    return usb.errorResponse("invalid device serial "+trans.getDeviceserial());
                }
                Boolean merchantExist = em.createNamedQuery("Merchant.findByPinVat", Boolean.class)
                        .setParameter("pin", trans.getMerchantpin())
                        .setParameter("vat", trans.getMerchantpin())
                        .getSingleResult();
                if (!merchantExist) {
                    return usb.errorResponse("invalid merchant pin "+trans.getMerchantpin());
                }
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    trans.setStamp(sdf.parse(trans.getTimestamp()));
                } catch (ParseException parseException) {
                    return usb.errorResponse("invalid timstamp format");
                }
                if (trans.getClientpin() != null) {
                    try {
                        Merchant clientMerchant = em.createNamedQuery("Merchant.findPinVat", Merchant.class)
                                .setParameter("pin", trans.getClientpin())
                                .setParameter("vat", trans.getClientpin())
                                .getSingleResult();
                        trans.setCategorytype(clientMerchant.getCategorytype());
                    } catch (NoResultException e) {
                        LOG.log(Level.WARNING, "Invalid client pin {0}", trans.getClientpin());
                    }
                }
                
                tx.begin();
                em.persist(trans);
                tx.commit();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
                try {
                    tx.setRollbackOnly();
                } catch (IllegalStateException | SystemException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage());
                }
                return usb.errorResponse("something went wrong");
            }
        }
        Map<String, String> responseOk = new HashMap<>();
        responseOk.put("status", "200");
        responseOk.put("message", "ok");

        response.put("response", responseOk);
        return Response.status(Response.Status.OK).entity(response).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
    }

}
