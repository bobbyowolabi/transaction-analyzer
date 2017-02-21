package com.owodigi.transaction.endpoint;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionEndpointResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
    
/**
 *
 */
public class MockTransactionEndpoint implements TransactionEndpoint {
    private final Path allTransactions;

    public MockTransactionEndpoint(final Path allTransactions) {
        this.allTransactions = allTransactions;
    }
    
    @Override
    public List<Transaction> getAllTransactions() throws IllegalArgumentException {
        try {
            final TransactionEndpointResult result = new ObjectMapper().readValue(Files.newInputStream(allTransactions), TransactionEndpointResult.class);
            return result.getTransactions();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public List<Transaction> getProjectedTransactionsForMonth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
