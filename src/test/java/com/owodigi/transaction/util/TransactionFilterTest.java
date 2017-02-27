package com.owodigi.transaction.util;

import com.owodigi.transaction.endpoint.FileBasedTransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TransactionFilterTest {
    private static final Path DONUTS_PATH = Paths.get("src/test/resources/donuts.json");
    private static final Path NO_DONUTS_PATH = Paths.get("src/test/resources/no-donuts.json");
    private static final Path CREDIT_CARD_PAYMENTS_PATH = Paths.get("src/test/resources/credit-card-payments.json");
    private static final Path CREDIT_CARD_PAYMENTS_DETECTED_PATH = Paths.get("src/test/resources/credit-card-payment-detected.json");
    private static final Path NO_CREDIT_CARD_PAYMENTS_PATH = Paths.get("src/test/resources/no-credit-card-payments.json");    
    private static final Path ORPHANED_DEBIT_CC_PAYMENT = Paths.get("src/test/resources/orphaned-debit-cc-payment.json");
    private static final Path ORPHANED_DEBIT_CC_PAYMENT_CC_PAYMENT_REMOVED = Paths.get("src/test/resources/orphaned-debit-cc-payment-cc-payments-removed.json");
    private static final Path ORPHANED_DEBIT_CC_PAYMENTS_DETECTED = Paths.get("src/test/resources/orphaned-debit-cc-payment-detected.json");
    private static final Path ORPHANED_CREDIT_CC_PAYMENT = Paths.get("src/test/resources/orphaned-credit-cc-payment.json");
    private static final Path ORPHANED_CREDIT_CC_PAYMENT_DETECTED = Paths.get("src/test/resources/orphaned-credit-cc-payment-detected.json");
    private static final Path ORPHANED_CREDIT_CC_PAYMENT_CC_PAYMENT_REMOVED = Paths.get("src/test/resources/orphaned-credit-cc-payment-cc-payments-removed.json");
    private static final Path ZERO_AMOUNT_TRANSACTION = Paths.get("src/test/resources/zero-amount-transaction.json");
    private static final Path ZERO_AMOUNT_TRANSACTION_CC_PAYMENT_REMOVED = Paths.get("src/test/resources/zero-amount-transaction-cc-payments-removed.json");
    
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
        final List<Transaction> expectedCreditCardPayments = new FileBasedTransactionEndpoint(CREDIT_CARD_PAYMENTS_DETECTED_PATH).getAllTransactions();
        List<Transaction> actual = new FileBasedTransactionEndpoint(CREDIT_CARD_PAYMENTS_PATH).getAllTransactions();
        final List<Transaction> acutalCreditCardPayments = new ArrayList<>();
        actual = TransactionFilter.creditCardTransactions(actual, acutalCreditCardPayments);
        AssertTransaction.assertEquals("Assert Credit Card Payments Were Removed", expected, actual);
        AssertTransaction.assertEquals("Credit Card Payments Detected", expectedCreditCardPayments, acutalCreditCardPayments);
    }
    
    @Test
    public void testFilterCreditCardPaymentsOrphanedDebitTransaction() {
        final List<Transaction> expected = new FileBasedTransactionEndpoint(ORPHANED_DEBIT_CC_PAYMENT_CC_PAYMENT_REMOVED).getAllTransactions();
        final List<Transaction> expectedCreditCardPayments = new FileBasedTransactionEndpoint(ORPHANED_DEBIT_CC_PAYMENTS_DETECTED).getAllTransactions();
        List<Transaction> actual = new FileBasedTransactionEndpoint(ORPHANED_DEBIT_CC_PAYMENT).getAllTransactions();
        final List<Transaction> acutalCreditCardPayments = new ArrayList<>();
        actual = TransactionFilter.creditCardTransactions(actual, acutalCreditCardPayments);
        AssertTransaction.assertEquals("Assert Credit Card Payments Removed", expected, actual);
        AssertTransaction.assertEquals("Credit Card Payments Detected", expectedCreditCardPayments, acutalCreditCardPayments);
    }
    
    @Test
    public void testFilterCreditCardPaymentsOrphanedCreditTransaction() {
        final List<Transaction> expected = new FileBasedTransactionEndpoint(ORPHANED_CREDIT_CC_PAYMENT_CC_PAYMENT_REMOVED).getAllTransactions();
        final List<Transaction> expectedCreditCardPayments = new FileBasedTransactionEndpoint(ORPHANED_CREDIT_CC_PAYMENT_DETECTED).getAllTransactions();
        List<Transaction> actual = new FileBasedTransactionEndpoint(ORPHANED_CREDIT_CC_PAYMENT).getAllTransactions();
        final List<Transaction> acutalCreditCardPayments = new ArrayList<>();
        actual = TransactionFilter.creditCardTransactions(actual, acutalCreditCardPayments);
        AssertTransaction.assertEquals("Assert Credit Card Payments Removed", expected, actual);
        AssertTransaction.assertEquals("Credit Card Payments Detected", expectedCreditCardPayments, acutalCreditCardPayments);
    }
    
    @Test
    public void testZeroAmountTransactionsWithin24HoursofEachOther() {
        final List<Transaction> expected = new FileBasedTransactionEndpoint(ZERO_AMOUNT_TRANSACTION_CC_PAYMENT_REMOVED).getAllTransactions();
        final List<Transaction> expectedCreditCardPayments = new FileBasedTransactionEndpoint(CREDIT_CARD_PAYMENTS_DETECTED_PATH).getAllTransactions();
        List<Transaction> actual = new FileBasedTransactionEndpoint(ZERO_AMOUNT_TRANSACTION).getAllTransactions();
        final List<Transaction> acutalCreditCardPayments = new ArrayList<>();
        actual = TransactionFilter.creditCardTransactions(actual, acutalCreditCardPayments);
        AssertTransaction.assertEquals("Assert Credit Card Payments Removed", expected, actual);
        AssertTransaction.assertEquals("Credit Card Payments Detected", expectedCreditCardPayments, acutalCreditCardPayments);
    }
}
