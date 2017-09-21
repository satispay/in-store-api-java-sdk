package com.satispay.protocore.models.transactions;

public class CloseTransaction {

    private String state;

    public CloseTransaction() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CloseTransaction(String state) {
        this.state = state;
    }
}
