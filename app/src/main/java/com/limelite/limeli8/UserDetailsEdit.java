package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsEdit extends AppCompatActivity {

    EditText number, pincode, houseNo, roadNo, city, state, landmark, name, alterNumber;
    Button save;
    String _number, _pincode, _houseNo, _roadNo, _city, _state, _landmark, _name, _alterNumber, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_edit);


        Toolbar toolbar = findViewById(R.id.homePageToolbar);
        number = findViewById(R.id.number);
        pincode = findViewById(R.id.pin_code);
        houseNo = findViewById(R.id.house_no);
        roadNo = findViewById(R.id.road);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        alterNumber = findViewById(R.id.alter_mobile);
        save = findViewById(R.id.save_btn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Address");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/userDetails/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    key = dataSnapshot1.getKey();

                    _houseNo = dataSnapshot1.child("houseNo").getValue(String.class);
                    _roadNo = dataSnapshot1.child("roadNo").getValue(String.class);
                    _city = dataSnapshot1.child("city").getValue(String.class);
                    _state = dataSnapshot1.child("state").getValue(String.class);
                    _pincode = dataSnapshot1.child("pinCode").getValue(String.class);
                    _landmark = dataSnapshot1.child("landmark").getValue(String.class);
                    _number = dataSnapshot1.child("number").getValue(String.class);
                    _alterNumber = dataSnapshot1.child("alterNumber").getValue(String.class);
                    _name = dataSnapshot1.child("name").getValue(String.class);

                }

                if (_houseNo.equals("")) {

                } else {
                    houseNo.setText(_houseNo);
                }
                if (_roadNo.equals("")) {

                } else {
                    roadNo.setText(_roadNo);
                }
                if (_city.equals("")) {

                } else {
                    city.setText(_city);
                }
                if (_state.equals("")) {

                } else {
                    state.setText(_state);
                }
                if (_pincode.equals("")) {

                } else {
                    pincode.setText(_pincode);
                }
                if (_landmark.equals("")) {

                } else {
                    landmark.setText(_landmark);
                }
                if (_number.equals("")) {

                } else {
                    number.setText(_number);
                }
                if (_alterNumber.equals("")) {

                } else {
                    alterNumber.setText(_alterNumber);
                }
                if (_name.equals("")) {

                } else {
                    name.setText(_name);
                }

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(UserDetailsEdit.this)
                                .setTitle("Save this Address!")
                                .setMessage("Are you sure you want to save this address?")

                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        _number = number.getText().toString();
                                        _pincode = pincode.getText().toString();
                                        _roadNo = roadNo.getText().toString();
                                        _city = city.getText().toString();
                                        _state = state.getText().toString();
                                        _houseNo = houseNo.getText().toString();
                                        _landmark = landmark.getText().toString();
                                        _name = name.getText().toString();
                                        _number = number.getText().toString();
                                        _alterNumber = alterNumber.getText().toString();

                                        if (_number.isEmpty()) {

                                            Toast.makeText(getApplicationContext(), "Phone number is mandatory!", Toast.LENGTH_LONG).show();
                                        } else {

                                            databaseReference.child(key).child("name").setValue(_name);
                                            databaseReference.child(key).child("houseNo").setValue(_houseNo);
                                            databaseReference.child(key).child("roadNo").setValue(_roadNo);
                                            databaseReference.child(key).child("city").setValue(_city);
                                            databaseReference.child(key).child("state").setValue(_state);
                                            databaseReference.child(key).child("landmark").setValue(_landmark);
                                            databaseReference.child(key).child("number").setValue(_number);
                                            databaseReference.child(key).child("alterNumber").setValue(_alterNumber);
                                            databaseReference.child(key).child("pinCode").setValue(_pincode, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                                    Toast.makeText(getApplicationContext(), "Your address is saved.", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            });

                                        }

                                    }
                                })

                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();



                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
