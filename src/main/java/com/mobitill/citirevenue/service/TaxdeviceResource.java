/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import com.mobitill.citirevenue.ejb.TaxRegisterSessionBean;
import com.mobitill.citirevenue.ejb.UtilitySessionBean;
import com.mobitill.citirevenue.ejb.ValidatorUtil;
import com.mobitill.citirevenue.entity.Taxregister;
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
@Path("taxdevice")
public class TaxdeviceResource {

    @Context
    private UriInfo context;

    @EJB
    private TaxRegisterSessionBean msb;
    @EJB
    ValidatorUtil validatorUtil;
    @EJB
    private UtilitySessionBean usb;

    /**
     * Creates a new instance of TaxdeviceResource
     */
    public TaxdeviceResource() {
    }

    @GET
    @Path("list")
    public Response getTaxregisters(@HeaderParam("appc") String appacess) {
        return Response.status(Response.Status.OK).entity(msb.getTaxregisters()).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();

    }

    @POST
    @Path("create")
    public Response createTaxregisters(String jsonString, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(jsonString, Taxregister.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            Taxregister tr = (Taxregister) obj;
            Response validate = validatorUtil.validate(tr);
            if (validate != null) {
                return validate;
            }
            Object createDevice = msb.createTaxRegister(tr);
            if (createDevice instanceof String) {
                String error = (String) createDevice;
                return usb.errorResponse(error);
            } else {
                Taxregister map = (Taxregister) createDevice;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }

    }

    @POST
    @Path("edit")
    public Response editTaxregisters(String jsonString, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(jsonString, Taxregister.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            Taxregister tr = (Taxregister) obj;
            Response validate = validatorUtil.validate(tr);
            if (validate != null) {
                return validate;
            }
            Object editDevice = msb.editTaxRegister(tr);
            if (editDevice instanceof String) {
                String error = (String) editDevice;
                return usb.errorResponse(error);
            } else {
                Taxregister map = (Taxregister) editDevice;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }

    @POST
    @Path("find")
    public Response find(String deviceid, @HeaderParam("appc") String appacess) {
        Object findDevice = msb.getTaxRegister(deviceid);
        if (findDevice instanceof String) {
            String error = (String) findDevice;
            return usb.errorResponse(error);
        } else {
            Taxregister map = (Taxregister) findDevice;
            return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
        }
    }
}
