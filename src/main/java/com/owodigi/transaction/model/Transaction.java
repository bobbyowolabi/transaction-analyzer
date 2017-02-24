package com.owodigi.transaction.model;

import java.math.BigDecimal;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a user's electronic financial transactions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonProperty("amount")
    public BigDecimal amount() {
        return amount;
    }

    public boolean isPending() {
        return isPending;
    }

    @JsonProperty("aggregation-time")
    public long aggregationTime() {
        return aggregationTime;
    }

    @JsonProperty("account-id")
    public String accountId() {
        return accountId;
    }

    @JsonProperty("clear-date")
    public long clearDate() {
        return clearDate;
    }

    @JsonProperty("transaction-id")
    public long transactionId() {
        return transactionId;
    }

    @JsonProperty("raw-merchant")
    public String rawMerchant() {
        return rawMerchant;
    }

    @JsonProperty("categorization")
    public String categorization() {
        return categorization;
    }

    @JsonProperty("merchant")
    public String merchant() {
        return merchant;
    }

    @JsonProperty("transaction-time")
    public String transactionTime() {
        return transactionTime;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @JsonProperty("is-pending")
    public void setIsPending(boolean isPending) {
        this.isPending = isPending;
    }

    @JsonProperty("aggregation-time")
    public void setAggregationTime(long aggregationTime) {
        this.aggregationTime = aggregationTime;
    }

    @JsonProperty("account-id")
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @JsonProperty("clear-date")
    public void setClearDate(long clearDate) {
        this.clearDate = clearDate;
    }

    @JsonProperty("transaction-id")
    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("raw-merchant")
    public void setRawMerchant(String rawMerchant) {
        this.rawMerchant = rawMerchant;
    }

    public void setCategorization(String categorization) {
        this.categorization = categorization;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    @JsonProperty("transaction-time")
    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }
}
