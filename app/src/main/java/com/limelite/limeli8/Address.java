package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Address extends AppCompatActivity {

    TextView userName, userDeatils, gstView, gstShow;
    CustomProgressBar customProgressBar;
    Button changeAddress;

    String houseNo, roadNo, city, state, pinCode, landmark, number, alterNumber, name, details = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Toolbar toolbar = findViewById(R.id.homePageToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Address");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);

        customProgressBar = new CustomProgressBar(Address.this);
        customProgressBar.startProgressBar();

        userName = findViewById(R.id.name);
        userDeatils = findViewById(R.id.details);
        gstView = findViewById(R.id.gst);
        gstShow = findViewById(R.id.gst_show);
        changeAddress = findViewById(R.id.change_address);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/userDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String houseNo, roadNo, city, state, pinCode, landmark, number, alterNumber, name, details = "";

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    houseNo = dataSnapshot1.child("houseNo").getValue(String.class);
                    roadNo = dataSnapshot1.child("roadNo").getValue(String.class);
                    city = dataSnapshot1.child("city").getValue(String.class);
                    state = dataSnapshot1.child("state").getValue(String.class);
                    pinCode = dataSnapshot1.child("pinCode").getValue(String.class);
                    landmark = dataSnapshot1.child("landmark").getValue(String.class);
                    number = dataSnapshot1.child("number").getValue(String.class);
                    alterNumber = dataSnapshot1.child("alterNumber").getValue(String.class);
                    name = dataSnapshot1.child("name").getValue(String.class);

                    if (name.equals("")) {
                        userName.setVisibility(View.GONE);
                    } else
                        userName.setText(name);


                    if (houseNo.equals("")) {
                    } else
                        details += houseNo + ", ";
                    if (roadNo.equals("")) {
                    } else
                        details += roadNo + "\n";
                    if (city.equals("")) {
                    } else
                        details += city + ", ";
                    if (state.equals("")) {
                    } else
                        details += state + "\n";
                    if (pinCode.equals("")) {
                    } else
                        details += "Pin - " + pinCode + "\n";
                    if (landmark.equals("")) {
                    } else
                        details += landmark + "\n";
                    if (number.equals("")) {
                    } else
                        details += "Phone - " + number;
                    if (alterNumber.equals("")) {
                    } else
                        details += "\nAlternate Phone - " + alterNumber;


                    userDeatils.setText(details);
                    customProgressBar.dismissProgressBar();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Address.this)
                        .setTitle("Change Address!")
                        .setMessage("Are you sure you want to change this address?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), UserDetailsEdit.class));
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
