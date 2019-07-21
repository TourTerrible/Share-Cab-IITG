package com.ahad.share_cab;

import android.content.Context;
/*import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.Viewholder> {

    private ArrayList<Journey> people;
    private  Onresultlistner  monresultlistner;

    public  JourneyAdapter(Context context, ArrayList<Journey> list, Onresultlistner onresultlistner){

        people= list;
        this.monresultlistner = onresultlistner;


    }


    public class Viewholder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tv_date_single,tv_time_single,pushid, tv_route;
        Onresultlistner onresultlistner;
        public Viewholder(@NonNull View itemView, Onresultlistner onresultlistner) {
            super(itemView);

            tv_date_single= itemView.findViewById(R.id.tv_date_single);
            tv_time_single = itemView.findViewById(R.id.tv_time_single);
            tv_route= itemView.findViewById(R.id.tv_route);
            pushid = itemView.findViewById(R.id.pushid);
            this.onresultlistner=onresultlistner;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

            onresultlistner.Onclickresult(getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View v) {
            onresultlistner.Onlongclickresult(getAdapterPosition());
            return true;
        }
    }

    public interface Onresultlistner{
        void Onclickresult(int position);
        void Onlongclickresult(int position);



    }


    @NonNull
    @Override
    public JourneyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_list,viewGroup,false);
        return new Viewholder(v, monresultlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyAdapter.Viewholder viewHolder, int i) {

        viewHolder.itemView.setTag(people.get(i));

        viewHolder.tv_date_single.setText((people.get(i).getDate()));
        viewHolder.tv_time_single.setText((people.get(i).getTime()));
        viewHolder.tv_route.setText(people.get(i).getRoute());
        viewHolder.pushid.setText((people.get(i).getPushid()));
        viewHolder.pushid.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return people.size();
    }

}
