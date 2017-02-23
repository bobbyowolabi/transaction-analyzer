package com.owodigi.transaction.util;

import com.owodigi.transaction.endpoint.EmptyResponseTransactionEndpoint;
import com.owodigi.transaction.endpoint.FileBasedTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.io.IOException;
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
    private static final Path ALL_TRANSACTIONS_FULL = Paths.get("src/test/resources/get-all-transactions-full.json");
    private static final Path ALL_EXPENSES_PATH = Paths.get("src/test/resources/all-expenses.json");
    private static final Path ALL_INCOME_PATH = Paths.get("src/test/resources/all-income.json");
    private static final Path INCOME_AND_EXPENSES_PATH = Paths.get("src/test/resources/income-and-expenses.json");
    
    @Test
    public void testMonthlyTotalAllExpenses() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(ALL_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(new BigDecimal("352300.00"), new BigDecimal("0.00")));
            put("2017-02", new TransactionReport(new BigDecimal("1000000.00"), new BigDecimal("0.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testAverageAllExpenses() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(ALL_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("average", new TransactionReport(new BigDecimal("270460.00"), new BigDecimal("0.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testMonthlyTotalIncome() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(ALL_INCOME_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(new BigDecimal("0.00"), new BigDecimal("352300.00")));
            put("2017-02", new TransactionReport(new BigDecimal("0.00"), new BigDecimal("1000000.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testAverageAllIncome() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(ALL_INCOME_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("average", new TransactionReport(new BigDecimal("0.00"), new BigDecimal("270460.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);        
    }
    
    @Test
    public void testMonthlyTotalIncomeAndExpense() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(INCOME_AND_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("2014-10", new TransactionReport(new BigDecimal("30200.00"), new BigDecimal("133300.00")));
            put("2017-02", new TransactionReport(new BigDecimal("188800.00"), new BigDecimal("1000000.00")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }

    @Test
    public void testAverageIncomeAndExpense() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(INCOME_AND_EXPENSES_PATH);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = new HashMap<String, TransactionReport>() {{
            put("average", new TransactionReport(new BigDecimal("109500.00"), new BigDecimal("377766.67")));
        }};
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }
    
    @Test
    public void testMonthlyTotalEmptyTransactions() throws IOException {
        endpoint = new EmptyResponseTransactionEndpoint();
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
    public void testAverageEmptyTransactions() throws IOException {
        endpoint = new EmptyResponseTransactionEndpoint();
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
    public void testMonthlyTotalRealisticData() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(ALL_TRANSACTIONS_FULL);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = TransactionAggregatorTestUtil.expectedMonthlyTotals(transactions);
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }
    
   @Test
    public void testAverageRealisticData() throws IOException {
        endpoint = new FileBasedTransactionEndpoint(ALL_TRANSACTIONS_FULL);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = TransactionAggregatorTestUtil.expectedAverage(transactions);
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }
}
