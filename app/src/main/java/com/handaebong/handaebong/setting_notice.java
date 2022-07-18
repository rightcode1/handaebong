package com.handaebong.handaebong;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

public class setting_notice extends AppCompatActivity {
    public ArrayList<notice_model> notice_model;
    public notice_adapter notice_adapters;

    private ImageView Img_Next;
    private RecyclerView List_Notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notice);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //레이아웃 초기화
        List_Notice = (RecyclerView)findViewById(R.id.list_notice);

        //모델 초기화
        notice_model = new ArrayList<notice_model>();

        //문의 내역 리스트 api 호출
        Async async = new Async();
        async.execute();
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

    //api 호출
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        String[] parseredData_access;
        String[][] parseredData_list;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(setting_notice.this,"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {
                HttpClient_Get http = new HttpClient_Get();
                String result = http.HttpClient_Get("", "v1/notice/list");
                parseredData_access = jsonParserList_access(result);

                if(parseredData_access[0].equals("success")){
                    parseredData_list = jsonParserList_List(result);

                    for (int i = 0; i < parseredData_list.length; i++) {
                        String id = parseredData_list[i][0];
                        String title = parseredData_list[i][1];
                        String content = parseredData_list[i][2];
                        String createdAt = parseredData_list[i][3];
                        notice_model.add(new notice_model(setting_notice.this, id, title, content, createdAt));
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

            LinearLayoutManager layoutManager = new LinearLayoutManager(setting_notice.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);

            notice_adapters = new notice_adapter(setting_notice.this, notice_model);
            List_Notice.setLayoutManager(layoutManager);
            List_Notice.setAdapter(notice_adapters);

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

            String[] jsonName = {"id","title", "content", "createdAt"};
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

}