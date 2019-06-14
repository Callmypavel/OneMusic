package cn.onepeacemaker.service;

import cn.onepeacemaker.entity.Music;

import java.util.ArrayList;

public interface MusicService {
    void addMusic(String musicName,String albumName,String singerName,String url,int trackPosition,int fileSize,String pictureUrl);
    ArrayList<Music> searchMusic(int preNumber,int pageSize,String keyword);
}
