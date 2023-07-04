package com.satispay.protocore.models.payment;

public class PaymentOptions {

    public PaymentOptions() {}

    public boolean partial_payment;

    public PaymentOptions(boolean partialPayment) {
        partial_payment = partialPayment;
    }

    public boolean getPartialPayment() { return partial_payment; }

    public void setPartialPayment(boolean partialPayment) {
        partial_payment = partialPayment;
    }
}
