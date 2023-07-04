package com.satispay.protocore.models.generic;

public class ProfilePaymentMethods {

    private boolean meal_voucher;

    public ProfilePaymentMethods() {}

    public ProfilePaymentMethods(boolean mealVoucher) {
        meal_voucher = mealVoucher;
    }

    public boolean getMealVoucher() {
        return meal_voucher;
    }

    public void setMealVoucher(boolean mealVoucher) {
        meal_voucher = mealVoucher;
    }
}
