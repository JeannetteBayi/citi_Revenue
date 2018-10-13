/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import com.mobitill.citirevenue.entity.Merchant;
import com.mobitill.citirevenue.entity.Taxregister;
import com.mobitill.citirevenue.entity.Transactions;
import com.mobitill.citirevenue.service.MerchantDescription;
import com.mobitill.citirevenue.service.MerchantSummary;
import com.mobitill.citirevenue.service.TaxSummary;
import com.mobitill.citirevenue.service.TransParam;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

/**
 *
 * @author Francis
 */
@Stateless
public class TransactionSessionBean {

    @PersistenceContext(unitName = "com.mobitill_CitiRevenue_war_1PU")
    private EntityManager em;
    @Resource
    private EJBContext context;

    @EJB
    private UtilitySessionBean usb;

    private static final Logger LOG = Logger.getLogger(TransactionSessionBean.class.getName());

    public Object saveTransaction(Transactions trans) {
        if (trans == null) {
            return "missing information";
        }
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean transExist = em.createNamedQuery("Transactions.findByReceiptnumber", Boolean.class)
                    .setParameter("receiptnumber", trans.getReceiptnumber())
                    .setParameter("pin", trans.getMerchantpin())
                    .getSingleResult();
            if (transExist) {
                LOG.log(Level.WARNING, "duplicate record");
                return "transaction already exist";
            }
            Taxregister device;
            try {
                device = em.createNamedQuery("Taxregister.findByDeviceid", Taxregister.class)
                        .setParameter("deviceid", trans.getDeviceserial())
                        .getSingleResult();
            } catch (NoResultException e) {
                return "invalid device serial";
            }
            if (device == null) {
                return "invalid device serial";
            }
            trans.setDevicetype(device.getDevicetype());
            Boolean merchantExist = em.createNamedQuery("Merchant.findByPinVat", Boolean.class)
                    .setParameter("pin", trans.getMerchantpin())
                    .setParameter("vat", trans.getMerchantpin())
                    .getSingleResult();
            if (!merchantExist) {
                return "invalid merchant pin";
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                trans.setStamp(sdf.parse(trans.getTimestamp()));
            } catch (ParseException parseException) {
                return "invalid timstamp format";
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

            em.persist(trans);

            Map<String, String> responseOk = new HashMap<>();
            responseOk.put("status", "200");
            responseOk.put("message", "ok");

            response.put("response", responseOk);
            return response;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }

    public List<Transactions> getTransactions() {
        List<Transactions> resultList = em.createNamedQuery("Transactions.findAll", Transactions.class).getResultList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Transactions transactions : resultList) {
            transactions.setTimestamp(sdf.format(transactions.getStamp()));
        }
        return resultList;
    }

