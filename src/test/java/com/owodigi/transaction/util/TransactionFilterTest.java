package com.owodigi.transaction.util;

import com.owodigi.transaction.endpoint.FileBasedTransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TransactionFilterTest {
    private static final Path DONUTS_PATH = Paths.get("src/test/resources/donuts.json");
    private static final Path NO_DONUTS_PATH = Paths.get("src/test/resources/no-donuts.json");
    private static final Path CREDIT_CARD_PAYMENTS_PATH = Paths.get("src/test/resources/credit-card-payments.json");
    private static final Path NO_CREDIT_CARD_PAYMENTS_PATH = Paths.get("src/test/resources/no-credit-card-payments.json");    
    
    @Test
    public void testFilterDonuts() throws IOException {
        final List<Transaction> expected = new FileBasedTransactionEndpoint(NO_DONUTS_PATH).getAllTransactions();
        List<Transaction> actual = new FileBasedTransactionEndpoint(DONUTS_PATH).getAllTransactions();
        Assert.assertTrue("Assert There exists Donut Transactions", actual.stream()
                .anyMatch(transaction -> transaction.merchant().contains("Donuts") || transaction.merchant().contains("DUNKIN")));
        actual = TransactionFilter.removeMerchants(actual, "Krispy Kreme Donuts", "DUNKIN #336784");
        AssertTransaction.assertEquals("Assert Donuts Transactions Removed", expected, actual);
    }
    
    @Test
    public void testFilterCreditCardPayments() {
        final List<Transaction> expected = new FileBasedTransactionEndpoint(NO_CREDIT_CARD_PAYMENTS_PATH).getAllTransactions();
        List<Transaction> actual = new FileBasedTransactionEndpoint(CREDIT_CARD_PAYMENTS_PATH).getAllTransactions();
        actual = TransactionFilter.creditCardTransactions(actual);
        AssertTransaction.assertEquals("Assert Credit Card Payments Removed", expected, actual);
    }
    
    //test with cc+  cc- cc+
    
    // test with cc+ cc- cc-
    
    // need functionality to retrieve cc payments
}
