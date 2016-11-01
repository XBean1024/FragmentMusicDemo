package com.example.music.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.music.utils.Tools;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.music.activity.MainActivity.mHander;
import static com.example.music.fragment.MusicControlFragment.controlHandler;
import static com.example.music.utils.Tools.cursor;
import static com.example.music.utils.Tools.cursorCols;

public class MusicPlayService extends Service {

    public static MediaPlayer mPlayer;
    private boolean continueToPaly;
    private MyReceiver myReceiver;

    public MusicPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();//在第一次启动服务时，实例化媒体播放对象
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                toNext();
            }
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getCurrentMusicInfo();
            }
        },500,1000);
        mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                mPlayer.start();
            }
        });
        //注册广播 接收seekBar事件
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("seekBarChange");
        registerReceiver(myReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }

        if (intent.getStringExtra("flag")!=null) {
            //getCurrentMusicInfo();
            String flag = intent.getStringExtra("flag");
            boolean control = intent.getBooleanExtra("control",false);
            if (control) {
                if (flag.equals("toLast")) {
                    toLast();
                }
                if (flag.equals("toStart")) {
                    toStart();
                }
                if (flag.equals("toPause")) {
                    toPause();
                }
                if (flag.equals("toNext")) {
                    toNext();
                }
                if (flag.equals("itemToStart")) {
                    itemToStart();
                }
            }else {
                Toast.makeText(this, "请先点击您要播放的音乐，再进行此操作！", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void itemToStart() {
        prepareToPlay();
    }

    private void toNext() {
        getCurrentMusicInfo();
        if (cursor.getCount() == cursor.getPosition()+1) {
            cursor.moveToFirst();
        }else {
            cursor.moveToNext();
        }
        prepareToPlay();//播放前的准备工作
    }

    private void toPause() {
        mPlayer.stop();
        continueToPaly = true;//再次点击播放时，从当前位置继续播放
    }

    private void toStart() {
        try {

            if (!mPlayer.isPlaying()) {//如果音乐暂停
                mPlayer.prepare();
                mPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareToPlay() {
        mPlayer.reset();
        String dataSource = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));//获取要播放的文件路径
        try {
            mPlayer.setDataSource(dataSource);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toLast() {
        if (0 == cursor.getPosition()) {
            cursor.moveToLast();
        }else {
            cursor.moveToPrevious();
        }
        prepareToPlay();//播放前的准备工作
    }

    //获得当前播放音乐的信息
    private void getCurrentMusicInfo(){
        if (cursor != null) {
            String artist = Tools.cursor.getString(Tools.cursor.getColumnIndexOrThrow(Tools.cursorCols[0]));//歌手
            String title = Tools.cursor.getString(Tools.cursor.getColumnIndexOrThrow(Tools.cursorCols[1]));//歌名
            int currentPosition = mPlayer.getCurrentPosition();
            int duration = Integer.valueOf(cursor.getString(cursor
                    .getColumnIndexOrThrow(cursorCols[3])));
            String nowTime = Tools.changeTime(currentPosition);
            String endTime = Tools.changeTime(duration);//歌名

            //通过handler传递musicInfo的相关信息
            Bundle bundle = new Bundle();
            Bundle bundle2 = new Bundle();
            Message msg = mHander.obtainMessage();
            Message msg2 = controlHandler.obtainMessage();

            bundle.putString("title", title);
            bundle.putString("artist", artist);

            bundle2.putString("nowTime", nowTime);
            bundle2.putString("endTime", endTime);

            bundle2.putInt("currentPosition",currentPosition);
            bundle2.putInt("duration",duration);

            msg.obj = bundle;
            msg2.obj = bundle2;
            mHander.sendMessage(msg);
            controlHandler.sendMessage(msg2);
        }
    }

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            int pos = intent.getIntExtra("pos",100);
            mPlayer.seekTo(pos);
            Log.i("xxx", "onReceive: "+"999999"+"===="+pos);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       /* mPlayer.stop();
        mPlayer.release();
        cursor.close();*/
        unregisterReceiver(myReceiver);//解绑广播
    }
}
