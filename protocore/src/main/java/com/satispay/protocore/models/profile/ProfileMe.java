package com.satispay.protocore.models.profile;

import com.satispay.protocore.models.generic.Device;
import com.satispay.protocore.models.generic.Shop;

public class ProfileMe {
    private Device spotDeviceBean;
    private Shop spotShopBean;


    public ProfileMe() {
    }

    public Device getDevice() {
        return spotDeviceBean;
    }

    public void setDevice(Device device) {
        this.spotDeviceBean = device;
    }

    public Shop getShop() {
        return spotShopBean;
    }

    public void setShop(Shop shop) {
        this.spotShopBean = shop;
    }
}
