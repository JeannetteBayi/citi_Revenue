/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.bean;

import com.mobitill.citirevenue.ejb.AccesSessionBean;
import com.mobitill.citirevenue.ejb.MerchantSessionBean;
import com.mobitill.citirevenue.ejb.TaxRegisterSessionBean;
import com.mobitill.citirevenue.ejb.TransactionSessionBean;
import com.mobitill.citirevenue.ejb.UtilitySessionBean;
import com.mobitill.citirevenue.ejb.ValidatorUtil;
import com.mobitill.citirevenue.entity.Merchant;
import com.mobitill.citirevenue.entity.MerchantCredentials;
import com.mobitill.citirevenue.entity.Taxregister;
import com.mobitill.citirevenue.entity.Transactions;
import com.mobitill.citirevenue.entity.User;
import com.mobitill.citirevenue.service.MerchantDescription;
import com.mobitill.citirevenue.service.MerchantSummary;
import com.mobitill.citirevenue.service.TaxSummary;
import com.mobitill.citirevenue.service.TransParam;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author franc
 */
@Named(value = "homeManagedBean")
@ViewScoped
public class HomeManagedBean implements Serializable {

    private static final long serialVersionUID = -4655527348102146861L;

    private boolean displayMerchants;
    private boolean displayTaxDevices;
    private boolean displayFindTaxDevices;
    private boolean displayTransactions;
    private boolean displayMerchantTransactions;
    private boolean displayMerchantProfile;
    private boolean displayMyAccount;
    private boolean displayUsers;

    //merchant
    private String merchantName;
    private String merchantPIN;
    private String merchantVAT;
    private String merchantLocation;
    private int merchantCategory;
    private long merchantPhone;
    private List<Merchant> merchants;
    private Merchant merchantSelected;
    private List<Transactions> merchantPurchases;
    private List<Transactions> merchantSales;
    private double vatPaid;
    private double vatSale;
    private double totalPurchases;
    private double totalSales;
    private List<MerchantSummary> merchantSalesSummary;
    private List<MerchantDescription> suppliers;
    private String sessionMerchantPIN;
    private int year;
    private String month;
    private List<Taxregister> merchantTaxregisters;
    private Merchant merchantProfile;
    private List<String> merchantPins;
    private String merchantEmail;

    //tax register
    private String trMerchantPIN;
    private String trType;
    private String trDeviceId;
    private String trStatus;
    private String trDateCreated;
    private Taxregister taxregisterSelected;
    private List<Taxregister> taxregisters;

    //transactions
    private List<Transactions> transactions;
    private TaxSummary taxSummary;
    private Date startDate;
    private Date stopDate;
    private Transactions selectedTransaction;
    private String filerMerchantPin;

    //current user
    private List<String> roles;
    private String username;
    private String password;
    private String prevpassword;

    //portal users
    private List<User> users;
    private String userEmailAddress;
    private User selectedUser;

    @EJB
    private MerchantSessionBean msb;
    @EJB
    private ValidatorUtil validatorUtil;
    @EJB
    private TaxRegisterSessionBean trsb;
    @EJB
    private TransactionSessionBean tsb;
    @EJB
    private AccesSessionBean asb;
    @EJB
    private UtilitySessionBean usb;
    private static final Logger LOG = Logger.getLogger(HomeManagedBean.class.getName());

    /**
     * Creates a new instance of HomeManagedBean
     */
    public HomeManagedBean() {
    }

