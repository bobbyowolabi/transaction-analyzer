package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class TransactionFilter {

    public static List<Transaction> removeMerchants(final List<Transaction> transactions, final String...merchants) {
        final Set<String> merchantSet = new HashSet<>();
        Collections.addAll(merchantSet, merchants);
        return transactions.stream()
            .filter(transaction -> merchantSet.contains(transaction.merchant()) == false)
            .collect(Collectors.toList());
    }
    
    public static List<Transaction> creditCardTransactions(final List<Transaction> transactions) {
        return Collections.emptyList();
    }
}
