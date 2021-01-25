package com.satispay.protocore.active;

import com.google.gson.Gson;
import com.satispay.protocore.SatispayContext;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import com.satispay.protocore.session.SessionManager;
import com.satispay.protocore.utility.NetworkUtilities;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public interface ProtoCoreProvider {
    SatispayContext getSatispayContext();

    /**
     * This has to be implemented by the final implementor. With this method a {@link SecurePersistenceManager} has to be
     * provided in order to persist data safely and securely.
     *
     * @return an implementation of the interface {@link SecurePersistenceManager}
     */
    SecurePersistenceManager getSecurePersistenceManager();

    /**
     * This has to be implemented by the final implementor. With this method a {@link SessionManager} has to be
     * provided in order to handle the log out / end of session, process. This step should clear the secure data in order
     * to be able to sign up again through the 3 dh steps.
     *
     * @return an implementation of the interface {@link SessionManager}
     */
    SessionManager getSessionManager();

    /**
     * This has to be implemented by the final implementor. With this method a {@link SdkDeviceInfo} has to be
     * provided in order report some analytics.
     *
     * @return an implementation of the interface {@link SdkDeviceInfo}
     */
    SdkDeviceInfo getSdkDeviceInfo();

    default ProtoCoreHttpClientProvider getProtocoreHttpClientProvider() {
        return ProtoCoreHttpClientProvider.getInstance();
    }

    default ProtoCore getProtocore() {
        Gson gson = NetworkUtilities.getGson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getSatispayContext().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(
                        getProtocoreHttpClientProvider().getProtocoreClient(
                                getSatispayContext(),
                                getSecurePersistenceManager(),
                                getSessionManager(),
                                getSdkDeviceInfo()
                        )
                )
                .build();
        return retrofit.create(ProtoCore.class);
    }

    default ProtoCore getProtocoreNoAuth() {
        Gson gson = NetworkUtilities.getGson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getSatispayContext().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(
                        getProtocoreHttpClientProvider().getProtocoreClientNoSignatureVerify(getSatispayContext())
                )
                .build();
        return retrofit.create(ProtoCore.class);
    }
}
