package com.handaebong.handaebong;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.HttpClinet_Delete;
import com.handaebong.handaebong.Ulitility.HttpClinet_Post;

public class setting_push extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private ImageView Img_Back;
    private Switch switch_push;
    private String str_user_pushtoken = "";
    private Boolean bol_push = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_push);

        //페이지 초기화
        setInit();

        //액셔 이벤트
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //캐시 - 유저 데이터, 푸시 토큰 데이터 불러오기
        preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
        bol_push = preferences.getBoolean("user_push", true);
        str_user_pushtoken = preferences.getString("user_pushtoken", "");

        //레이아웃 초기화
        switch_push = (Switch)findViewById(R.id.switch_push);

        //현재 푸시 상태 초기화
        if(bol_push == true){
            switch_push.setChecked(true);
        }
        else{
            switch_push.setChecked(false);
        }
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

        //푸시 전화 이벤트
        switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    getFCMToken();

                    editor = preferences.edit();
                    editor.putBoolean("user_push", true);
                    editor.commit();
                }
                else{
                    HttpClinet_Delete httpClinet_post = new HttpClinet_Delete();
                    String result = httpClinet_post.HttpClinet_Delete("not", "v1/notification/remove?notificationToken="+str_user_pushtoken);

                    editor = preferences.edit();
                    editor.putBoolean("user_push", false);
                    editor.putString("user_pushtoken", "");
                    editor.commit();
                }
            }
        });
    }

    //푸시값 가져오기
    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("getting token fail","");
                return;
            }
            String token = task.getResult();

            HttpClinet_Post httpClinet_post = new HttpClinet_Post();
            httpClinet_post.HttpClinet_Post("not", "v1/notification/register", "notificationToken", token);
            str_user_pushtoken = token;

            editor = preferences.edit();
            editor.putString("user_pushtoken", token);
            editor.commit();

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