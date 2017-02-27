package com.owodigi.transaction.endpoint;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.GetAllTransactionResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
    
/**
 * Testing class used to mock the production TransactionEndpoint by loading
 * its responses from JSON files on disk.
 */
public class FileBasedTransactionEndpoint implements TransactionEndpoint {
    private final Path allTransactions;

    public FileBasedTransactionEndpoint(final Path allTransactions) {
        this.allTransactions = allTransactions;
    }
    
    @Override
    public List<Transaction> getAllTransactions() throws IllegalArgumentException {
        try {
            final GetAllTransactionResponse result = new ObjectMapper().readValue(Files.newInputStream(allTransactions), GetAllTransactionResponse.class);
            return result.getTransactions();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public List<Transaction> getProjectedTransactionsForMonth() {
        throw new UnsupportedOperationException("getProjectedTransactionsForMonth is not supported yet.");
    }
}
