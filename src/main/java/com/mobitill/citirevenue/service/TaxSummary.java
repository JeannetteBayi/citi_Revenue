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
public class TaxSummary {
    
    private int merchants;
    private BigDecimal tax1;
    private BigDecimal tax2;
    private BigDecimal tax3;
    private BigDecimal tax4;

    public TaxSummary(BigDecimal tax1, BigDecimal tax2, BigDecimal tax3, BigDecimal tax4) {
        this.tax1 = (tax1 == null ? BigDecimal.ZERO : tax1);
        this.tax2 = (tax2 == null ? BigDecimal.ZERO : tax2);
        this.tax3 = (tax3 == null ? BigDecimal.ZERO : tax3);
        this.tax4 = (tax4 == null ? BigDecimal.ZERO : tax4);
    }

    public int getMerchants() {
        return merchants;
    }

    public void setMerchants(int merchants) {
        this.merchants = merchants;
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
