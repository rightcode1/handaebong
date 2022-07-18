package com.handaebong.handaebong;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.handaebong.handaebong.Adapter.notice_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.model.notice_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class setting_notice_focus extends AppCompatActivity {
    private ImageView Img_Next;
    private TextView Txt_Title, Txt_Date, Txt_Content;

    private String str_title, str_content, str_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notice_focus);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();
    }

    //페이지 초기화화
    public void setInit() {
        //이전 페이지 데이터 전송
        Intent intent1 = getIntent();
        str_title = intent1.getStringExtra("title");
        str_content = intent1.getStringExtra("content");
        str_date = intent1.getStringExtra("date");

        //레이아웃 초기화
        Txt_Title = (TextView) findViewById(R.id.txt_title);
        Txt_Date = (TextView) findViewById(R.id.txt_date);
        Txt_Content = (TextView) findViewById(R.id.txt_content);

        //레이아웃 초기 데이터 넣기
        Txt_Title.setText(str_title);
        Txt_Date.setText(str_date);
        Txt_Content.setText(str_content);
    }

    //액션 이벤트
    public void setAction(){
        //뒤로가기 이벤트
        Img_Next = (ImageView)findViewById(R.id.img_back);
        Img_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
    }

    //페이지 종료 이벤트
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}