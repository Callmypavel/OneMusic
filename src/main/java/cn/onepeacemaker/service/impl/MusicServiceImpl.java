package cn.onepeacemaker.service.impl;

import cn.onepeacemaker.dao.MusicDao;
import cn.onepeacemaker.entity.Album;
import cn.onepeacemaker.entity.Music;
import cn.onepeacemaker.entity.Singer;
import cn.onepeacemaker.service.MusicService;
import cn.onepeacemaker.util.LogTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MusicServiceImpl implements MusicService {
    @Autowired
    private MusicDao musicDao;
    @Override
    public void addMusic(String musicName, String albumName, String singerName, String url, int trackPosition, int fileSize,String pictureUrl) {
        Music music = new Music();
        music.setMusicName(musicName);
        music.setFileSize(fileSize);
        music.setUrl(url);
        music.setSingerName(singerName);
        music.setAlbumName(albumName);

        Singer singer = new Singer();
        singer.setSingerName(singerName);

        Album album = new Album();
        album.setAlbumName(albumName);
        album.setPictureUrl(pictureUrl);

        musicDao.insertSinger(singer);
        int singerId = singer.getSingerId();

        if(singerId==0){
            //已经插入过该歌手，查找其id
            singerId = musicDao.getSingerId(singerName);
        }
        LogTool.log(this,"查看歌手id:"+singerId);

        musicDao.insertAlbum(album);
        int albumId = album.getAlbumId();
        if(albumId==0){
            //已经插入过该专辑，查找其id
            albumId = musicDao.getAlbumId(pictureUrl);
        }
        LogTool.log(this,"查看专辑id:"+albumId);

        musicDao.insertMusic(music);
        int musicId = music.getMusicId();
        if(musicId==0){
            //已经插入过该音乐，查找其id
            musicId = musicDao.getMusicId(url);
        }
        LogTool.log(this,"查看音乐id:"+musicId);

        musicDao.connectSingerAndMusic(singerId,musicId);
        musicDao.connectMusicAndAlbum(musicId,albumId);
        musicDao.connectSingerAndAlbum(singerId,albumId);
    }

    @Override
    public ArrayList<Music> searchMusic(int preNumber, int pageSize, String keyword) {

        return musicDao.getMusics(preNumber,pageSize,keyword);
    }


}
