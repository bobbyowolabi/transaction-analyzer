package com.owodigi.transaction.model;

/**
 * Represents the response from a TransactionEndpoint
 */
public interface TransactionEndpointResponse {
    
    /**
     * Returns the status of the Response from the server.
     * By default "no-error" is returned.
     * 
     * @return 
     */
    public String error();
}
