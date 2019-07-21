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

public class personAdapter extends RecyclerView.Adapter<personAdapter.Viewholder> {

    private ArrayList<Person> people;
    private  Onresultlistner  monresultlistner;

    public  personAdapter(Context context, ArrayList<Person> list, Onresultlistner onresultlistner){

        people= list;
        this.monresultlistner = onresultlistner;


    }

    /*public personAdapter(activity2 context, ArrayList<com.example.share_cab.Person> list) {

        people=list;
    }*/

    public class Viewholder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtname;
        TextView txtsurname,userid, name_single;
        Onresultlistner onresultlistner;
        public Viewholder(@NonNull View itemView, Onresultlistner onresultlistner) {
            super(itemView);

            txtname= itemView.findViewById(R.id.txtname);
            txtsurname = itemView.findViewById(R.id.txtsurname);
            name_single=itemView.findViewById(R.id.time_single);
            userid = itemView.findViewById(R.id.userid);
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
    public personAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items,viewGroup,false);
        return new Viewholder(v, monresultlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull personAdapter.Viewholder viewHolder, int i) {

        viewHolder.itemView.setTag(people.get(i));

        viewHolder.txtname.setText((people.get(i).getName()));
        viewHolder.txtsurname.setText((people.get(i).getSurname()));
        viewHolder.name_single.setText(people.get(i).getTime_single());
        viewHolder.userid.setText((people.get(i).getUserid()));
        viewHolder.userid.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return people.size();
    }

}
