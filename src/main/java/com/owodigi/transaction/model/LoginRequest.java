package com.owodigi.transaction.model;

/**
 * Represents the Request expected from the Login Endpoint
 */
public class LoginRequest implements TransactionEndpointRequest {
    private String email;
    private String password;
    private final CommonArgs args;

    public LoginRequest(final String email, final String password, final CommonArgs args) {
        this.email = email;
        this.password = password;
        this.args = args;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public CommonArgs getArgs() {
        return args;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
