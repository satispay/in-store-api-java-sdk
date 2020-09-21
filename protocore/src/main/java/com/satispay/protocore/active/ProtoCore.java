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
import com.satispay.protocore.models.transactions.CloseTransaction;
import com.satispay.protocore.models.transactions.DailyClosure;
import com.satispay.protocore.models.transactions.HistoryTransactionsModel;
import com.satispay.protocore.models.transactions.TransactionProposal;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;
import rx.Observable;

import java.util.Date;
import java.util.List;

/**
 * This interface describes the API exposed for the active state of a business application.
 */
public interface ProtoCore {


    // Version API

    @GET("v2/versions/{os}/{versionCode}")
    Observable<VersionUpdate> versionUpdate(@Path("os") String os, @Path("versionCode") Long versionCode);


    // Devices API

    @POST("v2/devices")
    Observable<ProtoCoreMessage> registrationToken(@Body RegistrationBean registrationBean);


    // Transactions API (@deprecated replaced by Payments API)

    /**
     * Get the list of the accepted transactions
     *
     * @deprecated replaced by {@link #getPaymentList(int, String, String, String)}
     *
     * @param limit         the count of transactions we want to receive
     * @param startingAfter the starting transactionId we want to retrieve transactions from (all the limit transactions)
     *                      accepted accepted before transaction with startingAfterId will be retrieved)
     * @param filter        the accepted filter is "proposed"  this trigger the pending transaction request
     * @return an Observable that emit the network response
     */
    @Deprecated
    @GET("v2.1/transactions")
    Observable<HistoryTransactionsModel> getTransactionHistory(@Query("limit") int limit, @Query("starting_after") String startingAfter, @Query("filter") String filter);

    /**
     * Get the detail of a transaction
     *
     * @deprecated replaced by {@link #getPayment(long)}
     *
     * @param transactionId the id of the requested transaction
     * @return a TransactionBean
     */
    @Deprecated
    @GET("v2.1/transactions/{id}")
    Observable<TransactionProposal> getTransactionDetail(@Path("id") long transactionId);

    /**
     * Communicate to approve a certain proposal
     *
     * @deprecated replaced by {@link #updatePayment(String, PaymentUpdate)}
     *
     * @param transactionId the id of the transaction
     * @return an Observable that emit the new TransactionBean
     */
    @Deprecated
    @PUT("v2.1/transactions/{id}/state")
    Observable<TransactionProposal> closeTransaction(@Body CloseTransaction closeTransaction, @Path("id") String transactionId);

    /**
     * Communicate to refund a certain transaction
     *
     * @deprecated replaced by {@link #createPayment(String, PaymentCreate)}
     *
     * @param transactionId the id of the transaction
     * @return an Observable that emit the new TransactionBean
     */
    @Deprecated
    @POST("v2/transactions/{id}/refunds")
    Observable<TransactionProposal> refundTransaction(@Path("id") String transactionId);

    /**
     * Given a date, will retrieve the shop daily amount related to the authentication key.
     *
     * @param date will represent the daily closure date the user requests, formatted like "YYYYmmDD" (eg. /daily_closure/20161003)
     * @param type will represent the type of daily closure requested, could be one of DEVICE or SHOP
     * @return a DailyClosureBean
     */
    @GET("v2/daily_closure/{date}/type/{type}")
    Observable<DailyClosure> getDailyClosure(@Path("date") String date, @Path("type") String type);

    /**
     * Retrieve the shop profile
     *
     * @return the shop profile info
     */
    @GET("v2/profile/me")
    Observable<ProfileMe> profileMe();


    // Payments API

    /**
     * API to retrieve a list of payments that can be filtered based their status
     *
     * @param limit                  A limit on the number of objects to be returned, between 1 and 100
     * @param startingAfter          Cursor to use in pagination. Starting_after is the id that defines your place in the list.
     *                               For instance, if you make a list request and receive 100 objects, ending with
     *                               "f0e8bf89-a119-45d4-ac1b-ee52cccc8932", your subsequent call can include
     *                               starting_after="f0e8bf89-a119-45d4-ac1b-ee52cccc8932" in order to fetch the next
     *                               page of the list.
     * @param startingAfterTimestamp A secondary cursor for use in pagination. starting_after_timestamp is the timestamp that
     *                               defines your place in the list. See the starting_after description for further details.
     * @param status                 Filter by the payment status ACCEPTED, PENDING or CANCELED
     * @return an Observable that emit the network response
     */
    @GET("/g_business/v1/payments")
    Observable<PaginatedList<Payment>> getPaymentList(@Query("limit") int limit, @Query("starting_after") String startingAfter, @Query("starting_after_timestamp") String startingAfterTimestamp, @Query("status") List<String> status);

