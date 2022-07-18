package com.handaebong.handaebong.Adapter;


import static com.handaebong.handaebong.shop_focus.Appbar;
import static com.handaebong.handaebong.shop_focus.List_Menu;
import static com.handaebong.handaebong.shop_focus.shop_menu_category_adapters;
import static com.handaebong.handaebong.shop_focus.shop_menu_category_models;
import static com.handaebong.handaebong.shop_focus.shop_menu_category_position_models;
import static com.handaebong.handaebong.shop_list.str_category;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.handaebong.handaebong.R;
import com.handaebong.handaebong.Ulitility.HttpClient_Get;
import com.handaebong.handaebong.model.shop_menu_category_model;
import com.handaebong.handaebong.model.shoplist_menu_model;
import com.handaebong.handaebong.model.shoplist_model;
import com.handaebong.handaebong.shop_focus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class shop_menu_category_adapter extends RecyclerView.Adapter<shop_menu_category_adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<shop_menu_category_model> arrData;
    private LinearLayout Layout;
    public shop_menu_category_adapter(Context c, ArrayList<shop_menu_category_model> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public shop_menu_category_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_menu_category, parent, false);
        return new shop_menu_category_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final shop_menu_category_adapter.ViewHolder holder, final int position) {
        final shop_menu_category_model items = arrData.get(holder.getAdapterPosition());

        holder.Txt_Title.setText(items.getTitle());

        if(items.getBol_choice() == true){
            holder.Txt_Title.setBackground(items.getActivity().getDrawable(R.drawable.border_menu_click));
            holder.Txt_Title.setTextColor(items.getActivity().getColor(R.color.white));
        }
        else{
            holder.Txt_Title.setBackground(items.getActivity().getDrawable(R.drawable.border_menu));
            holder.Txt_Title.setTextColor(items.getActivity().getColor(R.color.black));
        }

        holder.Txt_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < arrData.size(); i ++){
                    if(i == position){
                        shop_menu_category_models.set(i,new shop_menu_category_model(items.getActivity(), arrData.get(i).getTitle(), true));
                        str_category = items.getTitle();
                    }
                    else{
                        shop_menu_category_models.set(i,new shop_menu_category_model(items.getActivity(), arrData.get(i).getTitle(), false));
                    }
                }
                shop_menu_category_adapters.notifyDataSetChanged();

                int position = 0;
                for(int i =0; i< shop_menu_category_position_models.size(); i ++){
                    Log.i("테스트123", shop_menu_category_position_models.get(i).getTitle()+"//"+str_category+"//"+shop_menu_category_position_models.get(i).getPosition());
                    if(shop_menu_category_position_models.get(i).getTitle().equals(str_category)){
                        position = shop_menu_category_position_models.get(i).getPosition();
                    }
                }
                //Appbar.scrollTo(0, 430);

                /*Log.i("테스트00", position+"");
                Log.i("테스트01",  Appbar.getTotalScrollRange()+"");*/

                LinearLayoutManager layoutManager1 = new LinearLayoutManager(items.getActivity());
                layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
                layoutManager1.scrollToPositionWithOffset(position,0);
                List_Menu.setLayoutManager(layoutManager1);
                //List_Menu.scrollToPosition(position);

                //List_Menu.scrollToPosition(6);
            }
        });
    }
    @Override

    public int getItemCount() {
        return this.arrData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Txt_Title;
        public ViewHolder(final View itemView) {
            super(itemView);
            Txt_Title = (TextView)itemView.findViewById(R.id.txt_title);
        }
    }

    /*public class Async extends AsyncTask<String, Void, String> {
        String[] parseredData_access, parseredData_info;
        String[][] parseredData_list;
        String[] parseredData_list_img;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //현재 좌표 받아오기
            try {

                HttpClient_Get http = new HttpClient_Get();

                String result_info = http.HttpClient_Get("not", "v1/store/list?category="+str_category);
                parseredData_info = jsonParserList_access(result_info);

                Shoplist_models.clear();
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

                        Shoplist_models.add(new shoplist_model(act_shop_list, str_id, str_name, str_intro, "true", str_thumb1, str_thumb2, str_thumb3));
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

            LinearLayoutManager layoutManager = new LinearLayoutManager(act_shop_list);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);

            Shop_Adapter = new shop_adapter(act_shop_list, Shoplist_models);
            List_Contents.setLayoutManager(layoutManager);
            List_Contents.setAdapter(Shop_Adapter);

        }
    }*/
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
