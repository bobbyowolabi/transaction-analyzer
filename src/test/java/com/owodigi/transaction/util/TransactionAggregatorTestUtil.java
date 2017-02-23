package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility providing verification logic for the functionality of the TransactionAggregator
 */
public class TransactionAggregatorTestUtil {

    /**
     * Verification method for TransactionAggregator that aggregates all of the 
     * given Transactions and returns one TransactionReport for every month-year
     * represented in the data.
     * 
     * @param transactions the transactions whose amounts should be aggregated
     * @return TransactionReport for every month-year represented in the data
     */
    public static final Map<String, TransactionReport> expectedMonthlyTotals(final List<Transaction> transactions) {
        final Map<String, TransactionReport> expected = new HashMap<>();
        for (final Transaction transaction : transactions) {
            final String id = transaction.transactionTime().substring(0, 7);
            final TransactionReport report = expected.getOrDefault(id, new TransactionReport(BigDecimal.ZERO.setScale(2), BigDecimal.ZERO.setScale(2)));
            expected.putIfAbsent(id, report);
            if (transaction.amount().signum() == -1) {
                report.setSpent(report.spent().add(transaction.amount().abs()));
            } else {
                report.setIncome(report.income().add(transaction.amount()));
            }
        }
        return expected;
    }

    /**
     * Verification method for TransactionAggregator that Aggregates all of the 
     * given Transactions and returns one TransactionReport representing the 
     * average for all the data represented in the data.
     * 
     * @param transactions the transactions whose amounts should be aggregated 
     * @return TransactionReport representing the average for the given data.
     */
    public static Map<String, TransactionReport> expectedAverage(final List<Transaction> transactions) {
        BigDecimal spentTotal = new BigDecimal(0), incomeTotal = new BigDecimal(0);
        int spentTransactionCount = 0, incomeTransactionCount = 0;
        for (final Transaction transaction : transactions) {
            if (transaction.amount().signum() == -1) {
                spentTotal = spentTotal.add(transaction.amount().abs());
                ++spentTransactionCount;
            } else {
                incomeTotal = incomeTotal.add(transaction.amount());
                ++incomeTransactionCount;
            }
        }
        final BigDecimal averageSpent = spentTotal.divide(BigDecimal.valueOf(spentTransactionCount), 2, RoundingMode.HALF_UP);
        final BigDecimal averageIncome = incomeTotal.divide(BigDecimal.valueOf(incomeTransactionCount), 2, RoundingMode.HALF_UP);
        final TransactionReport average = new TransactionReport(averageSpent, averageIncome);
        return Collections.singletonMap("average", average);
    }
}
