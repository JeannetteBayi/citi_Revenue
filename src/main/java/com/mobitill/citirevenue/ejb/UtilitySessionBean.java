/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import java.util.Calendar;
import java.util.TimeZone;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Francis
 */
@Stateless
public class UtilitySessionBean {

    
    public Response errorResponse(String msg) {
        return Response.status(Response.Status.BAD_REQUEST).entity(String.format("{\"msg\":\"%s\"}", msg)).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
    }

    public Response okResponse(String msg) {
        return Response.status(Response.Status.OK).entity(String.format("{\"msg\":\"%s\"}", msg)).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
    }
    /**
     * Check to see if String is null or empty
     *
     * @param value
     * @return False if value is null or empty else true
     */
    public boolean trimedString(String value) {
        if (value != null) {
            return !value.trim().isEmpty();
        } else {
            return false;
        }
    }
    public Calendar getCalendar(){
        return Calendar.getInstance(TimeZone.getTimeZone("GST"));
    }
}
