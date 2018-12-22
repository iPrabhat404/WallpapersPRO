package com.prabhat.wallpaperspro.models;

import java.io.Serializable;

public class WallpaperCategory implements Serializable {

    private String categoryName;
    private String categoryImageUrl;

    public WallpaperCategory(String categoryName, String categoryImageUrl) {
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
    }

    public WallpaperCategory()  {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }
}
