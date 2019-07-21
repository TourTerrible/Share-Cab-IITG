package com.ahad.share_cab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.makeText;

public class about_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageButton btnsend_suggestion;
        final EditText suggestion;
        final String current_user_id;
        final ProgressDialog mProgressDialog;
        TextView googlelink;
        googlelink= findViewById(R.id.googlelink);

        btnsend_suggestion= findViewById(R.id.btnsend_suggestion);
        suggestion = findViewById(R.id.suggestion);

        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Suggestions").child(current_user_id).child("suggestion").setValue(suggestion.getText().toString());
        mProgressDialog = new ProgressDialog( com.ahad.share_cab.about_activity.this);

        suggestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {


                    if(TextUtils.isEmpty(suggestion.getText().toString())){
                        Toast toast = makeText(getApplicationContext(), "Enter some suggestion!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        mProgressDialog.setTitle("Sending.....");
                        mProgressDialog.setMessage("Please wait");
                        mProgressDialog.show();
                        mDatabase.child("users").child(current_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("Name").getValue().toString();
                                mDatabase.child("Suggestions").child(current_user_id).push().child("suggestion").setValue(suggestion.getText().toString());
                                mDatabase.child("Suggestions").child(current_user_id).child("Name").setValue(name);
                                mProgressDialog.dismiss();
                                Toast toast = makeText(getApplicationContext(), "Feedback Submitted", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }/* Write your logic here that will be executed when user taps next button */


                    handled = true;
                }
                return handled;
            }
        });


        btnsend_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(suggestion.getText().toString())){
                    Toast toast = makeText(getApplicationContext(), "Enter some suggestion!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    mProgressDialog.setTitle("Sending.....");
                    mProgressDialog.setMessage("Please wait");
                    mProgressDialog.show();
                    mDatabase.child("users").child(current_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("Name").getValue().toString();
                            mDatabase.child("Suggestions").child(current_user_id).push().child("suggestion").setValue(suggestion.getText().toString());
                            mDatabase.child("Suggestions").child(current_user_id).child("Name").setValue(name);
                            mProgressDialog.dismiss();
                            Toast toast = makeText(getApplicationContext(), "Feedback Submitted", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });




        googlelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/3Fqsh8FDC8Sxb6Es6"));
                startActivity(browserIntent);
            }
        });


    }
}
