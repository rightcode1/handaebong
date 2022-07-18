package com.handaebong.handaebong;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.handaebong.handaebong.Adapter.shop_menu_adapter;
import com.handaebong.handaebong.Adapter.shop_menu_category_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.model.shop_focus_menu_model;
import com.handaebong.handaebong.model.shop_menu_category_model;

import org.json.JSONException;
import org.json.JSONObject;

public class setting_qa extends AppCompatActivity {
    private ImageView Img_Back;
    private EditText Edit_Name;
    private EditText Edit_Phone;
    private EditText Edit_Email;
    private EditText Edit_Content;

    private ImageView Img_Input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_qa);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();
    }

    //페이지 초기화화
    public void setInit() {
        //레이아웃 초기화
        Img_Back = (ImageView)findViewById(R.id.img_back);
        Edit_Name = (EditText) findViewById(R.id.edit_name);
        Edit_Phone = (EditText) findViewById(R.id.edit_phone);
        Edit_Email = (EditText) findViewById(R.id.edit_email);
        Edit_Content = (EditText) findViewById(R.id.edit_content);
        Img_Input = (ImageView)findViewById(R.id.img_input);
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

        //문의하기 이벤트
        Img_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edit_Name.getText().toString().equals("")){
                    Toast.makeText(setting_qa.this, "이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Edit_Phone.getText().toString().equals("")){
                        Toast.makeText(setting_qa.this, "휴대폰번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(Edit_Email.getText().toString().equals("")){
                            Toast.makeText(setting_qa.this, "이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(Edit_Content.getText().toString().equals("")){
                                Toast.makeText(setting_qa.this, "문의내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //입점 문의 api 호출
                                Async async = new Async();
                                async.execute();
                            }
                        }
                    }
                }
            }
        });
    }

    //입점 문의 등록 api 호출
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        String[] parseredData_access;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(setting_qa.this,"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {
                HttpClient http = new HttpClient();
                String result = http.HttpClient( "v1/storeInquiry/register", "name", Edit_Name.getText().toString(),"tel", Edit_Content.getText().toString(), "email", Edit_Email.getText().toString(), "content", Edit_Content.getText().toString());
                parseredData_access = jsonParserList_access(result);

                return "succed";
            } catch (Exception e) {
                Log.i("테스트0", e+ "테스트");
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(parseredData_access[0].equals("success")){
                Toast.makeText(setting_qa.this, "입점 문의가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }

            progressDialog.dismiss();
        }
    }

    //api 호출
    public String[] jsonParserList_access(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"message"};
            String[] parseredData = new String[jsonName.length];
            for (int j = 0; j < jsonName.length; j++) {
                parseredData[j] = json.getString(jsonName[j]);
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //페이지 종료 이벤트
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}