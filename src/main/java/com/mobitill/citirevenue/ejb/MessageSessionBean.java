/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import com.mobitill.citirevenue.entity.Message;
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
import javax.persistence.PersistenceContext;

/**
 *
 * @author Francis
 */
@Stateless
public class MessageSessionBean {
    @PersistenceContext(unitName = "com.mobitill_CitiRevenue_war_1PU")
    private EntityManager em;
    @Resource
    private EJBContext context;
    
    @EJB
    private UtilitySessionBean usb;
    
    @EJB
    private AccesSessionBean asb;
    
    private static final Logger LOG = Logger.getLogger(MessageSessionBean.class.getSimpleName());
    
    public Object createMessage(Message msg){
        if(msg == null){
            return "missing content";
        }
        try {
            Boolean merchantExist = em.createNamedQuery("Merchant.findByPinVat", Boolean.class)
                    .setParameter("pin", msg.getPin())
                    .setParameter("vat", msg.getPin())
                    .getSingleResult();
            if (!merchantExist) {
                return "invalid merchant pin";
            }
            Calendar cal = usb.getCalendar();
            msg.setStamp(cal.getTime());
            em.persist(msg);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            msg.setTimestamp(sdf.format(msg.getStamp()));
            
            return msg;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }
    public List<Message> getMessages(){
        List<Message> resultList = em.createNamedQuery("Message.findAll", Message.class).getResultList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Message msg : resultList) {
            msg.setTimestamp(sdf.format(msg.getStamp()));
        }
        return resultList;
    }
    public List<Message> getMessages(String pin){
        List<Message> resultList = em.createNamedQuery("Message.findByPin", Message.class)
                .setParameter("pin", pin)
                .getResultList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Message msg : resultList) {
            msg.setTimestamp(sdf.format(msg.getStamp()));
        }
        return resultList;
    }
}
