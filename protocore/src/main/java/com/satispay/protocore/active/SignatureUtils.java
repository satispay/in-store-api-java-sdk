package com.satispay.protocore.active;

import com.satispay.protocore.crypto.Base64Utils;
import com.satispay.protocore.crypto.CryptoUtils;
import com.satispay.protocore.errors.ProtoCoreError;
import com.satispay.protocore.errors.ProtoCoreErrorType;
import com.satispay.protocore.log.ProtoLogger;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignatureUtils {

    /**
     * Generates the digest header using SHA-512 algorithm
     *
     * @param body the string that should be passed as argument to the hashing function
     * @return a String representing the result of the hashing function encoded in Base64
     * @throws NoSuchAlgorithmException
     */
    public static String generateDigestHeader(byte[] body) throws NoSuchAlgorithmException {
        return "SHA-512=" + Base64Utils.encode(MessageDigest.getInstance("SHA-512").digest(body));
    }

    /**
     * Generates the date header the format of the date should be the following: "EEE, d MMM yyyy HH:mm:ss z"
     *
     * @return a String containing the date header content
     */
    public static String generateDateHeader() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date());

    }

    /**
     * Retrieves the algorithm used to generate the signature
     *
     * @return a String describing the algorithm
     */
    private static String getAlgorithm() {
        return "hmac-sha256";
    }

    /**
     * Indicates if the server should sign the response in the same way the client signs the requests
     *
     * @return a String containing enable
     */
    private static String getResignEnabled() {
        return "enable";
    }

    /**
     * Retrieve the headers used for the signature
     *
     * @return a String containing the list of headers used to sign
     */
    private static String getSignedHeaders() {
        return "(request-target) host date digest";
    }

    /**
     * Generates the Authorization header
     *
     * @param stringToSign the string generated from the mandatory header that has to be signed in order to be verified
     *                     by the server
     * @return the content of the Authorization header
     */
    public static String generateAuthHeader(String stringToSign, SecurePersistenceManager securePersistenceManager) throws ProtoCoreError {

        byte[] bytes = generateSignature(stringToSign, securePersistenceManager);
        if (bytes == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.SIGNATURE_ERROR;
            errorType.setMessage("error generating signature");
            throw new ProtoCoreError(errorType);

        }
        return "Signature keyId=\"" + securePersistenceManager.getPersistedData(SecurePersistenceManager.USER_KEY_ID_KEY) + "\", " +
                "algorithm=\"" + getAlgorithm() + "\", " +
                "satispayresign=\"" + getResignEnabled() + "\", " +
                "headers=\"" + getSignedHeaders() + "\", " +
                "signature=\"" + Base64Utils.encode(bytes) + "\", " +
                "satispaysequence=\"" + securePersistenceManager.getPersistedData(SecurePersistenceManager.SEQUENCE_KEY) + "\", " +
                "satispayperformance=\"LOW\"";

    }

    /**
     * Generates the signature
     *
     * @param stringToSign the string that has to be signed
     * @return a String containing the result of the signature operation encoded in Base64
     */
    private static byte[] generateSignature(String stringToSign, SecurePersistenceManager securePersistenceManager) throws ProtoCoreError {

        byte[] kMaster = Base64Utils.decode(securePersistenceManager.getPersistedData(MemoryPersistenceManager.KMASTER_KEY));
        return CryptoUtils.hmacSha256Raw(
                CryptoUtils.generateKAuth(Integer.parseInt(securePersistenceManager.getPersistedData(SecurePersistenceManager.SEQUENCE_KEY)), kMaster),
                stringToSign.getBytes());

    }

    /**
     * Fills the headers with the correct values
     *
     * @param requestBuilder  the {@link okhttp3.Request.Builder} instance which will perform the requests
     * @param originalRequest the {@link Request} the original request trapped by an interceptor
     * @return a request builder that will build a request with all needed headers filled
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static Request.Builder fillHeaders(Request.Builder requestBuilder, Request originalRequest, SdkDeviceInfo sdkDeviceInfo, final SecurePersistenceManager securePersistenceManager) throws IOException, NoSuchAlgorithmException, ProtoCoreError {

        String hostHeaderString = originalRequest.url().host();
        // ==> this is the string to sign: "host: <host>"
        String hostHeaderStringToSign = "host: " + hostHeaderString + "\n";

        String dateHeaderString = SignatureUtils.generateDateHeader();
        // ==> this is the string to sign: "date: <date>"
        String dateHeaderStringToSign = "date: " + dateHeaderString + "\n";

        String requestTargetHeader = originalRequest.method().toLowerCase() + " " + originalRequest.url().encodedPath() + (originalRequest.url().encodedQuery() != null ? "?" + originalRequest.url().encodedQuery() : "");
        // ==> this is the string to sign: "(request-target): <request-target>"
        String requestTargetHeaderString = "(request-target): " + requestTargetHeader + "\n";

        String bodyString = getBodyAsString(originalRequest);

        String digestHeaderString = (bodyString != null
                ? SignatureUtils.generateDigestHeader(bodyString.getBytes())
                : SignatureUtils.generateDigestHeader("".getBytes()));
        // ==> this is the string to sign: "digest: <digest>"
        String digestHeaderStringToSign = "digest: " + digestHeaderString;

        String signatureString = requestTargetHeaderString + hostHeaderStringToSign + dateHeaderStringToSign + digestHeaderStringToSign;

        requestBuilder.header("Date", dateHeaderString);
        requestBuilder.header("Digest", digestHeaderString);

        String encodedPathString = originalRequest.url().encodedPath();

        if (sdkDeviceInfo != null && encodedPathString != null && encodedPathString.endsWith("/transactions")) {
            requestBuilder.header("x-satispay-devicetype", sdkDeviceInfo.deviceType);
            requestBuilder.header("x-satispay-deviceinfo", sdkDeviceInfo.deviceInfo);
            requestBuilder.header("x-satispay-os", sdkDeviceInfo.osName);
            requestBuilder.header("x-satispay-osv", sdkDeviceInfo.osVersion);
            requestBuilder.header("x-satispay-apph", sdkDeviceInfo.appHouse);
            requestBuilder.header("x-satispay-appn", sdkDeviceInfo.appName);
            requestBuilder.header("x-satispay-appv", sdkDeviceInfo.appVersion);
            if (sdkDeviceInfo.trackingCode != null && !sdkDeviceInfo.trackingCode.isEmpty())
                requestBuilder.header("x-satispay-tracking-code", sdkDeviceInfo.trackingCode);
        }

        requestBuilder
                .header("Authorization", SignatureUtils.generateAuthHeader(signatureString, securePersistenceManager))
                .method(originalRequest.method(), originalRequest.body())
                .build();

        return requestBuilder;

    }

    /**
     * Helper method which obtains a string representing the body of a request
     *
     * @param request obtains a String starting from a request
     * @return a String representing the body of the given request
     * @throws IOException
     */
    public static String getBodyAsString(Request request) throws IOException {

        String bodyString = null;
        if (request.body() != null) {

            final RequestBody copy = request.body();
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            bodyString = buffer.readUtf8();

        }
        return bodyString;

    }

    /**
     * Check whether the signature returned from wally is correct
     *
     * @param authHeader               the authorization header received from wally
     * @param response                 the {@link Response} obtained by the server
     * @param securePersistenceManager an instance of {@link SecurePersistenceManager} which contains the useful
     *                                 information for the signature
     * @return a boolean that indicates if the check was successful or not
     */
    public static boolean checkSignatureResponse(String authHeader, Response response, SecurePersistenceManager securePersistenceManager, byte[] body) throws ProtoCoreError {

        String patternString = "(\\w+)=\\\"([^\"]*)\\\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(authHeader);
        HashMap<String, String> authorizationValues = new HashMap<String, String>();
        while (matcher.find()) {
            authorizationValues.put(matcher.group(1), matcher.group(2));
        }
        String[] headersToCalculate = authorizationValues.get("headers").split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < headersToCalculate.length; i++) {

            String header = headersToCalculate[i];
            stringBuilder.append(header).append(": ").append(response.header(header));
            if (i != headersToCalculate.length - 1) {
                stringBuilder.append("\n");
            }

        }
        String digestServer = response.header("digest");
        String myDigest = "";
        try {
            myDigest = generateDigestHeader(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProtoLogger.info("######==> string to sign for check: " + stringBuilder.toString());

        String sequence = authorizationValues.get("satispaysequence");
        securePersistenceManager.persistSecurely(SecurePersistenceManager.SEQUENCE_KEY, sequence != null ? sequence : "2");
        String signatureString = Base64Utils.encode(generateSignature(stringBuilder.toString(), securePersistenceManager));

        return (authorizationValues.get("signature").equals(signatureString) && myDigest.equals(digestServer));
    }

}
