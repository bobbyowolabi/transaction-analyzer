package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
     * All of the values represent in the TransactionReport are expanded in the
     * hundredths place.
     * 
     * The key in the resulting map should be regarded as an identifier for the 
     * TransactionReport; but it may be helpful to know that it is of the format
     * {@literal <month>-<year>}.
     * 
     * @param transactions the transactions whose amounts should be aggregated 
     * @return TransactionReport for every month-year represented in the data
     * @throws NullPointerException if given Transaction List is null
     */
    public static Map<String, TransactionReport> monthlyTotals(List<Transaction> transactions) throws NullPointerException {
        final Map<String, TransactionReport> reports = new HashMap<>();
        for (final Transaction transaction : transactions) {
            final String identifier = identifier(transaction);
            TransactionReport report = reports.get(identifier);
            if (report == null) {
                report = new TransactionReport(BigDecimal.ZERO.setScale(2), BigDecimal.ZERO.setScale(2));
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
    
    /**
     * Generates and returns the identifier of the given transaction.
     * 
     * The format of the identifier is of the format {@literal <month>-<year>}.
     * 
     * @param transaction whose identifier is to be produced
     * @return identifier of the given transaction
     */
    private static String identifier(final Transaction transaction) {
        final LocalDateTime date = LocalDateTime.parse(transaction.transactionTime(), DateTimeFormatter.ofPattern(TRANSACTION_TIME_FORMAT));
        return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
    }
    
    /**
     * Returns true if the given Transaction is an expense; otherwise returns 
     * false. In the case the transaction is not an expense, it is safe to 
     * assume it is income.
     * 
     * @param transaction Transaction whose expense status is to be determined.
     * @return true if the given Transaction is an expense; otherwise returns 
     * false
     */
    private static boolean isExpense(final Transaction transaction) {
        return transaction.amount().signum() == -1;
    }
    
    /**
     * Returns a BigDecimal whose value is (a / b). The result is 
     * rounded up when the fraction is >= 0.50; otherwise it is rounded down.
     * 
     * If b
     * 
     * @param a dividend in the division operation
     * @param b divisor in the division operation
     * @return the quotient, that is a/b
     */
    private static BigDecimal divide(final BigDecimal a, final int b) {
        return a.divide(BigDecimal.valueOf(b), RoundingMode.HALF_UP);
    }
    
    /**
     * Aggregates all of the given Transactions and returns one TransactionReport
     * representing the average for all the data represented in the data.
     * 
     * All of the values represented in the TransactionReport are expanded to the
     * hundredths place and rounded up when the fraction is >= 0.50; otherwise, 
     * they're rounded down.
     * 
     * The key in the resulting map should be regarded as an identifier for the 
     * TransactionReport; but it may be helpful to know that it is the value 
     * "average".
     * 
     * @param transactions the transactions whose amounts should be aggregated 
     * @return TransactionReport representing the average for the given data.
     * @throws NullPointerException if given Transaction List is null
     */
    public static Map<String, TransactionReport> average(final List<Transaction> transactions) throws NullPointerException {
        if (transactions.isEmpty()) {
            return Collections.emptyMap();
        }
        final TransactionReport report = new TransactionReport(BigDecimal.ZERO.setScale(2), BigDecimal.ZERO.setScale(2));
        final AtomicInteger spentCount = new AtomicInteger(), incomeCount = new AtomicInteger();
        transactions.stream().map(Transaction::amount).forEach(amount -> {
            if (amount.signum() == -1) {
                report.setSpent(report.spent().add(amount.abs()));
                spentCount.incrementAndGet();
            } else {
                report.setIncome(report.income().add(amount));
                incomeCount.incrementAndGet();
            }
        });
        if (spentCount.get() > 0) {
            report.setSpent(divide(report.spent(), spentCount.get()));
        }
        if (incomeCount.get() > 0) {
            report.setIncome(divide(report.income(), incomeCount.get()));
        }
        return Collections.singletonMap("average", report);
    }
}
 