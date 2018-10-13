/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.service;

import java.math.BigDecimal;

/**
 *
 * @author Francis
 */
public class MerchantSummary {
    
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal amount3;
    private BigDecimal amount4;
    private BigDecimal tax1;
    private BigDecimal tax2;
    private BigDecimal tax3;
    private BigDecimal tax4;

    public MerchantSummary(BigDecimal amount1, BigDecimal amount2, BigDecimal amount3, BigDecimal amount4, BigDecimal tax1, BigDecimal tax2, BigDecimal tax3, BigDecimal tax4) {
        this.amount1 = (amount1 == null ? BigDecimal.ZERO : amount1);
        this.amount2 = (amount2 == null ? BigDecimal.ZERO : amount2);
        this.amount3 = (amount3 == null ? BigDecimal.ZERO : amount3);
        this.amount4 = (amount4 == null ? BigDecimal.ZERO : amount4);
        this.tax1 = (tax1 == null ? BigDecimal.ZERO : tax1);
        this.tax2 = (tax2 == null ? BigDecimal.ZERO : tax2);
        this.tax3 = (tax3 == null ? BigDecimal.ZERO : tax3);
        this.tax4 = (tax4 == null ? BigDecimal.ZERO : tax4);
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount3() {
        return amount3;
    }

    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

    public BigDecimal getAmount4() {
        return amount4;
    }

    public void setAmount4(BigDecimal amount4) {
        this.amount4 = amount4;
    }

    public BigDecimal getTax1() {
        return tax1;
    }

    public void setTax1(BigDecimal tax1) {
        this.tax1 = tax1;
    }

    public BigDecimal getTax2() {
        return tax2;
    }

    public void setTax2(BigDecimal tax2) {
        this.tax2 = tax2;
    }

    public BigDecimal getTax3() {
        return tax3;
    }

    public void setTax3(BigDecimal tax3) {
        this.tax3 = tax3;
    }

    public BigDecimal getTax4() {
        return tax4;
    }

    public void setTax4(BigDecimal tax4) {
        this.tax4 = tax4;
    }
    
    
}
