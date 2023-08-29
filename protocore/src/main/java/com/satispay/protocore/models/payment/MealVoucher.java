package com.satispay.protocore.models.payment;

public class MealVoucher {

    public MealVoucher() {}

    public long max_amount_unit;

    public int max_number;

    public MealVoucher(long maxAmountUnit) {
        max_amount_unit = maxAmountUnit;
    }

    public MealVoucher(long maxAmountUnit, int maxNumber) {
        max_amount_unit = maxAmountUnit;
        max_number = maxNumber;
    }

    public long getMaxAmountUnit() { return max_amount_unit; }

    public int getMaxNumber() { return max_number; }

    public void setMaxAmountUnit(int maxAmountUnit) {
        max_amount_unit = maxAmountUnit;
    }

    public void setMaxNumber(int maxNumber) {
        max_number = maxNumber;
    }
}