package com.owodigi.transaction.util;

import com.owodigi.transaction.endpoint.EmptyTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionResult;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TransactionAggregatorTest {
    private TransactionEndpoint endpoint;
    
    @Test
    public void testSumEmptyTransactions() {
        endpoint = new EmptyTransactionEndpoint();
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final List<TransactionResult> expected = Collections.emptyList();
        final List<TransactionResult> actual = TransactionAggregator.sum(transactions);
        Assert.assertEquals("sum of empty transactions", expected, actual);
    }
}
