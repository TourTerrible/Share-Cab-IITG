package com.ahad.share_cab;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat_activity extends AppCompatActivity {

    ImageButton btnsend;
    EditText msg;
    boolean seen;
    DatabaseReference rootref;
    FirebaseAuth auth;
    String current_user_id, chat_user_name,chat_user_id, current_user_name;
    private RecyclerView message_list;
    private final List<messages> msg_list= new ArrayList<>();

    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        msg= findViewById(R.id.msg);
        btnsend= findViewById(R.id.btnsend);

        mAdapter= new MessageAdapter(msg_list);

        message_list=findViewById(R.id.message_list);

        mLinearLayout= new LinearLayoutManager(this);
        message_list.setHasFixedSize(true);
        message_list.setLayoutManager(mLinearLayout);

        message_list.setAdapter(mAdapter);


        rootref= FirebaseDatabase.getInstance().getReference();

        auth= FirebaseAuth.getInstance();
        current_user_id    =  auth.getCurrentUser().getUid();

        chat_user_id= getIntent().getStringExtra("matchuserid");
        chat_user_name= getIntent().getStringExtra("searchuser");

        getSupportActionBar().setTitle(chat_user_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       /* rootref.child("userState").child(chat_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String LastSeen= dataSnapshot.child("state").getValue().toString();
                String LastDate= dataSnapshot.child("date").getValue().toString();
                String LastTime= dataSnapshot.child("time").getValue().toString();
               if(LastSeen.equals("online")){
                getSupportActionBar().setSubtitle("Online");
               }
               else{
                   getSupportActionBar().setSubtitle("Last Seen: "+ LastDate+" "+ LastTime);
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/






        //=====================================================================

        // retriving messages_  ==============================================================================

        rootref.child("messages").child(current_user_id).child(chat_user_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              messages msg= dataSnapshot.getValue(messages.class);

              msg_list.add(msg);
              mAdapter.notifyDataSetChanged();
              message_list.smoothScrollToPosition(message_list.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




//CREATING CHATS DATA==========================================================================================================================


        rootref.child("users").child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               current_user_name= dataSnapshot.child("Name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        msg.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    sendmessage();/* Write your logic here that will be executed when user taps next button */


                    handled = true;
                }
                return handled;
            }
        });



//SEND MESSAGE BUTTON==================================================================================================================================
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });

    }


    private void sendmessage() {

        String message= msg.getText().toString();
        if(!TextUtils.isEmpty(message)){

              String current_user_ref= "messages/"+ current_user_id+ "/"+ chat_user_id;
              String chat_user_ref= "messages/"+ chat_user_id+ "/"+ current_user_id;


              DatabaseReference user_message_push= rootref.child("messages").child(current_user_id).child(chat_user_id).push();
              String push_id= user_message_push.getKey();

              final Map messagemap= new HashMap();
              messagemap.put("message",message);

           /* rootref.child("chats").child(chat_user_id).child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean value= ((boolean) dataSnapshot.child("seen").getValue());
                    messagemap.put("seen",value);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

              messagemap.put("type","text");
              messagemap.put("time",ServerValue.TIMESTAMP);
              messagemap.put("from",current_user_id);

              Map messageusermap =new HashMap();
              messageusermap.put(current_user_ref+"/"+push_id,messagemap);
              messageusermap.put(chat_user_ref+"/"+push_id,messagemap);

              msg.setText("");

              rootref.updateChildren(messageusermap, new DatabaseReference.CompletionListener() {
                  @Override
                  public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                      if(databaseError!=null){
                          Log.d("Chat_log",databaseError.getMessage().toString());
                      }
                  }
              });

              addnotification(current_user_id,chat_user_id,message,current_user_name);

        }


    }

    private void addnotification(String current_user_id, String chat_user_id, String message, String current_user_name) {
        HashMap<String, String> Notificationmap = new HashMap<>();
        Notificationmap.put("Notification", message);
        Notificationmap.put("sender_id", current_user_id);
        Notificationmap.put("sender_name", current_user_name);

        rootref.child("Notification_req").child(chat_user_id).push().setValue(Notificationmap);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    /*private void updateUserState(String state){
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

        current_user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("userState").child(current_user_id).updateChildren(onlinestate);



    }*/



}
