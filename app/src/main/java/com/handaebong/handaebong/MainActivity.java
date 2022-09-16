package com.handaebong.handaebong;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.handaebong.handaebong.Adapter.shop_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.HttpClinet_Post;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.Ulitility.SocketConnection;
import com.handaebong.handaebong.Ulitility.ViewPager_Wrap;
import com.handaebong.handaebong.model.event_model;
import com.handaebong.handaebong.model.shoplist_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager_Wrap ViewPager_Event;
    public ArrayList<event_model> event_models;
    public static TimerTask main_event_myTask;
    public static Timer main_event_timer;

    private TextView Txt_More;
    private RecyclerView List_Shop;
    public ArrayList<shoplist_model> Shoplist_models;
    public shop_adapter Shop_Adapter;

    private ImageView Img_Menu, Img_Like;
    private DrawerLayout Layout_Main;

    //private LinearLayout Layout_Category_All;
    private LinearLayout Layout_Category_Chicken;
    private LinearLayout Layout_Category_Fastfood;
    private LinearLayout Layout_Category_Snack;
    private LinearLayout Layout_Category_Japan;
    private LinearLayout Layout_Category_Korea;
    private LinearLayout Layout_Category_Soup;
    private LinearLayout Layout_Category_Meat;
    private LinearLayout Layout_Category_China;
    private LinearLayout Layout_Category_Noodle;
    private LinearLayout Layout_Category_Desert;
    private LinearLayout Layout_Category_Life;

    private LinearLayout Layout_Home;
    private LinearLayout Layout_Chat;
    private LinearLayout Layout_Qacenter;
    private LinearLayout Layout_Qa;
    private LinearLayout Layout_Notice;
    private LinearLayout Layout_Setting;

    private TextView Edit_Search;
    private ImageView Img_Search;
    private ImageView Img_Chat;
    private TextView Txt_Chat_Title;

    boolean bol_nickcheck = false;
    String nickcheck_name = "";

    String str_user_name = "";
    String str_user_deviceId = "";
    Boolean bol_user_push = true;
    Boolean bol_islogin = false;

    public static int screen_width = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //스크린 크기 초기화
        screen_width = getScreenWidth(MainActivity.this);

        //페이지 초기화
        setInit();

        //액션 초기화
        setAction();
    }

    //페이지 종료 이벤트
    @Override
    public void onBackPressed() {
        //오른쪽 메뉴 열린 경우 닫기
        if (Layout_Main.isDrawerOpen(Gravity.RIGHT)) {
            Layout_Main.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    //페이지 초기화
    public void setInit() {
        //푸시 토큰 초기화
        getFCMToken();

        //캐시 - 유저 이름, uuid, 푸시 정보 불러오기
        preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
        str_user_name = preferences.getString("user_name", "");
        str_user_deviceId = preferences.getString("user_uuid", "");
        bol_user_push = preferences.getBoolean("user_push", true);

        Intent intent1 = getIntent();
        bol_islogin = intent1.getBooleanExtra("isLogin", false);

        //레이아웃 초기화
        Layout_Main = (DrawerLayout)findViewById(R.id.drawer_layout);
        Img_Menu = (ImageView)findViewById(R.id.img_menu);
        Img_Like = (ImageView)findViewById(R.id.img_like);
        List_Shop = (RecyclerView)findViewById(R.id.list_shop);
        Shoplist_models = new ArrayList<shoplist_model>();
        event_models = new ArrayList<event_model>();

        Txt_More = (TextView)findViewById(R.id.txt_more);

        Layout_Home = (LinearLayout)findViewById(R.id.layout_home);
        Layout_Chat = (LinearLayout)findViewById(R.id.layout_chat);
        Layout_Qacenter = (LinearLayout)findViewById(R.id.layout_qacenter);
        Layout_Qa = (LinearLayout)findViewById(R.id.layout_qa);
        Layout_Notice = (LinearLayout)findViewById(R.id.layout_notice);
        Layout_Setting = (LinearLayout)findViewById(R.id.layout_setting);

        //Layout_Category_All = (LinearLayout)findViewById(R.id.layout_category_all);
        Layout_Category_Chicken = (LinearLayout)findViewById(R.id.layout_category_chicken);
        Layout_Category_Fastfood = (LinearLayout)findViewById(R.id.layout_category_fastfood);
        Layout_Category_Snack = (LinearLayout)findViewById(R.id.layout_category_snack);
        Layout_Category_Japan = (LinearLayout)findViewById(R.id.layout_category_japan);
        Layout_Category_Korea = (LinearLayout)findViewById(R.id.layout_category_korea);
        Layout_Category_Soup = (LinearLayout)findViewById(R.id.layout_category_soup);
        Layout_Category_Meat = (LinearLayout)findViewById(R.id.layout_category_meat);
        Layout_Category_China = (LinearLayout)findViewById(R.id.layout_category_china);
        Layout_Category_Noodle = (LinearLayout)findViewById(R.id.layout_category_noodle);
        Layout_Category_Desert = (LinearLayout)findViewById(R.id.layout_category_desert);
        Layout_Category_Life = (LinearLayout)findViewById(R.id.layout_category_life);

        Edit_Search = (TextView)findViewById(R.id.edit_search);
        Img_Search = (ImageView)findViewById(R.id.img_search);
        Img_Chat = (ImageView)findViewById(R.id.img_chat);
        Txt_Chat_Title = (TextView)findViewById(R.id.txt_chat_title);

        Glide.with(this).load(R.raw.home_chat).into(Img_Chat);

        if(bol_islogin == true){
            Img_Chat.setVisibility(View.GONE);
            Txt_Chat_Title.setVisibility(View.GONE);
            Layout_Chat.setVisibility(View.GONE);
        }
        else{
            Img_Chat.setVisibility(View.VISIBLE);
            Txt_Chat_Title.setVisibility(View.VISIBLE);
            Layout_Chat.setVisibility(View.VISIBLE);
        }

        //하단 상품 정보 리스트 초기화
        Async async = new Async();
        async.execute();
    }

    //상단 배너 초기화
    public void setViewPager(String[][] data) {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager() ,data);

        ViewPager_Event = (ViewPager_Wrap)findViewById(R.id.viewpage_event);
        ViewPager_Event.setAdapter(mSectionsPagerAdapter);
        ViewPager_Event.setOffscreenPageLimit(3);
        ViewPager_Event.setClipToPadding(false);

        //배너 양쪽 간격 주기
        ViewPager_Event.setPadding(50, 0, 50, 0);
        ViewPager_Event.setPageMargin(10);
        final int pageCount = data.length;

        //배너 자동 스크롤 타이머
        main_event_myTask = new TimerTask() {
            public void run() {
                if(MainActivity.this != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int currentPage = ViewPager_Event.getCurrentItem();
                            if( currentPage >= pageCount - 1 ) ViewPager_Event.setCurrentItem( 0, true );
                            else ViewPager_Event.setCurrentItem( currentPage + 1, true );
                            //Indicator_Event.setSelectedItem( ( currentPage + 1 == pageCount ) ? 0 : currentPage + 1, true );
                        }
                    });
                }
            }
        };
        main_event_timer = new Timer();
        main_event_timer.schedule(main_event_myTask, 4000, 3000); // 5초후 첫실행, 3초마다 계속실행
        ViewPager_Event.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Indicator_Event.setSelectedItem( ViewPager_Event.getCurrentItem(), true );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //액션 이벤트
    public void setAction(){
        //오른쪽 상단 메뉴 클릭 이벤트
        Img_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Layout_Main.isDrawerOpen(Gravity.RIGHT)) {

                } else {
                    Layout_Main.openDrawer(Gravity.RIGHT);
                }
            }
        });

        //좋아요 업체 이동 이벤트
        Img_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, setting_like.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //홈 이동 이벤트
        Layout_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Layout_Main.closeDrawer(Gravity.RIGHT);
            }
        });

        //채팅방 이동 이벤트
        Layout_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_user_name.equals("")){
                    setChatNick_Input();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, chat_room.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
                }

            }
        });

        //문의하기 이동 이벤트
        Layout_Qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, setting_qa.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //고객센터 이동 이벤트
        Layout_Qacenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, setting_qacenter.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //공지사항 이동 이벤트
        Layout_Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, setting_notice.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //설정 이동 이벤트
        Layout_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, setting_menu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 이동 이벤트
        Txt_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list.class);
                intent.putExtra("category", "전체");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 전체 이동 이벤트
       /* Layout_Category_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "전체");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });*/

        //상점 리스트 - 치킨 이동 이벤트
        Layout_Category_Chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "치킨");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 피자/햄버거 이동 이벤트
        Layout_Category_Fastfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "피자/햄버거");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 분식/도시락 이동 이벤트
        Layout_Category_Snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "분식/도시락");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 일식/돈까스 이동 이벤트
        Layout_Category_Japan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "일식/돈까스");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 백반/국수 이동 이벤트
        Layout_Category_Korea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "백반/국수");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 찜/탕/찌개 이동 이벤트
        Layout_Category_Soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "찜/탕/찌개");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 고기/구이 이동 이벤트
        Layout_Category_Meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "고기/구이");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 중식 이동 이벤트
        Layout_Category_China.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "중식");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 양식/아시안 이동 이벤트
        Layout_Category_Noodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "양식/아시안");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 카페/디저트 이동 이벤트
        Layout_Category_Desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "카페/디저트");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //상점 리스트 - 뷰티/편의 이동 이벤트
        Layout_Category_Life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_list_v2.class);
                intent.putExtra("category", "뷰티/편의");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //검색 이동 이벤트
        Img_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_search.class);
                intent.putExtra("search", Edit_Search.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //검색창 클릭 이벤트
        Edit_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, shop_search.class);
                intent.putExtra("search", Edit_Search.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });

        //채팅방 이동 이벤트
        Img_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //닉네임 등록 안된 경우 등록
                if(str_user_name.equals("")){
                    setChatNick_Input();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, chat_room.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
                }

            }
        });
    }

    //하단 상점 api 호출
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        String[] parseredData_access, parseredData_info;
        String[][] parseredData_list, parseredData_bannerlist;
        String[] parseredData_list_img;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(MainActivity.this,"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {

                HttpClient_Get http = new HttpClient_Get();

                String result_info = http.HttpClient_Get("not", "v1/store/list?isPremium=true");
                parseredData_info = jsonParserList_access(result_info);

                if(parseredData_info[0].equals("success")){
                    parseredData_list = jsonParserList_ShopList(result_info);

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

                        Shoplist_models.add(new shoplist_model(MainActivity.this, str_id, str_name, str_intro, "true", str_thumb1, str_thumb2, str_thumb3));
                    }
                }

                String result_event = http.HttpClient_Get("not", "v1/advertisement/list?location=메인배너");
                parseredData_bannerlist = jsonParserList_BannerList(result_event);
                for(int i =0; i < parseredData_bannerlist.length;i++){
                    String str_id = parseredData_list[i][0];
                    String str_title = parseredData_list[i][1];
                    String str_sortCode = parseredData_list[i][2];
                    String str_diff = parseredData_list[i][3];
                    String str_location = parseredData_list[i][4];
                    String str_url = parseredData_list[i][5];
                    String str_thumbnail = parseredData_list[i][6];
                    String str_image = parseredData_list[i][7];
                    String str_storeId = parseredData_list[i][8];

                    event_models.add(new event_model(MainActivity.this, str_id, str_title, str_sortCode, str_diff, str_location, str_url, str_thumbnail, str_image, str_storeId));
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

            setViewPager(parseredData_bannerlist);

            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);

            Shop_Adapter = new shop_adapter(MainActivity.this, Shoplist_models);
            List_Shop.setLayoutManager(layoutManager);
            List_Shop.setAdapter(Shop_Adapter);

            progressDialog.dismiss();
        }
    }

    //채팅방 닉네임 없는 경우 팝업 이벤트
    public void setChatNick_Input(){
        MaterialDialog dialog;
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_nick_register, (ViewGroup)findViewById(R.id.root));
        final ImageView Img_Yes = (ImageView)layout.findViewById(R.id.img_yes);
        final ImageView Img_No = (ImageView)layout.findViewById(R.id.img_no);
        final EditText Edit_Nick = (EditText)layout.findViewById(R.id.edit_nick);
        final TextView Txt_Warning = (TextView) layout.findViewById(R.id.txt_warning);
        final ImageView Layout_Top = (ImageView) layout.findViewById(R.id.layout_top);
        final ImageView Image_Nick_Check = (ImageView) layout.findViewById(R.id.img_nick_check);
        final LinearLayout Layout_Edit = (LinearLayout) layout.findViewById(R.id.layout_edit);

        bol_nickcheck = false;
        nickcheck_name = "";
        dialog = new MaterialDialog(MainActivity.this);
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
                        Toast.makeText(MainActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(bol_nickcheck == false){
                            Toast.makeText(MainActivity.this, "닉네임을 중복 검사 해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            setNick_input(nickcheck_name, str_user_deviceId);
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
                    Toast.makeText(MainActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
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

    //닉네임 중복 체크 api 호출
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

    //닉네임 등록 api 호출
    public void setNick_input(String str_nick, String str_deviceId){
        HttpClinet_Post http = new HttpClinet_Post();

        String result_info = http.HttpClinet_Post("not", "v1/chatUser/register", "name", str_nick, "deviceId", str_deviceId);
        String[] parseredData_info = jsonParserList_access(result_info);

        if(parseredData_info[0].equals("success")){
            editor = preferences.edit();
            editor.putString("user_name", str_nick);
            editor.putString("user_deviceId", str_deviceId);
            editor.commit();

            str_user_name = str_nick;

            Intent intent = new Intent(MainActivity.this, chat_room.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
        }
    }

    //api 파싱
    public String[] jsonParserList_access(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용1", pRecvServerPage);
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

    //api 파싱
    public String[][] jsonParserList_BannerList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용_", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");
            Log.i("서버에서 받은 전체 내용_", "1");
            String[] jsonName = {"id", "title", "sortCode", "diff", "location", "url", "thumbnail", "image", "storeId"};
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

    //api 파싱
    public String[][] jsonParserList_ShopList(String pRecvServerPage) {
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

    //상단 이미지 뷰페이저 정의
    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        String[][] data;

        public SectionsPagerAdapter(FragmentManager fm, String[][] data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f = new Fragment_event();
            Bundle bundle = new Bundle();

            //이미지 URL 동적 전송 ex) 1_1
            String title = data[position][1];
            String diff = data[position][3];
            String url = data[position][5];
            String thumbnail = data[position][6];
            String image = data[position][7];
            String storeId = data[position][8];
            bundle.putString("title", title);
            bundle.putString("diff", diff);
            bundle.putString("url", url);
            bundle.putString("thumbnail", thumbnail);
            bundle.putString("image", image);
            bundle.putString("storeId", storeId);
            f.setArguments(bundle);

            return f;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return data.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    //푸시 토큰 불러오기
    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("getting token fail","");
                return;
            }
            String token = task.getResult();

            if(bol_user_push == true){
                HttpClinet_Post httpClinet_post = new HttpClinet_Post();
                httpClinet_post.HttpClinet_Post("not", "v1/notification/register", "notificationToken", token);

                editor = preferences.edit();
                editor.putString("user_pushtoken", token);
                editor.commit();
            }

            //notificationTokenRegister(token);
        });
    }

    //디바이스 스크린 사이즈 구하기
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        return width;
    }
}