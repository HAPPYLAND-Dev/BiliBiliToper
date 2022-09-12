package me.xiaozhangup.bilibilitoper.bilibiliapi;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import me.xiaozhangup.bilibilitoper.utils.tools.IString;
import org.jetbrains.annotations.Nullable;

public class BGetter {

    @Nullable
    public static JSONObject getVideo(String bvid) {
        String api = IString.getStringFromURL("http://api.bilibili.com/x/web-interface/view?&bvid=" + bvid);

        if (api == null) return null;
        JSONObject data = JSON.parseObject(api);

        if (data.getIntValue("code") != 0) {
            return null;
        } else {
            return data.getJSONObject("data");
        }

    }

}
