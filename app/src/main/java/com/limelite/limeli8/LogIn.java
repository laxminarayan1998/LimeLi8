package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LogIn extends AppCompatActivity {

    TextView logInButton;
    EditText phoneNumber, otp;
    Button resendOtp;

    private String mVerificationId;
    private FirebaseAuth mAuth;

    CustomProgressBar customProgressBar;

    String mobile, enteredOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logInButton = findViewById(R.id.log_in_button);
        phoneNumber = findViewById(R.id.phone_number);
        otp = findViewById(R.id.otp);
        resendOtp = findViewById(R.id.resend_otp);

        mAuth = FirebaseAuth.getInstance();
        customProgressBar = new CustomProgressBar(this);

        customProgressBar.startProgressBar();
        customProgressBar.dialog.setCanceledOnTouchOutside(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            customProgressBar.dismissProgressBar();
            Toast.makeText(getApplicationContext(), "Your're already logged in.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            customProgressBar.dismissProgressBar();
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customProgressBar.startProgressBar();
                customProgressBar.dialog.setCanceledOnTouchOutside(false);

                mobile = phoneNumber.getText().toString().trim();
                enteredOtp = otp.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() < 10) {
                    phoneNumber.setError("Enter a valid mobile");
                    phoneNumber.requestFocus();
                    customProgressBar.dismissProgressBar();
                    return;
                } else if (!enteredOtp.isEmpty()) {
                    verifyVerificationCode(enteredOtp);
                } else {
                    sendVerificationCode(mobile);
                }

            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                customProgressBar.startProgressBar();
                customProgressBar.dialog.setCanceledOnTouchOutside(false);

                if (mobile.isEmpty() || mobile.length() < 10) {
                    phoneNumber.setError("Enter a valid mobile");
                    phoneNumber.requestFocus();
                    customProgressBar.dismissProgressBar();
                    return;
                } else {
                    sendVerificationCode(mobile);
                }

                resendOtp.setVisibility(View.INVISIBLE);

            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        String code = phoneAuthCredential.getSmsCode();

                        if (code != null) {
                            otp.setText(code);
                            verifyVerificationCode(code);
                        } else {
                            customProgressBar.dismissProgressBar();
                            otp.requestFocus();
                            Toast.makeText(getApplicationContext(), "Please enter OTP if received, or click on resend OTP!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        customProgressBar.dismissProgressBar();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        mVerificationId = s;
                        Toast.makeText(getApplicationContext(), "Your verification code is sent in given phone number.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);

                        Toast.makeText(getApplicationContext(), "Sms auto retrieval timed-out. If OTP received please enter or click on the resend OTP.", Toast.LENGTH_LONG).show();
                        customProgressBar.dismissProgressBar();
                        resendOtp.setVisibility(View.VISIBLE);
                        otp.requestFocus();
                    }
                });
    }

    private void verifyVerificationCode(String code) {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            e.printStackTrace();
            customProgressBar.dismissProgressBar();
            Toast.makeText(getApplicationContext(), "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();
            otp.setText("");
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                            finish();
                        } else {

                            String message = "Something is wrong, we will fix it soon.";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered!";
                            }

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            otp.requestFocus();

                        }

                        customProgressBar.dismissProgressBar();
                    }
                });
    }

}
