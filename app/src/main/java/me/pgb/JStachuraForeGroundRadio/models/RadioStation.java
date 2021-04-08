package me.pgb.JStachuraForeGroundRadio.models;

public class RadioStation {

    private String streamLink;
    private String name;
    private String country;
    private String imageLink;

    public RadioStation(String streamLink, String name, String country, String imageLink) {
        this.streamLink = streamLink;
        this.name = name;
        this.country = country;
        this.imageLink = imageLink;
    }

    public String getStreamLink() {
        return streamLink;
    }
    public String getName(){
        return name;
    }
    public String getCountry() {
        return country;
    }
    public String getImageLink() {
        return imageLink;
    }
}