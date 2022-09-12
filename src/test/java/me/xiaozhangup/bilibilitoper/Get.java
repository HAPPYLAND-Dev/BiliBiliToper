package me.xiaozhangup.bilibilitoper;

import me.xiaozhangup.bilibilitoper.bilibiliapi.BGetter;
import org.testng.annotations.Test;

public class Get {

    @Test
    public void get() {
        System.out.println(BGetter.getVideo("BV1zU4y1r7F2").getString("title"));
    }

}
