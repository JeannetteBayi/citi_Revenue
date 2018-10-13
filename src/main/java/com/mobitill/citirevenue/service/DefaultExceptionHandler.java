/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mobitill.citirevenue.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Francis
 */
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {


    @Override
    public Response toResponse(Exception e) {
        Logger.getLogger("jax-rs").log(Level.SEVERE, e.getMessage(), e);
        if (e instanceof javax.ws.rs.NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity("not found").type(MediaType.TEXT_PLAIN).build();
        } else {
            StringBuilder response = new StringBuilder("{");
            response.append("\"status\" : \"ERROR\", ");
            response.append("\"message\" : ").append("\"contact admin\"}");
            return Response.serverError().entity(response.toString()).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
