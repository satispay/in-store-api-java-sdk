package com.satispay.protocore.dh;

import com.satispay.protocore.dh.beans.DHEncryptedRequestBean;
import com.satispay.protocore.dh.beans.DHEncryptedResponseBean;
import com.satispay.protocore.dh.beans.ExchangeRequestBean;
import com.satispay.protocore.dh.beans.ExchangeResponseBean;
import retrofit2.http.Body;
import retrofit2.http.POST;
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
}
