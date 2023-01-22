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
import java.util.List;
import java.util.Map;

import static me.xiaozhangup.bilibilitoper.BiliBiliToper.mm;

public class ChatInput implements Listener {

    public static final @NotNull Component nomatch = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>视频不符合要求!</yellow>");
    public static final @NotNull Component accnomatch = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>视频发布者账号和绑定账号不一致!</yellow>");
    public static final @NotNull Component bound = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>视频发布者账号已被绑定!</yellow>");
    public static final @NotNull Component cantread = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <red>服务器遇到错误无法获取数据,请重试!</red>");
    public static final @NotNull Component posted = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>你投稿过这个视频了!</yellow>");
    public static final @NotNull Component rubbish = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>该视频质量过低! 时长需满足20分钟</yellow>");
    public static final @NotNull Component stock = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>该视频属于库存视频(发布日期超过1天)! 请你按照时间间隔上传视频不然宣传效果太差! 谢谢理解支持!</yellow>");
    public static final @NotNull Component toofast = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>你太快了!要做一个持久的人! 距离上次投稿未达48小时</yellow>");
    public static final @NotNull Component cancel = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 已取消操作");
    public static final @NotNull Component donepost = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <yellow>你的视频提交成功</yellow>");
    public static HashMap<Player, Integer> state = new HashMap<>();
    public static HashMap<Player, Long> cool = new HashMap<>();

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
                    Map<String,Object> userList = DataMaster.getUserList();
                    //打印用户列表
                    for(String userConfigKey: userList.keySet()){
                        if (((String)userList.get(userConfigKey)).trim().equals(message.trim())) {
                            p.sendMessage(bound);
                            return;
                        }
                    }
                    DataMaster.setPlayerAccount(p, message);
                    p.sendMessage(mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 已成功绑定账号 " + message));
                    cool.put(p, System.currentTimeMillis());
                }
                case 2 -> {
                    //debug
                    state.remove(p);

                    List<String> postedVideos = DataMaster.getPostedVideos(p);
                    //判断是否距离上次投稿超过48小时
                    boolean postedFlag = false;
                    long lastTime = 0;
                    for (String s : postedVideos) {
                        String[] strings = s.split(":");
                        if (strings[0].contains(message)) {
                            postedFlag = true;
                        }
                        long time = Long.parseLong(strings[1]);
                        lastTime = Math.max(time, lastTime);
                    }
                    if (postedFlag) {
                        p.sendMessage(posted);
                        return;
                    }
                    if (System.currentTimeMillis() - lastTime < 172800000) {
                        p.sendMessage(toofast);
                        return;
                    }

                    JSONObject jsonObject = BGetter.getBaseJson(message);
                    if (jsonObject == null) {
                        p.sendMessage(cantread);
                        return;
                    }

                    if (BGetter.getDuration(jsonObject) < 20 * 60) {
                        p.sendMessage(rubbish);
                        return;
                    }

                    //视频上传时间不能超过24小时
                    if (System.currentTimeMillis() - BGetter.getPubdate(jsonObject) > 86400000) {
                        p.sendMessage(stock);
                        return;
                    }

                    if (!BGetter.getPoster(jsonObject).equals(DataMaster.getNick(p))) {
                        p.sendMessage(accnomatch);
                        return;
                    }

                    JSONObject video = BGetter.getVideo(jsonObject);
                    if (!check(video)) { //皮飞
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

    public static boolean check(JSONObject video) {
        if (
                        !video.getString("tname").equals(BiliBiliToper.part) ||
                        !video.getString("desc").contains(BiliBiliToper.qqgroup) ||
                        !video.getString("desc").contains(BiliBiliToper.serverip)
        ) {
            return false;
        } else {
            final boolean[] ali = {false};
            String title = video.getString("title");
            BiliBiliToper.alias.forEach(s -> {
                if (title.contains(s)) {
                    ali[0] = true;
                }
            });
            if (ali[0]) return true;
            return video.getString("title").contains(BiliBiliToper.tname);
        }
    }

    //code
    //!video.getString("title").contains(BiliBiliToper.tname) && !video.getString("title").contains("111111"))

}
