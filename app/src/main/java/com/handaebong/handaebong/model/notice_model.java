package com.handaebong.handaebong.model;

import android.app.Activity;

public class notice_model {
    private android.app.Activity Activity;
    private String Id;
    private String Title;
    private String Content;
    private String CreatedAt;

    public notice_model(android.app.Activity activity, String id, String title, String content, String createdAt){
        this.Activity = activity;
        this.Id = id;
        this.Title = title;
        this.Content = content;
        this.CreatedAt = createdAt;
    }

    public String getTitle() {
        return Title;
    }

    public String getId() {
        return Id;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public String getContent() {
        return Content;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }
}
