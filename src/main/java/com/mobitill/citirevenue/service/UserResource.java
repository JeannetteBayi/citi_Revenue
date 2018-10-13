/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import com.mobitill.citirevenue.ejb.AccesSessionBean;
import com.mobitill.citirevenue.ejb.UtilitySessionBean;
import com.mobitill.citirevenue.ejb.ValidatorUtil;
import com.mobitill.citirevenue.entity.User;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Francis
 */
@Path("user")
public class UserResource {

    @Context
    private UriInfo context;
    @EJB
    private ValidatorUtil validatorUtil;
    @EJB
    private AccesSessionBean asb;
    @EJB
    private UtilitySessionBean usb;

    private static final String ROLE = "MANAGEMENT";

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }

    @POST
    @Path("create")
    public Response createUser(String jsonString, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(jsonString, User.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            User u = (User) obj;
            Response validate = validatorUtil.validate(u);
            if (validate != null) {
                return validate;
            }
            Object createUser = asb.createUser(u);
            if (createUser instanceof String) {
                String error = (String) createUser;
                return usb.errorResponse(error);
            } else {
                User map = (User) createUser;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }

    }

    @POST
    @Path("edit")
    public Response editMerchants(String jsonString, @HeaderParam("appc") String appacess) {
        Object obj = validatorUtil.validJson(jsonString, User.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            User u = (User) obj;
            Response validate = validatorUtil.validate(u);
            if (validate != null) {
                return validate;
            }
            Object editUser = asb.editUser(u);
            if (editUser instanceof String) {
                String error = (String) editUser;
                return usb.errorResponse(error);
            } else {
                User map = (User) editUser;
                return Response.status(Response.Status.OK).entity(map).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
            }
        }
    }

    @GET
    @Path("list")
    public Response getUserList(@HeaderParam("appc") String appacess) {
        return Response.status(Response.Status.OK).entity(asb.getUsers()).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
    }

    @POST
    @Path("login")
    public Response login(@HeaderParam("appc") String appacess, String jsonString) {
        Object obj = validatorUtil.validJson(jsonString, LoginCredential.class);
        if (obj instanceof Response) {
            return (Response) obj;
        } else {
            LoginCredential u = (LoginCredential) obj;
            Response validate = validatorUtil.validate(u);
            if (validate != null) {
                return validate;
            }
            try {
                String hash = asb.generatePasswordHash(u.getUsername(), u.getPassword()).toString();
                Map<String, String> requestLogin = asb.requestLogin(hash);
                if (requestLogin != null) {
                    return Response.status(Response.Status.OK).entity(requestLogin).type(MediaType.APPLICATION_JSON).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity("Forbidden").type(MediaType.TEXT_PLAIN_TYPE).build();
                }
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                return usb.errorResponse("error occured");
            }
        }
    }
}
