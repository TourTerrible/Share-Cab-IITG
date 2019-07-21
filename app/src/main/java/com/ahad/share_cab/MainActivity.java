package com.ahad.share_cab;


import android.app.ProgressDialog;
import android.content.Intent;
/*import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;*/
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etname;

    Button btnsubmit;
    TextView tvresult;
    FirebaseAuth mAuth;
    Firebase mRef;
    Toast toast,toast1;
    private ProgressDialog Progressbar;


    FirebaseUser currentUser;
    String name;
    private DatabaseReference mDatabase;

    Spinner gender;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsubmit = findViewById(R.id.btnsubmit);
        etname = findViewById(R.id.etname);

        tvresult = findViewById(R.id.tvresult);
        mRef = new Firebase("https://sharecab-6e731.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        Progressbar = new ProgressDialog(this);
        gender= findViewById(R.id.gender);








        gender.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);
    }



    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            String token=  FirebaseInstanceId.getInstance().getToken().toString();
            mDatabase.child("users").child(currentUser.getUid().toString()).child("Token").setValue(token);
            Intent intent= new Intent(MainActivity.this,com.ahad.share_cab.activity2.class);
            startActivity(intent);
        }
        else {
            btnsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = etname.getText().toString().trim();
                    if(!TextUtils.isEmpty(name)){
                        Progressbar.setTitle("Logging In");
                        Progressbar.setMessage("Please wait ");
                        Progressbar.setCanceledOnTouchOutside(false);
                        Progressbar.show();

                    updateUI(currentUser);



                }
                    if(TextUtils.isEmpty(name)){

                         toast = Toast.makeText(getApplicationContext(), "Empty,Please Enter your name", Toast.LENGTH_SHORT);
                         toast.show();


                    }
                }

            });

        }



    }



    private void updateUI(FirebaseUser user){

        if(user==null){
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         FirebaseUser user= mAuth.getCurrentUser();
                         String userId= user.getUid();
                          //user.getIdToken(true).toString();
                         String token=  FirebaseInstanceId.getInstance().getToken().toString();

                         mDatabase= FirebaseDatabase.getInstance().getReference();
                         mDatabase.child("users").child(userId).child("Name").setValue(name);
                         mDatabase.child("users").child(userId).child("Token").setValue(token);
                         mDatabase.child("users").child(userId).child("Gender").setValue(gender.getSelectedItem().toString());
                         toast1 = Toast.makeText(getApplicationContext(), " Successfully Logged In as "+ name , Toast.LENGTH_SHORT);
                         toast1.show();

                         Intent intent= new Intent(MainActivity.this,com.ahad.share_cab.activity2.class);
                         intent.putExtra("name", name);
                         startActivity(intent);

                     } else{
                         toast = Toast.makeText(getApplicationContext(), "Failed : Network or any other issue", Toast.LENGTH_LONG);
                         toast.show();
                     }

                }
            });

        }else {
            toast = Toast.makeText(getApplicationContext(), "Already logged in ", Toast.LENGTH_LONG);
            toast.show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String item = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
