package com.example.music.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.music.R;

public class MusicListFragment extends Fragment{
    public ListView  musicListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_music_list, container, false);
        musicListView = (ListView) v.findViewById(R.id.music_listview);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
