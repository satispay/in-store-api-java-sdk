package com.satispay.protocore.models.generic;

public class ImageV1 {

    private int height;
    private String resolution;
    private String url;
    private int width;

    public ImageV1() {
    }

    public ImageV1(int height, String resolution, String url, int width) {
        this.height = height;
        this.resolution = resolution;
        this.url = url;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
