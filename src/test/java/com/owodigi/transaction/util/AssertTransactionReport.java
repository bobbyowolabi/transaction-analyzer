package com.owodigi.transaction.util;

import com.owodigi.transaction.model.TransactionReport;
import java.util.Map;
import org.junit.Assert;

/**
 * Useful Utility methods for verification of TransactioReport Objects
 */
public class AssertTransactionReport {

    /**
     * Asserts that two Map<String, TransactionReport> objects are equal. If 
     * they are not, an AssertionError is thrown. It is assumed that expected
     * and actual are not null.
     * 
     * @param message the identifying message for the AssertionError (null okay)
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
     * @param message the identifying message for the AssertionError (null okay)
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
