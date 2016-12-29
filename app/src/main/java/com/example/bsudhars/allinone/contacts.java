package com.example.bsudhars.allinone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BSUDHARS on 12/27/2016.
 */

public class contacts extends Fragment {

    public contacts(){
    }

    String vfile;
    Cursor cursor;
    ArrayList<String> vCard;
    Button  add, create_vcf_file, remove_all_contacts;
    static Context mContext;
    ProgressBar pb1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts, container, false);

        add = (Button) rootView.findViewById(R.id.include1);
        create_vcf_file = (Button) rootView.findViewById(R.id.export);
        remove_all_contacts = (Button) rootView.findViewById(R.id.remove);
        pb1 = (ProgressBar) rootView.findViewById(R.id.pb1);

        pb1.setProgress(0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcontacts();
            }
        });


        create_vcf_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    create();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        remove_all_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removecontacts();
            }
        });

        return rootView;
    }

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };

    @Override
    public void onStart() {
        super.onStart();
        if (checkPermissions()) {
            // permissions granted.
        } else {
            // show dialog informing them that we lack certain permissions
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (checkPermissions()) {
            // permissions granted.
        } else {
            // show dialog informing them that we lack certain permissions
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (checkPermissions()) {
            // permissions granted.
        } else {
            // show dialog informing them that we lack certain permissions
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

//---------------------------------------------------add--------------------------------------------
    private void addcontacts() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("/storage/emulated/0"); // a directory
        intent.setDataAndType(uri, "*/*");
        startActivity(intent);
        Toast.makeText(getActivity().getApplication().getBaseContext(), "Importing Contacts", Toast.LENGTH_SHORT).show();
    }
//-----------------------------------------------create---------------------------------------------
    private void create() throws IOException {

        final String vfile = "Contacts.vcf";
        // TODO Auto-generated method stub
        vCard = new ArrayList<String>();
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor!=null&&cursor.getCount()>0)
        {
            int i;
            String storage_path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
            FileOutputStream mFileOutputStream = new FileOutputStream(storage_path, false);
            cursor.moveToFirst();
            pb1.setMax(cursor.getCount()+1);
            for(i = 0;i<cursor.getCount();i++)
            {
                get(cursor);
                pb1.setProgress(i+1);
                Log.d("TAG", "Contact "+(i+1)+"VcF String is"+vCard.get(i));
                cursor.moveToNext();
                mFileOutputStream.write(vCard.get(i).toString().getBytes());
            }
            mFileOutputStream.close();
            Toast.makeText(getActivity().getApplication().getBaseContext(), "Export successful", Toast.LENGTH_SHORT).show();

            cursor.close();
        }
        else
        {
            Log.d("TAG", "No Contacts in Your Phone");
        }
    }
    private void get(Cursor cursor2) {
        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd;
        try {
            fd = getActivity().getContentResolver().openAssetFileDescriptor(uri, "r");

            FileInputStream fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);
            String vcardstring= new String(buf);
            vCard.add(vcardstring);
        } catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }





    //------------------------------------------remove--------------------------------------------------
    private void removecontacts() {
        int i=0;
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        pb1.setProgress(i);
        pb1.setMax(cur.getCount()+1);

        Toast.makeText(getActivity().getApplication().getBaseContext(), "All Contacts Deleted", Toast.LENGTH_SHORT).show();

        while (cur.moveToNext()) {
            try {
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                System.out.println("The uri is " + uri.toString());
                cr.delete(uri, null, null);
                pb1.setProgress(++i);

            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }

    }
}
