#Transaction Analyzer

## Requirements
* JDK 1.8 or newer
* Apache Maven (Tested with version 3.3.9)

## How To Build:
- Execute `mvn clean install` in the top level directory of the source.
- Find executable jar file in target directory named:

        `transaction-analyzer-<version>-jar-with-dependencies.jar`
        
## How to Run:
Run the following command:

        java -jar <path to executable jar>

The following usage will be produced:

        usage: transaction-analyzer [--ignore-cc-payments] [--ignore-donuts] -p
               <Password> -u <User Name>
            --ignore-cc-payments     Disregards credit card payments. Credit card
                                     payments consist of two transactions with
                                     opposite amounts (e.g. 5000000 centocents and
                                     -5000000 centocents) within 24 hours of each
                                     other. A list of detected credit card payment
                                     transactions will be outputted.
            --ignore-donuts          Disregards all donut-related transactions
                                     from being included in the output.  The
                                     merchant field is used to determine what's a
                                     donut - donut transactions are named "Krispy
                                     Kreme Donuts" or "DUNKIN #336784".
         -p,--password <Password>    Password for the account
         -u,--username <User Name>   User name of the account

*At a minimum the account user name and password must be provided as arguments in
order to execute.*
