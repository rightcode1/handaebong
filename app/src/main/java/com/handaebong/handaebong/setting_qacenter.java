package com.handaebong.handaebong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class setting_qacenter extends AppCompatActivity {
    private ImageView Img_Back;
    private ImageView Img_Kakao;

    private String str_kakao = "http://pf.kakao.com/_WYctb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_qacenter);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //레이아웃 초기화
        Img_Back = (ImageView)findViewById(R.id.img_back);
        Img_Kakao = (ImageView)findViewById(R.id.img_kakao);
    }

    //액션 초기화
    public void setAction(){
        //뒤로가기 이벤트트
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        //카카오 채널 이동 이벤트
        Img_Kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_kakao));
                startActivity(intent);
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