/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import com.mobitill.citirevenue.ejb.CitiSessionBean;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Francis
 */
@Path("service")
public class ServiceResource {

    @Context
    private UriInfo context;
    
    @EJB
    private CitiSessionBean csb;

    /**
     * Creates a new instance of ServiceResource
     */
    public ServiceResource() {
    }

    /**
     * Retrieves representation of an instance of com.mobitill.citirevenue.entity.ServiceResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("show")
    public Response getXml() {
        return Response.status(Response.Status.OK).entity(csb.getStatus()).type(MediaType.APPLICATION_JSON).build();
    }
}
