package com.handaebong.handaebong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class setting_privacy extends AppCompatActivity {
    private ImageView Img_Back;
    private WebView Webview;

    private String str_url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_privacy);

        //페이지 초기화
        setInit();

        //뒤로가기 이벤트
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //스트링 정의
        str_url = "http://13.124.228.201:3000/privacy.html";

        //레이아웃 초기화 및 url 로드
        Webview = (WebView)findViewById(R.id.webview);
        Webview.getSettings().setJavaScriptEnabled(true);
        Webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        Webview.setWebChromeClient(new WebChromeClient());
        Webview.loadUrl(str_url);
    }

    //액션 이벤트
    public void setAction(){
        //뒤로가기 이벤트
        Img_Back = (ImageView)findViewById(R.id.img_back);
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
    }

    //페이지 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}

