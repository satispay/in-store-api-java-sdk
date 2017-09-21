package com.satispay.protocore.dh;

import java.math.BigInteger;
import java.util.UUID;

public class DHValues {

    private static volatile DHValues instance = new DHValues();

    public static DHValues getInstance() {
        return instance;
    }

    private DHValues() {
    }

    /*************
     * DH values *
     *************/

    // ==> client generated values
    private DHKeys dhKeys;
    private UUID uuid;
    private int sequence;

    // ==> common key between client and server
    private byte[] kMaster;

    // ==> keys derived from kMaster
    private byte[] kSess;
    private byte[] kAuth;

    // ==> unused key in business app
    private byte[] kSafe;
    private byte[] kSafeApp;
    private byte[] kSafeWally;


    // ==> values retrieved from server
    private String nonce;
    private BigInteger publicKey;
    private String userKeyId;


    public DHKeys getDhKeys() {
        return dhKeys;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getSequence() {
        return sequence;
    }

    public byte[] getkMaster() {
        return kMaster;
    }

    public byte[] getkSess() {
        return kSess;
    }

    public byte[] getkAuth() {
        return kAuth;
    }

    public byte[] getkSafe() {
        return kSafe;
    }

    public byte[] getkSafeApp() {
        return kSafeApp;
    }

    public byte[] getkSafeWally() {
        return kSafeWally;
    }

    public String getNonce() {
        return nonce;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public String getUserKeyId() {
        return userKeyId;
    }

    public static void setInstance(DHValues instance) {
        DHValues.instance = instance;
    }

    public void setDhKeys(DHKeys dhKeys) {
        this.dhKeys = dhKeys;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setkMaster(byte[] kMaster) {
        this.kMaster = kMaster;
    }

    public void setkSess(byte[] kSess) {
        this.kSess = kSess;
    }

    public void setkAuth(byte[] kAuth) {
        this.kAuth = kAuth;
    }

    public void setkSafe(byte[] kSafe) {
        this.kSafe = kSafe;
    }

    public void setkSafeApp(byte[] kSafeApp) {
        this.kSafeApp = kSafeApp;
    }

    public void setkSafeWally(byte[] kSafeWally) {
        this.kSafeWally = kSafeWally;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }

    public void setUserKeyId(String userKeyId) {
        this.userKeyId = userKeyId;
    }

}