    @PostConstruct
    private void loadValues() {
        setMerchants(msb.getMerchants());
        setTaxregisters(trsb.getTaxregisters());
        merchantCategory = 5024;
        merchantPhone = 2547;
        setMerchantPins(msb.getMerchantsPins());
        Calendar cal = Calendar.getInstance();
        setStartDate(cal.getTime());
        setStopDate(cal.getTime());
        getTransactionsAndSummary();
        List<MerchantSummary> ms_summarys = new ArrayList<>();
        ms_summarys.add(new MerchantSummary(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
        setMerchantSalesSummary(ms_summarys);

        Principal userPrincipal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

        if (userPrincipal != null) {
            System.out.println("name " + userPrincipal.getName());
            setSessionMerchantPIN(asb.getPin(userPrincipal.getName()));
            setRoles(asb.getRoles(userPrincipal.getName()));
            setUsername(userPrincipal.getName());
        }

        getMerchantTransactionsAndSummary();
        getMerchantProfileandTaxDevices();
        setUsers(asb.getUsers());
    }

    public String showHome() {
        return "/demo/home.xhtml?faces-redirect=true";
    }

    public void logout() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
    }

    public void showMerchants() {
        setDisplayMerchants(Boolean.TRUE);
        setDisplayTaxDevices(Boolean.FALSE);
        setDisplayFindTaxDevices(Boolean.FALSE);
        setDisplayTransactions(Boolean.FALSE);
        setDisplayMerchantTransactions(Boolean.FALSE);
        setDisplayMerchantProfile(Boolean.FALSE);
        setDisplayMyAccount(Boolean.FALSE);
        setDisplayUsers(Boolean.FALSE);
        clearMerchant();
    }

    public void showTaxDevice() {
        setDisplayTaxDevices(Boolean.TRUE);
        setDisplayMerchants(Boolean.FALSE);
        setDisplayFindTaxDevices(Boolean.FALSE);
        setDisplayTransactions(Boolean.FALSE);
        setDisplayMerchantTransactions(Boolean.FALSE);
        setDisplayMerchantProfile(Boolean.FALSE);
        setDisplayMyAccount(Boolean.FALSE);
        setDisplayUsers(Boolean.FALSE);
        clearTaxregister();
    }

    public void showFindTaxDevice() {
        setTrType(null);
        setDisplayFindTaxDevices(Boolean.TRUE);
        setDisplayTaxDevices(Boolean.FALSE);
        setDisplayMerchants(Boolean.FALSE);
        setDisplayTransactions(Boolean.FALSE);
        setDisplayMerchantTransactions(Boolean.FALSE);
        setDisplayMerchantProfile(Boolean.FALSE);
        setDisplayMyAccount(Boolean.FALSE);
        setDisplayUsers(Boolean.FALSE);
        setTrDeviceId("");
        setTrDateCreated("");
        setTrMerchantPIN("");
        setTrType("");
        setTrStatus("");
    }

    public void showTransactions() {
        setDisplayTransactions(Boolean.TRUE);
        setDisplayTaxDevices(Boolean.FALSE);
        setDisplayMerchants(Boolean.FALSE);
        setDisplayFindTaxDevices(Boolean.FALSE);
        setDisplayMerchantTransactions(Boolean.FALSE);
        setDisplayMerchantProfile(Boolean.FALSE);
        setDisplayMyAccount(Boolean.FALSE);
        setDisplayUsers(Boolean.FALSE);
    }

    public void showMerchantTransactions() {
        setDisplayMerchantTransactions(Boolean.TRUE);
        setDisplayTransactions(Boolean.FALSE);
        setDisplayTaxDevices(Boolean.FALSE);
        setDisplayMerchants(Boolean.FALSE);
        setDisplayFindTaxDevices(Boolean.FALSE);
        setDisplayMerchantProfile(Boolean.FALSE);
        setDisplayMyAccount(Boolean.FALSE);
        setDisplayUsers(Boolean.FALSE);
    }

    public void showMerchantProfile() {
        setDisplayMerchantProfile(Boolean.TRUE);
        setDisplayMerchantTransactions(Boolean.FALSE);
        setDisplayTransactions(Boolean.FALSE);
        setDisplayTaxDevices(Boolean.FALSE);
        setDisplayMerchants(Boolean.FALSE);
        setDisplayFindTaxDevices(Boolean.FALSE);
        setDisplayMyAccount(Boolean.FALSE);
        setDisplayUsers(Boolean.FALSE);
    }

    public void showMyAccount() {
        setDisplayMyAccount(Boolean.TRUE);
        setDisplayMerchantProfile(Boolean.FALSE);
        setDisplayMerchantTransactions(Boolean.FALSE);
        setDisplayTransactions(Boolean.FALSE);
        setDisplayTaxDevices(Boolean.FALSE);
        setDisplayMerchants(Boolean.FALSE);
        setDisplayFindTaxDevices(Boolean.FALSE);
        setDisplayUsers(Boolean.FALSE);
        clearMyAccountFields();
    }

    public void showUsers() {
        setDisplayUsers(Boolean.TRUE);
        setDisplayMyAccount(Boolean.FALSE);
        setDisplayMerchantProfile(Boolean.FALSE);
        setDisplayMerchantTransactions(Boolean.FALSE);
        setDisplayTransactions(Boolean.FALSE);
        setDisplayTaxDevices(Boolean.FALSE);
        setDisplayMerchants(Boolean.FALSE);
        setDisplayFindTaxDevices(Boolean.FALSE);
        setSelectedUser(null);
        setUserEmailAddress(null);
    }

    public void onMerchantSelect() {
        System.out.println("was called onMerchant");
        if (merchantSelected != null) {
            setMerchantName(merchantSelected.getName());
            setMerchantPIN(merchantSelected.getPin());
            setMerchantVAT(merchantSelected.getVat());
            setMerchantLocation(merchantSelected.getLocation());
            setMerchantCategory(Integer.parseInt(merchantSelected.getCategorytype()));
            setMerchantPhone(Long.parseLong(merchantSelected.getPhonenumber()));
        }
        System.out.println(" merchant null "+merchantSelected == null);
    }

    public void save_edit_Merchant() {
        MerchantCredentials m = new MerchantCredentials();
        m.setName(merchantName);
        m.setPin(merchantPIN);
        m.setVat(merchantVAT);
        m.setLocation(merchantLocation);
        m.setPhonenumber(Long.toString(merchantPhone));
        m.setCategorytype(Integer.toString(merchantCategory));
        m.setEmail(merchantEmail);
        
        LOG.log(Level.INFO, "name     {0}",m.getName());
        LOG.log(Level.INFO, "PIN      {0}",m.getPin());
        LOG.log(Level.INFO, "VAT      {0}",m.getVat());
        LOG.log(Level.INFO, "Location {0}",m.getLocation());
        LOG.log(Level.INFO, "Phone    {0}",m.getPhonenumber());
        LOG.log(Level.INFO, "Category {0}",m.getCategorytype());
        LOG.log(Level.INFO, "Email    {0}",m.getEmail());

        String validate = validatorUtil.validateString(m);
        if (validate != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", validate));
            return;
        }
        if (!m.getPhonenumber().startsWith("2547")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "invalid phone number"));
            return;
        }

