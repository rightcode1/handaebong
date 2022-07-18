package com.handaebong.handaebong.model;

import android.app.Activity;

public class shoplist_menu_model {
    private android.app.Activity Activity;
    private String Title;
    private Boolean Bol_choice;

    public shoplist_menu_model(android.app.Activity activity, String title, boolean bol_choice){
        this.Activity = activity;
        this.Title = title;
        this.Bol_choice = bol_choice;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public String getTitle() {
        return Title;
    }

    public Boolean getBol_choice() {
        return Bol_choice;
    }
}
