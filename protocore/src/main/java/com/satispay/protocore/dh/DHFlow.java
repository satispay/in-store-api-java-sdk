package com.satispay.protocore.dh;

import com.google.gson.Gson;
import com.satispay.protocore.SatispayContext;
import com.satispay.protocore.crypto.CryptoUtils;
import com.satispay.protocore.dh.beans.*;
import com.satispay.protocore.errors.ProtoCoreError;
import com.satispay.protocore.errors.ProtoCoreErrorType;
import com.satispay.protocore.log.ProtoLogger;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import com.satispay.protocore.utility.NetworkUtilities;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

/**
 * Interface which describes and implement the three DH steps.
 */
public interface DHFlow {
    SatispayContext getSatispayContext();

    /**
     * This has to be implemented by the final implementor. With this method a {@link SecurePersistenceManager} has to be
     * provided in order to persist data safely and securely.
     *
     * @return an implementation of the interface {@link SecurePersistenceManager}
     */
    SecurePersistenceManager getSecurePersistenceManager();

    /**
     * This has to be implemented by the final implementor. With this method a {@link UptimeMillisProvider} has to be
     * provided in order use the uptime_millis information in the dh process.
     *
     * @return an implementation of the interface {@link UptimeMillisProvider}
     */
    UptimeMillisProvider getUptimeMillisProvider();

    default DHValues getDHValues() {
        return DHValues.getInstance();
    }

    default DHProvider getDhProvider() {
        return DHFlow.this::getSatispayContext;
    }

    default Observable<ExchangeResponseBean> performExchange() {
        return Observable.create((Observable.OnSubscribe<Void>) subscriber -> {
            try {
                getDHValues().setDhKeys(CryptoUtils.generateDHKeys(getUptimeMillisProvider()));
            } catch (ProtoCoreError protoCoreError) {
                subscriber.onError(protoCoreError);
            }
            subscriber.onNext(null);
            subscriber.onCompleted();
        }).switchMap((Func1<Object, Observable<ExchangeResponseBean>>) o -> getDhProvider().getDH().exchange(
                new ExchangeRequestBean(
                        getDHValues().getDhKeys().getP().toString(),
                        getDHValues().getDhKeys().getG().toString(),
                        getDHValues().getDhKeys().getPublicKey().toString(),
                        "IT",
                        "IT"
                )
        )).doOnNext(
                exchangeResponseBean -> {
                    getDHValues().setUserKeyId(exchangeResponseBean.getUserKeyId());
                    getDHValues().setPublicKey(exchangeResponseBean.getPublicKey());
                }
        );
    }

