/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import com.mobitill.citirevenue.ejb.MerchantSessionBean;
import com.mobitill.citirevenue.ejb.UtilitySessionBean;
import com.mobitill.citirevenue.ejb.ValidatorUtil;
import com.mobitill.citirevenue.entity.Merchant;
import com.mobitill.citirevenue.entity.MerchantCredentials;
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
@Path("merchant")
public class MerchantResource {

    @Context
    private UriInfo context;

    @EJB
    private MerchantSessionBean msb;
    @EJB
    private ValidatorUtil validatorUtil;
    @EJB
    private UtilitySessionBean usb;

    /**
     * Creates a new instance of MerchantResource
     */
    public MerchantResource() {
    }

    @GET
    @Path("list")
    public Response getMerchants(@HeaderParam("appc") String appacess) {
        return Response.status(Response.Status.OK).entity(msb.getMerchants()).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
    }

    @POST
    @Path("create")
    public Response createMerchants(String jsonString, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(jsonString, MerchantCredentials.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            MerchantCredentials m = (MerchantCredentials) obj;
            Response validate = validatorUtil.validate(m);
            if (validate != null) {
                return validate;
            }
            Object createMerchant = msb.createMerchant(m);
            if (createMerchant instanceof String) {
                String error = (String) createMerchant;
                return usb.errorResponse(error);
            }else{
                Map<String,Object> map = (Map<String,Object>) createMerchant;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }

    }

    @POST
    @Path("edit")
    public Response editMerchants(String jsonString, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(jsonString, Merchant.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            Merchant m = (Merchant) obj;
            Response validate = validatorUtil.validate(m);
            if (validate != null) {
                return validate;
            }
            Object editMerchant = msb.editMerchant(m);
            if (editMerchant instanceof String) {
                String error = (String) editMerchant;
                return usb.errorResponse(error);
            }else{
                Merchant map = (Merchant) editMerchant;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }

    @POST
    @Path("find")
    public Response find(String merchantpin, @HeaderParam("appc") String appacess) {
        Object findMerchant = msb.getMerchant(merchantpin);
        if (findMerchant instanceof String) {
            String error = (String) findMerchant;
            return usb.errorResponse(error);
        }else{
            Map<String,Object> map = (Map<String,Object>) findMerchant;
            return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
        }
    }
}
