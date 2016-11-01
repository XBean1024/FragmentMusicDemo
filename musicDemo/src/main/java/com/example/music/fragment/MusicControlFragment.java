package com.example.music.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music.R;
import com.example.music.activity.MainActivity;

import static com.example.music.R.id.begin;


public class MusicControlFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{

    private ControlMusicPlay controlMusicPlay;
    private static TextView beginTV;
    private static TextView endTV;
    private static SeekBar seekBar;

    public final static Handler controlHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           // Bundle bundle = (Bundle) msg.obj;
            final Bundle bundle = (Bundle) msg.obj;
            if (bundle != null) {

                String nowTime = bundle.getString("nowTime");
                String endTime = bundle.getString("endTime");

                beginTV.setText(nowTime);
                endTV.setText(endTime);

                seekBar.setMax(bundle.getInt("duration")/1000);
                seekBar.setProgress(bundle.getInt("currentPosition")/1000);
            }
        }
    };

    public MusicControlFragment() {}

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        controlMusicPlay = (MainActivity)getActivity();//绑定回调对象
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View music_control_fragment = inflater.inflate(R.layout.fragment_music_control, container, false);

        Button btnLast = (Button) music_control_fragment.findViewById(R.id.last);
        Button btnPlay = (Button) music_control_fragment.findViewById(R.id.play);
        Button btnPause = (Button) music_control_fragment.findViewById(R.id.pause);
        Button btnNext = (Button) music_control_fragment.findViewById(R.id.next);

        beginTV = (TextView) music_control_fragment.findViewById(begin);
        endTV = (TextView) music_control_fragment.findViewById(R.id.end);
        seekBar = (SeekBar) music_control_fragment.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlMusicPlay.toLast();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               controlMusicPlay.toStart();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               controlMusicPlay.toPause();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               controlMusicPlay.toNext();
            }
        });

        //接收Activity传过来的数据

        return music_control_fragment;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        controlMusicPlay.seekBarChangee(seekBar.getProgress()*1000);
    }

    /**
     * 回调接口，当点击该fragment的按钮式，立刻响应
     */
    public interface ControlMusicPlay{
        void toLast();
        void toStart();
        void toPause();
        void toNext();
        void seekBarChangee(int pos);//SeekBar被拖动时，回调该方法，在MainActivity里实现
    }

}
