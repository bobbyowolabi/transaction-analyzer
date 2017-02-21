package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.math.BigDecimal;
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
     * @return TransactionReport for every month-year represented in the data
     * @throws NullPointerException if given Transaction List is null
     */
    public static Map<String, TransactionReport> sum(List<Transaction> transactions) throws NullPointerException {
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
     * Aggregates all of the given Transactions and returns one TransactionReport
     * representing the average for all the data represented in the data.
     * 
     * The key in the resulting map should be regarded as an identifier for the 
     * TransactionReport; but it may be helpful to know that it is the value 
     * "average".
     * 
     * @param transactions the transactions whose amounts should be aggregated 
     * @return TransactionReport representing the average for the given data.
     * @throws NullPointerException if given Transaction List is null
     */
    public static Map<String, TransactionReport> average(List<Transaction> transactions) throws NullPointerException {
        final TransactionReport average = new TransactionReport();
        final Map<String, TransactionReport> sum = sum(transactions);
        sum.forEach((id, transaction) -> {
            average.setSpent(average.spent().add(transaction.spent().abs()));
            average.setIncome(average.income().add(transaction.income()));
        });
        if (sum.isEmpty() == false) {
            average.setSpent(average.spent().divide(BigDecimal.valueOf(sum.size())));
            average.setIncome(average.income().divide(BigDecimal.valueOf(sum.size())));
            return Collections.singletonMap("average", average);
        }
        return Collections.emptyMap();
    }
}
 