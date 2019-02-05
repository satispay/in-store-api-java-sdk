package com.satispay.protocore.models.payment;

import java.util.Date;


public class PaymentCreate {
    public static String FLOW_MATCH_CODE = "MATCH_CODE";
    public static String FLOW_REFUND = "REFUND";

    private String flow;
    private Long amountUnit;
    private String currency;
    private Date expirationDate;
    private String metadata;
    private String callbackUrl;
    private String parentPaymentUid;

    public PaymentCreate() {
    }

    /**
     * Create a payment
     *
     * @param flow The flow of the payment (MATCH_CODE or REFUND)
     * @param amountUnit Amount of the payment in cents
     * @param currency Currency of the payment
     * @param expirationDate The expiration date of the payment
     * @param metadata Generic field that can be used to store the order_id
     * @param callbackUrl The url that will be called when the Payment changes state. When url is called a Get payment details can be called to know the new Payment status. Note that {uuid} will be replaced with the Payment ID
     * @param parentPaymentUid Unique ID of the payment to refund (Required if flow is REFUND)
     */
    public PaymentCreate(String flow, Long amountUnit, String currency, Date expirationDate, String metadata, String callbackUrl, String parentPaymentUid) {
        this.flow = flow;
        this.amountUnit = amountUnit;
        this.currency = currency;
        this.expirationDate = expirationDate;
        this.metadata = metadata;
        this.callbackUrl = callbackUrl;
        this.parentPaymentUid = parentPaymentUid;
    }

    public static PaymentCreate refund(Long amountUnit, String currency, String metadata, String parentPaymentUid) {
        return new PaymentCreate(FLOW_REFUND, amountUnit, currency, null, metadata, null, parentPaymentUid);
    }

    public static PaymentCreate matchCode(Long amountUnit, String currency, Date expirationDate, String metadata, String callbackUrl) {
        return new PaymentCreate(FLOW_MATCH_CODE, amountUnit, currency, expirationDate, metadata, callbackUrl, null);
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(Long amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getParentPaymentUid() {
        return parentPaymentUid;
    }

    public void setParentPaymentUid(String parentPaymentUid) {
        this.parentPaymentUid = parentPaymentUid;
    }
}
