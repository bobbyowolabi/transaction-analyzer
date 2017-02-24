package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.junit.Assert;

/**
 * Useful Utility methods for verification of Transaction related Objects
 */
public class AssertTransaction {

    /**
     * Asserts that two Transaction objects are equal. If they are not, an 
     * AssertionError is thrown. It is assumed that actual and expected are not
     * null.
     * 
     * @param message the identifying message for the AssertionError
     * @param expected expected value
     * @param actual the value to check against expected
     * @throws AssertionError if expected and actual are not equal
     */
    public static void assertEquals(final String message, final Collection<Transaction> expected, final Collection<Transaction> actual) {
        Assert.assertEquals("Transaction List Size", expected.size(), actual.size());
        final Iterator<Transaction> expectedIterator = expected.iterator(), actualIterator = actual.iterator();
        while (expectedIterator.hasNext()) {
            assertEquals(message, expectedIterator.next(), actualIterator.next());
        }
    }
    
    /**
     * Asserts that two Transaction objects are equal. If they are not, an 
     * AssertionError is thrown. If expected and actual are 
     * null, they are considered equal.
     * 
     * @param message the identifying message for the AssertionError
     * @param expected expected value
     * @param actual the value to check against expected
     * @throws AssertionError if expected and actual are not equal
     */
    public static void assertEquals(final String message, final Transaction expected, final Transaction actual) {
        if (expected == null) {
            Assert.assertNull(message, actual);
            return;
        }
        Assert.assertNotNull(message + ": should not be null", actual);
        Assert.assertEquals(message + ": accountId", expected.accountId(), actual.accountId());
        Assert.assertEquals(message + ": aggregationTime", expected.aggregationTime(), actual.aggregationTime());
        Assert.assertEquals(message + ": amount", expected.amount(), actual.amount());
        Assert.assertEquals(message + ": categorization", expected.categorization(), actual.categorization());
        Assert.assertEquals(message + ": clearDate", expected.clearDate(), actual.clearDate());
        Assert.assertEquals(message + ": isPending", expected.isPending(), actual.isPending());
        Assert.assertEquals(message + ": merchant", expected.merchant(), actual.merchant());
        Assert.assertEquals(message + ": rawMerchant", expected.rawMerchant(), actual.rawMerchant());
        Assert.assertEquals(message + ": transactionId", expected.transactionId(), actual.transactionId());
        Assert.assertEquals(message + ": transactionTime", expected.transactionTime(), actual.transactionTime());
    }
    
    /**
     * Asserts that two Map<String, TransactionReport> objects are equal. If 
     * they are not, an AssertionError is thrown. It is assumed that expected
     * and actual are not null.
     * 
     * @param message the identifying message for the AssertionError
     * @param expected expected value
     * @param actual the value to check against expected
     * @throws AssertionError if expected and actual are not equal
     */
    public static void assertEquals(final String message, final Map<String, TransactionReport> expected, final Map<String, TransactionReport> actual) throws AssertionError {
        Assert.assertEquals("TransactionReport Map Size", expected.size(), actual.size());
        expected.entrySet().stream().forEach(entry -> {
            assertEquals(message + ": " + entry.getKey(), entry.getValue(), actual.get(entry.getKey()));
        });
    }

    /**
     * Asserts that two TransactionReport objects are equal. If 
     * they are not, an AssertionError is thrown. If expected and actual are 
     * null, they are considered equal.
     * 
     * @param message the identifying message for the AssertionError
     * @param expected expected value
     * @param actual the value to check against expected
     * @throws AssertionError if expected and actual are not equal 
     */
    private static void assertEquals(final String message, final TransactionReport expected, final TransactionReport actual) throws AssertionError {
        if (expected == null) {
            Assert.assertNull(message, actual);
            return;
        }
        Assert.assertNotNull(message + ": should not be null", actual);
        Assert.assertEquals(message + ": income", expected.income(), actual.income());
        Assert.assertEquals(message + ": spent", expected.spent(), actual.spent());
    }
}
