package com.handaebong.handaebong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class setting_menu extends AppCompatActivity {
    private ImageView Img_Back;
    private TextView Txt_Notice, Txt_Push, Txt_Privacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_menu);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //레아아웃 초기화
        Img_Back = (ImageView)findViewById(R.id.img_back);
        Txt_Notice = (TextView) findViewById(R.id.txt_notice);
        Txt_Push = (TextView) findViewById(R.id.txt_push);
        Txt_Privacy = (TextView) findViewById(R.id.txt_privacy);
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

        //문의하기 이동 이벤트
        Txt_Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_menu.this, setting_rec.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //이용약관 이동 이벤트
        Txt_Privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_menu.this, setting_privacy.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //푸시 설정 이동 이벤트
        Txt_Push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_menu.this, setting_push.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });
    }
}