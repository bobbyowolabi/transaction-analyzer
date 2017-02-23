package com.owodigi.transaction.commandline;

import com.owodigi.transaction.endpoint.RestTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import com.owodigi.transaction.util.TransactionAggregator;
import com.owodigi.transaction.util.TransactionFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 */
public class TransactionAnalyzer {
    private static final Option USER_NAME_OPTION = Option.builder("u")
        .argName("User Name")
        .desc("User name of the account")
        .hasArg(true)
        .longOpt("username")
        .numberOfArgs(1)
        .required(true)
        .build();
    private static final Option PASSWORD_OPTION = Option.builder("p")
        .argName("Password")
        .desc("Password for the account")
        .hasArg(true)
        .longOpt("password")
        .numberOfArgs(1)
        .required(true)
        .build();
    private static final Option IGNORE_DONUTS_OPTION = Option.builder()
        .argName("Ignore Donuts")
        .desc("Disregards all donut-related transactions from being included " + 
                "in the output.  The merchant field is used to determine what's " + 
                "a donut - donut transactions are named \"Krispy Kreme Donuts\" " + 
                "or \"DUNKIN #336784\".")
        .hasArg(false)
        .longOpt("ignore-donuts")
        .optionalArg(true)
        .build();
    
    public static void main(final String[] args) throws ParseException, IOException {
        final Options options = new Options();
        options.addOption(USER_NAME_OPTION);
        options.addOption(PASSWORD_OPTION);
        options.addOption(IGNORE_DONUTS_OPTION);

        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);
        
        final String username = commandLine.getOptionValue(USER_NAME_OPTION.getOpt());
        final String password = commandLine.getOptionValue(PASSWORD_OPTION.getOpt());
        
        final TransactionEndpoint endpoint = new RestTransactionEndpoint(username, password);
        List<Transaction> transactions = endpoint.getAllTransactions();
        
        if (commandLine.hasOption(IGNORE_DONUTS_OPTION.getLongOpt())) {
            System.out.println("Removing Donut Transactions\n");
            transactions = TransactionFilter.removeMerchants(transactions, 
                    "Krispy Kreme Donuts", "DUNKIN #336784");
        }
        
        final Map<String, TransactionReport> monthlyTotals = TransactionAggregator.monthlyTotals(transactions);
        final Map<String, TransactionReport> average = TransactionAggregator.average(transactions);
        final Map<String, TransactionReport> report = new TreeMap<>(monthlyTotals);
        report.putAll(average);
        write(report);
        
        //TODO: Print usage in event of error
    }
    
    private static void write(Map<String, TransactionReport> totals) {
        System.out.print("{");
        int count = 0;
        for (final Entry<String, TransactionReport> entry : totals.entrySet()) {
            ++count;
            System.out.print("\"" + entry.getKey() + "\"");
            System.out.print(":");
            System.out.print("{");
            System.out.print("\"spent\":\"$" + entry.getValue().spent() + "\",");
            System.out.print("\"income\":\"$" + entry.getValue().income() + "\"");
            System.out.print("}");
            if (count != totals.size()) {
                System.out.println(",");
            }
        }
        System.out.println("}");
    }
}
