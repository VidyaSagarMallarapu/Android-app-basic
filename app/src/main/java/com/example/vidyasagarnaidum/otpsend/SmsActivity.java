package com.example.vidyasagarnaidum.otpsend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SmsActivity extends AppCompatActivity {


    EditText e1;
    String phone="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        e1=(EditText)findViewById(R.id.editText4);
        phone=getIntent().getStringExtra("phone");
    }

    public void sms(View view) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, e1.getText().toString(), null, null);
        Toast.makeText(this,"Sms Send To the Number Sucessfully ..",Toast.LENGTH_SHORT).show();

    }

    public void home(View view) {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
