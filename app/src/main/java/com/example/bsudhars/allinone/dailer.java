package com.example.bsudhars.allinone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by BSUDHARS on 12/27/2016.
 */
public class dailer extends Fragment{
    public dailer(){
    }

    Button call;
    String no;
    EditText et;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dailer, container, false);

        call = (Button) rootView.findViewById(R.id.buttoncall);
        et = (EditText) rootView.findViewById(R.id.editText);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makecall();
            }
        });

        return rootView;
    }

    private void makecall() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity().getApplicationContext()
                , Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 123);
        } else {
            no = et.getText().toString();
            no = "tel:" + no;
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse(no.toString())));
            Toast.makeText(getActivity().getApplication().getBaseContext(), "Calling", Toast.LENGTH_SHORT).show();
        }

    }
}
