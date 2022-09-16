package com.handaebong.handaebong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import me.drakeet.materialdialog.MaterialDialog;

public class flash extends AppCompatActivity {
    //버전 값 정의
    String Project_version = "1.0.8";
    String[] ParsedData_Setting;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String Device_Id = "";
    String Product_Id;
    Boolean bol_islogin = false;
    Boolean Guide = true;

    TimerTask myTask;
    Timer timer;
    String strCurToday;

    ImageView Img_Title, Img_Img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        //플래시 화면 gif 초기화
        Img_Title = (ImageView)findViewById(R.id.img_title);
        Img_Img = (ImageView)findViewById(R.id.img_img);
        Glide.with(this).load(R.raw.flash_title).into(Img_Title);
        Glide.with(this).load(R.raw.flash_img).into(Img_Img);

        //디바이스 값 저장
        setDeviceId();
        //현재 시간 초기화
        currentTime();
        //스토어 등록용 해시값 설정
        setKeyhash();
        //버전 값 체크하기
        Check_Setting();

    }

    //디바이스 값 저장
    public void setDeviceId(){
        preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
        Device_Id = preferences.getString("user_uuid", "");

        if(Device_Id.equals("")){
            String uuid = UUID.randomUUID().toString(); Log.d("TAG", "UUID >> " + uuid);
            editor = preferences.edit();
            editor.putString("user_uuid", uuid);
            editor.commit();
        }
    }

    //버전 값 체크하기
    public void Check_Setting(){
        HttpClient_Get http_visit = new HttpClient_Get();
        http_visit.HttpClient_Get("not","v1/visitors");

        HttpClient_Get http_setting = new HttpClient_Get();
        String result = http_setting.HttpClient_Get("not","v1/version");
        ParsedData_Setting = jsonParserList_Setting(result);
        String[] parsed = jsonParserList_Setting_and(ParsedData_Setting[0]);

        bol_islogin = Boolean.parseBoolean(parsed[1]);

        String test1 = Project_version.replace(".", "");
        String test2 = parsed[0].replace(".", "");

        int str_project_version = Integer.parseInt(test1);
        int str_server_version = Integer.parseInt(test2);

        //서버 최소 버전 값 보다 낮은 경우 스토어 이동
        if(str_project_version <  str_server_version){
            LayoutInflater inflater = (LayoutInflater) flash.this.getSystemService(flash.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_update, (ViewGroup)findViewById(R.id.Customdialog_Update_Root));
            final Button Customdialog_Update_Button_Ok = (Button)layout.findViewById(R.id.Customdialog_Update_Button_Ok);
            final MaterialDialog TeamInfo_Dialog = new MaterialDialog(flash.this);
            TeamInfo_Dialog
                    .setContentView(layout)
                    .setCanceledOnTouchOutside(true);
            TeamInfo_Dialog.show();
            Customdialog_Update_Button_Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.handaebong.handaebong"));
                    startActivity(intent);
                    finish();
                }
            });
        }
        //서버 최소 버전 보다 높은 경우 2초뒤 메인 이동
        else{
            myTask = new TimerTask() {
                int i = 2;

                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 해당 작업을 처리함
                            if (i <= 0) {
                                timer.cancel();

                                setIntent();
                                //finish();
                            }
                        }
                    });
                    i--;
                    //시간이 초과된 경우 game 내 데이터 삭제 및 초기화.

                }
            };
            timer = new Timer();
            timer.schedule(myTask, 1000, 1000); // 5초후 첫실행, 1초마다 계속실행
        }
    }
    //메인 이동 이벤트
    public void setIntent(){
        Intent intent = new Intent(flash.this, MainActivity.class).setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Product_Id != null){
            intent.putExtra("productId", Product_Id);
        }
        else{
        }
        intent.putExtra("isLogin", bol_islogin);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_fade_out);
        finish();

        //Log.i("테스트", socketConnection.login(User_Pk)+"");

        //socketConnection.emit();
        Log.d("test","test");
    }
    //현재 시간값 저장
    public void currentTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyyMMdd");
        strCurToday = CurDateFormat.format(date);
    }

    //api 파싱
    public String[] jsonParserList_Setting(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"data"};
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
    public String[] jsonParserList_Setting_and(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"android", "isLogin"};
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

    //스토어 등록용 해시값 설정
    public void setKeyhash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.joinmarket.joinmarket", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("해시", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}