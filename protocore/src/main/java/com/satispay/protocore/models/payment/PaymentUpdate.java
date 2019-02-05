package com.satispay.protocore.models.payment;

public class PaymentUpdate {
    public static String ACTION_ACCEPT = "ACCEPT";
    public static String ACTION_CANCEL = "CANCEL";

    private String action;
    private String metadata;

    public PaymentUpdate() {
    }

    public PaymentUpdate(String action, String metadata) {
        this.action = action;
        this.metadata = metadata;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public static PaymentUpdate accept() {
        return new PaymentUpdate(ACTION_ACCEPT, null);
    }

    public static PaymentUpdate cancel() {
        return new PaymentUpdate(ACTION_CANCEL, null);
    }

    public static PaymentUpdate withMetadata(String metadata) {
        return new PaymentUpdate(null, metadata);
    }
}
