package com.satispay.protocore.errors;

public enum ProtoCoreErrorType {
    SIGNATURE_ERROR,
    DH_ERROR,
    CRYPTO_ERROR,
    INVALID_ACTIVATION_CODE;

    String message;

    ProtoCoreErrorType() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
