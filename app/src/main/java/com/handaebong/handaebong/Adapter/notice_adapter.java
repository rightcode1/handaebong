package com.handaebong.handaebong.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.handaebong.handaebong.R;
import com.handaebong.handaebong.model.notice_model;
import com.handaebong.handaebong.setting_notice_focus;
import com.handaebong.handaebong.setting_rec_focus;

import java.util.ArrayList;

public class notice_adapter extends RecyclerView.Adapter<notice_adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<notice_model> arrData;
    private LinearLayout Layout;

    public notice_adapter(Context c, ArrayList<notice_model> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public notice_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notice, parent, false);
        return new notice_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final notice_adapter.ViewHolder holder, final int position) {
        final notice_model items = arrData.get(position);

        holder.Txt_Title.setText(items.getTitle());
        holder.Txt_Contents.setText(items.getContent());
        holder.Txt_Date.setText(items.getCreatedAt());

        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(items.getActivity(), setting_notice_focus.class);
                intent.putExtra("title", items.getTitle());
                intent.putExtra("content", items.getContent());
                intent.putExtra("date", items.getCreatedAt());
                items.getActivity().startActivity(intent);
                items.getActivity().overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_none);
            }
        });
    }
    @Override

    public int getItemCount() {
        return this.arrData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Txt_Title, Txt_Contents, Txt_Date;
        LinearLayout Layout;
        public ViewHolder(final View itemView) {
            super(itemView);
            Txt_Title = (TextView) itemView.findViewById(R.id.txt_title);
            Txt_Contents = (TextView) itemView.findViewById(R.id.txt_contents);
            Txt_Date = (TextView) itemView.findViewById(R.id.txt_date);
            Layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}

