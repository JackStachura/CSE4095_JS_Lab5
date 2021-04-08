package me.pgb.JStachuraForeGroundRadio.models;

public class RadioStationArray {

    private static String[] stringList;

    public static RadioStation[] radioStations = {
            new RadioStation("http://vprbbc.streamguys.net:80/vprbbc24.mp3" ,"BBC", "UK", ""),
            new RadioStation("https://playerservices.streamtheworld.com/api/livestream-redirect/WMEAFM.mp3", "Maine Public Radio", "USA", ""),
            new RadioStation("http://stream.whus.org:8000/whusfm", "UConn Student Radio", "USA", ""),
            new RadioStation("http://89.16.185.174:8000/stream", "Linn Jazz", "UK", "")
    };

    public static String[] getArrayOfRadioLinks() {
        stringList = new String[radioStations.length];

        for (int i = 0; i < radioStations.length; i++) {
            stringList[i] = radioStations[i].getStreamLink();
        }
        return stringList;
    }

    public static String[] getArrayOfRadioNames() {
        stringList = new String[radioStations.length];

        for (int i = 0; i < radioStations.length; i++) {
            stringList[i] = radioStations[i].getName();
        }
        return stringList;
    }
}
