package com.owodigi.transaction.model;

import java.math.BigDecimal;

/**
 * Represents a user's electronic financial transactions
 */
public class Transaction {
    private BigDecimal amount;
    private boolean isPending;
    private long aggregationTime;
    private String accountId;
    private long clearDate;
    private long transactionId;
    private String rawMerchant;
    private String categorization;
    private String merchant;
    private String transactionTime;

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isIsPending() {
        return isPending;
    }

    public long getAggregationTime() {
        return aggregationTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public long getClearDate() {
        return clearDate;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public String getRawMerchant() {
        return rawMerchant;
    }

    public String getCategorization() {
        return categorization;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setIsPending(boolean isPending) {
        this.isPending = isPending;
    }

    public void setAggregationTime(long aggregationTime) {
        this.aggregationTime = aggregationTime;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setClearDate(long clearDate) {
        this.clearDate = clearDate;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public void setRawMerchant(String rawMerchant) {
        this.rawMerchant = rawMerchant;
    }

    public void setCategorization(String categorization) {
        this.categorization = categorization;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }
}
