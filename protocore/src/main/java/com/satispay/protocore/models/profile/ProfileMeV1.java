package com.satispay.protocore.models.profile;

import com.satispay.protocore.models.generic.AddressV1;
import com.satispay.protocore.models.generic.ImageV1;
import com.satispay.protocore.models.generic.LocationV1;
import java.util.ArrayList;


public class ProfileMeV1 {

    private String id;
    private String merchantUid;
    private String name;
    private AddressV1 address;
    private LocationV1 geolocation;
    private ArrayList<ImageV1> images;
    private String localization;
    private String acceptance;
    private String model;
    private String qrCodeIdentifier;

    public ProfileMeV1() { }

    public ProfileMeV1(String id, String merchantUid, String name, AddressV1 address, LocationV1 geolocation, ArrayList<ImageV1> images, String localization, String acceptance, String model, String qrCodeIdentifier) {
        this.id = id;
        this.merchantUid = merchantUid;
        this.name = name;
        this.address = address;
        this.geolocation = geolocation;
        this.images = images;
        this.localization = localization;
        this.acceptance = acceptance;
        this.model = model;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressV1 getAddress() {
        return address;
    }

    public void setAddress(AddressV1 address) {
        this.address = address;
    }

    public LocationV1 getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(LocationV1 geolocation) {
        this.geolocation = geolocation;
    }

    public ArrayList<ImageV1> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageV1> images) {
        this.images = images;
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