    public Object getTransactionRange(TransParam param) {
        try {
            if (param == null) {
                return "missing information";
            }
            if (!usb.trimedString(param.getStart()) && usb.trimedString(param.getStop())) {
                return "missing start or stop dates";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            List<Transactions> resultList;
            try {
                Calendar calStart = configCalendar(dateFormat, param.getStart(), 0, 0, 0);
                Calendar calStop = configCalendar(dateFormat, param.getStop(), 23, 59, 59);

                if (calStop.before(calStart)) {
                    calStop = calStart;
                }

                if (param.getPin() == null) {
                    resultList = em.createNamedQuery("Transactions.findByStamp", Transactions.class)
                            .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                            .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                            .getResultList();
                } else {
                    resultList = em.createNamedQuery("Transactions.findByStampAndMerchant", Transactions.class)
                            .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                            .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                            .setParameter("pin", param.getPin())
                            .getResultList();
                }
            } catch (ParseException parseException) {
                return "invalid date format in start or stop dates";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Transactions transactions : resultList) {
                transactions.setTimestamp(sdf.format(transactions.getStamp()));
            }
            return resultList;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }

    public Object getMerchantTransactions(TransParam param) {
        try {
            if (param == null) {
                return "missing information";
            }
            if (!usb.trimedString(param.getStart()) && usb.trimedString(param.getStop())) {
                return "missing start or stop dates";
            }
            Boolean merchantExist = em.createNamedQuery("Merchant.findByPinVat", Boolean.class)
                    .setParameter("pin", param.getPin())
                    .setParameter("vat", param.getPin())
                    .getSingleResult();
            if (!merchantExist) {
                return "invalid merchant pin";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calStart;
            Calendar calStop;
            try {
                calStart = configCalendar(dateFormat, param.getStart(), 0, 0, 0);
                calStop = configCalendar(dateFormat, param.getStop(), 23, 59, 59);

            } catch (ParseException parseException) {
                return "invalid date format in start or stop dates";
            }

            List<Transactions> merchantPurchases = em.createNamedQuery("Transactions.findByStampClientpin", Transactions.class)
                    .setParameter("pin", param.getPin())
                    .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                    .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                    .getResultList();

            List<Transactions> merchantSales = em.createNamedQuery("Transactions.findByStampMerchantpin", Transactions.class)
                    .setParameter("pin", param.getPin())
                    .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                    .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                    .getResultList();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            double vatPaid = 0;
            double vatSale = 0;

            for (Transactions transactions : merchantPurchases) {
                transactions.setTimestamp(sdf.format(transactions.getStamp()));
                vatPaid += addCategoryAmounts(transactions.getTax1(), transactions.getTax2(), transactions.getTax3(), transactions.getTax4());
            }
            for (Transactions transactions : merchantSales) {
                transactions.setTimestamp(sdf.format(transactions.getStamp()));
                vatSale += addCategoryAmounts(transactions.getTax1(), transactions.getTax2(), transactions.getTax3(), transactions.getTax4());
            }

            double vatDue = (vatSale - vatPaid) < 0 ? vatSale : (vatSale - vatPaid);
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("purchases", merchantPurchases);
            map.put("sales", merchantSales);
            map.put("vatPaid", vatPaid);
            map.put("vatDue", Math.round(vatDue * 100D) / 100D);

            return map;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }

    private Calendar configCalendar(SimpleDateFormat dateFormat, String date, int hour, int min, int sec) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFormat.parse(date));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);

        return cal;
    }

    private Calendar configCalendar(SimpleDateFormat dateFormat, Integer year, Integer month, int hour, int min, int sec) throws ParseException {
        Calendar cal = Calendar.getInstance();
        String date;
        if (month == null) {
            date = String.format("%d-01-01", year);
        } else {
            date = String.format("%d-%02d-01", year, month);
        }
        cal.setTime(dateFormat.parse(date));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);

        if (hour == 23) {//last timestamp
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (month == null) {
                cal.set(Calendar.MONTH, 11);
                cal.set(Calendar.DAY_OF_MONTH, 31);
            }
        }
        return cal;
    }

    private double addCategoryAmounts(BigDecimal tax1, BigDecimal tax2, BigDecimal tax3, BigDecimal tax4) {
        return tax1.doubleValue() + tax2.doubleValue() + tax3.doubleValue() + tax4.doubleValue();
    }

