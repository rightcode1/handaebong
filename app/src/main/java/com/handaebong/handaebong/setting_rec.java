package com.handaebong.handaebong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.handaebong.handaebong.Adapter.inquiry_adapter;
import com.handaebong.handaebong.Adapter.notice_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.model.inquiry_model;
import com.handaebong.handaebong.model.notice_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class setting_rec extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private ImageView Img_Back;
    private ImageView Img_Input;

    public ArrayList<inquiry_model> inquiry_models;
    public inquiry_adapter inquiry_adapters;
    private RecyclerView List_Content;
    private RelativeLayout Layout_None;

    private String str_user_name;
    private String str_user_deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_rec);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();

        //내 문의 내역 api 호출
        Async async = new Async();
        async.execute();
    }

    //등록 후 페이지 돌아왔을 때 이벤트
    @Override
    protected void onRestart() {
        super.onRestart();
        // 내 문의 내역 재호출
        Async async = new Async();
        async.execute();
    }

    //페이지 초기화
    public void setInit() {
        //캐시 - 유저 이름, uuid 불러오기
        preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
        str_user_name = preferences.getString("user_name", "");
        str_user_deviceId = preferences.getString("user_uuid", "");

        //레이아웃 초기화
        Img_Back = (ImageView)findViewById(R.id.img_back);
        Img_Input = (ImageView)findViewById(R.id.img_input);
        List_Content = (RecyclerView)findViewById(R.id.list_contents);
        Layout_None = (RelativeLayout)findViewById(R.id.layout_none);
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

        //문의 등록하기 페이지 이동
        Img_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_rec.this, setting_rec_input.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });
    }

    //내 문의내역 api 호출
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        String[] parseredData_access;
        String[][] parseredData_list;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(setting_rec.this,"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {
                HttpClient_Get http = new HttpClient_Get();
                String result = http.HttpClient_Get("", "v1/inquiry/list?deviceId="+str_user_deviceId);
                parseredData_access = jsonParserList_access(result);

                inquiry_models = new ArrayList<inquiry_model>();

                if(parseredData_access[0].equals("success")){
                    parseredData_list = jsonParserList_List(result);

                    for (int i = 0; i < parseredData_list.length; i++) {
                        String id = parseredData_list[i][0];
                        String title = parseredData_list[i][1];
                        String content = parseredData_list[i][2];
                        String comment = parseredData_list[i][3];
                        String createdAt = parseredData_list[i][4];
                        String commentTitle = parseredData_list[i][5];
                        String memo = parseredData_list[i][6];

                        inquiry_models.add(new inquiry_model(setting_rec.this, id, title, content, comment, createdAt, commentTitle, memo));
                    }
                }

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

            if(parseredData_list.length == 0){
                Layout_None.setVisibility(View.VISIBLE);
                List_Content.setVisibility(View.GONE);
            }
            else{
                Layout_None.setVisibility(View.GONE);
                List_Content.setVisibility(View.VISIBLE);

                LinearLayoutManager layoutManager = new LinearLayoutManager(setting_rec.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager.scrollToPosition(0);

                inquiry_adapters = new inquiry_adapter(setting_rec.this, inquiry_models);
                List_Content.setLayoutManager(layoutManager);
                List_Content.setAdapter(inquiry_adapters);
            }

            progressDialog.dismiss();
        }
    }

    //api 파싱
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

    //api 파싱
    public String[][] jsonParserList_List(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");

            String[] jsonName = {"id","title", "content", "comment", "createdAt", "commentTitle", "memo"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
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