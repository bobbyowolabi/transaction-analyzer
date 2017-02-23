package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class TransactionUtils {
    private static final String TRANSACTION_TIME_FORMAT = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 
     * @param transaction
     * @return 
     */
    public static LocalDateTime transactionTime(final Transaction transaction) {
        return LocalDateTime.parse(transaction.transactionTime(), DateTimeFormatter.ofPattern(TRANSACTION_TIME_FORMAT));
    }
}
