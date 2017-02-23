package com.owodigi.transaction.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents the set of arguments that can be included in every 
 * TransactionEndpoint request.
 */
public class CommonArgs {
    private long uid;
    private String token;
    private String apiToken;
    private boolean jsonStrictMode;
    private boolean jsonVerboseResponse;

    public CommonArgs() {}

    public CommonArgs(final long uid, final String token, final String apiToken, final boolean jsonStrictMode, final boolean jsonVerboseResponse) {
        this.uid = uid;
        this.token = token;
        this.apiToken = apiToken;
        this.jsonStrictMode = jsonStrictMode;
        this.jsonVerboseResponse = jsonVerboseResponse;
    }
    
    public long getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    @JsonProperty("api-token")
    public String getApiToken() {
        return apiToken;
    }

    @JsonProperty("json-strict-mode")
    public boolean isJsonStrictMode() {
        return jsonStrictMode;
    }

    @JsonProperty("json-verbose-response")
    public boolean isJsonVerboseResponse() {
        return jsonVerboseResponse;
    }

    public void setUuid(long uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public void setJsonStrictMode(boolean jsonStrictMode) {
        this.jsonStrictMode = jsonStrictMode;
    }

    public void setJsonVerboseResponse(boolean jsonVerboseResponse) {
        this.jsonVerboseResponse = jsonVerboseResponse;
    }
    
    public static class Builder {
        private long uid;
        private String token;
        private String apiToken;
        private boolean jsonStrictMode;
        private boolean jsonVerboseResponse;

        public CommonArgs build() {
            return new CommonArgs(uid, token, apiToken, jsonStrictMode, jsonVerboseResponse);
        }
        
        public Builder uid(final long uid) {
            this.uid = uid;
            return this;
        }

        public Builder token(final String token) {
            this.token = token;
            return this;
        }

        public Builder apiToken(final String apiToken) {
            this.apiToken = apiToken;
            return this;
        }

        public Builder jsonStrictMode(final boolean jsonStrictMode) {
            this.jsonStrictMode = jsonStrictMode;
            return this;
        }

        public Builder jsonVerboseResponse(final boolean jsonVerboseResponse) {
            this.jsonVerboseResponse = jsonVerboseResponse;
            return this;
        }
    }
}
