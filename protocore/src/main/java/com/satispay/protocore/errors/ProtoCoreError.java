package com.satispay.protocore.errors;

public class ProtoCoreError extends Throwable {
    ProtoCoreErrorType protoCoreErrorType;

    public ProtoCoreError(ProtoCoreErrorType protoCoreErrorType) {
        super(protoCoreErrorType.getMessage());
        this.protoCoreErrorType = protoCoreErrorType;
    }

    public ProtoCoreErrorType getProtoCoreErrorType() {
        return protoCoreErrorType;
    }

    public void setProtoCoreErrorType(ProtoCoreErrorType protoCoreErrorType) {
        this.protoCoreErrorType = protoCoreErrorType;
    }
}
