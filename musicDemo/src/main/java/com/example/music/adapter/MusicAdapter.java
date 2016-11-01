package com.example.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.music.R;
import com.example.music.beans.MusicBean;

import java.util.List;

/**
 * Created by xu on 2016/9/7.
 */

public class MusicAdapter extends BaseAdapter {

    private List<MusicBean> musicBeanList;
    private LayoutInflater mInflater;

    public MusicAdapter(List<MusicBean> musicList, Context c) {
        musicBeanList = musicList;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return musicBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
//----if 模块 --实例化ViewHolder
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.activity_local_music_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
            viewHolder.song = (TextView) convertView.findViewById(R.id.song);
            viewHolder.size = (TextView) convertView.findViewById(R.id.size);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            //convertView.setBackgroundResource(R.drawable.selector);
            convertView.setTag(viewHolder);// TODO: 2016/9/6 可不可以只用一个tag

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //给viewHolder赋值

        viewHolder.artist.setText(musicBeanList.get(pos).getArtist());
        viewHolder.song.setText(musicBeanList.get(pos).getSong());
        viewHolder.size.setText(musicBeanList.get(pos).getSize());
        viewHolder.duration.setText(musicBeanList.get(pos).getDuration());

        return convertView;
    }

    static class ViewHolder{
        TextView artist;
        TextView song;
        TextView size;
        TextView duration;
    }
}
