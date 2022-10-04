package me.xiaozhangup.bilibilitoper.data;

import me.xiaozhangup.bilibilitoper.utils.manager.ConfigManager;
import org.bukkit.entity.Player;

import java.util.List;

public class DataMaster {

    //账号数据的存储
    public static void setPlayerAccount(Player p, String nick) {
        ConfigManager.writeConfig("users", p.getUniqueId().toString(), nick);
    }

    public static String getNick(Player p) {
        if (ConfigManager.getConfig("users").getString(p.getUniqueId().toString()) == null) return "无";
        return ConfigManager.getConfig("users").getString(p.getUniqueId().toString());
    }

    //视频提交数据的存储
    public static void addPostedVideo(Player p, String bvid) {
        String path = p.getUniqueId().toString();
        ConfigManager.writeConfig("videos", path, ConfigManager.getConfig("videos").getStringList(path).add(bvid));
    }

    public static int getTotalVideo(Player p) {
        return ConfigManager.getConfig("videos").getStringList(p.getUniqueId().toString()).size();
    }

    public static List<String> getPostedVideos(Player p) {
        return ConfigManager.getConfig("videos").getStringList(p.getUniqueId().toString());
    }

}
