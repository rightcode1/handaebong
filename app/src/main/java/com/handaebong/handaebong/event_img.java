package com.handaebong.handaebong;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.handaebong.handaebong.Adapter.notice_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.model.notice_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//사용하지 않는 페이지입니다.
public class event_img extends AppCompatActivity {
    private ImageView Img_Next, Img;
    private String str_img = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        setInit();

    }

    public void setInit() {

        Intent intent1 = getIntent();
        str_img = intent1.getStringExtra("img");

        Img = (ImageView)findViewById(R.id.img);
        Glide.with(event_img.this).load(str_img).into(Img);
        //Glide.with(this).load(R.raw.home_chat).into(Img);
        Img_Next = (ImageView)findViewById(R.id.img_back);
        Img_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

    }
}