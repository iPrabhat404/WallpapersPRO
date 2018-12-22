package com.prabhat.wallpaperspro.models;

public class FavoriteWallpapersModel {

    private int id;
    private String wallpaperURL;

    public FavoriteWallpapersModel(int id, String wallpaperURL) {
        this.id = id;
        this.wallpaperURL = wallpaperURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWallpaperURL() {
        return wallpaperURL;
    }

    public void setWallpaperURL(String wallpaperURL) {
        this.wallpaperURL = wallpaperURL;
    }
}
