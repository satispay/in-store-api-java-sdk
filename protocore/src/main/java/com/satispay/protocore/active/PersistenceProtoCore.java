package com.satispay.protocore.active;

import com.satispay.protocore.ProtoCoreMessage;
import com.satispay.protocore.models.analytics.AppStartedBean;
import com.satispay.protocore.models.device.DeviceToken;
import com.satispay.protocore.models.generic.*;
import com.satispay.protocore.models.payment.Payment;
import com.satispay.protocore.models.payment.PaymentCreate;
import com.satispay.protocore.models.payment.PaymentUpdate;
import com.satispay.protocore.models.profile.ProfileMe;
import com.satispay.protocore.models.profile.ProfileMeV1;
import com.satispay.protocore.models.registration.RegistrationBean;
import com.satispay.protocore.models.request.CheckConsumer;
import com.satispay.protocore.models.transactions.*;
import com.satispay.protocore.persistence.PersistenceManager;
import com.satispay.protocore.utility.GBusinessConverter;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

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
            ArrayList<TransactionProposal> transactionsToPersist = new ArrayList<>();
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
    default Observable<PaginatedList<Payment>> getPaymentList(@Query("limit") int limit, @Query("starting_after") String startingAfter, @Query("starting_after_timestamp") String startingAfterTimestamp, @Query("status") List<String> status) {
        return getProtoCoreProvider().getProtocore().getPaymentList(limit, startingAfter, startingAfterTimestamp, status).map(paymentPaginatedList -> {
            HistoryTransactionsModel historyTransactionsModel = GBusinessConverter.toHistoryTransactionsModel(paymentPaginatedList);
            if (status != null) {
                getPersistenceManager().persistTransactionsPolling(historyTransactionsModel.getFilteredList());
            } else {
                getPersistenceManager().persistTransactions(historyTransactionsModel.getFilteredList());
            }
            return paymentPaginatedList;
        });
    }

    @Override
    default Observable<Payment> getPayment(@Path("id") long paymentId) {
        return getProtoCoreProvider().getProtocore().getPayment(paymentId).map(payment -> {
            TransactionProposal transactionProposal = payment.toTransactionProposal();
            ArrayList<TransactionProposal> transactionsToPersist = new ArrayList<>();
            transactionsToPersist.add(transactionProposal);
            getPersistenceManager().persistTransactions(transactionsToPersist);
            return payment;
        });
    }

    @Override
    default Observable<Payment> updatePayment(@Path("id") String paymentId, @Body PaymentUpdate paymentUpdate) {
        return getProtoCoreProvider().getProtocore().updatePayment(paymentId, paymentUpdate).map(payment -> {
            TransactionProposal transactionProposal = payment.toTransactionProposal();
            getPersistenceManager().persistClosedTransaction(transactionProposal);
            return payment;
        });
    }

    @Override
    default Observable<Payment> createPayment(String idempotencyKey, PaymentCreate paymentCreate) {
        return getProtoCoreProvider().getProtocore().createPayment(idempotencyKey, paymentCreate);
    }

    @Override
    default Observable<ProfileMe> profileMe() {
        return getProtoCoreProvider().getProtocore().profileMe();
    }

    @Override
    default Observable<ProfileMeV1> profileMeV1() {
        return getProtoCoreProvider().getProtocore().profileMeV1();
    }

    @Override
    default Observable<CheckConsumer> idFromPhone(String phone) {
        return getProtoCoreProvider().getProtocore().idFromPhone(phone);
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
    default Observable<DeviceToken> generateDeviceToken() {
        return getProtoCoreProvider().getProtocore().generateDeviceToken();
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
    default Observable<Payment> supportMessage(String idempotencyKey, SupportMessageRequest messageRequest) {
        return getProtoCoreProvider().getProtocore().supportMessage(idempotencyKey, messageRequest);
    }

    @Override
    default Observable<Response<ResponseBody>> getObject(@Url String url) {
        return getProtoCoreProvider().getProtocore().getObject(url);
    }

    @Override
    default Observable<Response<ResponseBody>> putObject(@Url String url, @Body RequestBody requestBody) {
        return getProtoCoreProvider().getProtocore().putObject(url, requestBody);
    }

    @Override
    default Observable<Response<ResponseBody>> postObject(@Url String url, @Body RequestBody requestBody) {
        return getProtoCoreProvider().getProtocore().postObject(url, requestBody);
    }

    @Override
    default Observable<ProtoCoreMessage> testSignature() {
        return getProtoCoreProvider().getProtocore().testSignature();
    }
}
