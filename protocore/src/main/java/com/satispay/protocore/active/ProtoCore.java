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
import retrofit2.http.*;
import rx.Observable;

/**
 * This interface describes the API exposed for the active state of a business application.
 */
public interface ProtoCore {

    /******************
     * version update *
     ******************/

    @GET("v2/versions/{os}/{versionCode}")
    Observable<VersionUpdate> versionUpdate(@Path("os") String os, @Path("versionCode") Long versionCode);

    /***********
     * devices *
     ***********/

    @POST("v2/devices")
    Observable<ProtoCoreMessage> registrationToken(@Body RegistrationBean registrationBean);

    /****************
     * transactions *
     ****************/

    /**
     * Get the list of the accepted transactions
     *
     * @param filter        the accepted filter is "proposed"  this trigger the pending transaction request
     * @param limit         the count of transactions we want to receive
     * @param startingAfter the starting transactionId we want to retrieve transactions from (all the limit transactions)
     *                      accepted accepted before transaction with startingAfterId will be retrieved)
     * @return an Observable that emit the network response
     */
    @GET("v2.1/transactions")
    Observable<HistoryTransactionsModel> getTransactionHistory(@Query("limit") int limit, @Query("starting_after") String startingAfter, @Query("filter") String filter);

    /**
     * Get the detail of a transaction
     *
     * @param transactionId the id of the requested transaction
     * @return a TransactionBean
     */
    @GET("v2.1/transactions/{id}")
    Observable<TransactionProposal> getTransactionDetail(@Path("id") long transactionId);

    /**
     * Communicate to approve a certain proposal
     *
     * @param transactionId the id of the transaction
     * @return an Observable that emit the new TransactionBean
     */
    @PUT("v2.1/transactions/{id}/state")
    Observable<TransactionProposal> closeTransaction(@Body CloseTransaction closeTransaction, @Path("id") String transactionId);

    /**
     * Communicate to refund a certain transaction
     *
     * @param transactionId the id of the transaction
     * @return an Observable that emit the new TransactionBean
     */
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

    /*********
     * shops *
     *********/

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

    /*************
     * consumers *
     *************/

    @GET("v2/consumers/{id}")
    Observable<Consumer> getConsumerProfile(@Path("id") String consumerId);

    @GET("v2/consumers/{id}/image")
    Observable<ProtoCoreMessage> getConsumerImage(@Path("id") String consumerId);

    /*************
     * analytics *
     *************/

    @POST("v2/analytics/events/started")
    Observable<Void> appStarted(@Body AppStartedBean appStartedBean);

    //test API for signature
    @GET("v2/wally-services/protocol/tests/signature")
    Observable<ProtoCoreMessage> testSignature();
}
