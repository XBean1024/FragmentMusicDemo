package com.example.music.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.music.R;
import com.example.music.adapter.MusicAdapter;
import com.example.music.beans.MusicBean;
import com.example.music.fragment.MusicControlFragment;
import com.example.music.service.MusicPlayService;
import com.example.music.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import static com.example.music.service.MusicPlayService.mPlayer;
import static com.example.music.utils.Tools.cursor;
import static com.example.music.utils.Tools.cursorCols;

/**
 *
 */
public class MainActivity extends Activity implements MusicControlFragment.ControlMusicPlay {

    public Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;//contentPrivoder提供的访问SD卡音乐信息的接口


    private MusicBean musicBean;
    private List<MusicBean> musicList;
    private MusicAdapter mAdapter;
    private ListView musicListView;

    private Intent intentPlayService;//与后台服务相关的意图
    private static CallbackMusicInfo callbackMusicInfo;//声明回到接口
    private static MusicControlFragment musicControlFragment;
    private static FragmentManager fragmentManager;


    public final static Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = (Bundle) msg.obj;//取得信息

            if (bundle != null) {
                //通过回调接口方式更新 MusicInfoFragment
                callbackMusicInfo.setInfo(bundle.getString("artist"),bundle.getString("title"));
                //不好的方式,每次都要重新更新 Fragemnt ,新的方式是在Fragment里定义一个handler,在服务里发送消息到handler
                //
//                musicControlFragment = new MusicControlFragment();
//                musicControlFragment.setArguments(bundle);
//                fragmentManager.beginTransaction().replace(R.id.music_control, musicControlFragment).commitAllowingStateLoss();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicList = new ArrayList<MusicBean>();
        //获取Frangment里的控件
        musicListView = (ListView) getFragmentManager().findFragmentById(R.id.music_list).getView()
                .findViewById(R.id.music_listview);
        musicList = getMusicInfo();
        mAdapter = new MusicAdapter(musicList,this);
        musicListView.setAdapter(mAdapter);
        intentPlayService = new Intent(MainActivity.this,MusicPlayService.class);

        musicControlFragment = new MusicControlFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.music_control, musicControlFragment).commit();

        callbackMusicInfo = (CallbackMusicInfo) getFragmentManager().findFragmentById(R.id.music_info);

        //为listview设置监听事件
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                cursor.moveToPosition(pos); //将cursor移动到指定的位置
                intentPlayService.putExtra("flag","itemToStart");
                intentPlayService.putExtra("control",true);
                startService(intentPlayService);
            }
        });
    }

    @Override
    public void toLast() {
        intentPlayService.putExtra("flag","toLast");
        startService(intentPlayService);
    }

    @Override
    public void toStart() {
        intentPlayService.putExtra("flag","toStart");
        startService(intentPlayService);
    }

    @Override
    public void toPause() {
        intentPlayService.putExtra("flag","toPause");
        startService(intentPlayService);
    }

    @Override
    public void toNext() {
        intentPlayService.putExtra("flag","toNext");
        startService(intentPlayService);
    }

    @Override
    public void seekBarChangee(int pos) {//SeekBar被拖动时，回调该方法
        Intent sendIntent = new Intent("seekBarChange");
        sendIntent.putExtra("pos",pos);
        sendBroadcast(sendIntent);
    }

    public List<MusicBean> getMusicInfo() {

        cursor = getContentResolver().query(uri,cursorCols,null,null,null);
        List<MusicBean> locMusicList = new ArrayList<MusicBean>();
        if (cursor != null) {
            while(cursor.moveToNext()){
                musicBean = new MusicBean();
                musicBean.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(cursorCols[0])));//歌手
                musicBean.setSong(cursor.getString(cursor.getColumnIndexOrThrow(cursorCols[1])));//歌名
                musicBean.setSize(Tools.changeSize(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndexOrThrow(cursorCols[2])))));//大小
                musicBean.setDuration("时长:"+Tools.changeTime(Integer.valueOf(cursor.getString(cursor
                        .getColumnIndexOrThrow(cursorCols[3])))));//时长

                locMusicList.add(musicBean);
            }
            cursor.moveToFirst();
        }
        return locMusicList;
    }

    public interface CallbackMusicInfo{
        void setInfo(String artist,String title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(intentPlayService);
    }

}
