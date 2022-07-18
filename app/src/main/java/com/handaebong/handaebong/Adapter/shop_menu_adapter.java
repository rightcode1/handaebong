package com.handaebong.handaebong.Adapter;


import static com.handaebong.handaebong.shop_focus.Img_Menu;
import static com.handaebong.handaebong.shop_focus.Layout_Menu;
import static com.handaebong.handaebong.shop_focus.Txt_Menu_Contents;
import static com.handaebong.handaebong.shop_focus.Txt_Menu_Intro;
import static com.handaebong.handaebong.shop_focus.Txt_Menu_Price;
import static com.handaebong.handaebong.shop_focus.Txt_Menu_Title;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.handaebong.handaebong.R;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.Price_Rest;
import com.handaebong.handaebong.model.shop_focus_menu_model;
import com.handaebong.handaebong.model.shoplist_menu_model;
import com.handaebong.handaebong.model.shoplist_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class shop_menu_adapter extends RecyclerView.Adapter<shop_menu_adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<shop_focus_menu_model> arrData;
    private LinearLayout Layout;
    public shop_menu_adapter(Context c, ArrayList<shop_focus_menu_model> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public shop_menu_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_menu, parent, false);
        return new shop_menu_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final shop_menu_adapter.ViewHolder holder, final int position) {
        final shop_focus_menu_model items = arrData.get(holder.getAdapterPosition());

        if(items.getBol_category() == true){
            if(position == 0){
                holder.Txt_TopLine.setVisibility(View.GONE);
            }
            else{
                holder.Txt_TopLine.setVisibility(View.VISIBLE);
            }
            holder.Txt_Title.setVisibility(View.VISIBLE);
        }
        else{
            holder.Txt_TopLine.setVisibility(View.GONE);
            holder.Txt_Title.setVisibility(View.GONE);
        }

        if(items.getBol_isbest() == true){
            holder.Img_Badge.setVisibility(View.VISIBLE);
        }
        else{
            holder.Img_Badge.setVisibility(View.GONE);
        }

        holder.Txt_Title.setText(items.getCategory_Title());
        holder.Txt_Name.setText(items.getMenu_title());
        holder.Txt_Intro.setText(items.getMenu_intro());

        Price_Rest price_rest =new Price_Rest();
        holder.Txt_Price.setText(price_rest.Price_rest(items.getMenu_price())+"원");

        if(items.getMenu_thumb().equals("null")){
            holder.Layout_Thumb.setVisibility(View.GONE);
        }
        else{
            holder.Layout_Thumb.setVisibility(View.VISIBLE);
            Glide.with(items.getActivity()).load(items.getMenu_thumb()).into(holder.Img_Thumb);
        }

        holder.Layout_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items.getMenu_thumb().equals("null") && items.getMenu_intro().equals("") && items.getMenu_contents().equals("")){
                }
                else{
                    Txt_Menu_Title.setText(items.getMenu_title());

                    if(items.getMenu_intro().equals("")){
                        Txt_Menu_Intro.setVisibility(View.GONE);
                    }
                    else{
                        Txt_Menu_Intro.setText(items.getMenu_intro());
                        Txt_Menu_Intro.setVisibility(View.VISIBLE);
                    }


                    Price_Rest price_rest =new Price_Rest();
                    Txt_Menu_Price.setText(price_rest.Price_rest(items.getMenu_price())+"원");

                    if(items.getMenu_contents().equals("")){
                        Txt_Menu_Contents.setVisibility(View.GONE);
                    }
                    else{
                        Txt_Menu_Contents.setText(items.getMenu_contents());
                        Txt_Menu_Contents.setVisibility(View.VISIBLE);
                    }

                    if(items.getMenu_thumb().equals("null")){
                        Img_Menu.setVisibility(View.GONE);
                    }
                    else{
                        Img_Menu.setVisibility(View.VISIBLE);
                        Glide.with(items.getActivity()).load(items.getMenu_thumb()).into(Img_Menu);
                    }


                    Layout_Menu.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override

    public int getItemCount() {
        return this.arrData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout Layout_Menu;
        private TextView Txt_Title, Txt_Name, Txt_Intro, Txt_Price, Txt_TopLine;
        private ImageView Img_Badge, Img_Thumb;
        public CardView Layout_Thumb;
        public ViewHolder(final View itemView) {
            super(itemView);
            Img_Badge = (ImageView) itemView.findViewById(R.id.img_badge);
            Img_Thumb = (ImageView) itemView.findViewById(R.id.img_thumb);
            Txt_Title = (TextView)itemView.findViewById(R.id.txt_title);
            Txt_Name = (TextView)itemView.findViewById(R.id.txt_name);
            Txt_Intro = (TextView)itemView.findViewById(R.id.txt_intro);
            Txt_Price = (TextView)itemView.findViewById(R.id.txt_price);
            Txt_TopLine = (TextView)itemView.findViewById(R.id.txt_topline);
            Layout_Menu = (LinearLayout)itemView.findViewById(R.id.layout_menu);
            Layout_Thumb = (CardView)itemView.findViewById(R.id.layout_thumb);
        }
    }

}

