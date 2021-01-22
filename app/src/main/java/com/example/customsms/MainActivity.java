package com.example.customsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etMsg,etNum;
    String SENT="SMS_SENT";
    String DELIV="SMS_DELIEVERED";
    PendingIntent sentPI,delivPI;
    BroadcastReceiver smsSentReciever,smsDelivREciever;
    int ODE=2020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMsg=findViewById(R.id.etMsg);
        etNum=findViewById(R.id.etnum);
        sentPI= PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        delivPI= PendingIntent.getBroadcast(this,0,new Intent(DELIV),0);


    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReciever=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())    {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,
                                "SMS SENT", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "GENERIC FAIL", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this,
                                "NULL PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this,
                                "NO SERVICE", Toast.LENGTH_SHORT).show();
                        break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(MainActivity.this,
                                    "RADIO OFF", Toast.LENGTH_SHORT).show();
                            break;

                }
            }
        };
        smsDelivREciever=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,
                                "SMS REACHED TO DESTINY", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "SMS NOT DELIVERED", Toast.LENGTH_SHORT).show();
                }
            }
        };
        registerReceiver(smsSentReciever,new IntentFilter(SENT));
        registerReceiver(smsDelivREciever,new IntentFilter(DELIV));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsDelivREciever);
        unregisterReceiver(smsSentReciever);
    }


    public void btn_sms_send(View view){
        String msg=etMsg.getText().toString().trim();
        String num=etNum.getText().toString().trim();

        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(num,null,msg,sentPI,delivPI);



    }

}