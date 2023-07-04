package com.satispay.protocore.models.payment;

public class PaymentMethods {

    public PaymentMethods() {}

    public MealVoucher meal_voucher;

    public PaymentMethods(MealVoucher mealVoucher) {
        meal_voucher = mealVoucher;
    }

    public MealVoucher getMealVoucher() { return meal_voucher; }

    public void setMealVoucher(MealVoucher mealVoucher) {
        meal_voucher = mealVoucher;
    }
}

