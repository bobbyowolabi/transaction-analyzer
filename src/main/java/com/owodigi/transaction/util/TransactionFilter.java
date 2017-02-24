package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class TransactionFilter {

    /**
     *
     * @param transactions
     * @param merchants
     * @return
     */
    public static List<Transaction> removeMerchants(final List<Transaction> transactions, final String...merchants) {
        final Set<String> merchantSet = new HashSet<>();
        Collections.addAll(merchantSet, merchants);
        return transactions.stream()
            .filter(transaction -> merchantSet.contains(transaction.merchant()) == false)
            .collect(Collectors.toList());
    }

    private static void remove(final Transaction transaction, final Map<BigDecimal, List<Transaction>> amountToTransactions) {
        final List<Transaction> candidates = amountToTransactions.get(transaction.amount());
        if (candidates != null) {
            candidates.remove(transaction);
        }
    }

    private static boolean within24Hours(final Transaction a, final Transaction b) {
        final LocalDateTime aDateTime = TransactionUtils.transactionTime(a);
        final LocalDateTime bDateTime = TransactionUtils.transactionTime(b);
        final long hoursDifference = ChronoUnit.HOURS.between(aDateTime, bDateTime);
        return Math.abs(hoursDifference) <= 24;
    }

    public static List<Transaction> creditCardTransactions(final List<Transaction> transactions) {
        final Map<BigDecimal, List<Transaction>> amountToTransactions = transactions.stream()
            .collect(Collectors.groupingBy(transaction -> transaction.amount()));
        final Set<Transaction> creditCardPayments = new HashSet<>();
        return transactions.stream()
            .filter(transaction -> {
                final List<Transaction> oppositeAmountTransactions = amountToTransactions.get(transaction.amount().negate());
                final boolean creditCardPayment = oppositeAmountTransactions != null && oppositeAmountTransactions.stream().anyMatch(oppositeAmountTransaction -> within24Hours(transaction, oppositeAmountTransaction));
                if (creditCardPayment) {
                    creditCardPayments.add(transaction);
                    return false;
                }
                return true;
            })
            .collect(Collectors.toList());
    }
}
