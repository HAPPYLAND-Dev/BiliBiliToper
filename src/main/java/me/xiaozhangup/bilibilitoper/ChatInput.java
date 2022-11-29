package me.xiaozhangup.bilibilitoper;

import com.alibaba.fastjson2.JSONObject;
import me.xiaozhangup.bilibilitoper.bilibiliapi.BGetter;
import me.xiaozhangup.bilibilitoper.data.DataMaster;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static me.xiaozhangup.bilibilitoper.BiliBiliToper.mm;

public class ChatInput implements Listener {

    public static final @NotNull Component nomatch = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>视频不符合要求!</yellow>");
    public static final @NotNull Component accnomatch = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>视频发布者账号和绑定账号不一致!</yellow>");
    public static final @NotNull Component cantread = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <red>服务器遇到错误无法获取数据,请重试!</red>");
    public static final @NotNull Component posted = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>你投稿过这个视频了!</yellow>");
    public static final @NotNull Component cancel = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 已取消操作");
    public static final @NotNull Component donepost = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>你的视频提交成功</yellow>");
    public static HashMap<Player, Integer> state = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        var p = e.getPlayer();
        var message = e.getMessage();
        if (state.get(p) != null) {
            e.setCancelled(true);
            if (message.equals("cancel")) {
                state.remove(p);
                p.sendMessage(cancel);
                return;
            }
            switch (state.get(p)) {
                case 1 -> {
                    state.remove(p);
                    DataMaster.setPlayerAccount(p, message);
                    p.sendMessage(mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 已成功绑定账号 " + message));
                }
                case 2 -> {
                    //debug
                    //debug
                    state.remove(p);
                    if (DataMaster.getPostedVideos(p).contains(message)) {
                        p.sendMessage(posted);
                        return;
                    }
                    JSONObject jsonObject = BGetter.getBaseJson(message);
                    if (jsonObject == null) {
                        p.sendMessage(cantread);
                        return;
                    }
                    if (!BGetter.getPoster(jsonObject).equals(DataMaster.getNick(p))) {
                        p.sendMessage(accnomatch);
                        return;
                    }
                    JSONObject video = BGetter.getVideo(jsonObject);
                    if (
                            !video.getString("title").contains(BiliBiliToper.tname) ||
                                    !video.getString("tname").equals("网络游戏") ||
                                    !video.getString("desc").contains(BiliBiliToper.qqgroup)
                    ) {
                        p.sendMessage(nomatch);
                        return;
                    }
                    DataMaster.addPostedVideo(p, message);
                    p.sendMessage(donepost);
                    //todo 奖赏代码/和全服广播
                    BiliBiliToper.runReward(p);
                    Bukkit.broadcast(mm.deserialize(""));
                    Bukkit.broadcast(mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 玩家<yellow>" + p.getName() + "</yellow>成功投稿了一次视频!"));
                    Bukkit.broadcast(mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 你可以<yellow><click:open_url:'" + "https://www.bilibili.com/video/" + message + "'>点击此处</click></yellow>前往BiliBili观看他的作品!"));
                    Bukkit.broadcast(mm.deserialize(""));
                }
            }
        }
    }

}
