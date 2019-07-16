package com.satispay.protocore.models;

public class Image {

    private String url;
    private Integer width;
    private Integer height;
    private String resolution;

    public Image() { }

    public Image(String url, Integer width, Integer height, String resolution) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.resolution = resolution;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public Integer getWidth() { return width; }

    public void setWidth(Integer width) { this.width = width; }

    public Integer getHeight() { return height; }

    public void setHeight(Integer height) { this.height = height; }

    public String getResolution() { return resolution; }

    public void setResolution(String resolution) { this.resolution = resolution; }
}
