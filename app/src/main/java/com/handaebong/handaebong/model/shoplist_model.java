package com.handaebong.handaebong.model;

import android.app.Activity;

public class shoplist_model {
    private Activity Activity;
    private String Id;
    private String Title;
    private String Intro;
    private String Like;
    private String Thumb1;
    private String Thumb2;
    private String Thumb3;

    public shoplist_model(android.app.Activity activity, String id, String title, String intro, String like, String thumb1, String thumb2, String thumb3){
        this.Activity = activity;
        this.Id = id;
        this.Title = title;
        this.Intro = intro;
        this.Like = like;
        this.Thumb1 = thumb1;
        this.Thumb2 = thumb2;
        this.Thumb3 = thumb3;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public String getId() {
        return Id;
    }

    public String getLike() {
        return Like;
    }

    public String getIntro() {
        return Intro;
    }

    public String getTitle() {
        return Title;
    }

    public String getThumb1() {
        return Thumb1;
    }

    public String getThumb2() {
        return Thumb2;
    }

    public String getThumb3() {
        return Thumb3;
    }
}
