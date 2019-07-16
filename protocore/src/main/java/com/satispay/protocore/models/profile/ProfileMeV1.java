package com.satispay.protocore.models.profile;

import com.satispay.protocore.models.generic.ImageV1;
import com.satispay.protocore.models.generic.LocationV1;
import com.satispay.protocore.models.generic.ShopV1;
import java.util.ArrayList;


public class ProfileMeV1 {

    private ShopV1 shop;
    private LocationV1 location;
    private ArrayList<ImageV1> images;

    public ProfileMeV1() {
    }

    public ProfileMeV1(ShopV1 shop, LocationV1 location, ArrayList<ImageV1> images) {
        this.shop = shop;
        this.location = location;
        this.images = images;
    }

    public ShopV1 getShop() {
        return shop;
    }

    public void setShop(ShopV1 shop) {
        this.shop = shop;
    }

    public LocationV1 getLocation() {
        return location;
    }

    public void setLocation(LocationV1 location) {
        this.location = location;
    }

    public ArrayList<ImageV1> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageV1> images) {
        this.images = images;
    }
}