package com.ahad.share_cab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
/*import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

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

public class history_activity extends AppCompatActivity implements JourneyAdapter.Onresultlistner {


    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<Journey> people;
    FirebaseDatabase database;
    DatabaseReference ref,refcurrent;
    ProgressDialog mProgressDialog;
    String current_user;
    TextView hint2;
    Button add_new,history,chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        hint2=findViewById(R.id.hint2);



        getSupportActionBar().setTitle("Your Journeys");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView= findViewById(R.id.history_recycler_view);


        add_new= findViewById(R.id.add_new);
        history= findViewById(R.id.history);
        chats= findViewById(R.id.chats);



        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(history_activity.this,com.ahad.share_cab.recent_chat_activity.class);
                startActivity(intent);

            }
        });

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(history_activity.this,com.ahad.share_cab.activity2.class);
                startActivity(intent);

            }
        });
        history.setClickable(false);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        people= new ArrayList<Journey>();
        database= FirebaseDatabase.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Loading added journey");
        mProgressDialog.setMessage("Please wait");
        current_user= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProgressDialog.show();
        database.getReference().child("journey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user)){
                mProgressDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(), "No journey Added", Toast.LENGTH_LONG);
                    toast.show();
                    hint2.setText("No journey added by you! Journeys added by you will be shown here!");}

                else{

                    hint2.setText("Single Click on journey to search and Long Click to delete");

                    final String current_user= FirebaseAuth.getInstance().getCurrentUser().getUid();

                    ref= database.getReference().child("journey").child(current_user);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            people.clear();

                            for(DataSnapshot item_snapshot:dataSnapshot.getChildren()){

                                   if(item_snapshot.child("Date").getValue()!=null){
                                       if(item_snapshot.child("Time").getValue()!=null){
                                           if(item_snapshot.child("route").getValue()!=null){
                                               if(item_snapshot.getKey()!=null){

                                    String date=item_snapshot.child("Date").getValue().toString();
                                    String time=item_snapshot.child("Time").getValue().toString();
                                    String route=item_snapshot.child("route").getValue().toString();
                                    String pushid= item_snapshot.getKey();
                                    people.add(new Journey(date, time ,pushid, route));
                                    mProgressDialog.dismiss();}}}}


                            }
                            myAdapter= new JourneyAdapter(com.ahad.share_cab.history_activity.this,people,com.ahad.share_cab.history_activity.this);
                            recyclerView.setAdapter(myAdapter);

                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            mProgressDialog.dismiss();
                            Toast toast = Toast.makeText(getApplicationContext(), "Failed : Network or any other issue", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
















    @Override
    public void Onclickresult(int position) {

        people.get(position);
        Intent intent= new Intent(history_activity.this,com.ahad.share_cab.activity3.class);
        //intent.putExtra("searchuser", people.get(position).getName());
        intent.putExtra("pushid", people.get(position).getPushid());
        intent.putExtra("Date",people.get(position).getDate());
        intent.putExtra("route",people.get(position).getRoute());

        startActivity(intent);

    }

    @Override
    public void Onlongclickresult(int position) {
        alertDialog(position);
    }


    private void alertDialog(final int position ) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Clicking Yes will delete your journey");
        dialog.setTitle("Delete Journey?");
        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                       final String pushid= people.get(position).getPushid();
                       ref.child(pushid).removeValue();

                    }
                });
        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"cancelled",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {onBackPressed();
                return true;}

            case R.id.about:
                Intent intent= new Intent(history_activity.this,com.ahad.share_cab.about_activity.class);
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

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}
