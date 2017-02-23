package com.owodigi.transaction.endpoint;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import com.owodigi.transaction.util.AssertTransaction;
import com.owodigi.transaction.util.TransactionAggregator;
import com.owodigi.transaction.util.TransactionAggregatorTestUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Hits live system; uses non-source controlled credential file")
public class RestTransactionEndpointTest {
    private static TransactionEndpoint endpoint;

    @BeforeClass
    public static void setup() throws IOException {
        final Properties properties = new Properties();
        properties.load(Files.newBufferedReader(Paths.get("src/test/resources/credentials")));
        endpoint = new RestTransactionEndpoint(properties.getProperty("username"), properties.getProperty("password"));
    }
    
    @Test
    public void testMonthlyTotal() throws IOException {
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = TransactionAggregatorTestUtil.expectedMonthlyTotals(transactions);
        final Map<String, TransactionReport> actual = TransactionAggregator.monthlyTotals(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }

   @Test
    public void testAverage() throws IOException {
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> expected = TransactionAggregatorTestUtil.expectedAverage(transactions);
        final Map<String, TransactionReport> actual = TransactionAggregator.average(transactions);
        AssertTransaction.assertEquals("Transaction Report", expected, actual);
    }
}
