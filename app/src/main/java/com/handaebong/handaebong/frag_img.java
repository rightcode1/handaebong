package com.handaebong.handaebong;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

//사용하지 않는 페이지 입니다.
public class frag_img extends Fragment {
    ImageView img;
    String str_Image = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_img, container, false);

        Bundle extra = getArguments();
        str_Image = extra.getString("Image");

        img = (ImageView) rootView.findViewById(R.id.img);
        Glide.with(getContext()).load(str_Image)
                .into(img);



        /*img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Async async = new Async();
                async.execute(str_Image, "and");*//*
            }
        });*/
        return rootView;
    }

}