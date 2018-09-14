package com.satispay.protocore.dh;

import com.satispay.protocore.dh.beans.*;
import retrofit2.http.*;
import rx.Observable;

/**
 * This interface describes the API exposed for the DH network requests.
 */
public interface DH {
    /**
     * This method performs the first step of the DH process
     *
     * @param exchangeRequestBean the bean containing all the information needed to complete the first dh step {@link ExchangeRequestBean}
     * @return an Observable that emit the network response
     */
    @POST("v2/dh/exchange")
    Observable<ExchangeResponseBean> exchange(@Body ExchangeRequestBean exchangeRequestBean);

    @POST("v2/dh/challenge")
    Observable<DHEncryptedResponseBean> challenge(@Body DHEncryptedRequestBean DHEncryptedRequestBean);

    @POST("v2/dh/verify")
    Observable<DHEncryptedResponseBean> tokenVerification(@Body DHEncryptedRequestBean tokenVerificationRequestBean);

    /********************
     * token generation *
     ********************/

    @GET("v2/device_token_recoveries/shops")
    Observable<TokenRecoveryResponseBean> tokenRecoveryShopList(@Query("code") String code, @Query("name") String name, @Query("starting_after") String startingAfter);

    @POST("v2/device_token_recoveries/shops/{id}/device_tokens")
    Observable<Void> tokenRecoverySend(@Path("id") String id, @Body SendTokenRequestBean sendTokenRequestBean);
}
