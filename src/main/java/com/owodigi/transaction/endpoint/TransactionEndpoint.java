package com.owodigi.transaction.endpoint;

import com.owodigi.transaction.model.Transaction;
import java.io.IOException;
import java.util.List;

/**
 * API used to interact with a user's transactions
 */
public interface TransactionEndpoint {
    public static final String API_TOKEN = "AppTokenForInterview";
    
    /**
     * Retrieves all of the available transaction history for a user
     * 
     * @return all of a user's transactions
     * @throws java.io.IOException
     */
    public List<Transaction> getAllTransactions() throws IOException;
    
    /**
     * Attempts to predict what transactions haven't occurred yet, but probably 
     * will occur for the user during the given month. If the month is in the 
     * past, returns nothing. If the month is in the future, returns every 
     * transaction we expect that month. If it's this month, returns every 
     * transaction we expect to see before the end of the month (potentially 
     * including some transactions that will occur later today).
     * 
     * @return projected Transactions for the month
     */
    public List<Transaction> getProjectedTransactionsForMonth();
}
