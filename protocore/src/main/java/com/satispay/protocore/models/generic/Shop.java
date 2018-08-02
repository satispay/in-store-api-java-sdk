package com.satispay.protocore.models.generic;

public class Shop extends Model {
    private String id;
    private String phoneNumber;
    private String name;
    private String type;
    private String imageUrl;
    private Address spotAddressBean;
    private String qrCodeIdentifier;

    public Shop() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Address getAddress() {
        return spotAddressBean;
    }

    public void setAddress(Address address) {
        this.spotAddressBean = address;
    }

    public String getQrCodeIdentifier() {
        return qrCodeIdentifier;
    }

    public void setQrCodeIdentifier(String qrCodeIdentifier) {
        this.qrCodeIdentifier = qrCodeIdentifier;
    }

    public enum MobilityType {

        ON_THE_MOVE("ON_THE_MOVE"),
        STABLE("STABLE");

        private String rawValue;

        MobilityType(String rawValue) {

            this.rawValue = rawValue;

        }

        public String getRawValue() {
            return rawValue;
        }
    }

}
