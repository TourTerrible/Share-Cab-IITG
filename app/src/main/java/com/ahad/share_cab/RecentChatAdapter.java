package com.ahad.share_cab;

import android.content.Context;
/*import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.Viewholder> {

    private ArrayList<RecentChat> people;
    private  Onresultlistner  monresultlistner;

    public  RecentChatAdapter(Context context, ArrayList<RecentChat> list, Onresultlistner onresultlistner){

        people= list;
        this.monresultlistner = onresultlistner;


    }


    public class Viewholder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name_chat_user, last_msg_single, time_last_msg;
        ImageView image_default, circle_crop;
        Onresultlistner onresultlistner;
        public Viewholder(@NonNull View itemView, Onresultlistner onresultlistner) {
            super(itemView);

            name_chat_user= itemView.findViewById(R.id.name_chat_user);
            last_msg_single= itemView.findViewById(R.id.last_msg_single);
            time_last_msg= itemView.findViewById(R.id.time_last_msg);
            image_default= itemView.findViewById(R.id.image_default);
            circle_crop= itemView.findViewById(R.id.crop_circle);
            this.onresultlistner=onresultlistner;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            onresultlistner.Onclickresult(getAdapterPosition());

        }
    }

    public interface Onresultlistner{
        void Onclickresult(int position);



    }

    @NonNull
    @Override
    public RecentChatAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_recent_single,viewGroup,false);
        return new Viewholder(v, monresultlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecentChatAdapter.Viewholder viewHolder, int i) {

        viewHolder.itemView.setTag(people.get(i));

        viewHolder.time_last_msg.setText(people.get(i).getTime_last_msg());
        viewHolder.name_chat_user.setText((people.get(i).getName()));
        viewHolder.last_msg_single.setText((people.get(i).getLast_msg_single()));
       FirebaseDatabase.getInstance().getReference().child("users").child(people.get(i).getUserid()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String gender=dataSnapshot.child("Gender").getValue().toString();
               if(gender.equals("Female")){

                   viewHolder.image_default.setImageResource(R.drawable.girl2);
               }

               else{
                   viewHolder.image_default.setImageResource(R.drawable.images);

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


        viewHolder.circle_crop.setImageResource(R.drawable.circle);



    }

    @Override
    public int getItemCount() {
        return people.size();
    }

}
