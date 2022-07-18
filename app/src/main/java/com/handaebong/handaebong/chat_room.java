package com.handaebong.handaebong;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.handaebong.handaebong.Adapter.chat_message_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.HttpClinet_Delete;
import com.handaebong.handaebong.Ulitility.HttpClinet_Post;
import com.handaebong.handaebong.Ulitility.SocketConnection;
import com.handaebong.handaebong.model.chat_message_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class chat_room extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public String str_nick = "";
    public String str_deviceId = "";
    private SocketConnection socketConnection;
    private chat_message_adapter Chat_message_adapter;

    private ImageView Img_Input, Img_Back;
    private TextView Txt_Nickname;
    public List<String> photos;
    private EditText Edit_Message;
    public static RecyclerView List_Chat;

    private TextView Txt_Title;
    private Boolean bol_nickcheck;
    private String nickcheck_name;
    public static ArrayList<chat_message_model> Chat_message_models;
    public static ArrayList<chat_message_model> Chat_message_all_models;
    public static LinearLayout Layout_Answer;
    private ImageView Img_Answer_Cancel;
    public static TextView Txt_Answer_Nickname;
    public static TextView Txt_Answer_Content;
    private LinearLayout Layout_Nickname;
    public static int answer_id = 0;

    public String str_exist_id = "";

    public int position = 0;
    public String str_chatdate = "";
    public String str_chattime = "";
    public String str_chatusername = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //페이지 초기화
        setInit();

        //액션 이벤트
        setAction();
    }

    public void setInit() {
        //캐시 - 유저 이름, uuid 불러오기
        preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
        str_nick = preferences.getString("user_name", "");
        str_deviceId = preferences.getString("user_deviceId", "");

        //소켓 커넥션
        SocketConnection.getInstance().disconnect();
        socketConnection = SocketConnection.getInstance();
        socketConnection.on().connect();

        //레이아웃 초기화
        Txt_Nickname = (TextView)findViewById(R.id.txt_nickname);
        Txt_Nickname.setText(str_nick);

        Layout_Answer = (LinearLayout)findViewById(R.id.layout_answer);
        Img_Answer_Cancel = (ImageView)findViewById(R.id.img_answer_cancel);
        Txt_Answer_Nickname = (TextView)findViewById(R.id.txt_answer_nickname);
        Txt_Answer_Content = (TextView)findViewById(R.id.txt_answer_content);
        Layout_Nickname = (LinearLayout)findViewById(R.id.layout_nickname);

        Txt_Title = (TextView)findViewById(R.id.txt_title);

        List_Chat = (RecyclerView)findViewById(R.id.list_chat);
        Img_Input = (ImageView)findViewById(R.id.img_input);
        Edit_Message = (EditText)findViewById(R.id.edit_message);

        //모델 초기화
        Chat_message_models = new ArrayList<chat_message_model>();
        Chat_message_all_models = new ArrayList<chat_message_model>();

        //소켓 로그인
        login();
    }

    //페이지 종료 이벤트
    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        super.onBackPressed();

    }

    //액션 이벤트
    public void setAction(){
        //뒤로가기 이벤트
        Img_Back = (ImageView) findViewById(R.id.img_back);
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        //닉네임 수정 이벤트
        Txt_Nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChatNick_Input();
            }
        });

        //닉네임 수정 이벤트
        Layout_Nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChatNick_Input();
            }
        });

        //답변하기 닫기 이벤트
        Img_Answer_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Layout_Answer.setVisibility(View.GONE);
            }
        });

        //채팅 입력 이벤트
        Img_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Edit_Message.getText().toString().equals("")){
                    Toast.makeText(chat_room.this, "메시지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        JSONObject data = new JSONObject();
                        data.put("message", Edit_Message.getText().toString());
                        if(Layout_Answer.getVisibility() == View.VISIBLE){
                            data.put("messageId", answer_id);
                        }
                        socketConnection.emit("sendMessage", data);
                    } catch (Exception e) {
                        Log.e("tete",e.getMessage());
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(Edit_Message.getWindowToken(), 0);
                    Edit_Message.setText("");
                    Layout_Answer.setVisibility(View.GONE);
                }
            }
        });
    }

    //api 파싱
    public String[][] jsonParserList_List(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("data");

            String[] jsonName = {"id", "type", "message", "userName","deviceId", "createdAt", "chatMessage"};
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
    public String[] jsonParserList_refresh(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"id", "type", "message", "userName", "deviceId", "createdAt", "chatMessage"};
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
    public String[] jsonParserList_answer(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"id", "type", "message", "userName", "deviceId", "createdAt"};
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

    //소켓 로그인하기
    private void login() {
        JSONObject data = new JSONObject();
        try {
            data.put("name", str_nick);
            data.put("deviceId", str_deviceId);
            //Log.d(PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Token, ""));
            socketConnection.emit("login", data, args -> {
                //roomJoin();
               /* JSONArray jsonArray = (JSONArray) args[0];*/
                //Log.e("test123", args[0].toString());
                enterRoom();
            });
        } catch (Exception e) {
            //Log.e(e.getMessage());
        }
    }

    //채팅방
    public void enterRoom(){
        try {
            //Log.d(PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Token, ""));
            JSONObject data = new JSONObject();
            data.put("", "");
            socketConnection.emit("enterRoom", data, args -> {
                //roomJoin();
                Log.e("test1234", args[0].toString());

                String [][] parseredData_list;

                String str_name = "";
                parseredData_list = jsonParserList_List(args[0].toString());
                for (int i = 0; i < parseredData_list.length; i++) {
                    String id = parseredData_list[i][0];
                    String type = parseredData_list[i][1];
                    String message = parseredData_list[i][2];
                    String userName = parseredData_list[i][3];
                    String deviceId = parseredData_list[i][4];
                    String createdAt = parseredData_list[i][5];
                    String chatMessage = parseredData_list[i][6];

                    Boolean bol_exist = false;
                    if(userName.equals(str_name)){
                        bol_exist = true;
                    }
                    else{
                        str_name = userName;
                    }

                    String str_onlydate = createdAt.substring(0, 10);

                    Boolean bol_linedate = false;
                    Log.i("데이트", str_onlydate);
                    if(str_chatdate.equals(str_onlydate)){
                        bol_linedate = false;
                    }
                    else{
                        bol_linedate = true;
                        str_chatdate = str_onlydate;
                    }

                    Boolean bol_existtime = false;
                    if(str_chattime.equals(createdAt) && str_chatusername.equals(userName)){
                        bol_existtime = true;
                    }
                    else{
                        bol_existtime = false;
                        str_chattime = createdAt;
                        str_chatusername = userName;
                    }

                    if(chatMessage.equals("null")) {
                        if(deviceId.equals(str_deviceId)){
                            Chat_message_models.add(new chat_message_model(chat_room.this, id, "message_my", type, message, userName, deviceId, createdAt, "", "", "", "", "", "", bol_exist, bol_linedate, bol_existtime));

                        }
                        else{
                            Chat_message_models.add(new chat_message_model(chat_room.this, id, "message_other",type, message, userName, deviceId, createdAt, "", "", "", "", "", "", bol_exist, bol_linedate, bol_existtime));
                        }
                    }
                    else {
                        String[] parse_answer = jsonParserList_answer(chatMessage);
                        String answer_id = parse_answer[0];
                        String answer_type = parse_answer[1];
                        String answer_message = parse_answer[2];
                        String answer_userName = parse_answer[3];
                        String answer_deviceId = parse_answer[4];
                        String answer_createdAt = parse_answer[5];


                        if(deviceId.equals(str_deviceId)){
                            Chat_message_models.add(new chat_message_model(chat_room.this, id, "message_my", type, message, userName, deviceId, createdAt, answer_id, answer_type, answer_message, answer_userName, answer_deviceId, answer_createdAt, bol_exist, bol_linedate, bol_existtime));
                        }
                        else{
                            Chat_message_models.add(new chat_message_model(chat_room.this, id, "message_other",type, message, userName, deviceId, createdAt, answer_id, answer_type, answer_message, answer_userName, answer_deviceId, answer_createdAt, bol_exist, bol_linedate, bol_existtime));
                        }

                       /* String[] firstChat = jsonParserList_FirstMessage(chatMessage);
                        String user_id = firstChat[0];
                        String user_name = userinfo[1];
                        String user_profile = userinfo[2];*/
                    }
                }
                runOnUiThread(() -> {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(chat_room.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    layoutManager.scrollToPosition(0);

                    Chat_message_adapter = new chat_message_adapter(chat_room.this, Chat_message_models);
                    List_Chat.setLayoutManager(layoutManager);
                    List_Chat.setAdapter(Chat_message_adapter);
                    List_Chat.scrollToPosition(Chat_message_adapter.getItemCount() - 1);

                    Chat_message_adapter.notifyDataSetChanged();


                });

                chat_refresh();
            });

        } catch (Exception e) {
            Log.e("테스트2",e.getMessage());
        }
    }

    public void chat_refresh(){
        socketConnection.addEvent("messageRefresh", args -> {
            try {
                position++;
                String str_name = "";



                String [] parseredData_message;

                parseredData_message = jsonParserList_refresh(args[0].toString());

                String id = parseredData_message[0];
                String type = parseredData_message[1];
                String message = parseredData_message[2];
                String userName = parseredData_message[3];
                String deviceId = parseredData_message[4];
                String createdAt = parseredData_message[5];
                String chatMessage = parseredData_message[6];

                Boolean bol_exist = false;
                if(userName.equals(str_name)){
                    bol_exist = true;
                }
                else{
                    str_name = userName;
                }

                String str_onlydate = createdAt.substring(0, 10);
                Boolean bol_linedate = false;

                if(str_chatdate.equals(str_onlydate)){
                    bol_linedate = false;
                }
                else{
                    bol_linedate = true;
                    str_chatdate = str_onlydate;
                }

                Boolean bol_existtime = false;
                if(str_chattime.equals(createdAt) && str_chatusername.equals(userName)){
                    bol_existtime = true;
                }
                else{
                    bol_existtime = false;
                    str_chattime = createdAt;
                    str_chatusername = userName;
                }

                if(chatMessage.equals("null")) {
                    if(deviceId.equals(str_deviceId)){
                        Chat_message_all_models.add(new chat_message_model(chat_room.this, id, "message_my", type, message, userName, deviceId, createdAt, "", "", "", "", "", "", false, bol_linedate, bol_existtime));

                    }
                    else{
                        Chat_message_all_models.add(new chat_message_model(chat_room.this, id, "message_other",type, message, userName, deviceId, createdAt, "", "", "", "", "", "", false, bol_linedate, bol_existtime));
                    }
                }
                else {
                    String[] parse_answer = jsonParserList_answer(chatMessage);
                    String answer_id = parse_answer[0];
                    String answer_type = parse_answer[1];
                    String answer_message = parse_answer[2];
                    String answer_userName = parse_answer[3];
                    String answer_deviceId = parse_answer[4];
                    String answer_createdAt = parse_answer[5];

                    if(deviceId.equals(str_deviceId)){
                        Chat_message_all_models.add(new chat_message_model(chat_room.this, id, "message_my", type, message, userName, deviceId, createdAt, answer_id, answer_type, answer_message, answer_userName, answer_deviceId, answer_createdAt, bol_exist, bol_linedate, bol_existtime));
                    }
                    else{
                        Chat_message_all_models.add(new chat_message_model(chat_room.this, id, "message_other",type, message, userName, deviceId, createdAt, answer_id, answer_type, answer_message, answer_userName, answer_deviceId, answer_createdAt, bol_exist, bol_linedate, bol_existtime));
                    }
                }

                if(Chat_message_all_models.size()>1){
                    int before_position = Chat_message_all_models.size()-2;
                    int now_position = Chat_message_all_models.size()-1;

                    if(!Chat_message_all_models.get(before_position).getId().equals(Chat_message_all_models.get(now_position).getId())){
                        Chat_message_models.add(new chat_message_model(chat_room.this, Chat_message_all_models.get(now_position).getId(), Chat_message_all_models.get(now_position).getCategory(),Chat_message_all_models.get(now_position).getType(), Chat_message_all_models.get(now_position).getMessage(), Chat_message_all_models.get(now_position).getUserName(), Chat_message_all_models.get(now_position).getDeviceId(), Chat_message_all_models.get(now_position).getCreatedAt(), Chat_message_all_models.get(now_position).getFirst_Id(), Chat_message_all_models.get(now_position).getFirst_Type(), Chat_message_all_models.get(now_position).getFirst_Message(), Chat_message_all_models.get(now_position).getFirst_UserName(), Chat_message_all_models.get(now_position).getFirst_DeviceId(), Chat_message_all_models.get(now_position).getFirst_CreatedAt(), Chat_message_all_models.get(now_position).getBol_exist(), Chat_message_all_models.get(now_position).getBol_linedate(), Chat_message_all_models.get(now_position).getBol_linetime()));
                    }
                }
                else{
                    Chat_message_models.add(new chat_message_model(chat_room.this, Chat_message_all_models.get(0).getId(), Chat_message_all_models.get(0).getCategory(),Chat_message_all_models.get(0).getType(), Chat_message_all_models.get(0).getMessage(), Chat_message_all_models.get(0).getUserName(), Chat_message_all_models.get(0).getDeviceId(), Chat_message_all_models.get(0).getCreatedAt(), Chat_message_all_models.get(0).getFirst_Id(), Chat_message_all_models.get(0).getFirst_Type(), Chat_message_all_models.get(0).getFirst_Message(), Chat_message_all_models.get(0).getFirst_UserName(), Chat_message_all_models.get(0).getFirst_DeviceId(), Chat_message_all_models.get(0).getFirst_CreatedAt(), Chat_message_all_models.get(0).getBol_exist(), Chat_message_all_models.get(0).getBol_linedate(), Chat_message_all_models.get(0).getBol_linetime()));
                }
                runOnUiThread(() -> {

                    Chat_message_adapter.notifyDataSetChanged();
                    List_Chat.scrollToPosition(Chat_message_adapter.getItemCount() - 1);
                });
            } catch (Exception e) {
                Log.e("test", e.getMessage());
            }
        });
    }
    public void setChatNick_Input(){
        MaterialDialog dialog;
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_nick_modi, (ViewGroup)findViewById(R.id.root));
        final ImageView Img_Yes = (ImageView)layout.findViewById(R.id.img_yes);
        final ImageView Img_No = (ImageView)layout.findViewById(R.id.img_no);
        final EditText Edit_Nick = (EditText)layout.findViewById(R.id.edit_nick);
        final TextView Txt_Warning = (TextView) layout.findViewById(R.id.txt_warning);
        final ImageView Layout_Top = (ImageView) layout.findViewById(R.id.layout_top);
        final ImageView Image_Nick_Check = (ImageView) layout.findViewById(R.id.img_nick_check);
        final LinearLayout Layout_Edit = (LinearLayout) layout.findViewById(R.id.layout_edit);

        Layout_Top.setVisibility(View.GONE);
        Layout_Edit.setVisibility(View.VISIBLE);

        bol_nickcheck = false;
        nickcheck_name = "";
        dialog = new MaterialDialog(chat_room.this);
        dialog
                .setContentView(layout)
                .setCanceledOnTouchOutside(true);
        dialog.show();


        Img_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Img_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Layout_Edit.getVisibility() == View.GONE){
                    Layout_Top.setVisibility(View.GONE);
                    Layout_Edit.setVisibility(View.VISIBLE);
                }
                else{
                    if(Edit_Nick.getText().toString().equals("")){
                        Toast.makeText(chat_room.this, "변경할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(bol_nickcheck == false){
                            Toast.makeText(chat_room.this, "닉네임을 중복 검사 해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            setNick_input(nickcheck_name, str_deviceId);
                            dialog.dismiss();
                        }
                    }
                }
            }
        });

        Image_Nick_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edit_Nick.getText().toString().equals("")){
                    Toast.makeText(chat_room.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //닉네임 중복인 경우
                    if(setNick_Check(Edit_Nick.getText().toString())== true){
                        Txt_Warning.setVisibility(View.VISIBLE);
                    }
                    else{
                        Txt_Warning.setVisibility(View.GONE);
                        bol_nickcheck = true;
                        nickcheck_name = Edit_Nick.getText().toString();
                        Image_Nick_Check.setImageResource(R.drawable.ic_dialog_nick_check_finish);
                    }
                }
            }
        });
    }

    public void setNick_input(String str_nick, String str_deviceId){
        HttpClinet_Delete http_delete = new HttpClinet_Delete();
        String result_info = http_delete.HttpClinet_Delete("not", "v1/chatUser/remove?deviceId="+str_deviceId);
        String[] parseredData_info = jsonParserList_access(result_info);

        if(parseredData_info[0].equals("success")){
            HttpClinet_Post http = new HttpClinet_Post();
            String result_register = http.HttpClinet_Post("not", "v1/chatUser/register", "name", str_nick, "deviceId", str_deviceId);
            String[] parseredData_register = jsonParserList_access(result_register);

            if(parseredData_register[0].equals("success")){
                editor = preferences.edit();
                editor.putString("user_name", str_nick);
                editor.putString("user_deviceId", str_deviceId);
                editor.commit();

                Toast.makeText(chat_room.this, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();

                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
            else{
                Toast.makeText(chat_room.this, parseredData_register[0], Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(chat_room.this, parseredData_info[0], Toast.LENGTH_SHORT).show();
        }
    }

    public boolean setNick_Check(String str_nick){
        boolean bol_nick = false;
        HttpClient_Get http = new HttpClient_Get();

        String result_info = http.HttpClient_Get("not", "v1/chatUser/list?name="+str_nick);
        String[] parseredData_info = jsonParserList_access(result_info);

        if(parseredData_info[0].equals("success")){
            String[][] parseredData_list = jsonParserList_ProductList(result_info);

            if(parseredData_list.length >0){
                bol_nick = true;
            }
        }
        return bol_nick;
    }

    public String[][] jsonParserList_ProductList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용_", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");
            Log.i("서버에서 받은 전체 내용_", "1");
            String[] jsonName = {"id", "name", "deviceId"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }

            }

            return parseredData;
        } catch (JSONException e) {
            Log.i("ㅌㅅㅌ", e+"");
            e.printStackTrace();
            return null;
        }
    }
}
