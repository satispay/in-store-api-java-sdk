package com.satispay.protocore.models.generic;

public class ShopV1 {

    private String id;
    private String merchantUid;
    private String localization;
    private String acceptance;
    private String model;
    private AddressV1 address;
    private String qrCodeIdentifier;

    public ShopV1() {
    }

    public ShopV1(String id, String merchantUid, String localization, String acceptance, String model, AddressV1 address, String qrCodeIdentifier) {
        this.id = id;
        this.merchantUid = merchantUid;
        this.localization = localization;
        this.acceptance = acceptance;
        this.model = model;
        this.address = address;
        this.qrCodeIdentifier = qrCodeIdentifier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public AddressV1 getAddress() {
        return address;
    }

    public void setAddress(AddressV1 address) {
        this.address = address;
    }

    public String getQrCodeIdentifier() {
        return qrCodeIdentifier;
    }

    public void setQrCodeIdentifier(String qrCodeIdentifier) {
        this.qrCodeIdentifier = qrCodeIdentifier;
    }

    public enum MobilityType {

        ON_THE_MOVE("ON_THE_MOVE"),
        STABLE("STABLE"),
        BRICK_AND_MORTAR("BRICK_AND_MORTAR");

        private String rawValue;

        MobilityType(String rawValue) {

            this.rawValue = rawValue;

        }

        public String getRawValue() {
            return rawValue;
        }
    }
}
