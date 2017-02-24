package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
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

    private static boolean within24Hours(final Transaction a, final Transaction b) {
        final LocalDateTime aDateTime = TransactionUtils.transactionTime(a);
        final LocalDateTime bDateTime = TransactionUtils.transactionTime(b);
        final long hoursDifference = ChronoUnit.HOURS.between(aDateTime, bDateTime);
        return Math.abs(hoursDifference) <= 24;
    }

    public static List<Transaction> creditCardTransactions(final List<Transaction> transactions, final Collection<Transaction> creditCardPayments) {
        final Map<BigDecimal, List<Transaction>> amountToTransactions = transactions.stream()
            .collect(Collectors.groupingBy(transaction -> transaction.amount()));
        return transactions.stream()
            .filter(transaction -> {
                if (creditCardPayments.contains(transaction)) {
                    return false;
                }
                final List<Transaction> oppositeAmountTransactions = amountToTransactions.get(transaction.amount().negate());
                if (oppositeAmountTransactions == null) {
                    return true;
                } else {
                    for (final Transaction oppositeAmountTransaction : oppositeAmountTransactions) {
                        if (creditCardPayments.contains(oppositeAmountTransaction) == false && 
                                within24Hours(transaction, oppositeAmountTransaction)) {
                            creditCardPayments.add(transaction);
                            creditCardPayments.add(oppositeAmountTransaction);
                            return false;
                        }
                    }
                }
                return true;
            })
            .collect(Collectors.toList());
    }
}
