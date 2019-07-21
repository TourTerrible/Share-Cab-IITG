package com.ahad.share_cab;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.makeText;

public class activity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView tv_date,tv_time;
    Button confirm_add,journeys, chats,add_new,history;
    FirebaseDatabase database;
    private DatabaseReference mDatabase, reference;
    DatePickerDialog dpd;
    TimePickerDialog picker;
    Calendar c;
    String date,time,push_id,name,userId,format;
    ProgressDialog mProgressDialog;
    Spinner route;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        getSupportActionBar().setTitle("New Journey");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        journeys= findViewById(R.id.journeys);
        journeys.setVisibility(View.INVISIBLE);

        tv_date=findViewById(R.id.tv_date);
        tv_time= findViewById(R.id.tv_time);

        confirm_add=findViewById(R.id.chats_recent_btn);
        route= findViewById(R.id.route);

        add_new= findViewById(R.id.add_new);
        history= findViewById(R.id.history);
        chats= findViewById(R.id.chats);



        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity2.this,com.ahad.share_cab.recent_chat_activity.class);
                startActivity(intent);

            }
        });

        add_new.setClickable(false);


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity2.this,com.ahad.share_cab.history_activity.class);
                startActivity(intent);

            }
        });




        mDatabase= FirebaseDatabase.getInstance().getReference();




        route.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Airport to Campus");
        categories.add("Campus to Airport");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        route.setAdapter(dataAdapter);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c= Calendar.getInstance();
                int day= c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);


                dpd= new DatePickerDialog(activity2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myear, int mmonth, int dayOfMonth) {
                        tv_date.setText(dayOfMonth + "/" + (mmonth+1) + "/" + myear);
                    }
                }, year,month,day);
                dpd.show();

            }
        });





        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(activity2.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                if (sHour == 0) {

                                    sHour += 12;

                                    format = "AM";
                                }
                                else if (sHour == 12) {

                                    format = "PM";

                                }
                                else if (sHour > 12) {

                                    sHour -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                                tv_time.setText(sHour + ":" + sMinute +" "+ format);

                            }
                        }, hour, minutes, false);
                picker.show();



            }
        });


        journeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity2.this,com.ahad.share_cab.history_activity.class);
                startActivity(intent);
            }
        });





       confirm_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               date=(tv_date.getText().toString());

               time= tv_time.getText().toString();
               database = FirebaseDatabase.getInstance();
               userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
               DatabaseReference data_push=mDatabase.child("journey").child(userId).push();
               push_id= data_push.getKey();
               mProgressDialog = new ProgressDialog( com.ahad.share_cab.activity2.this);

               mProgressDialog.setTitle("Adding journey");
               mProgressDialog.setMessage("Please wait");
               mProgressDialog.show();

               reference= mDatabase.child("users").child(userId).child("Name");
               reference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       name= dataSnapshot.getValue().toString();
                       if(!date.equals("")){
                       mDatabase.child("journey").child(userId).child(push_id).child("Name").setValue(name);
                       mDatabase.child("journey").child(userId).child(push_id).child("Date").setValue(date);
                       mDatabase.child("journey").child(userId).child(push_id).child("Time").setValue(time);
                       mDatabase.child("journey").child(userId).child(push_id).child("Id").setValue(userId);
                       mDatabase.child("journey").child(userId).child(push_id).child("route").setValue(route.getSelectedItem().toString());

                       mProgressDialog.dismiss();

                       Toast toast = makeText(getApplicationContext(), "Journey added successfully ", Toast.LENGTH_SHORT);
                       toast.show();
                       journeys.setVisibility(View.VISIBLE);
                           Intent intent= new Intent(activity2.this,com.ahad.share_cab.history_activity.class);
                           startActivity(intent);

                       }
                       else{
                           mProgressDialog.dismiss();
                           Toast toast = makeText(getApplicationContext(), "Please select Date and Time", Toast.LENGTH_SHORT);
                           toast.show();

                       }



                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       name="failed";

                   }
               });



           }
       });







    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String item = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {onBackPressed();
                return true;}

            case R.id.about:
                Intent intent= new Intent(activity2.this,com.ahad.share_cab.about_activity.class);
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

