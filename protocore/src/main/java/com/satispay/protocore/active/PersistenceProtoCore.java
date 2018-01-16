package com.satispay.protocore.active;

import com.satispay.protocore.ProtoCoreMessage;
import com.satispay.protocore.models.analytics.AppStartedBean;
import com.satispay.protocore.models.generic.Consumer;
import com.satispay.protocore.models.generic.Location;
import com.satispay.protocore.models.generic.VersionUpdate;
import com.satispay.protocore.models.profile.ProfileMe;
import com.satispay.protocore.models.registration.RegistrationBean;
import com.satispay.protocore.models.transactions.CloseTransaction;
import com.satispay.protocore.models.transactions.DailyClosure;
import com.satispay.protocore.models.transactions.HistoryTransactionsModel;
import com.satispay.protocore.models.transactions.TransactionProposal;
import com.satispay.protocore.persistence.PersistenceManager;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

import java.util.ArrayList;

/**
 * This class describes the class that will handle persistence operation just after network operations.
 * To achieve his task this interface need a reference to a {@link PersistenceManager} and a {@link ProtoCoreProvider}
 * in order to know ho will persist the data and who will perform the network requests.
 */
public interface PersistenceProtoCore extends ProtoCore {
    /**
     * This has to be implemented by the final implementor. With this method a {@link PersistenceManager} has to be
     * provided in order to persist data.
     *
     * @return an implementation of the interface {@link PersistenceManager}
     */
    PersistenceManager getPersistenceManager();

    /**
     * This has to be implemented by the final implementor. With this method a {@link ProtoCoreProvider} has to be
     * provided in order to retrieve the {@link ProtoCore} which will handle the in store api requests.
     *
     * @return an implementation of the interface {@link ProtoCoreProvider}
     */
    ProtoCoreProvider getProtoCoreProvider();

    @Override
    default Observable<VersionUpdate> versionUpdate(@Path("os") String os, @Path("versionCode") Long versionCode) {
        return getProtoCoreProvider().getProtocore().versionUpdate(os, versionCode);
    }

    @Override
    default Observable<ProtoCoreMessage> registrationToken(@Body RegistrationBean registrationBean) {
        return getProtoCoreProvider().getProtocore().registrationToken(registrationBean);
    }

    @Override
    default Observable<HistoryTransactionsModel> getTransactionHistory(@Query("limit") int limit, @Query("starting_after") String startingAfter, @Query("filter") String filter) {
        return getProtoCoreProvider().getProtocore().getTransactionHistory(limit, startingAfter, filter).map(historyTransactionsModel -> {
            if (filter != null) {
                getPersistenceManager().persistTransactionsPolling(historyTransactionsModel.getList());
            } else {
                getPersistenceManager().persistTransactions(historyTransactionsModel.getList());
            }
            return historyTransactionsModel;
        });
    }

    @Override
    default Observable<TransactionProposal> getTransactionDetail(@Path("id") long transactionId) {
        return getProtoCoreProvider().getProtocore().getTransactionDetail(transactionId).map(transactionProposal -> {
            ArrayList<TransactionProposal> transactionsToPersist;
            transactionsToPersist = new ArrayList<>();
            transactionsToPersist.add(transactionProposal);
            getPersistenceManager().persistTransactions(transactionsToPersist);
            return transactionProposal;
        });
    }

    @Override
    default Observable<TransactionProposal> closeTransaction(@Body CloseTransaction closeTransaction, @Path("id") String transactionId) {
        return getProtoCoreProvider().getProtocore().closeTransaction(closeTransaction, transactionId).map(transactionProposal -> {
            getPersistenceManager().persistClosedTransaction(transactionProposal);
            return transactionProposal;
        });
    }

    @Override
    default Observable<TransactionProposal> refundTransaction(@Path("id") String transactionId) {
        return getProtoCoreProvider().getProtocore().refundTransaction(transactionId).map(transactionProposal -> {
            getPersistenceManager().persistClosedTransaction(transactionProposal);
            return transactionProposal;
        });
    }

    @Override
    default Observable<ProfileMe> profileMe() {
        return getProtoCoreProvider().getProtocore().profileMe();
    }

    @Override
    default Observable<ProtoCoreMessage> getShopImage(@Path("id") String shopId) {
        return getProtoCoreProvider().getProtocore().getShopImage(shopId);
    }

    @Override
    default Observable<Void> saveShopLocation(@Path("id") String shopId, @Body Location location) {
        return getProtoCoreProvider().getProtocore().saveShopLocation(shopId, location);
    }

    @Override
    default Observable<Consumer> getConsumerProfile(@Path("id") String consumerId) {
        return getProtoCoreProvider().getProtocore().getConsumerProfile(consumerId);
    }

    @Override
    default Observable<ProtoCoreMessage> getConsumerImage(@Path("id") String consumerId) {
        return getProtoCoreProvider().getProtocore().getConsumerImage(consumerId);
    }

    @Override
    default Observable<Void> appStarted(@Body AppStartedBean appStartedBean) {
        return getProtoCoreProvider().getProtocore().appStarted(appStartedBean);
    }

    @Override
    default Observable<DailyClosure> getDailyClosure(@Path("date") String date, @Path("type") String type) {
        return getProtoCoreProvider().getProtocore().getDailyClosure(date, type);
    }

    @Override
    default Observable<ProtoCoreMessage> testSignature() {
        return getProtoCoreProvider().getProtocore().testSignature();
    }

    @Override
    default Observable<Response<ResponseBody>> getObject(@Url String url) {
        return getProtoCoreProvider().getProtocore().getObject(url);
    }

    @Override
    default Observable<Response<ResponseBody>> putObject(@Url String url, @Body RequestBody requestBody)  {
        return getProtoCoreProvider().getProtocore().putObject(url, requestBody);
    }

    @Override
    default Observable<Response<ResponseBody>> postObject(@Url String url, @Body RequestBody requestBody) {
        return getProtoCoreProvider().getProtocore().postObject(url, requestBody);
    }
}
