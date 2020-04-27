package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailsActivity extends AppCompatActivity {

    EditText number, pincode, houseNo, roadNo, city, state, landmark, name, alterNumber;
    Button save;
    String _number, _pincode, _houseNo, _roadNo, _city, _state, _landmark, _name, _alterNumber;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

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
        getSupportActionBar().setTitle("Add a address");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);

        number.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/userDetails/");

        final String key = databaseReference.push().getKey();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });


    }
}
