package cn.onepeacemaker.dao;

import cn.onepeacemaker.entity.Album;
import cn.onepeacemaker.entity.Music;
import cn.onepeacemaker.entity.Singer;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface MusicDao {
    int insertMusic(Music music);
    int insertAlbum(Album album);
    int insertSinger(Singer singer);
    int getMusicId(@Param("url")String url);
    int getSingerId(@Param("singerName")String singerName);
    int getAlbumId(@Param("pictureUrl")String pictureUrl);
    int connectSingerAndMusic(@Param("singerId")int singerId,@Param("musicId")int musicId);
    int connectSingerAndAlbum(@Param("singerId")int singerId,@Param("albumId")int albumId);
    int connectMusicAndAlbum(@Param("musicId")int musicId,@Param("albumId")int albumId);
    Music getMusic(@Param("musicId")int musicId);
    ArrayList<Music> getMusics(@Param("preNumber") int preNumber, @Param("pageSize") int pageSize,@Param("keyword")String keyword);

}
