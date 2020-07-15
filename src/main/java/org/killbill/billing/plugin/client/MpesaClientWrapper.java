package org.killbill.billing.plugin.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.jooby.mvc.POST;
import org.json.JSONObject;
import org.killbill.billing.plugin.util.http.HttpClient;
import org.killbill.billing.plugin.util.http.InvalidRequest;
import org.killbill.billing.plugin.util.http.ResponseFormat;
import org.killbill.billing.plugin.models.APIResourceConstants;
import org.killbill.billing.plugin.models.IdentifierTypes;
import org.killbill.billing.plugin.models.MpesaCoreBase;
import org.killbill.billing.plugin.models.RegisterURLRequest;
import org.killbill.billing.plugin.models.RegisterURLResponse;
import org.killbill.billing.plugin.models.SimulateTransactionRequest;
import org.killbill.billing.plugin.models.SimulateTransactionResponse;
import org.killbill.billing.plugin.models.TransactionStatusRequest;
import org.killbill.billing.plugin.models.TransactionStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class MpesaClientWrapper extends HttpClient {

    public static final String EXCEPTION_CLASS = "exceptionClass";
    public static final String EXCEPTION_MESSAGE = "exceptionMessage";
    public static final String MPESA_MESSAGE = "mpesaMessage";

    private static final Logger logger = LoggerFactory.getLogger(MpesaClientWrapper.class);

    private final String token;
    private final MpesaConfigProperties config;

    public MpesaClientWrapper(MpesaConfigProperties mpesaProps) throws GeneralSecurityException {
        super(mpesaProps.getDefaultPaymentUrl(), null, null, mpesaProps.getProxyServer(), 
                        mpesaProps.getProxyPort(), mpesaProps.getTrustAllCertificates());
        this.token = getToken();
        this.config = mpesaProps;
    }

  
    public String getToken() {
        String url = String.format("%s%s", this.url, MpesaConfigProperties.SECURE_TOKEN_URL);
        String appKeySecret = config.getApiKey() + ":" + config.getApiSecret();
        byte[] keySecretBytes = appKeySecret.getBytes(StandardCharsets.ISO_8859_1);
        String auth = Base64.encodeBase64String(keySecretBytes);

        String tokenJson = " { access_token: '' , expires_in : '' }";

        try {
            tokenJson = this.httpClient.prepareGet(url)
                    .addHeader("Authorization", "Basic " + auth)
                    .addHeader("cache-control", "no-cache")
                    .setRequestTimeout(6000).execute().get()
                    .getResponseBody();
            logger.info("got a token from safcom: %s", tokenJson);
        } catch (IOException | InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            logger.error("unable to get token from safaricom error: ", e);
        }

        JSONObject jsonObject = new JSONObject(tokenJson);
        return jsonObject.getString("access_token");

    }

    public String getToken(String credentialsUri) {
        String appKeySecret = config.getApiKey()+ ":" + config.getApiSecret();
        byte[] keySecretBytes = appKeySecret.getBytes(StandardCharsets.ISO_8859_1);
        String auth = Base64.encodeBase64String(keySecretBytes);
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic " + auth);
        headers.put("cache-control", "no-cache");
        String tokenResults = "";
        try {
            tokenResults = doCallAndReturnTextResponse(GET, credentialsUri, null,
                ImmutableMap.<String, String>of(), headers);
        } catch (InvalidRequest | InterruptedException | ExecutionException | IOException | TimeoutException
                | URISyntaxException e) {
            // TODO Auto-generated catch block
            logger.error("unable to get token from safcom error: ", e);
        }
        return tokenResults;
    }

    public SimulateTransactionResponse triggerSimulateTransaction(final TransactionStatusRequest sTransactionRequest){
        try{
            return doCall(POST,
                        MpesaConfigProperties.SIMULATE_C2B_TRANSACTION,
                        mapper.writeValueAsString(sTransactionRequest),
                        ImmutableMap.<String,String>of(),
                        SimulateTransactionResponse.class);
        }  catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
            
            logger.warn("unable to Trigger simulate transacion on safcom details '{}'", sTransactionRequest, e);
            return toSimulateTransactionResponse(e, null);
        } catch (InvalidRequest invalidReq){
            String body;
            try {
                body = invalidReq.getResponse() != null ? invalidReq.getResponse().getResponseBody() : null;
            } catch (final IOException ignored) {
                body = null;
            }
            logger.warn("Unable to Trigger simulate transacion on safcom ='{}', body='{}'", sTransactionRequest, body, invalidReq);
            return toSimulateTransactionResponse(invalidReq, body);
        }
    }

    private SimulateTransactionResponse toSimulateTransactionResponse(final Throwable invalidReq,@Nullable String body) {
         SimulateTransactionResponse sTransactionResponse = new SimulateTransactionResponse();
         sTransactionResponse.setExactMessage(getErrorMessage(invalidReq, body));
         return sTransactionResponse;
    }

    public TransactionStatusResponse checktransactionStatus(final TransactionStatusRequest tStatusRequest){
        try {
            return doCall(POST, MpesaConfigProperties.TRANSACTIONS_STATUS, mapper.writeValueAsString(tStatusRequest),
                        ImmutableMap.<String,String>of(), TransactionStatusResponse.class);
            
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
            logger.warn("unable to make Simulate Transaction Request on safcom details '{}' ", tStatusRequest, e);
            return toTransactionStatusResponse(e, null);
        }catch (InvalidRequest invalidReq){
            String body;
            try {
                body = invalidReq.getResponse() != null ? invalidReq.getResponse().getResponseBody() : null;
            } catch (final IOException ignored) {
                body = null;
            }
            logger.warn("Unable to Check transaction status with safcom ='{}', body='{}'", tStatusRequest, body, invalidReq);
            return toTransactionStatusResponse(invalidReq, body);
        }
    }

    private TransactionStatusResponse toTransactionStatusResponse(final Throwable e,@Nullable String body){
        TransactionStatusResponse  tStatusResponse = new TransactionStatusResponse();
        tStatusResponse.setExactMessage(getErrorMessage(e, body));
        return tStatusResponse;
    }

    public RegisterURLResponse registerMPesaPaybillRequest(final RegisterURLRequest rUrlRequest) {
        try {
            return doCall(POST, MpesaConfigProperties.REGISTER_URL, mapper.writeValueAsString(rUrlRequest), ImmutableMap.<String,String>of(),
                   RegisterURLResponse.class);
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
            
            logger.warn("unable to register server url with safcom details '{}'", rUrlRequest, e);
            return toRegisterURLResponse(e);
        } catch (InvalidRequest invalidReq){
            String body;
            try {
                body = invalidReq.getResponse() != null ? invalidReq.getResponse().getResponseBody() : null;
            } catch (final IOException ignored) {
                body = null;
            }
            logger.warn("Unable to register server url with safcom ='{}', body='{}'", rUrlRequest, body, invalidReq);
            return toRegisterURLResponse(invalidReq, body);
        }
    }


    private RegisterURLResponse toRegisterURLResponse(final Throwable e){
        return toRegisterURLResponse(e, null);
    }

    private RegisterURLResponse toRegisterURLResponse(final Throwable e, @Nullable final String body) {
        final RegisterURLResponse rUrlResponse = new RegisterURLResponse();
        rUrlResponse.setExactMessage(getErrorMessage(e, body));
        return rUrlResponse;
    }

    @Override
    protected <T> T doCall(final String verb, final String uri, final String body, 
    final Map<String, String> options, final Class<T> clazz) throws InterruptedException, ExecutionException, TimeoutException, IOException, URISyntaxException, InvalidRequest {
        final String url = String.format("%s%s", this.url, uri);

        final AsyncHttpClient.BoundRequestBuilder builder = getBuilderWithHeaderAndQuery(verb, url, options);
        if (!GET.equals(verb) && !HEAD.equals(verb)) {
            if (body != null) {
                logger.info("Mpesa request: {}", body);
                builder.setBody(body);
            }
        }

        setHeaders(body, builder);

        return executeAndWait(builder, DEFAULT_HTTP_TIMEOUT_SEC, clazz, ResponseFormat.JSON);
    }

    @Override
    protected <T> T deserializeResponse(final Response response, final Class<T> clazz, ResponseFormat format) throws IOException {
        final String responseBody = response.getResponseBody();
        logger.info("Mpesa response: {}", responseBody);
        return super.deserializeResponse(response, clazz, format);
    }

    public void setHeaders(final String body, final AsyncHttpClient.BoundRequestBuilder builder){
        builder.addHeader(MpesaConfigProperties.AUTHORIZE, 
                   MpesaConfigProperties.TOKEN + getToken());
        builder.addHeader("User-Agent", "KillBill 0.22");
        builder.addHeader("accept", APPLICATION_JSON);
        builder.addHeader("Content-Type", APPLICATION_JSON);   

    }

    private String getErrorMessage(final Throwable e, @Nullable final String body) {
        final Map<String, String> bodyMap = new HashMap<String, String>();
        bodyMap.put(EXCEPTION_CLASS, e.getClass().getCanonicalName());
        bodyMap.put(EXCEPTION_MESSAGE, e.getMessage());
        if (body != null) {
            bodyMap.put(MPESA_MESSAGE, body);
        }

        String messageJSON;
        try {
            messageJSON = mapper.writeValueAsString(bodyMap);
        } catch (final JsonProcessingException ignored) {
            messageJSON = null;
        }
        return messageJSON;
    }

    private byte[] toHex(final byte[] arr) {
        final String hex = byteArrayToHex(arr);
        return hex.getBytes();
    }

    private String byteArrayToHex(final byte[] a) {
        final StringBuilder sb = new StringBuilder(a.length * 2);
        for (final byte b : a) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

}