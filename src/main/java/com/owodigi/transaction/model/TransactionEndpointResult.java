package com.owodigi.transaction.model;

import java.util.List;

/**
 *
 */
public class TransactionEndpointResult {
    private String error;
    private List<Transaction> transactions;

    public String getError() {
        return error;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
