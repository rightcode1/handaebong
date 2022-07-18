package com.handaebong.handaebong;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//홈 상단 배너 프래그먼트 정의
public class Fragment_event extends Fragment {
    ImageView img;
    String str_title = "", str_diff = "", str_url ="";
    String str_thumbnail = "", str_image = "", str_storeId ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        //이전 데이터값 받아오기
        Bundle extra = getArguments();
        str_title = extra.getString("title");
        str_diff = extra.getString("diff");
        str_url = extra.getString("url");
        str_thumbnail = extra.getString("thumbnail");
        str_image = extra.getString("image");
        str_storeId = extra.getString("storeId");

        //이미지 정의
        img = (ImageView) rootView.findViewById(R.id.img);
        Glide.with(getActivity()).load(str_thumbnail)
                .into(img);

        //배너 이미지 클릭시 이동 경로 정의
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_diff.equals("업체이동")){
                    Intent intent = new Intent(getActivity(), shop_focus.class);
                    intent.putExtra("id", str_storeId);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
                }
                else if(str_diff.equals("이미지")){
                    Intent intent = new Intent(getActivity(), event_img.class);
                    intent.putExtra("img", str_image);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
                }
                else if(str_diff.equals("url")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_url));
                    getActivity().startActivity(browserIntent);
                }
            }
        });
        return rootView;
    }
}
