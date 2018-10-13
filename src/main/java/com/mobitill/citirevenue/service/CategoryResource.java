/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import com.mobitill.citirevenue.ejb.MerchantCategorySessionBean;
import com.mobitill.citirevenue.ejb.UtilitySessionBean;
import com.mobitill.citirevenue.entity.MerchantCategory;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Francis
 */
@Path("category")
public class CategoryResource {

    @Context
    private UriInfo context;

    @EJB
    private MerchantCategorySessionBean mcsb;
    @EJB
    private UtilitySessionBean usb;

    /**
     * Creates a new instance of CategoryResource
     */
    public CategoryResource() {
    }

    @GET
    @Path("list")
    public Response getCategories() {
        return Response.status(Response.Status.OK).entity(mcsb.merchantList()).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
    }

    @POST
    @Path("createlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createList(List<MerchantCategory> list) {
        mcsb.createMerchantCategory(list);
        return "ok";
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(MerchantCategory msc) {
        try {
            return Response.status(Response.Status.OK).entity(mcsb.createMerchantCategory(msc)).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
        } catch (Exception e) {
            return usb.errorResponse(e.getMessage());
        }
    }
}
