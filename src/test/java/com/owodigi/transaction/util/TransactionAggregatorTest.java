package com.owodigi.transaction.util;

import com.owodigi.transaction.endpoint.EmptyTransactionEndpoint;
import com.owodigi.transaction.endpoint.MockTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class TransactionAggregatorTest {
    private TransactionEndpoint endpoint;
    private static final Path ALL_EXPENSES_PATH = Paths.get("src/test/resources/all-expenses.json");
    private static final Path ALL_INCOME_PATH = Paths.get("src/test/resources/all-income.json");
    private static final Path INCOME_AND_EXPENSES_PATH = Paths.get("src/test/resources/income-and-expenses.json");
    
    private void assertEquals(final String message, final Map<String, TransactionReport> expected, final Map<String, TransactionReport> actual) {
        Assert.assertEquals("TransactionReport Map Size", expected.size(), actual.size());
        expected.entrySet().stream().forEach(entry -> {
            assertEquals(message + ": " + entry.getKey(), entry.getValue(), actual.get(entry.getKey()));
        });
    }
    
    private void assertEquals(final String message, final TransactionReport expected, final TransactionReport actual) {
        if (expected == null) {
            Assert.assertNull(message, actual);
            return;
        }
        Assert.assertNotNull(message + ": should not be null" , actual);
        Assert.assertEquals(message + ": income", expected.income(), actual.income());
        Assert.assertEquals(message + ": spent", expected.spent(), actual.spent());
    }
    
    @Test
    public void testSumAllExpenses() {
        endpoint = new MockTransactionEndpoint(ALL_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(BigDecimal.valueOf(352300L), BigDecimal.ZERO));
            put("2017-02", new TransactionReport(BigDecimal.valueOf(1000000L), BigDecimal.ZERO));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.sum(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testSumAllIncome() {
        endpoint = new MockTransactionEndpoint(ALL_INCOME_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(BigDecimal.ZERO, BigDecimal.valueOf(352300L)));
            put("2017-02", new TransactionReport(BigDecimal.ZERO, BigDecimal.valueOf(1000000L)));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.sum(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testIncomeAndExpense() {
        endpoint = new MockTransactionEndpoint(INCOME_AND_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(BigDecimal.valueOf(30200L), BigDecimal.valueOf(133300L)));
            put("2017-02", new TransactionReport(BigDecimal.valueOf(188800L), BigDecimal.valueOf(1000000L)));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.sum(transactions);
        assertEquals("Transaction Report", expected, actual);
    }

    @Test
    public void testSumEmptyTransactions() {
        endpoint = new EmptyTransactionEndpoint();
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = Collections.emptyMap();
        final Map<String, TransactionReport> actual = TransactionAggregator.sum(transactions);
        Assert.assertEquals("sum of empty transactions", expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSumNullTransactions() {
        TransactionAggregator.sum(null);
    }

    @Test
    public void testAverageEmptyTransactions() {
        endpoint = new EmptyTransactionEndpoint();
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = Collections.emptyMap();
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        Assert.assertEquals("sum of empty transactions", expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testAverageNullTransactions() {
        TransactionAggregator.average(null);
    }
}
