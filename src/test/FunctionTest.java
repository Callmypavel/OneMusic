import cn.onepeacemaker.entity.Music;
import cn.onepeacemaker.service.MusicService;
import cn.onepeacemaker.util.LogTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("src/main/resouces")
@ContextConfiguration(locations={"classpath:spring-controller.xml","classpath:spring-dao.xml", "classpath:spring-service.xml"})
public class FunctionTest {
    @Resource
    private MusicService musicService;

    @Test
    public void test(){
        ArrayList<Music> resultMusics = musicService.searchMusic(0,10,"Bila");
        LogTool.log(this,"查看数量"+resultMusics.size());
        for (Music music : resultMusics) {
            LogTool.log(this,music.getMusicName()+","+music.getSingerName());
        }
    }
}
