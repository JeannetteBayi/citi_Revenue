/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import com.mobitill.citirevenue.ejb.BulkTransactionSessionBean;
import com.mobitill.citirevenue.ejb.TransactionSessionBean;
import com.mobitill.citirevenue.ejb.UtilitySessionBean;
import com.mobitill.citirevenue.ejb.ValidatorUtil;
import com.mobitill.citirevenue.entity.Transactions;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Francis
 */
@Path("transaction")
public class TransactionResource {

    @Context
    private UriInfo context;
    @EJB
    private ValidatorUtil validatorUtil;
    @EJB
    private TransactionSessionBean tsb;
    @EJB
    private BulkTransactionSessionBean btsb;
    @EJB
    private UtilitySessionBean usb;

    /**
     * Creates a new instance of TransactionResource
     */
    public TransactionResource() {
    }

    @POST
    @Path("save")
    public Response putJson(String content, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(content, Transactions.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            Transactions trans = (Transactions) obj;
            Response validate = validatorUtil.validate(trans);
            if (validate != null) {
                return validate;
            }
            Object saveTransaction = tsb.saveTransaction(trans);
            if (saveTransaction instanceof String) {
                String error = (String) saveTransaction;
                return usb.errorResponse(error);
            }else{
                Map<String,String> map = (Map<String,String>) saveTransaction;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }
    @POST
    @Path("bulk")
    public Response bulkTransaction(String content, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(content, Transactions[].class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            Transactions[] trans = (Transactions[]) obj;
            System.out.println("size "+trans.length);
            for (Transactions tran : trans) {
                Response validate = validatorUtil.validate(tran);
                if (validate != null) {
                    return validate;
                }
                System.out.println("merchantpin "+tran.getMerchantpin());
            }
            return btsb.saveBulkTransaction(Arrays.asList(trans));
        }
    }

    @GET
    @Path("list")
    public Response getTransactions(@HeaderParam("appc") String appacess) {
        return Response.status(Response.Status.OK).entity(tsb.getTransactions()).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
    }

    @POST
    @Path("range")
    public Response getTransactionsRange(String content, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(content, TransParam.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            TransParam trans = (TransParam) obj;
            Response validate = validatorUtil.validate(trans);
            if (validate != null) {
                return validate;
            }
            Object transactions = tsb.getTransactionRange(trans);
            if (transactions instanceof String) {
                String error = (String) transactions;
                return usb.errorResponse(error);
            }else{
                List<Transactions> map = (List<Transactions>) transactions;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }

    @POST
    @Path("merchant")
    public Response getMerchantTransactions(String content, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(content, TransParam.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            TransParam trans = (TransParam) obj;
            Response validate = validatorUtil.validate(trans);
            if (validate != null) {
                return validate;
            }
            Object transactions = tsb.getMerchantTransactions(trans);
            if (transactions instanceof String) {
                String error = (String) transactions;
                return usb.errorResponse(error);
            }else{
                Map<String,Object> map = (Map<String,Object>) transactions;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }
    @POST
    @Path("summary")
    public Response getTransactionsRangeSummary(String content, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(content, TransParam.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            TransParam trans = (TransParam) obj;
            Response validate = validatorUtil.validate(trans);
            if (validate != null) {
                return validate;
            }
            Object summary = tsb.getTransactionRangeSummary(trans);
            if (summary instanceof String) {
                String error = (String) summary;
                return usb.errorResponse(error);
            }else{
                TaxSummary map = (TaxSummary) summary;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }
    @POST
    @Path("merchantsummary")
    public Response getMerchantSummaryTransactions(String content, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(content, TransParam.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            TransParam trans = (TransParam) obj;
            Response validate = validatorUtil.validate(trans);
            if (validate != null) {
                return validate;
            }
            Object merchantsummary = tsb.getMerchantSummaryTransactions(trans);
            if (merchantsummary instanceof String) {
                String error = (String) merchantsummary;
                return usb.errorResponse(error);
            }else{
                Map<String,Object> map = (Map<String,Object>) merchantsummary;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }
}
