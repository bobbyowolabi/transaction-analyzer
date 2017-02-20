package com.owodigi.transaction.endpoint;

import com.owodigi.transaction.model.Transaction;
import java.util.List;

/**
 *
 */
public class MockTransactionEndpoint implements TransactionEndpoint {

    @Override
    public List<Transaction> getAllTransactions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Transaction> getProjectedTransactionsForMonth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