        Object merchant;
        if (merchantSelected == null) {
            merchant = msb.createMerchant(m);
        } else {
            merchant = msb.editMerchant(m);
        }
        if (merchant instanceof String) {
            String error = (String) merchant;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", error));
        } else if (merchant instanceof Merchant) {
            Merchant editMerchant = (Merchant) merchant;

            setMerchantName(editMerchant.getName());
            setMerchantLocation(editMerchant.getLocation());

            merchantSelected.setName(editMerchant.getName());
            merchantSelected.setLocation(editMerchant.getLocation());
            merchantSelected.setPhonenumber(m.getPhonenumber());
            merchantSelected.setCategorytype(m.getCategorytype());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Merchant Modified"));

        } else {
            Map<String, Object> map = (Map<String, Object>) merchant;
            if (merchantSelected == null) {
                setMerchantName((String) map.get("name"));
                setMerchantPIN((String) map.get("pin"));
                setMerchantVAT((String) map.get("vat"));
                setMerchantLocation((String) map.get("location"));

                m.setId((int) map.get("id"));
                m.setDatecreated((String) map.get("datecreated"));
                m.setName((String) map.get("name"));
                m.setPin((String) map.get("pin"));
                m.setVat((String) map.get("vat"));
                m.setLocation((String) map.get("location"));

                getMerchants().add(m);

                getUsers().clear();
                getUsers().addAll(asb.getUsers());
            }
            getMerchantPins().add(m.getPin());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Merchant Created"));
        }
    }

    public void clearMerchant() {
        setMerchantName(null);
        setMerchantPIN(null);
        setMerchantVAT(null);
        setMerchantLocation(null);
        setMerchantEmail(null);
        setMerchantCategory(5042);
        setMerchantPhone(2547);
        merchantSelected = null;
    }

    public void clearTaxregister() {
        setTrDeviceId(null);
        setTrMerchantPIN(null);
        setTrType(null);
        taxregisterSelected = null;
    }

    public void onTaxregisterSelect() {
        if (taxregisterSelected != null) {
            setTrDeviceId(taxregisterSelected.getDeviceid());
            setTrMerchantPIN(taxregisterSelected.getMerchantpin());
            setTrType(taxregisterSelected.getDevicetype());
        }
    }

    public void save_edit_Taxregister() {
        Taxregister taxregister = new Taxregister();
        taxregister.setDevicetype(trType);
        taxregister.setMerchantpin(trMerchantPIN);

        String validate = validatorUtil.validateString(taxregister);
        if (validate != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", validate));
            return;
        }

        Object tr;
        if (taxregisterSelected == null) {
            tr = trsb.createTaxRegister(taxregister);
        } else {
            taxregisterSelected.setDevicetype(trType);
            taxregisterSelected.setMerchantpin(trMerchantPIN);

            tr = trsb.editTaxRegister(taxregisterSelected);
        }
        if (tr instanceof String) {
            String error = (String) tr;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", error));
        } else if (tr instanceof Taxregister) {
            Taxregister mergedTaxregister = (Taxregister) tr;
            setTrDeviceId(mergedTaxregister.getDeviceid());

            if (taxregisterSelected == null) {
                getTaxregisters().add(mergedTaxregister);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Tax Register Created"));
            } else {
                taxregisterSelected.setStatusMessage(mergedTaxregister.getStatusMessage());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Tax Register Modified"));
            }
        }
    }

    public void findTaxregister() {
        Object tr = trsb.getTaxRegister(trDeviceId);
        if (tr instanceof String) {
            setTrMerchantPIN(null);
            setTrType(null);
            taxregisterSelected = null;
            setTrDateCreated(null);
            setTrStatus(null);

            String error = (String) tr;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", error));
        } else if (tr instanceof Taxregister) {
            Taxregister taxregister = (Taxregister) tr;
            setTrStatus(taxregister.getStatusMessage());
            setTrMerchantPIN(taxregister.getMerchantpin());
            setTrType(taxregister.getDevicetype());
            setTrDateCreated(taxregister.getDateissued());
        }
    }

    private String getTransactionsAndSummary() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TransParam param = new TransParam();
        param.setStart(dateFormat.format(getStartDate()));
        param.setStop(dateFormat.format(getStopDate()));

        if (filerMerchantPin != null) {
            param.setPin(filerMerchantPin.isEmpty() ? null : filerMerchantPin);
            if (filerMerchantPin.equals("Select Merchant")) {
                param.setPin(null);
            }
        } else {
            param.setPin(null);
        }

        Object transactionRange = tsb.getTransactionRange(param);
        if (transactionRange instanceof String) {
            String error = (String) transactionRange;
            LOG.log(Level.SEVERE, error);
            return error;
        } else {
            List<Transactions> resultList = (List<Transactions>) transactionRange;
            if (getTransactions() == null) {
                setTransactions(resultList);
            } else {
                getTransactions().clear();
                getTransactions().addAll(resultList);
            }
        }
        Object transactionSummary = tsb.getTransactionRangeSummary(param);
        if (transactionSummary instanceof String) {
            String error = (String) transactionSummary;
            LOG.log(Level.SEVERE, error);
            return error;
        } else {
            TaxSummary summary = (TaxSummary) transactionSummary;
            setTaxSummary(summary);
        }
        return null;
    }

    public void searchTransactionRange() {
        String error = null;
        try {
            error = getTransactionsAndSummary();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Missing information"));
        }
        if (error != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", error));
        }
    }

    private String getMerchantTransactionsAndSummary() {
        if (getSessionMerchantPIN() == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TransParam param = new TransParam();
        param.setStart(dateFormat.format(getStartDate()));
        param.setStop(dateFormat.format(getStopDate()));
        param.setPin(getSessionMerchantPIN());

        Object merchantSummary = tsb.getMerchantSummaryTransactions(param);
        if (merchantSummary instanceof String) {
            String error = (String) merchantSummary;
            LOG.log(Level.SEVERE, error);
            return error;
        } else {
            Map<String, Object> resultList = (Map<String, Object>) merchantSummary;
            if (getMerchantPurchases() == null) {
                setMerchantPurchases((List<Transactions>) resultList.get("purchasesList"));
            } else {
                getMerchantPurchases().clear();
                getMerchantPurchases().addAll((List<Transactions>) resultList.get("purchasesList"));
            }
            if (getMerchantSales() == null) {
                setMerchantSales((List<Transactions>) resultList.get("salesList"));
            } else {
                getMerchantSales().clear();
                getMerchantSales().addAll((List<Transactions>) resultList.get("salesList"));
            }
            //setMonth((String)resultList.get("month"));
            //setYear((int)resultList.get("year"));
            setVatPaid((double) resultList.get("vatPaid"));
            setVatSale((double) resultList.get("vatDue"));
            setTotalPurchases((double) resultList.get("purchases"));
            setTotalSales((double) resultList.get("sales"));
            if (getMerchantSalesSummary() == null) {
                setMerchantSalesSummary((List<MerchantSummary>) resultList.get("salesdetailed"));
            } else {
                getMerchantSalesSummary().clear();
                getMerchantSalesSummary().addAll((List<MerchantSummary>) resultList.get("salesdetailed"));
            }
            if (getSuppliers() == null) {
                setSuppliers((List<MerchantDescription>) resultList.get("suppliers"));
            } else {
                getSuppliers().clear();
                getSuppliers().addAll((List<MerchantDescription>) resultList.get("suppliers"));
            }
        }
        return null;
    }

    public void searchMerchantTransaction() {
        String error = null;
        try {
            error = getMerchantTransactionsAndSummary();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Missing information"));
        }
        if (error != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", error));
        }
    }

    public double summaryAmount1() {
        return getMerchantSalesSummary().get(0).getAmount1().doubleValue();
    }

    public double summaryAmount2() {
        return getMerchantSalesSummary().get(0).getAmount2().doubleValue();
    }

    public double summaryAmount3() {
        return getMerchantSalesSummary().get(0).getAmount3().doubleValue();
    }

    public double summaryAmount4() {
        return getMerchantSalesSummary().get(0).getAmount4().doubleValue();
    }

    public double summaryTax1() {
        return getMerchantSalesSummary().get(0).getTax1().doubleValue();
    }

    public double summaryTax2() {
        return getMerchantSalesSummary().get(0).getTax2().doubleValue();
    }

    public double summaryTax3() {
        return getMerchantSalesSummary().get(0).getTax3().doubleValue();
    }

    public double summaryTax4() {
        return getMerchantSalesSummary().get(0).getTax4().doubleValue();
    }

    public boolean getDisplayMerchants() {
        return displayMerchants;
    }

    private String getMerchantProfileandTaxDevices() {
        if (getSessionMerchantPIN() == null) {
            return null;
        }
        Object profile = msb.getMerchant(getSessionMerchantPIN());
        if (profile instanceof String) {
            String error = (String) profile;
            LOG.log(Level.SEVERE, error);
            return error;
        } else {
            Map<String, Object> map = (Map<String, Object>) profile;
            setMerchantProfile((Merchant) map.get("merchant"));
            if (getMerchantTaxregisters() == null) {
                setMerchantTaxregisters((List<Taxregister>) map.get("taxdevice"));
            } else {
                getMerchantTaxregisters().clear();
                getMerchantTaxregisters().addAll((List<Taxregister>) map.get("taxdevice"));
            }
        }
        return null;
    }

    public String getTransactionReceiptInfo() {
        if (selectedTransaction != null) {
            StringBuilder sb = new StringBuilder("<pre>");
            sb.append(selectedTransaction.getReceiptinfo() == null ? "" : selectedTransaction.getReceiptinfo());
            sb.append("</pre>");
            return sb.toString();

        }
        return "";
    }

    public void setTransactionReceiptInfo(String value) {

    }

    public void changePassword() {
        if (!usb.trimedString(getPrevpassword()) || !usb.trimedString(getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Missing Information"));
            return;
        }
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            Object changePass = asb.changePassword(user, getPrevpassword());
            if (changePass instanceof String) {
                String error = (String) changePass;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", error));
            } else if (changePass instanceof User) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Password change"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "something went wrong"));
        }
    }

    public void clearMyAccountFields() {
        setPassword(null);
        setPrevpassword(null);
    }

    public void resetPassword() {
        if (!usb.trimedString(selectedUser.getEmail())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Missing Email Address"));
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgUsers').show();");
            return;
        }
        try {
            Pattern ptr = Pattern.compile("(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)");
            if (!ptr.matcher(selectedUser.getEmail()).matches()) {
                selectedUser.setEmail(null);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Invalid Email Address"));
                return;
            }
            Object changePass = asb.resetPassword(selectedUser);
            if (changePass instanceof String) {
                String error = (String) changePass;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", error));
            } else if (changePass instanceof User) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Password Reset"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "something went wrong"));
        }
    }

    public void setDisplayMerchants(boolean displayMerchants) {
        this.displayMerchants = displayMerchants;
    }

    public boolean getDisplayTaxDevices() {
        return displayTaxDevices;
    }

    public void setDisplayTaxDevices(boolean displayTaxDevices) {
        this.displayTaxDevices = displayTaxDevices;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantPIN() {
        return merchantPIN;
    }

    public void setMerchantPIN(String merchantPIN) {
        this.merchantPIN = merchantPIN;
    }

    public String getMerchantVAT() {
        return merchantVAT;
    }

    public void setMerchantVAT(String merchantVAT) {
        this.merchantVAT = merchantVAT;
    }

    public String getMerchantLocation() {
        return merchantLocation;
    }

    public void setMerchantLocation(String merchantLocation) {
        this.merchantLocation = merchantLocation;
    }

    public long getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(long merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }

    public Merchant getMerchantSelected() {
        return merchantSelected;
    }

    public void setMerchantSelected(Merchant merchantSelected) {
        this.merchantSelected = merchantSelected;
    }

    public int getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(int merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    public String getTrMerchantPIN() {
        return trMerchantPIN;
    }

    public void setTrMerchantPIN(String trMerchantPIN) {
        this.trMerchantPIN = trMerchantPIN;
    }

    public String getTrType() {
        return trType;
    }

    public void setTrType(String trType) {
        this.trType = trType;
    }

    public String getTrDeviceId() {
        return trDeviceId;
    }

    public void setTrDeviceId(String trDeviceId) {
        this.trDeviceId = trDeviceId;
    }

    public Taxregister getTaxregisterSelected() {
        return taxregisterSelected;
    }

    public void setTaxregisterSelected(Taxregister taxregisterSelected) {
        this.taxregisterSelected = taxregisterSelected;
    }

    public List<Taxregister> getTaxregisters() {
        return taxregisters;
    }

    public void setTaxregisters(List<Taxregister> taxregisters) {
        this.taxregisters = taxregisters;
    }

    public boolean getDisplayFindTaxDevices() {
        return displayFindTaxDevices;
    }

    public void setDisplayFindTaxDevices(boolean displayFindTaxDevices) {
        this.displayFindTaxDevices = displayFindTaxDevices;
    }

    public String getTrStatus() {
        return trStatus;
    }

    public void setTrStatus(String trStatus) {
        this.trStatus = trStatus;
    }

    public String getTrDateCreated() {
        return trDateCreated;
    }

    public void setTrDateCreated(String trDateCreated) {
        this.trDateCreated = trDateCreated;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    public TaxSummary getTaxSummary() {
        return taxSummary;
    }

    public void setTaxSummary(TaxSummary taxSummary) {
        this.taxSummary = taxSummary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public boolean getDisplayTransactions() {
        return displayTransactions;
    }

    public void setDisplayTransactions(boolean displayTransactions) {
        this.displayTransactions = displayTransactions;
    }

    public List<Transactions> getMerchantPurchases() {
        return merchantPurchases;
    }

    public void setMerchantPurchases(List<Transactions> merchantPurchases) {
        this.merchantPurchases = merchantPurchases;
    }

    public List<Transactions> getMerchantSales() {
        return merchantSales;
    }

    public void setMerchantSales(List<Transactions> merchantSales) {
        this.merchantSales = merchantSales;
    }

    public double getVatPaid() {
        return vatPaid;
    }

    public void setVatPaid(double vatPaid) {
        this.vatPaid = vatPaid;
    }

    public double getVatSale() {
        return vatSale;
    }

    public void setVatSale(double vatSale) {
        this.vatSale = vatSale;
    }

    public String getSessionMerchantPIN() {
        return sessionMerchantPIN;
    }

    public void setSessionMerchantPIN(String sessionMerchantPIN) {
        this.sessionMerchantPIN = sessionMerchantPIN;
    }

    public double getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(double totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public List<MerchantSummary> getMerchantSalesSummary() {
        return merchantSalesSummary;
    }

    public void setMerchantSalesSummary(List<MerchantSummary> merchantSalesSummary) {
        this.merchantSalesSummary = merchantSalesSummary;
    }

    public List<MerchantDescription> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<MerchantDescription> suppliers) {
        this.suppliers = suppliers;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean getDisplayMerchantTransactions() {
        return displayMerchantTransactions;
    }

    public void setDisplayMerchantTransactions(boolean displayMerchantTransactions) {
        this.displayMerchantTransactions = displayMerchantTransactions;
    }

    public boolean getDisplayMerchantProfile() {
        return displayMerchantProfile;
    }

    public void setDisplayMerchantProfile(boolean displayMerchantProfile) {
        this.displayMerchantProfile = displayMerchantProfile;
    }

    public List<Taxregister> getMerchantTaxregisters() {
        return merchantTaxregisters;
    }

    public void setMerchantTaxregisters(List<Taxregister> merchantTaxregisters) {
        this.merchantTaxregisters = merchantTaxregisters;
    }

    public Merchant getMerchantProfile() {
        return merchantProfile;
    }

    public void setMerchantProfile(Merchant merchantProfile) {
        this.merchantProfile = merchantProfile;
    }

    public Transactions getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedTransaction(Transactions selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    public String getFilerMerchantPin() {
        return filerMerchantPin;
    }

    public void setFilerMerchantPin(String filerMerchantPin) {
        this.filerMerchantPin = filerMerchantPin;
    }

    public List<String> getMerchantPins() {
        return merchantPins;
    }

    public void setMerchantPins(List<String> merchantPins) {
        this.merchantPins = merchantPins;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getDisplayMyAccount() {
        return displayMyAccount;
    }

    public void setDisplayMyAccount(boolean displayMyAccount) {
        this.displayMyAccount = displayMyAccount;
    }

    public String getPrevpassword() {
        return prevpassword;
    }

    public void setPrevpassword(String prevpassword) {
        this.prevpassword = prevpassword;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public boolean isDisplayUsers() {
        return displayUsers;
    }

    public void setDisplayUsers(boolean displayUsers) {
        this.displayUsers = displayUsers;
    }

}
