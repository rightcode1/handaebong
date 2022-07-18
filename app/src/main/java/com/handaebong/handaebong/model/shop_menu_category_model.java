package com.handaebong.handaebong.model;

public class shop_menu_category_model {
    private android.app.Activity Activity;
    private String Title;
    private Boolean Bol_choice;

    public shop_menu_category_model(android.app.Activity activity, String title, boolean bol_choice){
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
