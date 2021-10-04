package com.satispay.protocore.utility;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.satispay.protocore.SatispayContext;
import com.satispay.protocore.crypto.CryptoUtils;
import com.satispay.protocore.log.ProtoLogger;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NetworkUtilities {

    public static OkHttpClient.Builder getClient(SatispayContext satispayContext) {
        OkHttpClient.Builder okHttpClientBuilder;
        okHttpClientBuilder = new OkHttpClient.Builder();

        // ==> the SSL context is build only in environments different from PROD / STAGING, where the server cert is self signed
        String serverCert = satispayContext.getServerCert();
        if (serverCert != null) {
            try {
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore;
                keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", CryptoUtils.certificateX509(serverCert));

                String trustManagerDefaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(trustManagerDefaultAlgorithm);
                trustManagerFactory.init(keyStore);

                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                okHttpClientBuilder.sslSocketFactory(sslSocketFactory);
            } catch (Exception e) {
                ProtoLogger.error("!!! Error generating TLS context !!!");
            }
        }
        okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.addInterceptor( new RequestNetworkLatencyInterceptor());
        return okHttpClientBuilder;
    }

    public static OkHttpClient.Builder getClientNoCert() {
        OkHttpClient.Builder okHttpClientBuilder;
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpClientBuilder.addInterceptor( new RequestNetworkLatencyInterceptor());
        return okHttpClientBuilder;
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }
}
