package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
    
    public static List<Transaction> creditCardTransactions(final List<Transaction> transactions) {
        final Map<BigDecimal, Map<LocalDateTime, Set<Transaction>>> sortedTransactions = new HashMap<>();
        transactions.stream().forEach(transaction -> {
            Map<LocalDateTime, Set<Transaction>> timeMapping = sortedTransactions.get(transaction.amount());
            if (timeMapping == null) {
                timeMapping = new TreeMap<>();
                
            }
        });
        return Collections.emptyList();
    }
}
