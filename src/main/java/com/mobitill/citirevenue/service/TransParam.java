/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author Francis
 */
public class TransParam {
    @Min(value = 2000)
    private Integer year;
    @Max(value = 12,message = "allowed values 1-12") @Min(value = 1,message = "allowed values 1-12")
    private Integer month;
    private String start;
    private String stop;
    private String pin;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
    
}
