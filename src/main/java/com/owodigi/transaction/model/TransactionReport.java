package com.owodigi.transaction.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents an overview of a user's financial history
 */
public class TransactionReport {
    private BigDecimal spent;
    private BigDecimal income;
    
    /**
     * Creates a new TransactionReport instance with zero amounts for the spent 
     * and income values.
     */
    public TransactionReport() {
        this.spent = new BigDecimal(BigInteger.ZERO);
        this.income = new BigDecimal(BigInteger.ZERO);
    }

    /**
     * Creates a new TransactionReport Instance with the given spent and income
     * values
     * 
     * @param spent the amount spent for this TransactionReport
     * @param income the amount received as income for this TransactionReport
     */
    public TransactionReport(final BigDecimal spent, final BigDecimal income) {
        this.spent = spent;
        this.income = income;
    }
    
    /**
     * Represents the amount that the user spent for this TransactionReport
     * 
     * @return the amount that the user spent for this TransactionReport
     */
    public BigDecimal spent() {
        return spent;
    }

    /**
     * Represents the amount that the user received as income for this 
     * TransactionReport
     * 
     * @return the amount that the user received as income for this 
     * TransactionReport
     */
    public BigDecimal income() {
        return income;
    }

    /**
     * Sets the amount spent for this TransactionReport.
     * 
     * @param spent the amount spent for this TransactionReport.
     */
    public void setSpent(final BigDecimal spent) {
        this.spent = spent;
    }

    /**
     * Sets the amount received as income for this TransactionReport.
     * 
     * @param income the amount received as income for this TransactionReport
     */
    public void setIncome(final BigDecimal income) {
        this.income = income;
    }
}
