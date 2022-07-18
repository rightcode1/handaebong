package com.handaebong.handaebong;

import android.app.Activity;
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

import com.handaebong.handaebong.Adapter.shop_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.model.shoplist_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class setting_like extends AppCompatActivity {
    private ImageView Img_Back;
    public static Activity act_setting_like;

    private SharedPreferences preferences;

    public RecyclerView List_Contents;
    public static ArrayList<shoplist_model> Shoplist_models;
    public static shop_adapter Shop_Adapter;
    private RelativeLayout Layout_None;

    String str_like = "";
    int likecount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_like);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //캐시 - 좋아요 내역
        preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
        str_like = preferences.getString("user_like", "");

        act_setting_like = setting_like.this;

        //레이아웃 초기화
        Img_Back = (ImageView)findViewById(R.id.img_back);
        List_Contents = (RecyclerView)findViewById(R.id.list_contents);
        Shoplist_models = new ArrayList<shoplist_model>();
        Layout_None = (RelativeLayout)findViewById(R.id.layout_none);

        //좋아요한 상점 api 호출
        Async async = new Async();
        async.execute();
    }

    //액션 이벤트
    public void setAction(){
        //뒤로가기 이벤트트
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_none);
            }
        });
    }

    //좋아요 업체 리스트 api
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        String[] parseredData_access, parseredData_info;
        String[][] parseredData_list;
        String[] parseredData_list_img;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(setting_like.this,"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {

                HttpClient_Get http = new HttpClient_Get();

                String result_info = http.HttpClient_Get("not", "v1/store/list");
                parseredData_info = jsonParserList_access(result_info);

                if(parseredData_info[0].equals("success")){
                    parseredData_list = jsonParserList_ProductList(result_info);

                    for(int i =0; i < parseredData_list.length;i++){
                        String str_id = parseredData_list[i][0];
                        String str_category = parseredData_list[i][1];
                        String str_name = parseredData_list[i][2];
                        String str_tel = parseredData_list[i][3];
                        String str_address = parseredData_list[i][4];
                        String str_intro = parseredData_list[i][5];
                        String str_info = parseredData_list[i][6];
                        String str_time = parseredData_list[i][7];
                        String str_content = parseredData_list[i][8];
                        String str_story = parseredData_list[i][9];
                        String str_origin = parseredData_list[i][10];
                        String str_thumb1 = parseredData_list[i][11];
                        String str_thumb2 = parseredData_list[i][12];
                        String str_thumb3 = parseredData_list[i][13];

                        String[] splitText = str_like.split("_");
                        boolean bol_like = false;
                        for(int j = 0; j < splitText.length; j++){
                            if(str_id.equals(splitText[j])){
                                bol_like = true;
                            }
                        }

                        if(bol_like == true){
                            likecount++;
                            Shoplist_models.add(new shoplist_model(setting_like.this, str_id, str_name, str_intro, "true", str_thumb1, str_thumb2, str_thumb3));
                        }
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

            if(likecount ==0){
                Layout_None.setVisibility(View.VISIBLE);
                List_Contents.setVisibility(View.GONE);
            }
            else{
                Layout_None.setVisibility(View.GONE);
                List_Contents.setVisibility(View.VISIBLE);
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(setting_like.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);

            Shop_Adapter = new shop_adapter(setting_like.this, Shoplist_models);
            List_Contents.setLayoutManager(layoutManager);
            List_Contents.setAdapter(Shop_Adapter);

            progressDialog.dismiss();
        }
    }

    //api 파싱
    public String[] jsonParserList_access(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용1", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"message", "list"};
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
    public String[][] jsonParserList_ProductList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용_", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");
            Log.i("서버에서 받은 전체 내용_", "1");
            String[] jsonName = {"id", "category", "name", "tel", "address", "intro","info", "time", "content", "story", "origin","thumbnails","thumbnails","thumbnails"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }

                //이미지 파싱
                JSONObject json_ = new JSONObject(jArr.getJSONObject(i).toString());
                JSONArray jArr_ = json_.getJSONArray("thumbnails");

                String[] jsonName_ = {"name"};
                String[][] parseredData_ = new String[jArr_.length()][jsonName_.length];

                for (int i_ = 0; i_ < jArr_.length(); i_++) {
                    json_ = jArr_.getJSONObject(i_);
                    for (int j_ = 0; j_ < jsonName_.length; j_++) {
                        parseredData_[i_][j_] = json_.getString(jsonName_[j_]);
                    }
                }
                Log.i("테스트1", parseredData_.length+"");
                if(parseredData_.length == 0){
                    parseredData[i][11] = "";
                    parseredData[i][12] = "";
                    parseredData[i][13] = "";
                }
                else if(parseredData_.length == 1){
                    parseredData[i][11] = parseredData_[0][0];
                    parseredData[i][12] = "";
                    parseredData[i][13] = "";
                }
                else if(parseredData_.length == 2){
                    parseredData[i][11] = parseredData_[0][0];
                    parseredData[i][12] = parseredData_[1][0];
                    parseredData[i][13] = "";
                }
                else if(parseredData_.length == 3){
                    parseredData[i][11] = parseredData_[0][0];
                    parseredData[i][12] = parseredData_[1][0];
                    parseredData[i][13] = parseredData_[2][0];
                }
                /*parseredData[i][11] = "not";
                parseredData[i][12] = "not";
                parseredData[i][13] = "not";*/
            }

            return parseredData;
        } catch (JSONException e) {
            Log.i("ㅌㅅㅌ", e+"");
            e.printStackTrace();
            return null;
        }
    }
}