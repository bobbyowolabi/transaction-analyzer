package com.owodigi.transaction.commandline;

import com.owodigi.transaction.endpoint.RestTransactionEndpoint;
import com.owodigi.transaction.endpoint.TransactionEndpoint;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionReport;
import com.owodigi.transaction.util.TransactionAggregator;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
    
    public static void main(final String[] args) throws ParseException, IOException {
        final Options options = new Options();
        options.addOption(USER_NAME_OPTION);
        options.addOption(PASSWORD_OPTION);

        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine = parser.parse(options, args);
        
        final String username = commandLine.getOptionValue(USER_NAME_OPTION.getOpt());
        final String password = commandLine.getOptionValue(PASSWORD_OPTION.getOpt());
        
        final TransactionEndpoint endpoint = new RestTransactionEndpoint(username, password);
        final List<Transaction> transactions = endpoint.getAllTransactions();
        final Map<String, TransactionReport> monthlyTotals = TransactionAggregator.monthlyTotals(transactions);
        
        final ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(monthlyTotals));
    }
}
