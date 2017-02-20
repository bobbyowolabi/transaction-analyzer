package com.owodigi.transaction.model;

import java.math.BigDecimal;

/**
 * Represents an overview of a user's financial history
 */
public class TransactionResult {
    private BigDecimal spent;
    private BigDecimal income;
    private int year;
    private int month;

    public BigDecimal getSpent() {
        return spent;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
