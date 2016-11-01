package com.example.music.musicControl;

import android.media.MediaPlayer;

/**
 * Created by xu on 2016/9/8.
 */

public class MusicControlImp implements MusicControlInterface {
    private MediaPlayer mediaPlayer;

    public MusicControlImp(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void toStart() {

    }

    @Override
    public void toPause() {

    }
}
