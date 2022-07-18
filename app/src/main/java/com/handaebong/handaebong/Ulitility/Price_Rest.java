package com.handaebong.handaebong.Ulitility;

import java.text.DecimalFormat;

public class Price_Rest {
    public String Price_rest(String price){
        if(price.equals("null") || price.equals("")){
            return price;
        }
        else{
            DecimalFormat df = new DecimalFormat("#,##0");

            return df.format(Integer.parseInt(price))+"";
        }

    }
}
