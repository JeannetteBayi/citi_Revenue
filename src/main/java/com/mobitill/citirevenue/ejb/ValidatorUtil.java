/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mobitill.citirevenue.ejb;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Startup
public class ValidatorUtil {
    private final ValidatorFactory factory;
    private final ObjectMapper mapper;
 
    {
        factory = Validation.buildDefaultValidatorFactory();
        mapper = new ObjectMapper();
    }
  
    public< T > Response validate( final T instance ) {
        
        final Validator validator = factory.getValidator();
  
        final Set< ConstraintViolation< T > > violations = validator
            .validate( instance, Default.class );

        if( !violations.isEmpty() ) {
            final Set< ConstraintViolation< ? > > constraints = 
                new HashSet<  >( violations.size() );

            for ( final ConstraintViolation< ? > violation: violations ) {
                constraints.add( violation );
            }
            StringBuilder sb = new  StringBuilder("");
            for (ConstraintViolation<?> constraintViolation : constraints) {
                sb.append(constraintViolation.getMessage());
                break;
            }
            //throw new ConstraintViolationException( constraints );
            return Response.status(Response.Status.BAD_REQUEST).entity(String.format("{\"msg\" : \"%s\"}", sb.toString())).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
        }
        return null;
    }
    public< T > String validateString( final T instance ) {
        
        final Validator validator = factory.getValidator();
  
        final Set< ConstraintViolation< T > > violations = validator
            .validate( instance, Default.class );

        if( !violations.isEmpty() ) {
            final Set< ConstraintViolation< ? > > constraints = 
                new HashSet<  >( violations.size() );

            for ( final ConstraintViolation< ? > violation: violations ) {
                constraints.add( violation );
            }
            StringBuilder sb = new  StringBuilder("");
            for (ConstraintViolation<?> constraintViolation : constraints) {
                sb.append(constraintViolation.getMessage());
                break;
            }
            //throw new ConstraintViolationException( constraints );
            return sb.toString();
        }
        return null;
    }
    public Object validJson(String jsonString,Class c){
        try {
            Object obj =  mapper.readValue(jsonString, c);
            return obj;
        } catch (IOException ex) {
            Logger.getLogger("validJson").log(Level.SEVERE, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"msg\" : \"invalid json structure\"}").type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").header("Access-Control-Max-Age", "1209600").build();
        }
    }
}
