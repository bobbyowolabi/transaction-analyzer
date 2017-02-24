package com.owodigi.transaction.commandline;

import com.owodigi.transaction.endpoint.RestTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import com.owodigi.transaction.util.TransactionAggregator;
import com.owodigi.transaction.util.TransactionFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

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
    
    public static void main(final String[] args) throws ParseException, IOException {
        final Options options = new Options();
        options.addOption(USER_NAME_OPTION);
        options.addOption(PASSWORD_OPTION);
        options.addOption(IGNORE_DONUTS_OPTION);
        options.addOption(IGNORE_CC_PAYMENTS_OPTION);

        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);
        
        final String username = commandLine.getOptionValue(USER_NAME_OPTION.getOpt());
        final String password = commandLine.getOptionValue(PASSWORD_OPTION.getOpt());
        
        final TransactionEndpoint endpoint = new RestTransactionEndpoint(username, password);
        List<Transaction> transactions = endpoint.getAllTransactions();
        
        if (commandLine.hasOption(IGNORE_DONUTS_OPTION.getLongOpt())) {
            System.out.println("Ignoring Donut Transactions");
            transactions = TransactionFilter.removeMerchants(transactions, 
                    "Krispy Kreme Donuts", "DUNKIN #336784");
        }
        
        if (commandLine.hasOption(IGNORE_CC_PAYMENTS_OPTION.getLongOpt())) {
            final List<Transaction> creditCardPayments = new ArrayList<>();
            transactions = TransactionFilter.creditCardTransactions(transactions, creditCardPayments);
            System.out.println("Ignoring " + creditCardPayments.size() + " Credit Card Payments:");
            write(creditCardPayments);
            System.out.print("\n\n\n");
        }
        
        
        final Map<String, TransactionReport> monthlyTotals = TransactionAggregator.monthlyTotals(transactions);
        final Map<String, TransactionReport> average = TransactionAggregator.average(transactions);
        final Map<String, TransactionReport> report = new TreeMap<>(monthlyTotals);
        report.putAll(average);
        write(report);
        System.out.println(BigDecimal.ZERO.signum());
        
        //TODO: Print usage in event of error
    }
    
    private static void write(final List<Transaction> transactions) throws IOException {
        final JsonGenerator generator = new JsonFactory().createJsonGenerator(System.out);
        final ObjectMapper mapper = new ObjectMapper();
        generator.writeStartArray();
        for (final Transaction transaction : transactions) {
            mapper.writeValue(generator, transaction);
        }
        generator.writeEndArray();
        generator.flush();
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
