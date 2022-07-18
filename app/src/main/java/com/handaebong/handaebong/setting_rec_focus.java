package com.handaebong.handaebong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.handaebong.handaebong.Ulitility.HttpClient;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;

import org.json.JSONException;
import org.json.JSONObject;

public class setting_rec_focus extends AppCompatActivity {
    private ImageView Img_Back;

    private TextView Txt_Title;
    private TextView Txt_Content;
    private TextView Txt_Date;
    private TextView Txt_Status;
    private TextView Txt_Comment_Title;
    private TextView Txt_Comment_Contents;

    private String str_title;
    private String str_content;
    private String str_date;
    private String str_comment_title;
    private String str_comment_contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_rec_focus);

        //페이지 초기화
        setInit();

        //뒤로가기 이벤트
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //이전 페이지 데이터 전송
        Intent intent1 = getIntent();
        str_title = intent1.getStringExtra("title");
        str_content = intent1.getStringExtra("content");
        str_date = intent1.getStringExtra("date");
        str_comment_title = intent1.getStringExtra("comment_title");
        str_comment_contents = intent1.getStringExtra("comment_comment");

        //레이아웃 초기화
        Img_Back = (ImageView)findViewById(R.id.img_back);
        Txt_Title = (TextView) findViewById(R.id.txt_title);
        Txt_Content = (TextView)findViewById(R.id.txt_content);
        Txt_Date = (TextView) findViewById(R.id.txt_date);
        Txt_Status = (TextView) findViewById(R.id.txt_status);
        Txt_Comment_Title = (TextView) findViewById(R.id.txt_comment_title);
        Txt_Comment_Contents = (TextView) findViewById(R.id.txt_comment_contents);

        //이전 페이지 데이터 레이아웃 초기 셋팅
        Txt_Title.setText(str_title);
        Txt_Content.setText(str_content);
        Txt_Date.setText(str_date);

        if(str_comment_title.equals("null")){
            Txt_Status.setText("답변대기");
            Txt_Status.setTextColor(Color.parseColor("#fe4e4d"));

            Txt_Comment_Title.setVisibility(View.GONE);
            Txt_Comment_Contents.setVisibility(View.GONE);
        }
        else{
            Txt_Status.setText("답변완료");
            Txt_Status.setTextColor(Color.parseColor("#85d800"));

            Txt_Comment_Title.setVisibility(View.VISIBLE);
            Txt_Comment_Contents.setVisibility(View.VISIBLE);

            Txt_Comment_Title.setText(str_comment_title);
            Txt_Comment_Contents.setText(str_comment_contents);
        }
    }

    //액션 이벤트
    public void setAction(){
        //뒤로가기 이벤트
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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