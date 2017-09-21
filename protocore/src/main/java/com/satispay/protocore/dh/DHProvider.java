package com.satispay.protocore.dh;

import com.google.gson.Gson;
import com.satispay.protocore.SatispayContext;
import com.satispay.protocore.utility.NetworkUtilities;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public interface DHProvider {
    SatispayContext getSatispayContext();

    default DH getDH() {
        Gson gson = NetworkUtilities.getGson();

        OkHttpClient.Builder clientBuilder = NetworkUtilities.getClient(getSatispayContext());
        if (getSatispayContext().enableLog()) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getSatispayContext().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(clientBuilder.build())
                .build();

        return retrofit.create(DH.class);
    }
}
