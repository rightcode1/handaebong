package com.handaebong.handaebong;

import static com.handaebong.handaebong.setting_like.Shop_Adapter;
import static com.handaebong.handaebong.setting_like.Shoplist_models;
import static com.handaebong.handaebong.setting_like.act_setting_like;
import static com.handaebong.handaebong.shop_list.str_category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.handaebong.handaebong.Adapter.shop_menu_adapter;
import com.handaebong.handaebong.Adapter.shop_menu_category_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.HttpClinet_Post;
import com.handaebong.handaebong.Ulitility.OnSingleClickListener;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.model.shop_focus_menu_model;
import com.handaebong.handaebong.model.shop_menu_category_model;
import com.handaebong.handaebong.model.shop_menu_category_position_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class shop_focus extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static AppBarLayout Appbar;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager Viewpage_Img;
    private TextView Txt_Title;
    private TextView Txt_Intro;
    private LinearLayout Layout1, Layout2;
    private LinearLayout Layout_Tab1, Layout_Tab2;
    private TextView Txt_Tab1_Line, Txt_Tab2_Line;
    private TextView Txt_Tabbar_Title, Txt_Input;
    private ImageView Img_Like, Img_Back;

    private RecyclerView List_Menucategory;
    public static ArrayList<shop_menu_category_model> shop_menu_category_models;
    public static shop_menu_category_adapter shop_menu_category_adapters;

    public static ArrayList<shop_menu_category_position_model> shop_menu_category_position_models;

    public static RecyclerView List_Menu;
    public static ArrayList<shop_focus_menu_model> shop_focus_menu_models;
    public static shop_menu_adapter shop_menu_adapters;

    private TextView Txt_Info_Title, Txt_Info_Address;
    private TextView Txt_Info_Info, Txt_Info_Time, Txt_Info_Content, Txt_Info_Story, Txt_Info_Origin;
    private LinearLayout Layout_Info_Content, Layout_Info_Story, Layout_Info_Origin;
    public static RelativeLayout Layout_Menu;
    public static ImageView Img_Menu, Img_Menu_Cancel;
    public static TextView Txt_Menu_Title, Txt_Menu_Intro, Txt_Menu_Price, Txt_Menu_Contents;

    private String str_id = "";
    private String str_category = "";
    private String str_name = "";
    private String str_tel = "";
    private String str_address = "";
    private String str_intro = "";
    private String str_info = "";
    private String str_time = "";
    private String str_content = "";
    private String str_story = "";
    private String str_origin = "";


    boolean bol_like;
    private int now_listposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopfocus2);

        //페이지 초기화
        setInit();

        //액션 초기화
        setAction();
    }

    //페이지 초기화
    public void setInit() {
        //이전 페이지 데이터 전송
        Intent intent1 = getIntent();
        str_id = intent1.getStringExtra("id");

        //모델 초기화
        shop_menu_category_models = new ArrayList<shop_menu_category_model>();
        shop_menu_category_position_models = new ArrayList<shop_menu_category_position_model>();
        shop_focus_menu_models = new ArrayList<shop_focus_menu_model>();

        //이미지 뷰페이저 초기화
        Viewpage_Img = (ViewPager) findViewById(R.id.viewpage_img);
        Viewpage_Img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Indicator_Event.setSelectedItem( Viewpage_Img.getCurrentItem(), true );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //찜 여부 캐시 조회
        preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
        String str_like = preferences.getString("user_like", "");
        String[] splitText = str_like.split("_");
        bol_like = false;
        for(int i = 0; i < splitText.length; i++){
            if(str_id.equals(splitText[i])){
                bol_like = true;
            }
        }

        //코디네이드 앱바 초기화
        Appbar = (AppBarLayout) findViewById(R.id.appbar);
        Appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset < -430){
                    Img_Back.setImageResource(R.drawable.back);
                    Txt_Tabbar_Title.setVisibility(View.VISIBLE);

                    if(bol_like == true){
                        Img_Like.setImageResource(R.drawable.like_on);
                    }
                    else{
                        Img_Like.setImageResource(R.drawable.like_off);
                    }

                }
                else{
                    Img_Back.setImageResource(R.drawable.ic_back_white);
                    Txt_Tabbar_Title.setVisibility(View.GONE);

                    if(bol_like == true){
                        Img_Like.setImageResource(R.drawable.like_on);
                    }
                    else{
                        Img_Like.setImageResource(R.drawable.like_white);
                    }
                }
            }
        });

        //레이아웃 초기화
        Txt_Input = (TextView)findViewById(R.id.txt_input);
        Txt_Title = (TextView)findViewById(R.id.txt_title);
        Txt_Intro = (TextView)findViewById(R.id.txt_intro);
        Txt_Tabbar_Title = (TextView)findViewById(R.id.txt_tabbar_title);
        Img_Like = (ImageView)findViewById(R.id.img_like);
        List_Menucategory = (RecyclerView)findViewById(R.id.list_menucategory);
        List_Menu = (RecyclerView)findViewById(R.id.list_menu);

        Layout1 = (LinearLayout)findViewById(R.id.layout_1);
        Layout2 = (LinearLayout)findViewById(R.id.layout_2);

        Layout_Tab1 = (LinearLayout)findViewById(R.id.layout_tab1);
        Layout_Tab2 = (LinearLayout)findViewById(R.id.layout_tab2);

        Txt_Tab1_Line = (TextView)findViewById(R.id.txt_tab1_line);
        Txt_Tab2_Line = (TextView)findViewById(R.id.txt_tab2_line);

        Txt_Info_Title = (TextView)findViewById(R.id.txt_info_title);
        Txt_Info_Address = (TextView)findViewById(R.id.txt_info_address);
        Txt_Info_Info = (TextView)findViewById(R.id.txt_info_info);
        Txt_Info_Time = (TextView)findViewById(R.id.txt_info_time);
        Txt_Info_Content = (TextView)findViewById(R.id.txt_info_content);
        Txt_Info_Story = (TextView)findViewById(R.id.txt_info_story);
        Txt_Info_Origin = (TextView)findViewById(R.id.txt_info_origin);

        Layout_Info_Content = (LinearLayout)findViewById(R.id.layout_content);
        Layout_Info_Story = (LinearLayout)findViewById(R.id.layout_story);
        Layout_Info_Origin = (LinearLayout)findViewById(R.id.layout_origin);

        Layout_Menu = (RelativeLayout)findViewById(R.id.layout_menu);
        Img_Menu_Cancel = (ImageView)findViewById(R.id.img_menu_cancel);
        Img_Menu = (ImageView)findViewById(R.id.img_menu);
        Txt_Menu_Title = (TextView)findViewById(R.id.txt_menu_title);
        Txt_Menu_Intro = (TextView)findViewById(R.id.txt_menu_intro);
        Txt_Menu_Price = (TextView)findViewById(R.id.txt_menu_price);
        Txt_Menu_Contents = (TextView)findViewById(R.id.txt_menu_content);


        //상점 디테일 api 호출
        Async async = new Async();
        async.execute();
    }

    //액션 이벤트
    public void setAction(){
        //뒤로가기 이벤트
        Img_Back = (ImageView)findViewById(R.id.img_back);
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        //메뉴 팝업 닫기 이벤트
        Img_Menu_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Layout_Menu.setVisibility(View.GONE);
            }
        });

        //첫번째 탭 이벤트
        Layout_Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Layout1.setVisibility(View.VISIBLE);
                Layout2.setVisibility(View.GONE);
                Txt_Tab1_Line.setVisibility(View.VISIBLE);
                Txt_Tab2_Line.setVisibility(View.INVISIBLE);
            }
        });

        //두번째 탭 이벤트
        Layout_Tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Layout1.setVisibility(View.GONE);
                Layout2.setVisibility(View.VISIBLE);
                Txt_Tab1_Line.setVisibility(View.INVISIBLE);
                Txt_Tab2_Line.setVisibility(View.VISIBLE);
            }
        });

        //좋아요 이벤트
        Img_Like.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Drawable temp = Img_Like.getDrawable();
                Drawable temp1 = getResources().getDrawable(R.drawable.like_on);

                Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
                Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();
                if(tmpBitmap.equals(tmpBitmap1)){
                    Toast.makeText(shop_focus.this, "찜 목록에 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Img_Like.setImageResource(R.drawable.like_off);

                    String str_like = preferences.getString("user_like", "");
                    str_like = str_like.replace("_"+str_id,"");
                    editor = preferences.edit();
                    editor.putString("user_like", str_like);
                    editor.commit();

                    bol_like = false;
                }
                else{
                    Toast.makeText(shop_focus.this, "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    Img_Like.setImageResource(R.drawable.like_on);

                    String str_like = preferences.getString("user_like", "");
                    str_like = str_like+"_"+str_id;
                    editor = preferences.edit();
                    editor.putString("user_like", str_like);
                    editor.commit();

                    bol_like = true;
                }
            }
        });

        //전화하기 이벤트
        Txt_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpClinet_Post http = new HttpClinet_Post();
                String resut = http.HttpClinet_Post("not", "v1/telHistory/register?","storeId", str_id);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + str_tel));
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

    //상점 상세 api 호출
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        String[] parseredData_access, parseredData_info;
        String[][] parseredData_list_img, parseredData_list_menucategory, parseredData_list_menu;
        String[] imglist;
        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(shop_focus.this,"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {

                HttpClient_Get http = new HttpClient_Get();

                String result_info = http.HttpClient_Get("not", "v1/store/detail?id="+str_id);
                parseredData_access = jsonParserList_access(result_info);

                if(parseredData_access[0].equals("success")){
                    parseredData_info = jsonParserList_info(parseredData_access[1]);
                    str_id = parseredData_info[0];
                    str_category = parseredData_info[1];
                    str_name = parseredData_info[2];
                    str_tel = parseredData_info[3];
                    str_address = parseredData_info[4];
                    str_intro = parseredData_info[5];
                    str_info = parseredData_info[6];
                    str_content = parseredData_info[7];
                    str_story = parseredData_info[8];
                    str_origin = parseredData_info[9];
                    str_time = parseredData_info[11];
                }

                parseredData_list_img = jsonParserList_Img(parseredData_access[1]);
                imglist = new String[parseredData_list_img.length];
                for(int i =0 ; i<parseredData_list_img.length; i++){
                    imglist[i] = parseredData_list_img[i][0];
                }

                String result_list = http.HttpClient_Get("not", "v1/storeMenuCategory/list?storeId="+str_id);
                parseredData_list_menucategory = jsonParserList_Menu_category(result_list);

                for(int i =0; i < parseredData_list_menucategory.length;i++){
                    if(i == 0){
                        shop_menu_category_models.add(new shop_menu_category_model(shop_focus.this, parseredData_list_menucategory[i][0], true));
                    }
                    else{
                        shop_menu_category_models.add(new shop_menu_category_model(shop_focus.this, parseredData_list_menucategory[i][0], false));
                    }
                }

                String result_menu_list = http.HttpClient_Get("not", "v1/storeMenu/list?storeId="+str_id);
                parseredData_list_menu = jsonParserList_Menu(result_menu_list);

                String str_category = "";
                for(int i =0; i < parseredData_list_menu.length;i++){
                    String str_id = parseredData_list_menu[i][0];
                    String str_isBest = parseredData_list_menu[i][1];
                    String str_storeMenuCategoryName = parseredData_list_menu[i][2];
                    String str_name = parseredData_list_menu[i][3];
                    String str_price = parseredData_list_menu[i][4];
                    String str_intro = parseredData_list_menu[i][5];
                    String str_content = parseredData_list_menu[i][6];
                    String str_thumbnail = parseredData_list_menu[i][7];

                    boolean bol_best = false;
                    if(str_isBest.equals("true")){
                        bol_best = true;
                    }
                    else{
                        bol_best = false;
                    }
                    //카테고리명 같은 경우 카테고리 표기, 아닌 경우 메뉴 표기
                    if(!str_category.equals(str_storeMenuCategoryName)){
                        str_category = str_storeMenuCategoryName;
                        shop_menu_category_position_models.add(new shop_menu_category_position_model(shop_focus.this, str_storeMenuCategoryName, i));
                        shop_focus_menu_models.add(new shop_focus_menu_model(shop_focus.this, true, str_storeMenuCategoryName, str_name,str_intro, str_price, str_content, str_thumbnail, bol_best));
                    }
                    else{
                        shop_focus_menu_models.add(new shop_focus_menu_model(shop_focus.this, false, str_storeMenuCategoryName, str_name,str_intro, str_price, str_content, str_thumbnail, bol_best));
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

            preferences = getSharedPreferences("myapplication", MODE_PRIVATE);
            String str_like = preferences.getString("user_like", "");
            String[] splitText = str_like.split("_");
            boolean bol_like = false;
            for(int i = 0; i < splitText.length; i++){
                if(str_id.equals(splitText[i])){
                    bol_like = true;
                }
            }

            if(bol_like == true){
                Img_Like.setImageResource(R.drawable.like_on);
            }
            else{
                Img_Like.setImageResource(R.drawable.like_off);
            }


            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager() ,imglist);
            Viewpage_Img.setAdapter(mSectionsPagerAdapter);

            ViewGroup.LayoutParams params = Viewpage_Img.getLayoutParams();
            int width = Viewpage_Img.getWidth();
            params.height = width/2;
            Viewpage_Img.setLayoutParams(params);


            Txt_Tabbar_Title.setText(str_name);
            Txt_Title.setText(str_name);
            Txt_Intro.setText(str_intro);

            LinearLayoutManager layoutManager = new LinearLayoutManager(shop_focus.this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            layoutManager.scrollToPosition(0);

            shop_menu_category_adapters = new shop_menu_category_adapter(shop_focus.this, shop_menu_category_models);
            List_Menucategory.setLayoutManager(layoutManager);
            List_Menucategory.setAdapter(shop_menu_category_adapters);

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(shop_focus.this);
            layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager1.scrollToPosition(0);

            shop_menu_adapters = new shop_menu_adapter(shop_focus.this, shop_focus_menu_models);
            List_Menu.setLayoutManager(layoutManager1);
            List_Menu.setAdapter(shop_menu_adapters);

            for(int i=0; i <shop_menu_category_position_models.size();i++){
                Log.i("ㅌㅌㅌ", shop_menu_category_position_models.get(i).getPosition()+"");
            }

            List_Menu.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if(now_listposition == lastVisibleItemPosition){

                    }
                    else{
                        now_listposition = lastVisibleItemPosition;
                        Log.i("테스트00", now_listposition+"");
                        for(int i = 0; i < shop_menu_category_position_models.size(); i ++){
                            if(now_listposition == shop_menu_category_position_models.get(i).getPosition()){
                                String choice_category = shop_menu_category_position_models.get(i).getTitle();
                                for(int j = 0; j < shop_menu_category_models.size(); j ++){
                                    if(choice_category.equals(shop_menu_category_models.get(j).getTitle())){
                                        shop_menu_category_models.set(j,new shop_menu_category_model(shop_menu_category_models.get(j).getActivity(), shop_menu_category_models.get(j).getTitle(), true));
                                        //str_category = items.getTitle();
                                    }
                                    else{
                                        shop_menu_category_models.set(j,new shop_menu_category_model(shop_menu_category_models.get(j).getActivity(), shop_menu_category_models.get(j).getTitle(), false));
                                    }
                                }
                                shop_menu_category_adapters.notifyDataSetChanged();
                            }
                        }
                        //shop_menu_category_adapters.notifyDataSetChanged();
                    }
                }
            });
            Txt_Info_Title.setText(str_name);
            Txt_Info_Address.setText(str_address);
            Txt_Info_Info.setText(str_info);
            Txt_Info_Time.setText(str_time);

            if(str_content.equals("null") || str_content.equals("")){
                Layout_Info_Content.setVisibility(View.GONE);
            }
            else{
                Layout_Info_Content.setVisibility(View.VISIBLE);
                Txt_Info_Content.setText(str_content);
            }

            if(str_story.equals("null") || str_story.equals("")){
                Layout_Info_Story.setVisibility(View.GONE);
            }
            else{
                Layout_Info_Story.setVisibility(View.VISIBLE);
                Txt_Info_Story.setText(str_story);
            }

            if(str_origin.equals("null") || str_origin.equals("")){
                Layout_Info_Origin.setVisibility(View.GONE);
            }
            else{
                Layout_Info_Origin.setVisibility(View.VISIBLE);
                Txt_Info_Origin.setText(str_origin);
            }

            progressDialog.dismiss();
        }
    }

    //api 파싱
    public String[] jsonParserList_access(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용1", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"message", "data"};
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
    public String[] jsonParserList_info(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용1", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);

            String[] jsonName = {"id", "category", "name", "tel", "address", "intro", "info", "content", "story", "origin", "createdAt", "time"};
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
    public String[][] jsonParserList_Img(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용_", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("images");
            String[] jsonName = {"name"};
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
    public String[][] jsonParserList_Menu_category(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용_", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");
            String[] jsonName = {"name"};
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
    public String[][] jsonParserList_Menu(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용_", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");
            String[] jsonName = {"id", "isBest", "storeMenuCategoryName", "name", "price", "intro", "content", "thumbnail"};
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

    //상단 이미지 뷰페이저 페이지 전환 이벤트 정의
    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        String[] data;

        public SectionsPagerAdapter(FragmentManager fm, String[] data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f = new frag_img();
            Bundle bundle = new Bundle();

            //이미지 URL 동적 전송 ex) 1_1
            String Image_txt = data[position];
            bundle.putString("Image", Image_txt);
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
}