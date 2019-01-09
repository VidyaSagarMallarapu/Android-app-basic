package com.example.vidyasagarnaidum.otpsend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
public class MainActivity extends AppCompatActivity {


    public EditText phone,otp;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks call;
    public Button b1,b2;
    String verifycode;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button)findViewById(R.id.otp);
        b2=(Button)findViewById(R.id.verify);
        phone=(EditText)findViewById(R.id.mobile);
        otp=(EditText)findViewById(R.id.editText2);
       //en otp.setFilters(new InputFilter[]{ new InputFilterMax(10)});
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        //firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+9491225371","121234");

        call=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override

            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(getApplicationContext(),"uare the user verified",Toast.LENGTH_LONG).show();
               signInWIthPhone(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),"Internet Connection not Available",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verifycode=s;
                Toast.makeText(getApplicationContext()," OTP send to the number successfully..",Toast.LENGTH_SHORT).show();
            }
        };
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mob="+91"+phone.getText().toString();
               // SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage(mob, null, "hellow sagar how are u ..", null, null);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        mob,60, TimeUnit.SECONDS,MainActivity.this,call);
               // String number = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                Toast.makeText(getApplicationContext(),"OTP is sending..",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  void signInWIthPhone(PhoneAuthCredential credential)

    {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"user signed in successfully",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(getApplicationContext(),LoginSuccess.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Enter correct OTP ",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    public void verify()
    {

        String ver=otp.getText().toString();
         verifyNumber(verifycode,ver);



    }
    public  void verifyNumber(String verifyc,String input)
    {
    PhoneAuthCredential cre=PhoneAuthProvider.getCredential(verifyc,input);
    signInWIthPhone(cre);
    }

    public void KnowUser(View view) {
        Intent i=new Intent(getApplicationContext(),Verified.class);
        startActivity(i);
    }
}