    default Observable<DHEncryptedResponseBean> performChallenge() {
        final Gson gson = NetworkUtilities.getGson();
        return Observable.create((Observable.OnSubscribe<ArrayList<String>>) subscriber -> {

            try {
                getDHValues().setUuid(UUID.randomUUID());
                String key = getSatispayContext().getPublicKey();
                String encryptedUuid = Base64.getEncoder().encodeToString(CryptoUtils.encryptRSA(key, getDHValues().getUuid().toString().getBytes()));
                String plainChallengePayload = gson.toJson(new ChallengeRequestBean(encryptedUuid));

                getDHValues().setSequence(2);
                getDHValues().setkMaster(CryptoUtils.generateKMaster(getDHValues().getDhKeys(), getDHValues().getPublicKey()));
                getDHValues().setkAuth(CryptoUtils.generateKAuth(getDHValues().getSequence(), getDHValues().getkMaster()));
                getDHValues().setkSess(CryptoUtils.generateKSess(getDHValues().getSequence(), getDHValues().getkMaster()));

                String hmac = Base64.getEncoder().encodeToString(CryptoUtils.hmacSha256Raw(getDHValues().getkAuth(), plainChallengePayload.getBytes()));
                String encryptedPayload = Base64.getEncoder().encodeToString(CryptoUtils.encryptPkcs5(getDHValues().getkSess(), plainChallengePayload.getBytes()));

                ProtoLogger.info("==> plain uuid: " + getDHValues().getUuid());
                ProtoLogger.info("==> encrypted uuid: " + encryptedUuid);
                ProtoLogger.info("==> kMaster: " + Base64.getEncoder().encodeToString(getDHValues().getkMaster()));
                ProtoLogger.info("==> kSess: " + Base64.getEncoder().encodeToString(getDHValues().getkSess()));
                ProtoLogger.info("==> kAuth: " + Base64.getEncoder().encodeToString(getDHValues().getkAuth()));
                ProtoLogger.info("==> plain payload: " + plainChallengePayload);
                ProtoLogger.info("==> encrypted payload: " + encryptedPayload);
                ProtoLogger.info("==> hmac plain nested payload: " + hmac);

                ArrayList<String> values = new ArrayList<>(2);
                values.add(encryptedPayload);
                values.add(hmac);
                subscriber.onNext(values);

            } catch (ProtoCoreError error) {
                subscriber.onError(error);
            }

        }).switchMap(values -> getDhProvider().getDH().challenge(
                new DHEncryptedRequestBean(
                        getDHValues().getUserKeyId(),
                        getDHValues().getSequence(),
                        "LOW",
                        values.get(0),
                        values.get(1)
                )
        ).switchMap((Func1<DHEncryptedResponseBean, Observable<? extends DHEncryptedResponseBean>>) dhEncryptedResponseBean -> Observable.create(subscriber -> {
            ChallengeResponseBean payloadBean;
            try {

                payloadBean = gson.fromJson(
                        new String(CryptoUtils.decryptPkcs5(getDHValues().getkSess(), Base64.getDecoder().decode(dhEncryptedResponseBean.getEncryptedPayload()))),
                        ChallengeResponseBean.class
                );

            } catch (ProtoCoreError error) {
                subscriber.onError(error);
                return;
            }

            String uuidFromServer = payloadBean.getChallengeResponse();
            String hmac;
            try {
                hmac = Base64.getEncoder().encodeToString(CryptoUtils.hmacSha256Raw(getDHValues().getkAuth(), gson.toJson(payloadBean).getBytes()));
            } catch (ProtoCoreError error) {
                subscriber.onError(error);
                return;
            }
            getDHValues().setNonce(payloadBean.getNonce());
            if (!uuidFromServer.equals(getDHValues().getUuid().toString())) {

                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("uuid returned from server doesn't match the one generated by client");
                subscriber.onError(new ProtoCoreError(errorType));
                return;

            }
            if (!hmac.equals(dhEncryptedResponseBean.getHmac())) {

                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("hmac doesn't match");
                subscriber.onError(new ProtoCoreError(errorType));
                return;

            }

            subscriber.onNext(dhEncryptedResponseBean);
            subscriber.onCompleted();
        })));
    }

