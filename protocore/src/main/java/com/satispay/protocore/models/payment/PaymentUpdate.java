package com.satispay.protocore.models.payment;

public class PaymentUpdate {
    public static String ACTION_ACCEPT = "ACCEPT";
    public static String ACTION_CANCEL = "CANCEL";

    private String action;
    private String metadata;

    private PaymentMethods payment_method_options;

    private PaymentOptions payment_options;

    public PaymentUpdate() {
    }

    public PaymentUpdate(String action, String metadata, PaymentMethods paymentMethods, PaymentOptions paymentOptions) {
        this.action = action;
        this.metadata = metadata;
        this.payment_method_options = paymentMethods;
        this.payment_options = paymentOptions;
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

    public PaymentMethods getPaymentMethods() {
        return payment_method_options;
    }

    public void setPaymentMethods(PaymentMethods paymentMethods) {
        this.payment_method_options = paymentMethods;
    }

    public PaymentOptions getPaymentOptions() {
        return payment_options;
    }

    public void setPaymentMethods(PaymentOptions paymentOptions) {
        this.payment_options = paymentOptions;
    }

    public static PaymentUpdate accept() {
        return new PaymentUpdate(ACTION_ACCEPT, null, null, null);
    }

    public static PaymentUpdate acceptWithMealVoucherAmount(long mealVoucherAmountUnit) {
        return acceptWithMealVoucherAmountAndPaymentOptions(mealVoucherAmountUnit, null);
    }

    public static PaymentUpdate acceptWithMealVoucherAmountAndPaymentOptions(long mealVoucherAmountUnit, PaymentOptions paymentOptions) {
        PaymentMethods paymentMethods = new PaymentMethods(new MealVoucher(mealVoucherAmountUnit));
        return new PaymentUpdate(ACTION_ACCEPT, null, paymentMethods, paymentOptions);
    }

    public static PaymentUpdate acceptWithPaymentOptions(PaymentOptions paymentOptions) {
        return new PaymentUpdate(ACTION_ACCEPT, null, null, paymentOptions);
    }

    public static PaymentUpdate cancel() {
        return new PaymentUpdate(ACTION_CANCEL, null, null, null);
    }

    public static PaymentUpdate withMetadata(String metadata) {
        return new PaymentUpdate(null, metadata, null, null);
    }

}

//"payment_method_options": {
//    "meal_voucher": {
//        "enable": true,
//        "max_amount_unit": 48,
//        "max_number": 8
//    }
//},
//"payment_options": {
//    "partial_payment": false
//}