package com.ahad.share_cab;


import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    String current_user_id,from_user;
    private List<messages> messagesList;
    public MessageAdapter(List<messages> messagesList){
        this.messagesList=messagesList;

    }



    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single,parent,false);

        return  new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder, int i) {
        current_user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();





        messages c = messagesList.get(i);
        from_user= c.getFrom();
        if(from_user.equals(current_user_id)){
            viewHolder.messageText.setVisibility(View.GONE);
            viewHolder.time_chat_msg.setVisibility(View.GONE);
            //viewHolder.messageText.setTextColor(Color.BLACK);
            //viewHolder.messageText.setText(c.getMessage());
            viewHolder.time_self_msg.setVisibility(View.VISIBLE);
            viewHolder.self_msg.setVisibility(View.VISIBLE);
            viewHolder.self_msg.setText(c.getMessage());
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(c.getTime());
            String date = DateFormat.format("dd-MM-yyyy hh:mm aa", cal).toString();
            viewHolder.time_self_msg.setText(date.substring(11,19));



        }

        else{
           viewHolder.self_msg.setVisibility(View.GONE);
           viewHolder.time_self_msg.setVisibility(View.GONE);
           //viewHolder.messageText.setBackgroundColor(Color.BLUE);
          //viewHolder.messageText.setTextColor(Color.WHITE);
            viewHolder.time_chat_msg.setVisibility(View.VISIBLE);
            viewHolder.messageText.setVisibility(View.VISIBLE);
            viewHolder.messageText.setText(c.getMessage());
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(c.getTime());
            Date d = cal.getTime();

            String date = DateFormat.format("dd-MM-yyyy hh:mm aa", cal).toString();
            viewHolder.time_chat_msg.setText(date.substring(11,19));

        }



    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    public class MessageViewHolder extends  RecyclerView.ViewHolder {
        public TextView messageText,self_msg, time_self_msg, time_chat_msg;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText= itemView.findViewById(R.id.message_text_layout);
            self_msg= itemView.findViewById(R.id.self_msg_text_layout);
            time_chat_msg=itemView.findViewById(R.id.time_chat_msg);
            time_self_msg= itemView.findViewById(R.id.time_self_msg);

        }
    }

//**************************Get time Ago******************************************************//



}
