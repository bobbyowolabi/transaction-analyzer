package com.owodigi.transaction.endpoint;

import com.owodigi.transaction.model.CommonArgs;
import com.owodigi.transaction.model.GetAllTransactionResponse;
import com.owodigi.transaction.model.GetAllTransactionsRequest;
import com.owodigi.transaction.model.LoginRequest;
import com.owodigi.transaction.model.LoginResponse;
import com.owodigi.transaction.model.Transaction;
import com.owodigi.transaction.model.TransactionEndpointResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A TransactionEndpoint that hits the live production TransactionEndpoint
 * instance.
 */
public class RestTransactionEndpoint implements TransactionEndpoint {
    private static final String GET_ALL_TRANSACTIONS_URL = "https://2016.api.levelmoney.com/api/v2/core/get-all-transactions";
    private static final String LOGIN_URL = "https://2016.api.levelmoney.com/api/v2/core/login";
    private final String email;
    private final String password;

    /**
     * Creates a new RestTransactionEndpoint with the given username
     * (email address) and password which is used to obtain the user's 
     * credentials needed to make calls to the API.
     *
     * @param username username of account, it is an email address.
     * @param password password for the account
     */
    public RestTransactionEndpoint(final String username, final String password) {
        this.email = username;
        this.password = password;
    }

    /**
     * Performs a POST to the Login Endpoint with the configured email and
     * password. Returns a LoginResponse with a fresh token and uid for the
     * user.
     *
     * @return LoginResponse containing information needed to perform additional
     * calls to the Endpoint.
     * @throws IOException if an error occurs during the POST.
     */
    private LoginResponse login() throws IOException {
        return post(LOGIN_URL, LoginResponse.class, new LoginRequest(email, password, new CommonArgs.Builder()
            .apiToken(API_TOKEN)
            .jsonStrictMode(false)
            .jsonVerboseResponse(false)
            .build()));
    }

    @Override
    public List<Transaction> getAllTransactions() throws IOException {
        final LoginResponse login = login();
        return post(GET_ALL_TRANSACTIONS_URL, GetAllTransactionResponse.class,
            new GetAllTransactionsRequest(new CommonArgs.Builder()
                .apiToken(API_TOKEN)
                .token(login.token())
                .uid(login.uid())
                .jsonStrictMode(false)
                .jsonVerboseResponse(false)
                .build())).getTransactions();
    }

    @Override
    public List<Transaction> getProjectedTransactionsForMonth() {
        throw new UnsupportedOperationException("getProjectedTransactionsForMonth is currently not supported.");
    }

    /**
     * Performs a POST to this TransactionEndpoint.  <p>The given request
     * is serialized to JSON and set as the body of the POST. The response,
     * if successful, will be interpreted as JSON and deserialized to the given
     * response type specified.
     * <p>
     * Headers:<br/>
     * Content application/json <br/>
     * Accept: application/json
     *
     * @param <T>   The type of the request
     * @param <J>   The type of the expected response
     * @param url   The URL to post to
     * @param request represents the the body of POST; will be serialized to json
     * @param responseType represents the expected response type;
     * @return Response of the post as specified type
     * @throws IOException if any error occurs during post.
     */
    private <T, J extends TransactionEndpointResponse> J post(final String url, final Class<J> responseType, final T request) throws IOException {
        final HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        final ObjectMapper mapper = new ObjectMapper();
        final HttpEntity arguments = EntityBuilder.create()
            .setBinary(mapper.writeValueAsBytes(request))
            .setContentEncoding(StandardCharsets.UTF_8.name())
            .setContentType(ContentType.APPLICATION_JSON)
            .build();
        post.setEntity(arguments);
        try (final CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                final J endpointResponse = new ObjectMapper().readValue(response.getEntity().getContent(), responseType);
                if (endpointResponse.error().equals("no-error")) {
                    return endpointResponse;
                } else {
                    throw new IOException("Reveived an error: " + endpointResponse.error());
                }
            } else {
                throw new IOException("Unable to Connect: " + response.getStatusLine().getReasonPhrase() +
                    "; Status Code: " + response.getStatusLine().getStatusCode() +
                    "; Body: " + EntityUtils.toString(response.getEntity()) +
                    "; Content Type: " + response.getEntity().getContentType());
            }
        }
    }
}