    public Object getTransactionRangeSummary(TransParam param) {
        try {
            if (param == null) {
                return "missing information";
            }
            if (!usb.trimedString(param.getStart()) && usb.trimedString(param.getStop())) {
                return "missing start or stop dates";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            TaxSummary summary;
            try {
                Calendar calStart = configCalendar(dateFormat, param.getStart(), 0, 0, 0);
                Calendar calStop = configCalendar(dateFormat, param.getStop(), 23, 59, 59);

                if (calStop.before(calStart)) {
                    calStop = calStart;
                }

                if (param.getPin() == null) {
                    summary = em.createNamedQuery("Transactions.findByStampSummary", TaxSummary.class)
                            .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                            .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                            .getSingleResult();
                    
                    List<String> merchants = (List<String>) em.createNamedQuery("Transactions.findByStampMerchants")
                            .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                            .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                            .getResultList();
                    
                    summary.setMerchants(merchants.size());
                } else {
                    summary = em.createNamedQuery("Transactions.findByStampSummaryAndMerchant", TaxSummary.class)
                            .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                            .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                            .setParameter("pin",param.getPin())
                            .getSingleResult();
                    
                    summary.setMerchants(1);
                }

            } catch (ParseException parseException) {
                return "invalid date format in start or stop dates";
            }
            return summary;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }

    public Object getMerchantSummaryTransactions(TransParam param) {
        try {
            if (param == null) {
                return "missing information";
            }
            if (!usb.trimedString(param.getStart()) && usb.trimedString(param.getStop())) {
                return "missing start or stop dates";
            }
            Boolean merchantExist = em.createNamedQuery("Merchant.findByPinVat", Boolean.class)
                    .setParameter("pin", param.getPin())
                    .setParameter("vat", param.getPin())
                    .getSingleResult();
            if (!merchantExist) {
                return "invalid merchant pin";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calStart;
            Calendar calStop;
            try {
                if (param.getStart() == null) {
                    calStart = configCalendar(dateFormat, param.getYear(), param.getMonth(), 0, 0, 0);
                    calStop = configCalendar(dateFormat, param.getYear(), param.getMonth(), 23, 59, 59);
                } else {
                    calStart = configCalendar(dateFormat, param.getStart(), 0, 0, 0);
                    calStop = configCalendar(dateFormat, param.getStop(), 23, 59, 59);
                }

            } catch (ParseException parseException) {
                return "invalid date format in start or stop dates";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LOG.log(Level.INFO, "startstamp {0}", sdf.format(calStart.getTime()));
            LOG.log(Level.INFO, "stopstamp {0}", sdf.format(calStop.getTime()));

            List<Transactions> merchantPurchases = em.createNamedQuery("Transactions.findByStampClientpin", Transactions.class)
                    .setParameter("pin", param.getPin())
                    .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                    .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                    .getResultList();

            List<Transactions> merchantSales = em.createNamedQuery("Transactions.findByStampMerchantpin", Transactions.class)
                    .setParameter("pin", param.getPin())
                    .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                    .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                    .getResultList();

            List<MerchantSummary> summarys = em.createNamedQuery("Transactions.findByMerchantStampSummary", MerchantSummary.class)
                    .setParameter("merchantpin", param.getPin())
                    .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                    .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                    .getResultList();

            double vatPaid = 0;
            double vatSale = 0;
            double purchases = 0;
            double sales = 0;
            for (Transactions transactions : merchantPurchases) {
                transactions.setTimestamp(sdf.format(transactions.getStamp()));
                vatPaid += addCategoryAmounts(transactions.getTax1(), transactions.getTax2(), transactions.getTax3(), transactions.getTax4());
                purchases += addCategoryAmounts(transactions.getAmount1(), transactions.getAmount2(), transactions.getAmount3(), transactions.getAmount4());
            }
            for (Transactions transactions : merchantSales) {
                transactions.setTimestamp(sdf.format(transactions.getStamp()));
                vatSale += addCategoryAmounts(transactions.getTax1(), transactions.getTax2(), transactions.getTax3(), transactions.getTax4());
                sales += addCategoryAmounts(transactions.getAmount1(), transactions.getAmount2(), transactions.getAmount3(), transactions.getAmount4());
            }

            List<MerchantDescription> suppliers = new ArrayList<>();

            List<String> resultList = em.createNamedQuery("Transactions.findByStampClientMerchants", String.class)
                    .setParameter("clientPin", param.getPin())
                    .setParameter("startstamp", calStart, TemporalType.TIMESTAMP)
                    .setParameter("stopstamp", calStop, TemporalType.TIMESTAMP)
                    .getResultList();
            for (String string : resultList) {
                try {
                    MerchantDescription md = em.createNamedQuery("Merchant.findNameCategory", MerchantDescription.class)
                            .setParameter("pin", string)
                            .setParameter("vat", string)
                            .getSingleResult();
                    suppliers.add(md);
                } catch (NoResultException e) {
                }
            }

            String months[] = {"January", "February", "March", "April",
                "May", "June", "July", "August", "September",
                "October", "November", "December"};

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("year", param.getYear());
            if (param.getMonth() != null) {
                map.put("month", months[param.getMonth() - 1]);
            }
            double vatDue = (vatSale - vatPaid) < 0 ? vatSale : (vatSale - vatPaid);
            map.put("salesdetailed", summarys);
            map.put("vatPaid", vatPaid);
            map.put("vatDue", Math.round(vatDue * 100D) / 100D);
            map.put("purchases", purchases);
            map.put("sales", sales);
            map.put("suppliers", suppliers);
            map.put("purchasesList", merchantPurchases);
            map.put("salesList", merchantSales);

            return map;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }
}
