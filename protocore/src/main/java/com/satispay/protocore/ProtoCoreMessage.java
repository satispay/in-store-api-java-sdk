package com.satispay.protocore;

public class ProtoCoreMessage {

    private boolean valid;
    private String etag;

    public ProtoCoreMessage() {
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }
}
