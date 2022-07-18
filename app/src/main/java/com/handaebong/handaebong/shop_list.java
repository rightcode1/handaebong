package com.handaebong.handaebong;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.handaebong.handaebong.Adapter.shop_adapter;
import com.handaebong.handaebong.Adapter.shop_menu_tab_adapter;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.Ulitility.Progressbar_wheel;
import com.handaebong.handaebong.model.shoplist_menu_model;
import com.handaebong.handaebong.model.shoplist_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//사용 안함
//이전 코드로 복귀시 사용

public class shop_list extends AppCompatActivity {
    public static Activity act_shop_list;

    private ImageView Img_Back;
    private RecyclerView List_Menu;
    private TextView Txt_Title;
    public static ArrayList<shoplist_menu_model> Shoplist_memu_models;
    public static shop_menu_tab_adapter shop_menu_tab_Adapter;

    public static RecyclerView List_Contents;
    public static ArrayList<shoplist_model> Shoplist_models;
    public static shop_adapter Shop_Adapter;

    public static String str_category = "";

    private ViewPagerAdapter mSectionsPagerAdapter;
    public static ViewPager2 mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);

        setInit();
        setAction();
    }

    public void setInit() {
        act_shop_list = shop_list.this;

        Intent intent1 = getIntent();
        str_category = intent1.getStringExtra("category");

        Img_Back = (ImageView)findViewById(R.id.img_back);

        Txt_Title = (TextView)findViewById(R.id.txt_title);
        List_Menu = (RecyclerView)findViewById(R.id.list_menu);
        Shoplist_memu_models = new ArrayList<shoplist_menu_model>();

        List_Contents = (RecyclerView)findViewById(R.id.list_contents);
        Shoplist_models = new ArrayList<shoplist_model>();

        mSectionsPagerAdapter = new ViewPagerAdapter(shop_list.this);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager2) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    str_category = "전체";
                    setMenu();
                }
                else if(position == 1){
                    str_category = "치킨";
                    setMenu();
                }
                else if(position == 2){
                    str_category = "피자/햄버거";
                    setMenu();
                }
                else if(position == 3){
                    str_category = "분식/도시락";
                    setMenu();
                }
                else if(position == 4){
                    str_category = "일식/돈까스";
                    setMenu();
                }
                else if(position == 5){
                    str_category = "백반/국수";
                    setMenu();
                }
                else if(position == 6){
                    str_category = "찜/탕/찌개";
                    setMenu();
                }
                else if(position == 7){
                    str_category = "고기/구이";
                    setMenu();
                }
                else if(position == 8){
                    str_category = "중식";
                    setMenu();
                }
                else if(position == 9){
                    str_category = "양식/아시안";
                    setMenu();
                }
                else if(position == 10){
                    str_category = "카페/디저트";
                    setMenu();
                }
                else if(position == 11){
                    str_category = "뷰티/편의";
                    setMenu();
                }
                Txt_Title.setText(str_category);
            }
        });

        setInit_Menu();

     /*   Async async = new Async();
        async.execute();*/
    }
    public void setAction(){
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    public void setInit_Menu(){
        if(str_category.equals("전체")){
            mViewPager.setCurrentItem(0);
        }
        if(str_category.equals("치킨")){
            mViewPager.setCurrentItem(1);
        }
        if(str_category.equals("피자/햄버거")){
            mViewPager.setCurrentItem(2);
        }
        if(str_category.equals("분식/도시락")){
            mViewPager.setCurrentItem(3);
        }
        if(str_category.equals("일식/돈까스")){
            mViewPager.setCurrentItem(4);
        }
        if(str_category.equals("백반/국수")){
            mViewPager.setCurrentItem(5);
        }
        if(str_category.equals("찜/탕/찌개")){
            mViewPager.setCurrentItem(6);
        }
        if(str_category.equals("고기/구이")){
            mViewPager.setCurrentItem(7);
        }
        if(str_category.equals("중식")){
            mViewPager.setCurrentItem(8);
        }
        if(str_category.equals("양식/아시안")){
            mViewPager.setCurrentItem(9);
        }
        if(str_category.equals("카페/디저트")){
            mViewPager.setCurrentItem(10);
        }
        if(str_category.equals("뷰티/편의")){
            mViewPager.setCurrentItem(11);
        }
    }
    public void setMenu(){
        Shoplist_memu_models.clear();
        if(str_category.equals("전체")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "전체", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "전체", false));
        }

        if(str_category.equals("치킨")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "치킨", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "치킨", false));
        }

        if(str_category.equals("피자/햄버거")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "피자/햄버거", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "피자/햄버거", false));
        }

        if(str_category.equals("분식/도시락")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "분식/도시락", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "분식/도시락", false));
        }

        if(str_category.equals("일식/돈까스")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "일식/돈까스", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "일식/돈까스", false));
        }

        if(str_category.equals("백반/국수")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "백반/국수", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "백반/국수", false));
        }

        if(str_category.equals("찜/탕/찌개")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "찜/탕/찌개", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "찜/탕/찌개", false));
        }
        if(str_category.equals("고기/구이")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "고기/구이", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "고기/구이", false));
        }
        if(str_category.equals("중식")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "중식", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "중식", false));
        }
        if(str_category.equals("양식/아시안")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "양식/아시안", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "양식/아시안", false));
        }
        if(str_category.equals("카페/디저트")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "카페/디저트", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "카페/디저트", false));
        }
        if(str_category.equals("뷰티/편의")){
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "뷰티/편의", true));
        }
        else{
            Shoplist_memu_models.add(new shoplist_menu_model(shop_list.this, "뷰티/편의", false));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(shop_list.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);

        shop_menu_tab_Adapter = new shop_menu_tab_adapter(shop_list.this, Shoplist_memu_models);
        List_Menu.setLayoutManager(layoutManager);
        List_Menu.setAdapter(shop_menu_tab_Adapter);

        int centerOfScreen = List_Menu.getWidth() / 2;
        LinearLayoutManager layoutManager1 = (LinearLayoutManager) List_Menu.getLayoutManager();

        if(str_category.equals("전체")){
            //List_Menu.scrollToPosition(2);
        }
        if(str_category.equals("치킨")){
            //List_Menu.scrollToPosition(3);
        }
        if(str_category.equals("피자/햄버거")){
            //List_Menu.scrollToPosition(4);
        }
        if(str_category.equals("분식/도시락")){
            List_Menu.scrollToPosition(1);
        }
        if(str_category.equals("일식/돈까스")){
            List_Menu.scrollToPosition(1);
        }
        if(str_category.equals("백반/국수")){
            List_Menu.scrollToPosition(3);
        }
        if(str_category.equals("찜/탕/찌개")){
            List_Menu.scrollToPosition(4);
        }
        if(str_category.equals("고기/구이")){
            List_Menu.scrollToPosition(5);
        }
        if(str_category.equals("중식")){
            List_Menu.scrollToPosition(6);
        }
        if(str_category.equals("양식/아시안")){
            List_Menu.scrollToPosition(7);
        }
        if(str_category.equals("카페/디저트")){
            List_Menu.scrollToPosition(8);
        }
        if(str_category.equals("뷰티/편의")){
            List_Menu.scrollToPosition(9);
        }
    }

    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        String[] parseredData_access, parseredData_info;
        String[][] parseredData_list;
        String[] parseredData_list_img;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(shop_list.this,"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {

                HttpClient_Get http = new HttpClient_Get();
                String str_query = "";
                if(str_category.equals("전체")){
                    str_query = "v1/store/list?";
                }
                else{
                    str_query = "v1/store/list?category="+str_category;
                }
                String result_info = http.HttpClient_Get("not", str_query);
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

                        Shoplist_models.add(new shoplist_model(shop_list.this, str_id, str_name, str_intro, "true", str_thumb1, str_thumb2, str_thumb3));
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

            LinearLayoutManager layoutManager = new LinearLayoutManager(shop_list.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);

            Shop_Adapter = new shop_adapter(shop_list.this, Shoplist_models);
            List_Contents.setLayoutManager(layoutManager);
            List_Contents.setAdapter(Shop_Adapter);

            progressDialog.dismiss();
        }
    }

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

    public String[][] jsonParserList_ProductList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용_", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");

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

    private class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    Fragment f = new frag_shoplist();
                    Bundle bundle = new Bundle();
                    bundle.putString("diff", "전체");
                    f.setArguments(bundle);
                    return f;
                case 1:
                    Fragment f1 = new frag_shoplist();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("diff", "치킨");
                    f1.setArguments(bundle1);
                    return f1;
                case 2:
                    Fragment f2 = new frag_shoplist();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("diff", "피자/햄버거");
                    f2.setArguments(bundle2);
                    return f2;
                case 3:
                    Fragment f3 = new frag_shoplist();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("diff", "분식/도시락");
                    f3.setArguments(bundle3);
                    return f3;
                case 4:
                    Fragment f4 = new frag_shoplist();
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("diff", "일식/돈까스");
                    f4.setArguments(bundle4);
                    return f4;
                case 5:
                    Fragment f5 = new frag_shoplist();
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("diff", "백반/국수");
                    f5.setArguments(bundle5);
                    return f5;
                case 6:
                    Fragment f6 = new frag_shoplist();
                    Bundle bundle6 = new Bundle();
                    bundle6.putString("diff", "찜/탕/찌개");
                    f6.setArguments(bundle6);
                    return f6;
                case 7:
                    Fragment f7 = new frag_shoplist();
                    Bundle bundle7 = new Bundle();
                    bundle7.putString("diff", "고기/구이");
                    f7.setArguments(bundle7);
                    return f7;
                case 8:
                    Fragment f8 = new frag_shoplist();
                    Bundle bundle8 = new Bundle();
                    bundle8.putString("diff", "중식");
                    f8.setArguments(bundle8);
                    return f8;
                case 9:
                    Fragment f9 = new frag_shoplist();
                    Bundle bundle9 = new Bundle();
                    bundle9.putString("diff", "양식/아시안");
                    f9.setArguments(bundle9);
                    return f9;
                case 10:
                    Fragment f10 = new frag_shoplist();
                    Bundle bundle10 = new Bundle();
                    bundle10.putString("diff", "카페/디저트");
                    f10.setArguments(bundle10);
                    return f10;
                case 11:
                    Fragment f11 = new frag_shoplist();
                    Bundle bundle11 = new Bundle();
                    bundle11.putString("diff", "뷰티/편의");
                    f11.setArguments(bundle11);
                    return f11;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 12;
        }
    }
}
