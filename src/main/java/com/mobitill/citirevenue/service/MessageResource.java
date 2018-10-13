/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import com.mobitill.citirevenue.ejb.MessageSessionBean;
import com.mobitill.citirevenue.ejb.UtilitySessionBean;
import com.mobitill.citirevenue.ejb.ValidatorUtil;
import com.mobitill.citirevenue.entity.Message;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Francis
 */
@Path("message")
public class MessageResource {

    @Context
    private UriInfo context;

    @EJB
    private MessageSessionBean msb;

    @EJB
    private ValidatorUtil validatorUtil;

    @EJB
    private UtilitySessionBean usb;

    /**
     * Creates a new instance of MessageResource
     */
    public MessageResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mobitill.citirevenue.service.MessageResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("list")
    public Response getMessages() {
        return Response.status(Response.Status.OK).entity(msb.getMessages()).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("create")
    public Response create(String jsonString) {
        Object obj = validatorUtil.validJson(jsonString, Message.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            Message m = (Message) obj;
            Response validate = validatorUtil.validate(m);
            if (validate != null) {
                return validate;
            }
            Object merchantMessage = msb.createMessage(m);
            if (merchantMessage instanceof String) {
                String error = (String) merchantMessage;
                return usb.errorResponse(error);
            } else {
                Message map = (Message) merchantMessage;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }

    @POST
    @Path("merchant")
    public Response getMessages(String jsonString) {
        if (jsonString != null) {
        return Response.status(Response.Status.OK).entity(msb.getMessages(jsonString)).type(MediaType.APPLICATION_JSON).build();
        } else {
            return usb.errorResponse("missing content");
        }
    }
}
