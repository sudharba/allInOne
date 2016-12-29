package com.example.bsudhars.allinone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class music extends Fragment {

    Button play, pause;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_music, container, false);

        play = (Button) rootView.findViewById(R.id.play);
        pause = (Button) rootView.findViewById(R.id.pause);

       // final MediaPlayer sound = MediaPlayer.create(getActivity(), R.raw.track2);
        return rootView;
    }


    }

