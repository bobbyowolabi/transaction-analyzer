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
 * A Utility class that provides the functionality of removing Transactions from
 * Collections based on certain criteria.
 */
public class TransactionFilter {

    /**
     * Removes the given merchants from the given List of Transactions.
     * 
     * @param transactions  Transactions to be filtered
     * @param merchants merchants to remove from the given list of Transactions
     * @return a new List of Transactions with the given merchants removed.
     */
    public static List<Transaction> removeMerchants(final List<Transaction> transactions, final String...merchants) {
        final Set<String> merchantSet = new HashSet<>();
        Collections.addAll(merchantSet, merchants);
        return transactions.stream()
            .filter(transaction -> merchantSet.contains(transaction.merchant()) == false)
            .collect(Collectors.toList());
    }

    /**
     * Returns true if the transactionTime of the given transactions occurred within
     * 24 hours of each other.
     * 
     * @param a first transaction in comparison
     * @param b second transaction in comparison
     * @return true if the transactionTime of the given transactions occurred 
     * within 24 hours of each other.
     */
    private static boolean within24Hours(final Transaction a, final Transaction b) {
        final LocalDateTime aDateTime = TransactionUtils.transactionTime(a);
        final LocalDateTime bDateTime = TransactionUtils.transactionTime(b);
        final long hoursDifference = ChronoUnit.HOURS.between(aDateTime, bDateTime);
        return Math.abs(hoursDifference) <= 24;
    }

    /**
     * Iterates through the given transaction and removes all Credit Card payment
     * transactions.  Returns a new List void of all credit card transactions.
     * 
     * A credit card payment transaction consists of two transactions with 
     * opposite amounts (e.g. 5000000 centocents and -5000000 centocents) within
     * 24 hours of each other.
     * 
     * @param transactions transactions to be filtered
     * @param creditCardPayments The collection to add the removed credit card
     * transactions to.
     * @return new List void of all credit card transactions
     */
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
