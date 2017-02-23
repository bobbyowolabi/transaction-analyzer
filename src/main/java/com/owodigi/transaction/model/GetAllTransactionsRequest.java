package com.owodigi.transaction.model;

/**
 * Represents the expected request for the GetAllTransaction Endpoint
 */
public class GetAllTransactionsRequest implements TransactionEndpointRequest {
    private final CommonArgs args;

    public GetAllTransactionsRequest(final CommonArgs args) {
        this.args = args;
    }

    @Override
    public CommonArgs getArgs() {
        return args;
    }
}
