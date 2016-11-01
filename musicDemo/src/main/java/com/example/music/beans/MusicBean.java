package com.example.music.beans;

/**
 * Created by xu on 2016/9/7.
 */

public class MusicBean {
    //歌手名字、歌曲名字、大小、时长
    private String artist;
    private String song;
    private String size;
    private String duration;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
