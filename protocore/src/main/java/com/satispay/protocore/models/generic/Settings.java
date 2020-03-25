package com.satispay.protocore.models.generic;

public class Settings {

    private boolean paymentRequest;

    public Settings() { }

    public Settings(boolean paymentRequest) { this.paymentRequest = paymentRequest; }

    public boolean isPaymentRequest() { return paymentRequest; }

    public void setPaymentRequest(boolean paymentRequest) { this.paymentRequest = paymentRequest; }
}
