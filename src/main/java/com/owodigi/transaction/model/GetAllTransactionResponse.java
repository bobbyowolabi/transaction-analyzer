package com.owodigi.transaction.model;

import java.util.List;

/**
 * Represents the Response received from the GetAllTransaction Endpoint
 */
public class GetAllTransactionResponse implements TransactionEndpointResponse {
    private String error;
    private List<Transaction> transactions;

    @Override
    public String error() {
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
