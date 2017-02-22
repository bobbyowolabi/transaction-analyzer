package com.owodigi.transaction.util;

import com.owodigi.transaction.endpoint.EmptyTransactionEndpoint;
import com.owodigi.transaction.endpoint.MockTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private static final Path ALL_TRANSACTIONS_FULL = Paths.get("src/test/resources/get-all-transactions-full.json");
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
    public void testMonthlyTotalAllExpenses() {
        endpoint = new MockTransactionEndpoint(ALL_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(new BigDecimal("352300.00"), new BigDecimal("0.00")));
            put("2017-02", new TransactionReport(new BigDecimal("1000000.00"), new BigDecimal("0.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testAverageAllExpenses() {
        endpoint = new MockTransactionEndpoint(ALL_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("average", new TransactionReport(new BigDecimal("270460.00"), new BigDecimal("0.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testMonthlyTotalIncome() {
        endpoint = new MockTransactionEndpoint(ALL_INCOME_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(new BigDecimal("0.00"), new BigDecimal("352300.00")));
            put("2017-02", new TransactionReport(new BigDecimal("0.00"), new BigDecimal("1000000.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testAverageAllIncome() {
        endpoint = new MockTransactionEndpoint(ALL_INCOME_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("average", new TransactionReport(new BigDecimal("0.00"), new BigDecimal("270460.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        assertEquals("Transaction Report", expected, actual);        
    }
    
    @Test
    public void testMonthlyTotalIncomeAndExpense() {
        endpoint = new MockTransactionEndpoint(INCOME_AND_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(new BigDecimal("30200.00"), new BigDecimal("133300.00")));
            put("2017-02", new TransactionReport(new BigDecimal("188800.00"), new BigDecimal("1000000.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        assertEquals("Transaction Report", expected, actual);
    }

    @Test
    public void testAverageIncomeAndExpense() {
        endpoint = new MockTransactionEndpoint(INCOME_AND_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("average", new TransactionReport(new BigDecimal("109500.00"), new BigDecimal("377766.67")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testMonthlyTotalEmptyTransactions() {
        endpoint = new EmptyTransactionEndpoint();
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = Collections.emptyMap();
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        Assert.assertEquals("sum of empty transactions", expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testMonthlyTotalNullTransactions() {
        TransactionAggregator.monthlyTotals(null);
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

    @Test
    public void testMonthlyTotalRealisticData() {
        endpoint = new MockTransactionEndpoint(ALL_TRANSACTIONS_FULL);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<>();
        for (final Transaction transaction : transactions) {
            final String id = transaction.transactionTime().substring(0, 7);
            final TransactionReport report = expected.getOrDefault(id, new TransactionReport(BigDecimal.ZERO.setScale(2), BigDecimal.ZERO.setScale(2)));
            expected.putIfAbsent(id, report);
            if (transaction.amount().signum() == -1) {
                report.setSpent(report.spent().add(transaction.amount().abs()));
            } else {
                report.setIncome(report.income().add(transaction.amount()));
            }
        }
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
    
   @Test
    public void testAverageRealisticData() {
        endpoint = new MockTransactionEndpoint(ALL_TRANSACTIONS_FULL);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        BigDecimal spentTotal = new BigDecimal(0), incomeTotal = new BigDecimal(0);
        int spentTransactionCount = 0, incomeTransactionCount = 0;
        for (final Transaction transaction : transactions) {
            if (transaction.amount().signum() == -1) {
                spentTotal = spentTotal.add(transaction.amount().abs());
                ++spentTransactionCount;
            } else {
                incomeTotal = incomeTotal.add(transaction.amount());
                ++incomeTransactionCount;
            }
        }
        final BigDecimal averageSpent = spentTotal.divide(BigDecimal.valueOf(spentTransactionCount), 2, RoundingMode.HALF_UP);
        final BigDecimal averageIncome = incomeTotal.divide(BigDecimal.valueOf(incomeTransactionCount), 2, RoundingMode.HALF_UP);
        final TransactionReport average = new TransactionReport(averageSpent, averageIncome);
        final Map<String, TransactionReport> expected = Collections.singletonMap("average", average);
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        assertEquals("Transaction Report", expected, actual);
    }
}
