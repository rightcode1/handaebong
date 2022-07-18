package com.handaebong.handaebong.Adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import static com.handaebong.handaebong.MainActivity.screen_width;
import static com.handaebong.handaebong.setting_like.Shop_Adapter;
import static com.handaebong.handaebong.setting_like.Shoplist_models;
import static com.handaebong.handaebong.setting_like.act_setting_like;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.handaebong.handaebong.R;
import com.handaebong.handaebong.Ulitility.OnSingleClickListener;
import com.handaebong.handaebong.model.shoplist_model;
import com.handaebong.handaebong.shop_focus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

public class shop_adapter extends RecyclerView.Adapter<shop_adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<shoplist_model> arrData;
    private LinearLayout Layout;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public shop_adapter(Context c, ArrayList<shoplist_model> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public shop_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop, parent, false);
        return new shop_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final shop_adapter.ViewHolder holder, final int position) {
        final shoplist_model items = arrData.get(position);

        holder.Txt_Title.setText(items.getTitle());
        holder.Txt_Menu.setText(items.getIntro());

        //가로의 332분할
        int oneSize = screen_width/332;
        //높이 적용
        int height = oneSize*176;
        //높이 적용
        int height_3img = oneSize*88;

        ViewGroup.LayoutParams params = holder.Layout_Img.getLayoutParams();
        params.height = height;
        holder.Layout_Img.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = holder.Layout_3.getLayoutParams();
        params1.width = height_3img;
        holder.Layout_3.setLayoutParams(params1);

        //좋아요 여부
        preferences = items.getActivity().getSharedPreferences("myapplication", MODE_PRIVATE);
        String str_like = preferences.getString("user_like", "");
        String[] splitText = str_like.split("_");
        boolean bol_like = false;
        for(int i = 0; i < splitText.length; i++){
            if(items.getId().equals(splitText[i])){
                bol_like = true;
            }
        }

        if(bol_like == true){
            holder.Img_Like.setImageResource(R.drawable.like_on);
        }
        else{
            holder.Img_Like.setImageResource(R.drawable.like_off);
        }

        if(items.getThumb3().equals("")){
            holder.Layout_3.setVisibility(View.GONE);
            holder.Img_3_line.setVisibility(View.GONE);

            Glide.with(items.getActivity()).load(items.getThumb1()).into(holder.Img_Thumb1);

        }
        else{
            holder.Layout_3.setVisibility(View.VISIBLE);
            holder.Img_3_line.setVisibility(View.VISIBLE);

            Glide.with(items.getActivity()).load(items.getThumb1()).into(holder.Img_Thumb1);
            Glide.with(items.getActivity()).load(items.getThumb1()).into(holder.Img_Thumb1);
            Glide.with(context).load(items.getThumb2()).into(holder.Img_Thumb2);
            Glide.with(context).load(items.getThumb3()).into(holder.Img_Thumb3);
        }

        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(items.getActivity(), shop_focus.class);
                intent.putExtra("id", items.getId());
                items.getActivity().startActivity(intent);
                items.getActivity().overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        holder.Img_Like.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Drawable temp = holder.Img_Like.getDrawable();
                Drawable temp1 = context.getResources().getDrawable(R.drawable.like_on);

                Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
                Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();
                if(tmpBitmap.equals(tmpBitmap1)){
                    Toast.makeText(items.getActivity(), "찜 목록에 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    holder.Img_Like.setImageResource(R.drawable.like_off);

                    String str_like = preferences.getString("user_like", "");
                    str_like = str_like.replace("_"+items.getId(),"");
                    editor = preferences.edit();
                    editor.putString("user_like", str_like);
                    editor.commit();

                    if(act_setting_like != null){
                        Shoplist_models.remove(items);
                        Shop_Adapter.notifyDataSetChanged();
                    }
                }
                else{
                    Toast.makeText(items.getActivity(), "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    holder.Img_Like.setImageResource(R.drawable.like_on);

                    String str_like = preferences.getString("user_like", "");
                    str_like = str_like+"_"+items.getId();
                    editor = preferences.edit();
                    editor.putString("user_like", str_like);
                    editor.commit();
                }
                }
            }
        );

     /*   holder.Img_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        /*ViewGroup.LayoutParams params = holder.Layout_Img.getLayoutParams();
        params.height = screen_width;
        holder.Layout_Img.setLayoutParams(params);*/
    }
    @Override

    public int getItemCount() {
        return this.arrData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView Txt_Title, Txt_Menu, Txt_Time;
        private ImageView Img_Like, Img_3_line;
        private ImageView Img_Thumb1, Img_Thumb2, Img_Thumb3;
        private LinearLayout Layout;
        private LinearLayout Layout_3;
        private LinearLayout Layout_Img;
        public ViewHolder(final View itemView) {
            super(itemView);
            Txt_Title = (TextView)itemView.findViewById(R.id.txt_title);
            Txt_Menu = (TextView)itemView.findViewById(R.id.txt_intro);
            Img_Like = (ImageView) itemView.findViewById(R.id.img_like);
            Img_Thumb1 = (ImageView) itemView.findViewById(R.id.img_thumb1);
            Img_Thumb2 = (ImageView) itemView.findViewById(R.id.img_thumb2);
            Img_Thumb3 = (ImageView) itemView.findViewById(R.id.img_thumb3);

            Img_3_line = (ImageView) itemView.findViewById(R.id.img_3_line);
            Layout_3 = (LinearLayout) itemView.findViewById(R.id.layout_3);

            Layout = (LinearLayout) itemView.findViewById(R.id.layout);
            Layout_Img = (LinearLayout) itemView.findViewById(R.id.layout_img);
        }
    }


}