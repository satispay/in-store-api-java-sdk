package com.satispay.protocore.models.request;

public class CheckConsumer {

    private String id;

    public CheckConsumer() { }

    public CheckConsumer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
