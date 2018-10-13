/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Francis
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.mobitill.citirevenue.service.CategoryResource.class);
        resources.add(com.mobitill.citirevenue.service.DefaultExceptionHandler.class);
        resources.add(com.mobitill.citirevenue.service.MerchantResource.class);
        resources.add(com.mobitill.citirevenue.service.MessageResource.class);
        resources.add(com.mobitill.citirevenue.service.ServiceResource.class);
        resources.add(com.mobitill.citirevenue.service.TaxdeviceResource.class);
        resources.add(com.mobitill.citirevenue.service.TransactionResource.class);
        resources.add(com.mobitill.citirevenue.service.UserResource.class);
    }
    
}
