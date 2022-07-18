package com.handaebong.handaebong.Adapter;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.handaebong.handaebong.chat_room.Chat_message_models;
import static com.handaebong.handaebong.chat_room.Layout_Answer;
import static com.handaebong.handaebong.chat_room.List_Chat;
import static com.handaebong.handaebong.chat_room.Txt_Answer_Content;
import static com.handaebong.handaebong.chat_room.Txt_Answer_Nickname;
import static com.handaebong.handaebong.chat_room.answer_id;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.handaebong.handaebong.MainActivity;
import com.handaebong.handaebong.R;
import com.handaebong.handaebong.model.chat_message_model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

public class chat_message_adapter extends RecyclerView.Adapter<chat_message_adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<chat_message_model> arrData;


    public chat_message_adapter(Context c, ArrayList<chat_message_model> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public chat_message_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == 0){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chatroom_date, parent, false);
        }
        else if(viewType == 1){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chatroom_message_my, parent, false);
        }
        else if(viewType == 2){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chatroom_message_other, parent, false);
        }
        else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chatroom_date, parent, false);
        }
        return new chat_message_adapter.ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if(arrData.get(position).getCategory().equals("date")){
            return 0;
        }
        else if(arrData.get(position).getCategory().equals("message_my")){
            return 1;
        }
        else{
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(final chat_message_adapter.ViewHolder holder, final int position) {
        final chat_message_model items = arrData.get(position);

        if(arrData.get(position).getCategory().equals("date")){
            //holder.Txt_Message.setText(items.getMessage());
        }
        else if(arrData.get(position).getCategory().equals("message_my")){
            if(arrData.get(position).getBol_linedate() == true){
                String str_onlydate = arrData.get(position).getCreatedAt().substring(0, 10);

                holder.Layout_Linedate.setVisibility(View.VISIBLE);
                holder.Txt_Linedate.setText(str_onlydate);
            }
            else{
                holder.Layout_Linedate.setVisibility(View.GONE);
            }

            if(position == arrData.size()-1){
                holder.Txt_Date.setVisibility(View.VISIBLE);
            }
            else{
                if(arrData.get(position).getCreatedAt().equals(arrData.get(position+1).getCreatedAt()) && arrData.get(position).getUserName().equals(arrData.get(position+1).getUserName())){
                    holder.Txt_Date.setVisibility(View.GONE);
                }
                else{
                    holder.Txt_Date.setVisibility(View.VISIBLE);
                }
            }
            /*if(arrData.get(position).getBol_linetime() == true){
                holder.Txt_Date.setVisibility(View.VISIBLE);
            }
            else{
                holder.Txt_Date.setVisibility(View.GONE);
            }*/

            holder.Txt_Message.setText(items.getMessage());
            holder.Txt_Name.setText(items.getUserName());

            if(items.getFirst_Id().equals("")){
                holder.Layout_Chat_Answer.setVisibility(View.GONE);
            }
            else{
                holder.Layout_Chat_Answer.setVisibility(View.VISIBLE);
                holder.Txt_Answer_Nickname.setText(items.getFirst_UserName());
                holder.Txt_Answer_Content.setText(items.getFirst_Message());
            }

            String str_onlytime = arrData.get(position).getCreatedAt().substring(11, 16);

            holder.Txt_Date.setText(str_onlytime);
        }
        else if(arrData.get(position).getCategory().equals("message_other")){
            if(arrData.get(position).getBol_linedate() == true){
                String str_onlydate = arrData.get(position).getCreatedAt().substring(0, 10);

                holder.Layout_Linedate.setVisibility(View.VISIBLE);
                holder.Txt_Linedate.setText(str_onlydate);
            }
            else{
                holder.Layout_Linedate.setVisibility(View.GONE);
            }

            if(position == arrData.size()-1){
                holder.Txt_Date.setVisibility(View.VISIBLE);
            }
            else{
                if(arrData.get(position).getCreatedAt().equals(arrData.get(position+1).getCreatedAt()) && arrData.get(position).getUserName().equals(arrData.get(position+1).getUserName())){
                    holder.Txt_Date.setVisibility(View.GONE);
                }
                else{
                    holder.Txt_Date.setVisibility(View.VISIBLE);
                }
            }


            holder.Txt_Message.setText(items.getMessage());
            holder.Txt_Name.setText(items.getUserName());

           /* if(items.getBol_exist() == true){
                holder.Txt_Name.setVisibility(View.GONE);
            }
            else{
                holder.Txt_Name.setVisibility(View.VISIBLE);
            }*/

            if(position != 0){
                if(arrData.get(position).getUserName().equals(arrData.get(position-1).getUserName())){
                    holder.Txt_Name.setVisibility(View.GONE);
                }
                else{
                    holder.Txt_Name.setVisibility(View.VISIBLE);
                }
            }


            if(items.getFirst_Id().equals("")){
                holder.Layout_Chat_Answer.setVisibility(View.GONE);
            }
            else{
                holder.Layout_Chat_Answer.setVisibility(View.VISIBLE);
                holder.Txt_Answer_Nickname.setText(items.getFirst_UserName());
                holder.Txt_Answer_Content.setText(items.getFirst_Message());
            }

            String str_onlytime = arrData.get(position).getCreatedAt().substring(11, 16);

            holder.Txt_Date.setText(str_onlytime);
           /* holder.Img_Answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Layout_Answer.setVisibility(View.VISIBLE);
                    answer_id = Integer.parseInt(items.getId());
                    Txt_Answer_Nickname.setText(items.getUserName()+"에게 답장");
                    Txt_Answer_Content.setText(items.getMessage());
                }
            });*/

        }

        holder.Layout_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!items.getFirst_Id().equals("")){
                    for(int i = 0 ; i< Chat_message_models.size(); i++){
                        if(Chat_message_models.get(i).getId().equals(items.getFirst_Id())){
                            List_Chat.scrollToPosition(i);
                        }
                    }
                }
                else{
                    setdialog(items);
                }
            }
        });

        holder.Txt_Answer_Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!items.getFirst_Id().equals("")){
                    for(int i = 0 ; i< Chat_message_models.size(); i++){
                        if(Chat_message_models.get(i).getId().equals(items.getFirst_Id())){
                            List_Chat.scrollToPosition(i);
                        }
                    }
                }
                else{
                    setdialog(items);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return this.arrData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Txt_Message;
        private TextView Txt_Name;
        private ImageView Img_Answer;

        private LinearLayout Layout_Chat;

        private LinearLayout Layout_Chat_Answer;
        private TextView Txt_Answer_Nickname;
        private TextView Txt_Answer_Content;
        private TextView Txt_Date;

        private LinearLayout Layout_Linedate;
        private TextView Txt_Linedate;
        public ViewHolder(final View itemView) {
            super(itemView);
            Txt_Message = (TextView) itemView.findViewById(R.id.txt_message);
            Txt_Name = (TextView)itemView.findViewById(R.id.txt_name);
            Img_Answer = (ImageView)itemView.findViewById(R.id.img_answer);
            Layout_Chat_Answer = (LinearLayout)itemView.findViewById(R.id.layout_answer);

            Txt_Answer_Nickname = (TextView)itemView.findViewById(R.id.txt_answer_nickname);
            Txt_Answer_Content = (TextView)itemView.findViewById(R.id.txt_answer_content);

            Layout_Chat = (LinearLayout)itemView.findViewById(R.id.layout_chat);

            Txt_Date = (TextView) itemView.findViewById(R.id.txt_date);
            Layout_Linedate = (LinearLayout)itemView.findViewById(R.id.layout_linedate);
            Txt_Linedate = (TextView) itemView.findViewById(R.id.txt_linedate);
            //Layout = (LinearLayout)itemView.findViewById(R.id.layout);
        }

    }

    public void setdialog(chat_message_model items){
        MaterialDialog dialog;
        LayoutInflater inflater = (LayoutInflater)items.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_chat, (ViewGroup)items.getActivity().findViewById(R.id.root));
        final TextView Dialog_Chat_Copy = (TextView) layout.findViewById(R.id.dialog_chat_copy);
        final TextView Dialog_Chat_Reply = (TextView) layout.findViewById(R.id.dialog_chat_reply);

        dialog = new MaterialDialog(items.getActivity());
        dialog
                .setContentView(layout)
                .setCanceledOnTouchOutside(true);
        dialog.show();


        Dialog_Chat_Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) items.getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", items.getMessage());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(items.getActivity(), "복사되었습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Dialog_Chat_Reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Layout_Answer.setVisibility(View.VISIBLE);
                answer_id = Integer.parseInt(items.getId());
                Txt_Answer_Nickname.setText(items.getUserName()+"에게 답장");
                Txt_Answer_Content.setText(items.getMessage());

                dialog.dismiss();
            }
        });
    }
}