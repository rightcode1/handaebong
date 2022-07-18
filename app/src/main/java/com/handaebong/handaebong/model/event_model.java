package com.handaebong.handaebong.model;

import android.app.Activity;

public class event_model {
    private android.app.Activity Activity;
    private String Id;
    private String Title;
    private String SortCode;
    private String Diff;
    private String Location;
    private String Url;
    private String Thumbnail;
    private String Image;
    private String StoreId;
    public event_model(android.app.Activity activity, String id, String title, String sortCode, String diff, String location, String url, String thumbnail, String image, String storeId){
        this.Activity = activity;
        this.Id = id;
        this.Title = title;
        this.SortCode = sortCode;
        this.Diff = diff;
        this.Location = location;
        this.Url = url;
        this.Thumbnail = thumbnail;
        this.Image = image;
        this.StoreId = storeId;
    }

    public String getId() {
        return Id;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public String getTitle() {
        return Title;
    }

    public String getDiff() {
        return Diff;
    }

    public String getImage() {
        return Image;
    }

    public String getLocation() {
        return Location;
    }

    public String getSortCode() {
        return SortCode;
    }

    public String getStoreId() {
        return StoreId;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getUrl() {
        return Url;
    }
}
