package com.ahad.share_cab;

import android.app.ProgressDialog;
import android.content.Intent;


import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class recent_chat_activity extends AppCompatActivity implements RecentChatAdapter.Onresultlistner  {



    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RecentChat> people;
    FirebaseDatabase database;
    DatabaseReference ref,ref_user,ref_message;
    ProgressDialog mProgressDialog;
    Button add_new,history,chats;





    String id,last_msg;
    long timestamp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_chat_activity);
        getSupportActionBar().setTitle("Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_new= findViewById(R.id.add_new);
        history= findViewById(R.id.history);
        chats= findViewById(R.id.chats);



        chats.setClickable(false);

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(recent_chat_activity.this,com.ahad.share_cab.activity2.class);
                startActivity(intent);

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(recent_chat_activity.this,com.ahad.share_cab.history_activity.class);
                startActivity(intent);

            }
        });


        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Loading Previous Chats");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.show();




        recyclerView= findViewById(R.id.chat_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        people= new ArrayList<RecentChat>();
        final String current_user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref= database.getInstance().getReference().child("messages").child(current_user);

        ref_message= database.getInstance().getReference().child("messages");
        ref_message.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user)){
                    mProgressDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(), "No Recent Chat", Toast.LENGTH_LONG);
                    toast.show();

                }

                else{
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot itemsnapshot:dataSnapshot.getChildren()){
                                people.clear();


                                final String id= itemsnapshot.getKey().toString();
                                for(DataSnapshot push_key:itemsnapshot.getChildren()){

                                    last_msg=push_key.child("message").getValue().toString();
                                    timestamp = ((long) push_key.child("time").getValue());
                                }
                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                cal.setTimeInMillis(timestamp);
                                Date d = cal.getTime();


                                final String last_chat=last_msg;
                                final String time_last= getTimeAgo(d);

                                ref_user= database.getInstance().getReference().child("users").child(id);
                                ref_user.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("Name").getValue()!=null) {
                                            String name = dataSnapshot.child("Name").getValue().toString();
                                            people.add(new RecentChat(name, id, last_chat, time_last));

                                            myAdapter = new RecentChatAdapter(com.ahad.share_cab.recent_chat_activity.this, people, com.ahad.share_cab.recent_chat_activity.this);
                                            recyclerView.setAdapter(myAdapter);
                                            mProgressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
        Intent intent= new Intent(recent_chat_activity.this,com.ahad.share_cab.chat_activity.class);
        intent.putExtra("searchuser", people.get(position).getName());
        intent.putExtra("matchuserid", people.get(position).getUserid());
        startActivity(intent);
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date) {

        if(date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (dim == 0) {

            timeAgo = "Just now";
        } else if (dim == 1) {
            return "1 minute ago" ;
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " minutes ago" ;
        } else if (dim >= 45 && dim <= 89) {
            timeAgo ="about an hour ago";
        } else if (dim >= 90 && dim <= 900) {
            timeAgo = "about " + (Math.round(dim / 60)) + " hours ago";
        }else if (dim >= 901 && dim <= 1439) {
            timeAgo = "Yesterday" ;
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 day ago";
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " days ago";
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo ="about a month ago";
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " months ago" ;
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo ="about an year ago";
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = "over a year ago";
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = "almost 2 years ago";
        } else {
            timeAgo = "about " + (Math.round(dim / 525600)) + " years" ;
        }

        return timeAgo;
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {onBackPressed();
                return true;}

            case R.id.about:
                Intent intent= new Intent(recent_chat_activity.this,com.ahad.share_cab.about_activity.class);
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
   /* private void updateUserState(String state){
        String save_current_date,save_current_time;

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat current_date= new SimpleDateFormat("MMM,dd");
        save_current_date= current_date.format(calendar.getTime());

        SimpleDateFormat current_time= new SimpleDateFormat("hh:mm a");
        save_current_time= current_time.format(calendar.getTime());


        HashMap<String, Object> onlinestate= new HashMap<>();

        onlinestate.put("time",save_current_time);
        onlinestate.put("date",save_current_date);
        onlinestate.put("state",state);
        onlinestate.put("timestamp", ServerValue.TIMESTAMP);

        String current_user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("userState").child(current_user_id).updateChildren(onlinestate);



    }*/
}
