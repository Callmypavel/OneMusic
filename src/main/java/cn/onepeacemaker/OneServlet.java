package cn.onepeacemaker;

import cn.onepeacemaker.util.LogTool;
import cn.onepeacemaker.util.MusicInfoParser;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;


public class OneServlet extends DispatcherServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        LogTool.log(this,"查看开始");
//        MusicInfoParser.parseMusicInfos("E:\\CloudMusic", new MusicInfoParser.OnMusicInfoListener() {
//
//
//            @Override
//            public void onMusicInfoLoaded(String url, String singerName, String songName, String albumName, String pictureUrl, int trackPosition, long size) {
//
//            }
//
//            @Override
//            public void onLoadFinished() {
//                LogTool.log(this,"我好了");
//            }
//        });
    }
}
