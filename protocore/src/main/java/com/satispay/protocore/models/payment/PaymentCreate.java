package com.satispay.protocore.models.payment;

import java.util.Date;


public class PaymentCreate {
    public static String FLOW_MATCH_CODE = "MATCH_CODE";
    public static String FLOW_REFUND = "REFUND";
    public static String MATCH_USER = "MATCH_USER";

    private String flow;
    private Long amountUnit;
    private String currency;
    private Date expirationDate;
    private String metadata;
    private String callbackUrl;
    private String parentPaymentUid;
    private String consumerId;
    private String consumerUid;
    private String comment;
    private String description;
    private String externalCode;
    private PaymentMethods payment_method_options;
    private PaymentOptions payment_options;

    public PaymentCreate() {
    }

    public PaymentCreate(String flow, Long amountUnit, String currency, Date expirationDate, String metadata, String callbackUrl, String parentPaymentUid, String consumerId, String consumerUid, String comment, String description, String externalCode, PaymentMethods paymentMethodOptions, PaymentOptions paymentOptions) {
        this.flow = flow;
        this.amountUnit = amountUnit;
        this.currency = currency;
        this.expirationDate = expirationDate;
        this.metadata = metadata;
        this.callbackUrl = callbackUrl;
        this.parentPaymentUid = parentPaymentUid;
        this.consumerId = consumerId;
        this.consumerUid = consumerUid;
        this.comment = comment;
        this.description = description;
        this.externalCode = externalCode;
        this.payment_method_options = paymentMethodOptions;
        this.payment_options = paymentOptions;
    }

    /**
     * Create a payment
     *
     * @param amountUnit       Amount of the payment in cents
     * @param currency         Currency of the payment
     * @param metadata         Generic field that can be used to store the order_id
     * @param parentPaymentUid Unique ID of the payment to refund (Required if flow is REFUND)
     */

    public static PaymentCreate refund(Long amountUnit, String currency, String metadata, String parentPaymentUid) {
        return new PaymentCreate(FLOW_REFUND, amountUnit, currency, null, metadata, null, parentPaymentUid, null, null, null, null, null, null, null);
    }

    /**
     * Create a payment
     *
     * @param amountUnit     Amount of the payment in cents
     * @param currency       Currency of the payment
     * @param expirationDate The expiration date of the payment
     * @param metadata       Generic field that can be used to store the order_id
     * @param callbackUrl    The url that will be called when the Payment changes state. When url is called a Get payment details can be called to know the new Payment status. Note that {uuid} will be replaced with the Payment ID
     */

    public static PaymentCreate matchCode(Long amountUnit, String currency, Date expirationDate, String metadata, String callbackUrl, PaymentMethods paymentMethodOptions, PaymentOptions paymentOptions) {
        return new PaymentCreate(FLOW_MATCH_CODE, amountUnit, currency, expirationDate, metadata, callbackUrl, null, null, null, null, null, null, paymentMethodOptions, paymentOptions);
    }

    /**
     * Create a payment
     *
     * @param amountUnit  Amount of the payment in cents
     * @param currency    Currency of the payment
     * @param consumerUid User ID
     * @param comment     Sent to the user
     */

    public static PaymentCreate matchUser(Long amountUnit, String currency, String consumerUid, String comment, PaymentMethods paymentMethodOptions, PaymentOptions paymentOptions) {
        return new PaymentCreate(MATCH_USER, amountUnit, currency, null, null, null, null, null, consumerUid, comment, null, null, paymentMethodOptions, paymentOptions);
    }

    /**
     * Create a payment
     *
     * @param amountUnit   Amount of the payment in cents
     * @param currency     Currency of the payment
     * @param consumerUid  User ID
     * @param description  Sent to the user
     * @param externalCode
     */

    public static PaymentCreate matchUserWorkAround(Long amountUnit, String currency, String consumerUid, String description, String externalCode, PaymentMethods paymentMethodOptions, PaymentOptions paymentOptions) {
        return new PaymentCreate(MATCH_USER, amountUnit, currency, null, null, null, null, null, consumerUid, null, description, externalCode, paymentMethodOptions, paymentOptions);
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

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerUid() {
        return consumerUid;
    }

    public void setConsumerUid(String consumerUid) {
        this.consumerUid = consumerUid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public PaymentMethods getPaymentMethodOptions() {
        return payment_method_options;
    }

    public void setPaymentMethodOptions(PaymentMethods paymentMethodOptions) {
        this.payment_method_options = paymentMethodOptions;
    }

    public PaymentOptions getPaymentOptions() {
        return payment_options;
    }

    public void setPaymentMethods(PaymentOptions paymentOptions) {
        this.payment_options = paymentOptions;
    }
}
