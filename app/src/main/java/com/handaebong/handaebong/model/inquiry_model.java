package com.handaebong.handaebong.model;

import android.app.Activity;

public class inquiry_model {
    private android.app.Activity Activity;
    private String Id;
    private String Title;
    private String Content;
    private String Comment;
    private String CreatedAt;
    private String Commenttitle;
    private String Memo;

    public inquiry_model(android.app.Activity activity, String id, String title, String content, String comment, String createdAt, String commenttitle, String memo){
        this.Activity = activity;
        this.Id = id;
        this.Title = title;
        this.Content = content;
        this.Comment = comment;
        this.CreatedAt = createdAt;
        this.Commenttitle = commenttitle;
        this.Memo = memo;
    }

    public String getTitle() {
        return Title;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public String getId() {
        return Id;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public String getContent() {
        return Content;
    }

    public String getComment() {
        return Comment;
    }

    public String getCommenttitle() {
        return Commenttitle;
    }

    public String getMemo() {
        return Memo;
    }
}
