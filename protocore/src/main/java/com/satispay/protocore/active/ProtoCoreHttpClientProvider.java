package com.satispay.protocore.active;

import com.satispay.protocore.SatispayContext;
import com.satispay.protocore.dh.beans.ProtoCoreSequenceErrorBean;
import com.satispay.protocore.errors.ProtoCoreError;
import com.satispay.protocore.log.ProtoLogger;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import com.satispay.protocore.session.SessionManager;
import com.satispay.protocore.utility.NetworkUtilities;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class ProtoCoreHttpClientProvider {

    private static volatile ProtoCoreHttpClientProvider instance = new ProtoCoreHttpClientProvider();

    public static ProtoCoreHttpClientProvider getInstance() {
        return instance;
    }

    private ProtoCoreHttpClientProvider() {
    }

    /**
     * This method generates an {@link OkHttpClient} client which add the needed signature headers for outgoing requests and check the
     * signature of the ingoing messages
     *
     * @param satispayContext indicates if an {@link okhttp3.logging.HttpLoggingInterceptor} needs to be added to the client
     * @return an instance of an {@link OkHttpClient}
     */
    public OkHttpClient getProtocoreClient(SatispayContext satispayContext,
                                           final SecurePersistenceManager securePersistenceManager,
                                           final SessionManager sessionManager,
                                           final SdkDeviceInfo sdkDeviceInfo) {

        OkHttpClient.Builder clientBuilder = NetworkUtilities.getClient(satispayContext);

        try {

            // ==> here the interceptor which populates the signature headers in all the requests is added to the client
            clientBuilder.addInterceptor(chain -> {

                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();

                try {

                    //**** ==> here request headers are populated
                    requestBuilder = SignatureUtils.fillHeaders(requestBuilder, originalRequest, sdkDeviceInfo, securePersistenceManager);

                } catch (Throwable t) {
                    t.printStackTrace();
                }

                String userKeyId = securePersistenceManager.getPersistedData(SecurePersistenceManager.USER_KEY_ID_KEY);

                Response response = chain.proceed(requestBuilder.build());

                if (userKeyId != null && !userKeyId.equals(securePersistenceManager.getPersistedData(SecurePersistenceManager.USER_KEY_ID_KEY))) {
                    throw new IllegalStateException("userKeyId does not match!");
                }

                // **** ==> here the signature is calculated and checked on the response
                String authHeader = response.header("WWW-Authenticate");
                byte[] body = response.body().bytes();

                if (response.isSuccessful()) {

                    try {

                        if (!SignatureUtils.checkSignatureResponse(authHeader, response, securePersistenceManager, body)) {
                            throw new IllegalStateException("The response has some problems digest or signature doesn't match");
                        } else {
                            ProtoLogger.info("Signature check OK!!");
                        }

                    } catch (ProtoCoreError error) {
                        ProtoLogger.error(error.getMessage());
                    }

                } else {

                    ProtoLogger.info("Error received: " + response.code());
                    if (response.code() == 403) {

                        String bodyString = new String(body);
                        try {

                            ProtoCoreSequenceErrorBean error = NetworkUtilities.getGson().fromJson(bodyString, ProtoCoreSequenceErrorBean.class);
                            if (error.getCode() == 35) {

                                String persistedData = securePersistenceManager.getPersistedData(SecurePersistenceManager.SEQUENCE_KEY);
                                int currentSequence = persistedData != null ? Integer.valueOf(persistedData) : 2;
                                ProtoLogger.info("Detected a sequence error, current sequence: " + currentSequence + " ++ incrementing sequence ++");
                                securePersistenceManager.persistSecurely(SecurePersistenceManager.SEQUENCE_KEY, String.valueOf(currentSequence + 2));

                            }

                        } catch (Throwable t) {
                            ProtoLogger.error(t.getMessage());
                        }

                    } else if (response.code() == 401) {
                        ProtoLogger.info("Logging out");
                        // ==> a logout operation is needed
                        sessionManager.clearData();
                    }

                }
                return response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), body))
                        .build();

            });

            // ==> the HTTP logging interceptor is added only in DEBUG mode
            if (satispayContext.enableLog()) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(interceptor);
            }

        } catch (RuntimeException exception) {
            exception.printStackTrace();
            ProtoLogger.error("!!! Something went wrong generating https signature okkhttp client !!!");
        }

        return clientBuilder.build();
    }

    /**
     * This method generates an {@link OkHttpClient} client which add the needed signature headers for outgoing requests this
     * client doesn't check the signature on the responses
     *
     * @param satispayContext indicates if an {@link okhttp3.logging.HttpLoggingInterceptor} needs to be added to the client
     * @return an instance of an {@link OkHttpClient}
     */
    public OkHttpClient getProtocoreClientNoSignatureVerify(SatispayContext satispayContext) {

        OkHttpClient.Builder clientBuilder = NetworkUtilities.getClientNoCert(satispayContext);
        try {
            // ==> the HTTP logging interceptor is added only in DEBUG mode
            if (satispayContext.enableLog()) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(interceptor);
            }

        } catch (RuntimeException exception) {
            ProtoLogger.error(exception.getClass().getSimpleName() + ": " + exception.getMessage() + ", maybe something went wrong generating https signature okkhttp client");
        }

        return clientBuilder.build();

    }

}
