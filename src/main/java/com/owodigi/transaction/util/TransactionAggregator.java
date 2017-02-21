package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class that combines the amounts of Transaction to produce reports
 * of a user's financial transaction history.
 */
public class TransactionAggregator {
    private static final String TRANSACTION_TIME_FORMAT = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Aggregates all of the given Transactions and returns one TransactionReport
     * for every month-year represented in the data.
     * 
     * The key in the resulting map should be regarded as an identifier for the 
     * TransactionReport; but it may be helpful to know that it is of the format
     * {@literal <month>-<year>}.
     * 
     * @param transactions the transactions whose amounts should be aggregated 
     * @return 
     */
    public static Map<String, TransactionReport> sum(List<Transaction> transactions) {
        final Map<String, TransactionReport> reports = new HashMap<>();
        for (final Transaction transaction : transactions) {
            final String identifier = identifier(transaction);
            TransactionReport report = reports.get(identifier);
            if (report == null) {
                report = new TransactionReport();
                reports.put(identifier, report);
            }
            if (isExpense(transaction)) {
                report.setSpent(report.spent().add(transaction.amount().abs()));
            } else {
                report.setIncome(report.income().add(transaction.amount()));
            }
        }
        return reports;
    }
    
    private static String identifier(final Transaction transaction) {
        final LocalDateTime date = LocalDateTime.parse(transaction.transactionTime(), DateTimeFormatter.ofPattern(TRANSACTION_TIME_FORMAT));
        return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
    }
    
    private static boolean isExpense(final Transaction transaction) {
        return transaction.amount().signum() == -1;
    }
    
    /**
     * 
     * @param transactions
     * @return 
     */
    public static Map<String, TransactionReport> average(List<Transaction> transactions) {
        return Collections.emptyMap();
    }
}
 