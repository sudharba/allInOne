package com.example.bsudhars.allinone;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Set;

import static com.example.bsudhars.allinone.R.id.bt;

/**
 * Created by BSUDHARS on 12/27/2016.
 */

public class wireless extends Fragment {
    public wireless() {
    }

    ToggleButton tb1;
    TextView blt, pd;

    //private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wireless, container, false);
        tb1 = (ToggleButton) rootView.findViewById(bt);
        blt = (TextView) rootView.findViewById(R.id.blt);
        pd = (TextView) rootView.findViewById(R.id.pd);
        adapter = BluetoothAdapter.getDefaultAdapter();
        //Set a filter to only receive bluetooth state changed events.
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mReceiver, filter);

       // pd.append("\nadapter:" + adapter);
        CheckBluetoothState();

        tb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetooth();
            }
        });


        return rootView;
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (bluetoothState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        tb1.setEnabled(false);
                        break;
                    case BluetoothAdapter.STATE_OFF:
                    case BluetoothAdapter.STATE_ON:
                        tb1.setEnabled(true);
                        CheckBluetoothState();
                        break;
                }
            }
        }
    };
    private void bluetooth() {
        if (adapter != null) {
            if (adapter.getState() == BluetoothAdapter.STATE_ON) {
                //BT state enable
                adapter.disable();
                Toast.makeText(getActivity().getApplication().getBaseContext()
                        , "Turning off BlueTooth", Toast.LENGTH_SHORT).show();

            } else if (adapter.getState() == BluetoothAdapter.STATE_OFF) {
                //BT state disable

                adapter.enable();
                Toast.makeText(getActivity().getApplication().getBaseContext()
                        , "Turning on BlueTooth", Toast.LENGTH_LONG).show();

            }
        }
    }
//------------------------------------------------paired list---------------------------------------
    private void CheckBluetoothState() {
        if (adapter == null) {
            pd.append("\n\nBluetooth not supported, Aborting");
        } else {
            if (adapter.isEnabled()) {
                pairingDev();
                tb1.setChecked(true);
            }

        }
    }
    private void pairingDev(){
        //pd.append("\nPaired devices are");
        pd.setText("");
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            pd.append("\n\n Device:" + device.getName() + ", " + device.getAddress());
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}