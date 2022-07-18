package com.handaebong.handaebong.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.handaebong.handaebong.R;
import com.handaebong.handaebong.model.inquiry_model;
import com.handaebong.handaebong.model.notice_model;
import com.handaebong.handaebong.setting_rec_focus;
import com.handaebong.handaebong.shop_focus;

import java.util.ArrayList;

public class inquiry_adapter extends RecyclerView.Adapter<inquiry_adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<inquiry_model> arrData;
    private LinearLayout Layout;

    public inquiry_adapter(Context c, ArrayList<inquiry_model> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public inquiry_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quiry, parent, false);
        return new inquiry_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final inquiry_adapter.ViewHolder holder, final int position) {
        final inquiry_model items = arrData.get(position);

        holder.Txt_Title.setText(items.getTitle());
        holder.Txt_Date.setText(items.getCreatedAt());

        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(items.getActivity(), setting_rec_focus.class);
                intent.putExtra("title", items.getTitle());
                intent.putExtra("content", items.getContent());
                intent.putExtra("date", items.getCreatedAt());
                intent.putExtra("comment_title", items.getCommenttitle());
                intent.putExtra("comment_comment", items.getComment());
                items.getActivity().startActivity(intent);
                items.getActivity().overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });
        if(items.getComment().equals("null")){
            holder.Txt_Status.setText("답변대기");
            holder.Txt_Status.setTextColor(Color.parseColor("#fe4e4d"));
        }
        else{
            holder.Txt_Status.setText("답변완료");
            holder.Txt_Status.setTextColor(Color.parseColor("#85d800"));
        }
    }
    @Override

    public int getItemCount() {
        return this.arrData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Txt_Title, Txt_Date, Txt_Status;
        LinearLayout Layout;
        public ViewHolder(final View itemView) {
            super(itemView);
            Txt_Title = (TextView) itemView.findViewById(R.id.txt_title);
            Txt_Status = (TextView) itemView.findViewById(R.id.txt_status);
            Txt_Date = (TextView) itemView.findViewById(R.id.txt_date);
            Layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}


