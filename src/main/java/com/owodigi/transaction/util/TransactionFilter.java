package com.owodigi.transaction.util;

import com.owodigi.transaction.model.Transaction;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class TransactionFilter {

    public static List<Transaction> filterMerchant(final String merchant) {
        return Collections.emptyList();
    }
    
    public static List<Transaction> filterCreditCardTransactions(final List<Transaction> transactions) {
        return Collections.emptyList();
    }
}
