package com.satispay.protocore.crypto;

import com.satispay.protocore.dh.DHKeys;
import com.satispay.protocore.dh.UptimeMillisProvider;
import com.satispay.protocore.errors.ProtoCoreError;
import com.satispay.protocore.errors.ProtoCoreErrorType;
import com.satispay.protocore.log.ProtoLogger;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;

import javax.crypto.*;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.*;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Formatter;

/**
 * This class contains all helper functions related with cryptography.
 */
public class CryptoUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final int LOW = 2617;
    public static final int DEFAULT = 65536;
    public static final int NORMAL = 34719;
    private static final int PRIME_NUMBERS_LENGTH = 512;
    public static int[] DIGITS_POWER = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    public static Cipher cipherAES(String transformation, Integer opmode, byte[] key, byte[] iv) throws ProtoCoreError {
        Cipher cipher = null;
        try {
            // -- Removed "BC" for https://android-developers.googleblog.com/2018/03/cryptography-changes-in-android-p.html
            // cipher = Cipher.getInstance(transformation, "BC");
            cipher = Cipher.getInstance(transformation);
            if (key != null && opmode != null) {
                cipher.init(opmode, new SecretKeySpec(key, 0, key.length, "AES"), new IvParameterSpec(iv != null ? iv : new byte[16]));
            }
        } catch (Exception e) {
            ProtoLogger.error("!!! Error creating cipherAES !!!");
        }
        if (cipher == null) {
            ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
            errorType.setMessage("cipher is null");
            throw new ProtoCoreError(errorType);
        }
        return cipher;

    }

    public static byte[] sha256(byte[] bytes, int iterations) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] ret = bytes;
        for (int i = 0; i < iterations; i++) {
            ret = messageDigest.digest(ret);
        }
        return ret;
    }

    public static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256").digest(bytes);
    }

    public static String stringWithHexBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte single : bytes) {
            formatter.format("%02x", single);
        }
        formatter.close();
        return sb.toString();
    }

    public static Cipher cipherRSA(String transformation, Integer opmode, PublicKey key, byte[] iv) throws ProtoCoreError {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(transformation);
            if (key != null && opmode != null) {
                cipher.init(opmode, key);
            }
        } catch (Exception e) {
            ProtoLogger.error("!!! Error creating cipherRSA !!!");
        }
        if (cipher == null) {
            ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
            errorType.setMessage("cipher is null");
            throw new ProtoCoreError(errorType);
        }
        return cipher;

    }

    public static Cipher aesCbcPkcs5Padding(int opmode, byte[] key, byte[] iv) throws ProtoCoreError {
        return cipherAES("AES/CBC/PKCS7Padding", opmode, key, iv);
    }

    public static Cipher aesCbcZeroBytePadding(int opmode, byte[] key, byte[] iv) throws ProtoCoreError {
        return cipherAES("AES/CBC/ZeroBytePadding", opmode, key, iv);
    }

    public static byte[] decryptPkcs5(byte[] key, byte[] bytesToDecrypt) throws ProtoCoreError {
        byte[] decryptedBytes = null;
        try {
            decryptedBytes = aesCbcPkcs5Padding(Cipher.DECRYPT_MODE, key, null).doFinal(bytesToDecrypt);
        } catch (Exception e) {
            ProtoLogger.error("!!! Error decrypting with AES/CBC/PKCS5 !!!");
        }
        if (decryptedBytes == null) {
            ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
            errorType.setMessage("decryptedBytes is null");
            throw new ProtoCoreError(errorType);

        }
        return decryptedBytes;

    }

    public static byte[] decryptZeroBytePadding(byte[] key, byte[] bytesToDecrypt, byte[] iv) throws ProtoCoreError {
        byte[] decryptedBytes = null;
        try {
            decryptedBytes = aesCbcZeroBytePadding(Cipher.DECRYPT_MODE, key, iv).doFinal(bytesToDecrypt);

        } catch (Exception e) {

            ProtoLogger.error("!!! Error decrypting with AES/CBC/PKCS5 !!!");

        }
        if (decryptedBytes == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
            errorType.setMessage("decryptedBytes is null");
            throw new ProtoCoreError(errorType);

        }
        return decryptedBytes;

    }

    public static byte[] encryptPkcs5(byte[] key, byte[] bytesToEncrypt) throws ProtoCoreError {

        byte[] encryptedBytes = null;
        try {

            encryptedBytes = aesCbcPkcs5Padding(Cipher.ENCRYPT_MODE, key, null).doFinal(bytesToEncrypt);

        } catch (Exception e) {

            ProtoLogger.error("!!! Error encrypting with AES/CBC/PKCS5 !!!");

        }
        if (encryptedBytes == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.CRYPTO_ERROR;
            errorType.setMessage("encryptedBytes is null");
            throw new ProtoCoreError(errorType);

        }
        return encryptedBytes;

    }

    public static byte[] encryptZeroBytePadding(byte[] key, byte[] bytesToEncrypt, byte[] iv) throws ProtoCoreError {

        byte[] encryptedBytes = null;
        try {

            encryptedBytes = aesCbcZeroBytePadding(Cipher.ENCRYPT_MODE, key, iv).doFinal(bytesToEncrypt);

        } catch (Exception e) {

            ProtoLogger.error("!!! Error encrypting with AES/CBC/PKCS5 !!!");

        }
        if (encryptedBytes == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.CRYPTO_ERROR;
            errorType.setMessage("encryptedBytes is null");
            throw new ProtoCoreError(errorType);

        }
        return encryptedBytes;

    }

    public static byte[] encryptRSA(String key, byte[] bytesToEncrypt) throws ProtoCoreError {

        byte[] encryptedBytes = null;
        try {

            encryptedBytes = rsa(Cipher.ENCRYPT_MODE, key, null).doFinal(bytesToEncrypt);

        } catch (Exception e) {

            ProtoLogger.error("!!! Error encrypting with RSA !!!");

        }

        if (encryptedBytes == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.CRYPTO_ERROR;
            errorType.setMessage("encryptedBytes is null");
            throw new ProtoCoreError(errorType);

        }
        return encryptedBytes;

    }

    public static byte[] decryptRSA(String key, byte[] bytesToDecrypt) throws ProtoCoreError {

        byte[] encryptedBytes = null;
        try {

            encryptedBytes = rsa(Cipher.DECRYPT_MODE, key, null).doFinal(bytesToDecrypt);

        } catch (Exception e) {

            ProtoLogger.error("!!! Error decrypting with RSA !!!");

        }
        if (bytesToDecrypt == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.CRYPTO_ERROR;
            errorType.setMessage("bytesToDecrypt is null");
            throw new ProtoCoreError(errorType);

        }
        return encryptedBytes;

    }

    public static Cipher rsa(int opmode, String key, byte[] iv) throws ProtoCoreError {

        return cipherRSA("RSA/ECB/PKCS1Padding", opmode, getPublicKey(key), iv);

    }

    public static Mac hmacSha1(byte[] key, String algorithm) throws ProtoCoreError {

        Mac mac = null;
        try {

            mac = Mac.getInstance("HmacSHA1");
            if (key != null) {

                mac.init(new SecretKeySpec(key, algorithm != null ? algorithm : mac.getAlgorithm()));

            }

        } catch (Exception e) {

            ProtoLogger.error("!!! Error generating hmac !!!");

        }
        if (mac == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.CRYPTO_ERROR;
            errorType.setMessage("mac is null");
            throw new ProtoCoreError(errorType);

        }
        return mac;

    }


    public static Mac hmacSha256(byte[] key, String algorithm) throws ProtoCoreError {

        Mac mac = null;
        try {

            mac = Mac.getInstance("HmacSHA256");
            if (key != null) {

                mac.init(new SecretKeySpec(key, algorithm != null ? algorithm : mac.getAlgorithm()));

            }

        } catch (Exception e) {

            ProtoLogger.error("!!! Error generating hmac !!!");

        }
        if (mac == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.CRYPTO_ERROR;
            errorType.setMessage("mac is null");
            throw new ProtoCoreError(errorType);

        }
        return mac;

    }

    public static byte[] hmacSha1Raw(byte[] key, byte[] bytes) throws ProtoCoreError {

        return CryptoUtils.hmacSha1(key, "RAW").doFinal(bytes);

    }

    public static byte[] hmacSha256Raw(byte[] key, byte[] bytes) throws ProtoCoreError {

        return CryptoUtils.hmacSha256(key, "RAW").doFinal(bytes);

    }

    public static String fillWithZero(String text, int size) {

        StringBuilder builder = new StringBuilder();
        builder.append(text);
        while (builder.length() - text.length() < size) {

            builder.append('0');

        }
        return builder.toString();

    }

    public static String generateRandomPassword(int length) {

        String alphabet = "abcdefghijklmnopqrstuvwxyzéèìòùçüøoöABCDEFGHIJKLMNOPQRSTUV-{}()WXZY0123456789";
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {

            result[i] = alphabet.charAt(new SecureRandom().nextInt(alphabet.length()));

        }
        return new String(result);

    }

    /**
     * This method generate p and g numbers then create a public and a private dh keys
     *
     * @param uptimeMillisProvider an instance of a class implementing {@link UptimeMillisProvider}
     * @return an instance of the class {@link DHKeys} containing all information needed for a Diffie Hellman algorithm
     */
    public static DHKeys generateDHKeys(UptimeMillisProvider uptimeMillisProvider) throws ProtoCoreError {

        long start = System.currentTimeMillis();
        BigInteger TWO = BigInteger.valueOf(2);
        int certainty = 300;
        BigInteger g = new BigInteger(PRIME_NUMBERS_LENGTH, 5, new SecureRandom());
        BigInteger p = g.add(BigInteger.ONE);
        if ((p.mod(TWO)).compareTo(BigInteger.ONE) != 0) {

            p = p.add(BigInteger.ONE);

        } else {

            ProtoLogger.info("P doesn't need to be updated");

        }
        long iteration = 0;
        while (true) {

            iteration++;
            if (p.isProbablePrime(certainty)) {

                ProtoLogger.info("We have a candidate [in " + (System.currentTimeMillis() - start) + "ms]");
                if (p.isProbablePrime(certainty * 5)) {

                    break;

                } else {

                    p = p.add(TWO);

                }

            } else {

                p = p.add(TWO);
                if (p.bitLength() > PRIME_NUMBERS_LENGTH) {

                    p = p.shiftRight(1);

                }
                if (iteration % 100 == 0L && certainty > 50) {

                    if (uptimeMillisProvider.uptimeMillis() - start > 8000) {

                        ProtoLogger.info("Decreasing certainty");
                        certainty = 50;

                    }

                }

            }

        }

        ProtoLogger.info("Generated P and G after " + iteration + " tries [in " + (System.currentTimeMillis() - start) + "ms]");
        KeyPair keyPair = null;
        KeyPairGenerator keyPairGenerator;
        try {

            keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(new DHParameterSpec(p, g), new SecureRandom());
            keyPair = keyPairGenerator.generateKeyPair();

        } catch (Exception e) {

            e.printStackTrace();

        }

        if (keyPair == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
            errorType.setMessage("keyPair is null");
            throw new ProtoCoreError(errorType);

        }
        return new DHKeys(p, g, ((DHPrivateKey) keyPair.getPrivate()).getX(), ((DHPublicKey) keyPair.getPublic()).getY());

    }

    public static X509Certificate certificateX509(String certificateInString) {

        X509Certificate certificate = null;
        try {

            InputStream inputStream = new ByteArrayInputStream(
                    certificateInString.getBytes(Charset.forName("UTF-8"))
            );

            // -- Removed "BC" for https://android-developers.googleblog.com/2018/03/cryptography-changes-in-android-p.html
            // certificate = (X509Certificate) CertificateFactory.getInstance("X.509", "BC").generateCertificate(inputStream);
            certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(inputStream);

        } catch (Exception e) {

            ProtoLogger.error("!!! Error generating X509 certificate !!!");

        }
        return certificate;

    }

    public static SSLSocketFactory buildSSLContext(X509Certificate caCert, X509Certificate clientCert, PrivateKey privateKey) {

        SSLSocketFactory sslSocketFactory = null;
        try {

            KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            caKeyStore.load(null, null);
            caKeyStore.setCertificateEntry("ca-certificate", caCert);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(caKeyStore);

            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            clientKeyStore.load(null, null);
            clientKeyStore.setCertificateEntry("certificate", clientCert);
            clientKeyStore.setKeyEntry("private-key", privateKey, "".toCharArray(), new Certificate[]{clientCert});

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, "".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (Exception exception) {

            ProtoLogger.error("***** ERROR GENERATING SSL CONTEXT");

        }

        return sslSocketFactory;

    }

    public static PrivateKey getPrivateKey(String keyString) {

        PrivateKey privateKey = null;

        try {

            BufferedReader br = new BufferedReader(new StringReader(keyString));
            PEMParser pp = new PEMParser(br);
            PKCS8EncryptedPrivateKeyInfo pemPrivateKeyInfo = (PKCS8EncryptedPrivateKeyInfo) pp.readObject();
            pp.close();

            InputDecryptorProvider pkcs8Prov = new JceOpenSSLPKCS8DecryptorProviderBuilder().build("1234".toCharArray());
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            privateKey = converter.getPrivateKey(pemPrivateKeyInfo.decryptPrivateKeyInfo(pkcs8Prov));

        } catch (Exception e) {

            ProtoLogger.error("!!! Error importing private key !!!");

        }

        return privateKey;

    }

    public static PublicKey getPublicKey(String keyString) {

        PublicKey publicKey = null;
        try {

            BufferedReader br = new BufferedReader(new StringReader(keyString));
            PEMParser pp = new PEMParser(br);
            SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) pp.readObject();
            pp.close();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            publicKey = converter.getPublicKey(publicKeyInfo);


        } catch (Exception e) {

            ProtoLogger.error("!!! Error importing public key !!!");

        }
        return publicKey;

    }

    public static byte[] generateKMaster(DHKeys dhKeys, BigInteger publicServer) throws ProtoCoreError {

        byte[] kMaster = null;
        try {

            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(keyFactory.generatePrivate(new DHPrivateKeySpec(dhKeys.getPrivateKey(), dhKeys.getP(), dhKeys.getG())));
            keyAgreement.doPhase(keyFactory.generatePublic(new DHPublicKeySpec(publicServer, dhKeys.getP(), dhKeys.getG())), true);
            kMaster = MessageDigest.getInstance("SHA1").digest(keyAgreement.generateSecret());

        } catch (Exception e) {

            e.printStackTrace();
        }
        if (kMaster == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
            errorType.setMessage("kMaster is null");
            throw new ProtoCoreError(errorType);

        }
        return kMaster;

    }

    public static String generateOtp(byte[] secret, int step, int digits) throws ProtoCoreError {

        int _step = step;
        byte[] text = new byte[8];
        for (int i = text.length - 1; i >= 0; i--) {

            text[i] = ((Integer) (_step & 0xff)).byteValue();
            _step = _step >> 8;

        }
        byte[] hash = hmacSha1Raw(secret, text);
        int offset = ((Byte) hash[hash.length - 1]).intValue() & 0x0f;
        int binary = ((((Byte) hash[offset]).intValue() & 0x7f) << 24) | ((((Byte) hash[offset + 1]).intValue() & 0xff) << 16) | ((((Byte) hash[offset + 2]).intValue() & 0xff) << 8) | (((Byte) hash[offset + 3]).intValue() & 0xff);
        int otp = binary % DIGITS_POWER[digits];
        return padStart(((Integer) otp).toString(), digits, '0');

    }

    public static String padStart(String string, int minLength, char padChar) {
        if (string.length() >= minLength) {
            return string;
        }
        StringBuilder sb = new StringBuilder(minLength);
        for (int i = string.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        sb.append(string);
        return sb.toString();
    }

    public static SecretKey pbkdf2WithHmacSha1(String password, int keyLength, int iterationCount) throws ProtoCoreError {

        SecretKey secretKey = null;
        try {

            secretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(password.toCharArray(), password.getBytes(), iterationCount, keyLength));

        } catch (Exception e) {

            e.printStackTrace();

        }
        if (secretKey == null) {

            ProtoCoreErrorType errorType = ProtoCoreErrorType.DH_ERROR;
            errorType.setMessage("secretKey is null");
            throw new ProtoCoreError(errorType);

        }
        return secretKey;

    }

    public static byte[] generateKSess(int sequence, byte[] kMaster) throws ProtoCoreError {

        return pbkdf2WithHmacSha1(generateOtp(kMaster, sequence * 2 - 1, 8), 128, LOW).getEncoded();

    }

    public static byte[] generateKAuth(int sequence, byte[] kMaster) throws ProtoCoreError {

        return pbkdf2WithHmacSha1(generateOtp(kMaster, sequence * 2, 8), 128, LOW).getEncoded();

    }
}
