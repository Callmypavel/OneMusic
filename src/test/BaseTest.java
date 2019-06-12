

import cn.onepeacemaker.service.MusicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("src/main/resouces")
@ContextConfiguration(locations={"classpath:spring-controller.xml","classpath:spring-dao.xml", "classpath:spring-service.xml"})
public class BaseTest{
    @Resource
    private MusicService musicService;
    @Test
    public void test(){
        musicService.addMusic("测试666音乐","真实专辑名","真实歌手名","来来url",1,114,"图片url");

    }

}