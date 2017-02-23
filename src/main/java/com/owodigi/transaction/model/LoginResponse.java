package com.owodigi.transaction.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Represents the Response from the Login Endpoint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse implements TransactionEndpointResponse {
    private String error;
    private long uid;
    private String token;

    @Override
    public String error() {
        return error;
    }

    public long uid() {
        return uid;
    }

    public String token() {
        return token;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
