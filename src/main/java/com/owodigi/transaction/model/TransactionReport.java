package com.owodigi.transaction.model;

import java.math.BigDecimal;

/**
 * Represents an overview of a user's financial history
 */
public class TransactionReport {
    private BigDecimal spent;
    private BigDecimal income;
    
    public TransactionReport() {
        this.spent = new BigDecimal(0);
        this.income = new BigDecimal(0);
    }

    public TransactionReport(final BigDecimal spent, final BigDecimal income) {
        this.spent = spent;
        this.income = income;
    }
    
    public BigDecimal spent() {
        return spent;
    }

    public BigDecimal income() {
        return income;
    }

    public void setSpent(final BigDecimal spent) {
        this.spent = spent;
    }

    public void setIncome(final BigDecimal income) {
        this.income = income;
    }
}
