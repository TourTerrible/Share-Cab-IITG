package com.ahad.share_cab;

import android.content.Intent;
/*import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;*/
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity3 extends AppCompatActivity implements personAdapter.Onresultlistner {

    TextView tv,hint;
    FirebaseDatabase database;
    DatabaseReference ref,ref_user;
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    LinearLayoutManager layoutManager;

    public String current_date,match_user_name,match_user_id, current_user_route;
    String namegot,id;

    ArrayList<Person> people;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);
        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database= FirebaseDatabase.getInstance();
        recyclerView= findViewById(R.id.list);
        hint=findViewById(R.id.hint);
        tv= findViewById(R.id.tv_popup_id);


        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        people= new ArrayList<Person>();
        current_date= getIntent().getStringExtra("Date");
        current_user_route = getIntent().getStringExtra("route");


               final String current_user=FirebaseAuth.getInstance().getCurrentUser().getUid();

                ref= database.getReference().child("journey");




                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        i=0;

                        people.clear();

                        for(DataSnapshot user_snapshot:dataSnapshot.getChildren()) {

                            for(DataSnapshot pushid_snap: user_snapshot.getChildren()){

                                if(pushid_snap.child("Date").getValue()!=null){
                                    if(pushid_snap.child("Time").getValue()!=null){
                                        if(pushid_snap.child("route").getValue()!=null){
                                            if(pushid_snap.child("Id").getValue()!=null){
                                            if(pushid_snap.getKey()!=null) {

                                                String date = pushid_snap.child("Date").getValue().toString();
                                                String Id = pushid_snap.child("Id").getValue().toString();
                                                String time = pushid_snap.child("Time").getValue().toString();
                                                String name = pushid_snap.child("Name").getValue().toString();
                                                String route = pushid_snap.child("route").getValue().toString();


                                                if (!current_user.equals(Id)) {
                                                    if (date.equals(current_date) & route.equals(current_user_route)) {


                                                        match_user_id = Id;
                                                        match_user_name = name;


                                                        people.add(new Person(match_user_name,date, match_user_id,time));
                                                        i = i + 1;


                                                    }
                                                }
                                            }}}}}
                            }


                        }
                        myAdapter= new personAdapter(com.ahad.share_cab.activity3.this,people,com.ahad.share_cab.activity3.this);
                        recyclerView.setAdapter(myAdapter);




                        if(i==0){
                            tv.setText("Bad Luck! No Journey on this day");
                            hint.setText("Don't Worry, You'll get a notification if anyone add journey on same day and if He/She messages you");
                        }
                        else {
                            tv.setText(i+" Journey(s) on same day!");
                            hint.setText("Click on search results to chat");
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        tv.setText("Failed to retrive data");

                    }
                });










    }

    public String findnamebyid(final String id) {

        ref_user=ref.getParent().child("users");

        ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                namegot  = dataSnapshot.child(id).child("Name").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                namegot="could not fetch name";

            }
        });

      return namegot;
    }

    @Override
    public void Onclickresult(int position) {
        people.get(position);
        Intent intent= new Intent(activity3.this,com.ahad.share_cab.chat_activity.class);
        intent.putExtra("searchuser", people.get(position).getName());
        intent.putExtra("matchuserid", people.get(position).getUserid());
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {onBackPressed();
                return true;}

                //help
            case R.id.about:
                Intent intent= new Intent(activity3.this,com.ahad.share_cab.about_activity.class);
                startActivity(intent);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_side,menu);
        return true;
    }
}
