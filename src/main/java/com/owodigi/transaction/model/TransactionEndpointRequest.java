package com.owodigi.transaction.model;

/**
 * Represents the request to a TransactionEndpoint
 */
public interface TransactionEndpointRequest {
    
    /**
     * Returns the common arguments of this Request
     * 
     * @return 
     */
    public CommonArgs getArgs();
}
