package com.example.music.utils;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by xu on 2016/9/8.
 */

public class Tools {
    public static Cursor cursor = null;
    public static String changeTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    public static String changeSize(int size) {

        size /= 1024;
        int m = size / 1024;
        int kb = m %1024;
        return String.format("大小:%02d.%02dM", m, kb);
    }
    public static String[] cursorCols = new String[]{
            MediaStore.Audio.Media.ARTIST,//歌手
            MediaStore.Audio.Media.TITLE,//歌名
            MediaStore.Audio.Media.SIZE,//大小
            MediaStore.Audio.Media.DURATION,//时长
            MediaStore.Audio.Media.DATA
    };
}