    default Observable<DHEncryptedResponseBean> performTokenVerification(final String token) {
        final Gson gson = NetworkUtilities.getGson();

        return Observable.create((Observable.OnSubscribe<ArrayList<String>>) subscriber -> {

            getDHValues().setNonce(String.valueOf(new BigInteger(getDHValues().getNonce()).add(BigInteger.ONE)));
            byte[] nonceBytes = getDHValues().getNonce().getBytes();

            int randomLen = 5 + new SecureRandom().nextInt(Integer.MAX_VALUE) % 30;

            byte[] kSafeNew = new byte[0];
            try {
                kSafeNew = CryptoUtils.pbkdf2WithHmacSha1(CryptoUtils.generateRandomPassword(randomLen), 128, 2617).getEncoded();
            } catch (ProtoCoreError error) {

                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("error encrypting kSafe");
                subscriber.onError(new ProtoCoreError(errorType));

            }
            getDHValues().setkSafe(kSafeNew);
            getDHValues().setkSafeApp(Arrays.copyOfRange(kSafeNew, 0, kSafeNew.length / 2));
            getDHValues().setkSafeWally(Arrays.copyOfRange(kSafeNew, kSafeNew.length / 2, kSafeNew.length));

            int verifyLength = nonceBytes.length + getDHValues().getkSafeWally().length;
            byte[] verify = new byte[verifyLength];
            Arrays.copyOfRange(verify, 0, nonceBytes.length);
            for (int i = 0; i < verifyLength; i++) {

                if (i < nonceBytes.length) {
                    verify[i] = nonceBytes[i];
                } else {
                    verify[i] = getDHValues().getkSafeWally()[i - nonceBytes.length];
                }

            }
            try {
                verify = MessageDigest.getInstance("SHA-256").digest(verify);
            } catch (Exception e) {

                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("error generating verify");
                subscriber.onError(new ProtoCoreError(errorType));
                return;

            }
            if (verify == null) {

                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("verify is null");
                subscriber.onError(new ProtoCoreError(errorType));
                return;

            }

            String plainTokenVerificationRequestString = gson.toJson(new TokenVerificationRequestBean(
                    Base64.getEncoder().encodeToString(verify),
                    token,
                    Base64.getEncoder().encodeToString(getDHValues().getkSafeWally())
            ));

            String encryptedPayload;
            String hmac;
            try {
                encryptedPayload = Base64.getEncoder().encodeToString(CryptoUtils.encryptPkcs5(getDHValues().getkSess(), plainTokenVerificationRequestString.getBytes()));
                hmac = Base64.getEncoder().encodeToString(CryptoUtils.hmacSha256Raw(getDHValues().getkAuth(), plainTokenVerificationRequestString.getBytes()));
            } catch (ProtoCoreError error) {
                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("error generating encryptedPayload and hmac");
                subscriber.onError(new ProtoCoreError(errorType));
                return;
            }

            ProtoLogger.info("==> nonceInc: " + getDHValues().getNonce());
            ProtoLogger.info("==> kSafe: " + Base64.getEncoder().encodeToString(kSafeNew));
            ProtoLogger.info("==> kSafeApp: " + Base64.getEncoder().encodeToString(getDHValues().getkSafeApp()));
            ProtoLogger.info("==> kSafeWally: " + Base64.getEncoder().encodeToString(getDHValues().getkSafeWally()));
            ProtoLogger.info("==> verify: " + Base64.getEncoder().encodeToString(verify));
            ProtoLogger.info("==> plain payload: " + plainTokenVerificationRequestString);
            ProtoLogger.info("==> encrypted payload: " + encryptedPayload);
            ProtoLogger.info("==> hmac: " + hmac);

            ArrayList<String> values = new ArrayList<String>(2);
            values.add(encryptedPayload);
            values.add(hmac);
            subscriber.onNext(values);

        }).switchMap(values -> getDhProvider().getDH().tokenVerification(new DHEncryptedRequestBean(
                getDHValues().getUserKeyId(),
                getDHValues().getSequence(),
                "LOW",
                values.get(0),
                values.get(1))
        ).switchMap(dhEncryptedResponseBean -> Observable.create(subscriber -> {
            TokenVerificationResponseBean payloadBean;

            try {
                payloadBean = gson.fromJson(
                        new String(CryptoUtils.decryptPkcs5(getDHValues().getkSess(), Base64.getDecoder().decode(dhEncryptedResponseBean.getEncryptedPayload()))),
                        TokenVerificationResponseBean.class
                );
            } catch (ProtoCoreError error) {
                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("error decrypting encrypted payload");
                subscriber.onError(new ProtoCoreError(errorType));
                return;
            }

            String hmac;

            try {
                hmac = Base64.getEncoder().encodeToString(CryptoUtils.hmacSha256Raw(getDHValues().getkAuth(), gson.toJson(payloadBean).getBytes()));
            } catch (ProtoCoreError error) {
                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("error generating hmac");
                subscriber.onError(new ProtoCoreError(errorType));
                return;
            }

            if (!hmac.equals(dhEncryptedResponseBean.getHmac())) {
                ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
                errorType.setMessage("hmac doesn't match");
                subscriber.onError(new ProtoCoreError(errorType));
                return;
            }
            if (!payloadBean.getResponse().equals("OK")) {
                ProtoCoreErrorType errorType = ProtoCoreErrorType.INVALID_ACTIVATION_CODE;
                errorType.setMessage("Response is not OK");
                subscriber.onError(new ProtoCoreError(errorType));
                return;
            }

            getSecurePersistenceManager().persistSecurely(SecurePersistenceManager.KMASTER_KEY, Base64.getEncoder().encodeToString(getDHValues().getkMaster()));
            getSecurePersistenceManager().persistSecurely(SecurePersistenceManager.SEQUENCE_KEY, String.valueOf(getDHValues().getSequence()));
            getSecurePersistenceManager().persistSecurely(SecurePersistenceManager.USER_KEY_ID_KEY, getDHValues().getUserKeyId());
            getSecurePersistenceManager().persistSecurely(SecurePersistenceManager.KSAFE_APP_KEY, Base64.getEncoder().encodeToString(getDHValues().getkSafeApp()));

            subscriber.onNext(dhEncryptedResponseBean);
            subscriber.onCompleted();
        })));
    }
}
