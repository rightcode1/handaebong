package com.handaebong.handaebong.model;

import android.app.Activity;

public class shop_menu_category_position_model {
    private android.app.Activity Activity;
    private String Title;
    private int Position;

    public shop_menu_category_position_model(android.app.Activity activity, String title, int position){
        this.Activity = activity;
        this.Title = title;
        this.Position = position;
    }

    public String getTitle() {
        return Title;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public int getPosition() {
        return Position;
    }
}
