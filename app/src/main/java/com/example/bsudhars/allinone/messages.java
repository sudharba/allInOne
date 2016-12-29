package com.example.bsudhars.allinone;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by BSUDHARS on 12/27/2016.
 */

public class messages extends Fragment {
    public messages(){
    }

    Button sendSMS;
    EditText phoneNo;
    EditText textMessage;
    TextView mCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.messages, container, false);

        sendSMS = (Button) rootView.findViewById(R.id.sendSMS);
        phoneNo = (EditText) rootView.findViewById(R.id.phoneNo);
        textMessage = (EditText) rootView.findViewById(R.id.textMessage);
        mCounter = (TextView) rootView.findViewById(R.id.counter);
        textMessage.addTextChangedListener(mTextEditorWatcher);


        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobNo = phoneNo.getText().toString();
                String message = textMessage.getText().toString();
                if (mobNo.length() > 0 && message.length() > 0)
                    sendSMS(mobNo, message);
                else
                    Toast.makeText(getActivity().getBaseContext(),
                            "Please enter both phone number and message.",
                            Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;


    }

    // Method to send SMS.
    private void sendSMS(String mobNo, String message) {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity().getApplicationContext()
                , Manifest.permission.SEND_SMS);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 123);
        } else {

            String smsSent = "SMS_SENT";
            String smsDelivered = "SMS_DELIVERED";
            PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                    new Intent(smsSent), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
                    new Intent(smsDelivered), 0);

            // Receiver for Sent SMS.
            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getActivity().getBaseContext(), "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getActivity().getBaseContext(), "Generic failure",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getActivity().getBaseContext(), "No service",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getActivity().getBaseContext(), "Null PDU",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getActivity().getBaseContext(), "Radio off",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(smsSent));

            // Receiver for Delivered SMS.
            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getActivity().getBaseContext(), "SMS delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getActivity().getBaseContext(), "SMS not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(smsDelivered));

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
            Toast.makeText(getActivity().getBaseContext(), "sending sms",
                    Toast.LENGTH_SHORT).show();

        }
        }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            //This sets a textview to the current length
            String smsNo;
            if(s.length() == 0)
                smsNo = "0";
            else
                smsNo = String.valueOf(s.length()/160 + 1);
            String smsLength = String.valueOf(160-(s.length()%160));
            mCounter.setText(smsLength+"/"+smsNo);
        }
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
        }
    };
}
