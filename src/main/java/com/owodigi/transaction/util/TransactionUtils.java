package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility operations for the Transaction Class
 */
public class TransactionUtils {
    private static final String TRANSACTION_TIME_FORMAT = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Converts the transactionTime field of a Transaction to a LocalDateTime 
     * object.
     * 
     * @param transaction transaction whose transactionTime field is to be converted
     * @return the transactionTime field of the Transaction object as a LocalDateTime object.
     */
    public static LocalDateTime transactionTime(final Transaction transaction) {
        return LocalDateTime.parse(transaction.transactionTime(), DateTimeFormatter.ofPattern(TRANSACTION_TIME_FORMAT));
    }
}
