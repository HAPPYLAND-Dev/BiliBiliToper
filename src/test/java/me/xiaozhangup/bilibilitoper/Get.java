package me.xiaozhangup.bilibilitoper;

import com.alibaba.fastjson2.JSONObject;
import me.xiaozhangup.bilibilitoper.bilibiliapi.BGetter;
import org.testng.annotations.Test;

public class Get {

    @Test
    public void get() {
        String bvid = "BV1zU4y1r7F2";
        JSONObject jsonObject = BGetter.getBaseJson(bvid);
        System.out.println(jsonObject);
        System.out.println(BGetter.getVideo(jsonObject).getString("title"));
    }

}
