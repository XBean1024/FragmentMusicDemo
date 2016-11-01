package com.example.music.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music.activity.MainActivity;
import com.example.music.R;


public class MusicInfoFragment extends Fragment implements MainActivity.CallbackMusicInfo {
    private TextView artistTV,titleTV;

    @Override
    public void setInfo(String artist, String title) {
        artistTV.setText("歌手:"+artist);
        titleTV.setText("歌名:"+title);
    }

    public MusicInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_music_info = inflater.inflate(R.layout.fragment_music_info, container, false);
        artistTV = (TextView) fragment_music_info.findViewById(R.id.artist);
        titleTV = (TextView) fragment_music_info.findViewById(R.id.title);
        // Inflate the layout for this fragment
        return fragment_music_info;
    }

}
