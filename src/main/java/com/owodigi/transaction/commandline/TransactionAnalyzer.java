package com.owodigi.transaction.commandline;

import com.owodigi.transaction.endpoint.RestTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import com.owodigi.transaction.util.TransactionAggregator;
import com.owodigi.transaction.util.TransactionFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Command line tool to display data retrieved from the TransactionEndpoint.
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
    private static final Option IGNORE_CC_PAYMENTS_OPTION = Option.builder()
        .argName("Ignore Credit Card Payments")
        .desc("Disregards credit card payments. Credit card payments consist of " + 
                "two transactions with opposite amounts (e.g. 5000000 centocents " + 
                "and -5000000 centocents) within 24 hours of each other. A list " + 
                "of detected credit card payment transactions will be outputted.")
        .hasArg(false)
        .longOpt("ignore-cc-payments")
        .optionalArg(true)
        .build();
    
    private static Options getOptions() {
        final Options options = new Options();
        options.addOption(USER_NAME_OPTION);
        options.addOption(PASSWORD_OPTION);
        options.addOption(IGNORE_DONUTS_OPTION);
        options.addOption(IGNORE_CC_PAYMENTS_OPTION);
        return options;
    }
    
    private static CommandLine getCommandLine(final String[] args) throws ParseException {
        final Options options = getOptions();
        final CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);        
    }
    
    private static TransactionEndpoint getEndpoint(final CommandLine commandLine) {
        final String username = commandLine.getOptionValue(USER_NAME_OPTION.getOpt());
        final String password = commandLine.getOptionValue(PASSWORD_OPTION.getOpt());
        return new RestTransactionEndpoint(username, password);        
    }
    
    private static Map<String, TransactionReport> generateMonthlyTotalsAndAverage(final List<Transaction> transactions) {
        final Map<String, TransactionReport> monthlyTotals = TransactionAggregator.monthlyTotals(transactions);
        final Map<String, TransactionReport> average = TransactionAggregator.average(transactions);
        final Map<String, TransactionReport> report = new TreeMap<>(monthlyTotals);
        report.putAll(average);
        return report;
    }
    
    public static void main(final String[] args) throws ParseException, IOException {
        try {
            final CommandLine commandLine = getCommandLine(args);
            final TransactionEndpoint endpoint = getEndpoint(commandLine);
            List<Transaction> transactions = endpoint.getAllTransactions();
            if (commandLine.hasOption(IGNORE_DONUTS_OPTION.getLongOpt())) {
                System.out.println("\nIgnoring Donut Transactions");
                transactions = TransactionFilter.removeMerchants(transactions, 
                    "Krispy Kreme Donuts", "DUNKIN #336784");
            }
            if (commandLine.hasOption(IGNORE_CC_PAYMENTS_OPTION.getLongOpt())) {
                final List<Transaction> creditCardPayments = new ArrayList<>();
                transactions = TransactionFilter.creditCardTransactions(transactions, creditCardPayments);
                System.out.println("\nIgnoring the following " + creditCardPayments.size() + " Credit Card Payments:");
                write(creditCardPayments);
            }
            final Map<String, TransactionReport> report = generateMonthlyTotalsAndAverage(transactions);
            System.out.println("\n\nMontly Totals & Average from GetAllTransactions Endpoint:");
            write(report);
            System.out.println();
        } catch(final Exception ex) {
            System.out.println("Encountered Error: " + ex + "\n\n");
            new HelpFormatter().printHelp("transaction-analyzer", getOptions(), true);
        }
    }
    
    private static void write(final List<Transaction> transactions) throws IOException {
        final JsonWriter writer = new JsonWriter(System.out);
        int count = 0;
        writer.writeStartArray();
        for (final Transaction transaction : transactions) {
            ++count;
            writer.writeStartObject();
            writer.writeStringField("accountId", transaction.accountId())
                .writeStringField("amount", "$" + transaction.amount().setScale(2))
                .writeStringField("merchant", transaction.merchant())
                .writeStringField("transactionTime", transaction.transactionTime(), true);
            writer.writeEndObject(count == transactions.size());
            writer.write(count != transactions.size() ? "\n" : "");
        }
        writer.writeEndArray();
    }
    
    private static void write(Map<String, TransactionReport> totals) {
        final JsonWriter writer = new JsonWriter(System.out);
        writer.writeStartObject();
        int count = 0;
        for (final Entry<String, TransactionReport> entry : totals.entrySet()) {
            ++count;
            writer.writeStringField(entry.getKey());
            writer.writeStartObject();
            writer.writeStringField("spent", "$" + entry.getValue().spent());
            writer.writeStringField("income", "$" + entry.getValue().income(), true);
            writer.writeEndObject(count == totals.size());
            writer.write(count != totals.size() ? "\n" : "");
        }        
        writer.writeEndObject();
    }
}