    //@GET("/g_business/v1/payments")
    //Observable<PaginatedList<Payment>> getPaymentAndRequestList(@Query("limit") int limit, @Query("starting_after") String startingAfter, @Query("starting_after_timestamp") String startingAfterTimestamp);

    /**
     * API to retrieve the detail of a specific payment
     *
     * @param paymentId     The id of the payment to retrieve
     * @return a {@link Payment}
     */
    @GET("/g_business/v1/payments/{id}")
    Observable<Payment> getPayment(@Path("id") long paymentId);

    /**
     * API to update the state or the metadata of a payment
     *
     * @param paymentId The id of the payment to retrieve
     * @param paymentUpdate Update: [action] to perform (ACCEPT or CANCEL) and/or [metadata] Generic field that can be used to store the order_id
     * @return an Observable that emit the new TransactionBean
     */
    @PUT("/g_business/v1/payments/{id}")
    Observable<Payment> updatePayment(@Path("id") String paymentId, @Body PaymentUpdate paymentUpdate);

    /**
     * API to create a payment, flows: MATCH_CODE or REFUND
     *
     * @param idempotencyKey The idempotent token of the request
     * @param paymentCreate Create payment, fields {@link PaymentCreate #PaymentCreate(String, Long, String, Date, String, String, String)}
     * @return an Observable that emit the new TransactionBean
     */
    @POST("/g_business/v1/payments")
    Observable<Payment> createPayment(@Header("Idempotency-Key") String idempotencyKey, @Body PaymentCreate paymentCreate);


    // Profile API

    /**
     * Retrieve the new shop profile
     *
     * @return the shop profile info
     */
    @GET("/g_business/v1/profile/me")
    Observable<ProfileMeV1> profileMeV1();


    /**
     * Retrieve the ID consumer from phone
     *
     * @return the id consumer
     */
    @GET("/g_business/v1/consumers/{phone}")
    Observable<CheckConsumer> idFromPhone(@Path("phone") String phone);

    // Shops API

    /**
     * Retrieve the image of a certain shop
     *
     * @param shopId the id of the shop
     * @return the shop image
     */
    @GET("v2/shops/{id}/image")
    Observable<ProtoCoreMessage> getShopImage(@Path("id") String shopId);

    /**
     * Communicate a new position for the shop
     *
     * @param shopId   the id of the shop
     * @param location retrieved location
     * @return an Observable that emits the callback for this request
     */
    @PUT("v2/shops/{id}/location")
    Observable<Void> saveShopLocation(@Path("id") String shopId, @Body Location location);


    // Consumers API

    @GET("v2/consumers/{id}")
    Observable<Consumer> getConsumerProfile(@Path("id") String consumerId);

    @GET("v2/consumers/{id}/image")
    Observable<ProtoCoreMessage> getConsumerImage(@Path("id") String consumerId);


    // Token API

    @POST("v2/device_tokens")
    Observable<DeviceToken> generateDeviceToken();


    // Analytics API

    @POST("v2/analytics/events/started")
    Observable<Void> appStarted(@Body AppStartedBean appStartedBean);


    // Support API

    /**
     * API to send a support message
     *
     * @param idempotencyKey The idempotent token of the request
     * @param messageRequest Create support request, fields {@link SupportMessageRequest#SupportMessageRequest(String, String, String, SupportMessageRequest.Device)}
     * @return an Observable that emit the new TransactionBean
     */
    @POST("/g_business/v1/support/messages")
    Observable<Payment> supportMessage(@Header("Idempotency-Key") String idempotencyKey, @Body SupportMessageRequest messageRequest);


    // Generic

    @GET()
    Observable<Response<ResponseBody>> getObject(@Url String url);

    @PUT()
    Observable<Response<ResponseBody>> putObject(@Url String url, @Body RequestBody requestBody);

    @POST()
    Observable<Response<ResponseBody>> postObject(@Url String url, @Body RequestBody requestBody);


    //test API for signature

    @GET("v2/wally-services/protocol/tests/signature")
    Observable<ProtoCoreMessage> testSignature();
}
