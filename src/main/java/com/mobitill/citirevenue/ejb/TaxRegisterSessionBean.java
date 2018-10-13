/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import com.mobitill.citirevenue.entity.Merchant;
import com.mobitill.citirevenue.entity.Taxregister;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Francis
 */
@Stateless
public class TaxRegisterSessionBean {

    @PersistenceContext(unitName = "com.mobitill_CitiRevenue_war_1PU")
    private EntityManager em;
    @Resource
    private EJBContext context;
    private static final Logger LOG = Logger.getLogger(TaxRegisterSessionBean.class.getName());
    
    @EJB
    private UtilitySessionBean usb;
    
    public Object createTaxRegister(Taxregister tr){
        if (tr == null) {
            return "missing information";
        }
        try {
            if(!tr.getDevicetype().equalsIgnoreCase("etr") && !tr.getDevicetype().equalsIgnoreCase("esd")){
                return "devicetype allowed options ETR or ESD";
            }
            tr.setDevicetype(tr.getDevicetype().toUpperCase());
            
            Merchant merchant;
            if (usb.trimedString(tr.getMerchantpin())) {
                try {
                    merchant = em.createNamedQuery("Merchant.findByPin", Merchant.class)
                            .setParameter("pin", tr.getMerchantpin())
                            .getSingleResult();
                } catch (NoResultException e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                    return "Merchant PIN provided not found";
                }
            } else {
                return "missing Merchant PIN information";                
            }
            String deviceid = String.format("K%s", RandomStringUtils.randomNumeric(10));
            while (em.createNamedQuery("Taxregister.existByDeviceid", Boolean.class)
                    .setParameter("deviceid", deviceid)
                    .getSingleResult()) {
                LOG.log(Level.WARNING, "Auto generate found duplicate deviceid");
                deviceid = String.format("K%s", RandomStringUtils.randomNumeric(10));
            }
            tr.setDeviceid(deviceid);
            
            Calendar cal = usb.getCalendar();
            tr.setIssuedate(cal.getTime());
            tr.setStatusStamp(cal.getTime());
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tr.setDateissued(sdf.format(tr.getIssuedate()));
            
            Taxregister merge = em.merge(tr);
            merge.setDateissued(sdf.format(tr.getIssuedate()));
            
            return merge;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }
    public Object editTaxRegister(Taxregister tr){
        if (tr == null) {
            return "missing information";
        }
        try {
            Merchant merchant;
            if (usb.trimedString(tr.getMerchantpin())) {
                try {
                    merchant = em.createNamedQuery("Merchant.findByPin", Merchant.class)
                            .setParameter("pin", tr.getMerchantpin())
                            .getSingleResult();
                } catch (NoResultException e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                    return "Merchant PIN provided not found";
                }
            } else {
                return "missing Merchant PIN information";                
            }
            Taxregister editTaxregister;
            if (usb.trimedString(tr.getDeviceid())) {
                try {
                    editTaxregister = em.createNamedQuery("Taxregister.findByDeviceid", Taxregister.class)
                            .setParameter("deviceid", tr.getDeviceid())
                            .getSingleResult();
                } catch (NoResultException e) {
                    LOG.log(Level.WARNING, "invalid deviceid");
                    return "Tax register not found";
                }
            }else{
                return "missing device id information";
            }
            editTaxregister.setMerchantpin(merchant.getPin());
            
            Calendar cal = usb.getCalendar();
            editTaxregister.setStatusStamp(cal.getTime());
            editTaxregister.setStatusMessage("update performed");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            editTaxregister.setDateissued(sdf.format(editTaxregister.getIssuedate()));
            
            return editTaxregister;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }
    public List<Taxregister> getTaxregisters(){
        List<Taxregister> resultList = em.createNamedQuery("Taxregister.findAll", Taxregister.class).getResultList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Taxregister taxregister : resultList) {
            taxregister.setDateissued(sdf.format(taxregister.getIssuedate()));
        }
        return resultList;
     }
    public Object getTaxRegister(String deviceid){
        if (usb.trimedString(deviceid)) {
            try {
                Taxregister tr = em.createNamedQuery("Taxregister.findByDeviceid", Taxregister.class)
                        .setParameter("deviceid", deviceid)
                        .getSingleResult();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                tr.setDateissued(sdf.format(tr.getIssuedate()));
                return tr;
            } catch (NoResultException e) {
                LOG.log(Level.WARNING, "invalid deviceid");
                return "Tax register not found";
            }
        } else {
            return "missing device id information";
        }
    }
    
}
