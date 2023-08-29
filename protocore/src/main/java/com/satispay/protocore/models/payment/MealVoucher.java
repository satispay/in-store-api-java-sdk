package com.satispay.protocore.models.payment;

public class MealVoucher {

    public MealVoucher() {}

    public long max_amount_unit;

    public MealVoucher(long maxAmountUnit) {
        max_amount_unit = maxAmountUnit;
    }

    public MealVoucher(long maxAmountUnit, int maxNumber) {
        max_amount_unit = maxAmountUnit;
    }

    public long getMaxAmountUnit() { return max_amount_unit; }

    public void setMaxAmountUnit(int maxAmountUnit) {
        max_amount_unit = maxAmountUnit;
    }

}