package com.example.android.booklisting;

/**
 * Class to represent an image link, with type and URL
 */

public class ImageLink {

    private String mType;
    private String mUrl;

    public ImageLink(String type, String url) {
        this.mType = type;
        this.mUrl = url;
    }

    public String getType() {
        return mType;
    }

    public String getmUrl() {
        return mUrl;
    }
}
