package com.handaebong.handaebong.model;

import android.app.Activity;

public class shop_focus_menu_model {
    private android.app.Activity Activity;
    private Boolean Bol_category;
    private String Category_Title;
    private String Menu_title;
    private String Menu_intro;
    private String Menu_price;
    private String Menu_contents;
    private String Menu_thumb;
    private Boolean Bol_isbest;

    public shop_focus_menu_model(android.app.Activity activity, boolean bol_category, String category_Title, String menu_title, String menu_intro, String menu_price, String menu_contents, String menu_thumb, Boolean bol_isbest){
        this.Activity = activity;
        this.Bol_category = bol_category;
        this.Category_Title = category_Title;
        this.Menu_title = menu_title;
        this.Menu_intro = menu_intro;
        this.Menu_price = menu_price;
        this.Menu_contents = menu_contents;
        this.Bol_isbest = bol_isbest;
        this.Menu_thumb = menu_thumb;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public Boolean getBol_category() {
        return Bol_category;
    }

    public Boolean getBol_isbest() {
        return Bol_isbest;
    }

    public String getCategory_Title() {
        return Category_Title;
    }

    public String getMenu_intro() {
        return Menu_intro;
    }

    public String getMenu_price() {
        return Menu_price;
    }

    public String getMenu_title() {
        return Menu_title;
    }

    public String getMenu_contents() {
        return Menu_contents;
    }

    public String getMenu_thumb() {
        return Menu_thumb;
    }
}
