package me.xiaozhangup.bilibilitoper;

import me.xiaozhangup.bilibilitoper.utils.command.Command;
import me.xiaozhangup.bilibilitoper.utils.manager.ConfigManager;
import me.xiaozhangup.bilibilitoper.utils.manager.ListenerManager;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BiliBiliToper extends JavaPlugin {

    public static Plugin plugin;
    public static ListenerManager listenerManager = new ListenerManager();
    public static MiniMessage mm = MiniMessage.miniMessage();
    public static final @NotNull Component reloaded = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 配置文件已重载");
    public static List<String> commands = new ArrayList<>();
    public static List<String> reward = new ArrayList<>();
    public static String qqgroup;
    public static String serverip;
    public static String tname;
    public static int cooldown;
    public static List<String> alias;


    public static void loadConfig() {
        FileConfiguration config = BiliBiliToper.plugin.getConfig();
        commands = config.getStringList("commands");
        reward = config.getStringList("reward");
        qqgroup = config.getString("qqgroup");
        serverip = config.getString("serverip");
        tname = config.getString("tname");
        alias = config.getStringList("alias");
        cooldown = config.getInt("cooldown");
        ToperUI.book = Book.book(
                Component.text("BiliBiliToper"),
                Component.text("xiaozhangup"),
                mm.deserialize("<b><red>视频要求</red></b>\n\n视频必须为您亲自上传,且上传账号必须和您绑定的账号相一致才算做有效的投稿\n\n不需要各种剪辑,只需录制您的日常游戏过程即可,最好配有背景音乐\n\n<b><gold>具体要求请参阅下一页</gold></b>"),
                mm.deserialize("<b><red>描述要求</red></b>\n\n视频标题需要含有<b>" + tname + "</b>\n视频分区需为<b>网络游戏</b>\n视频简介必须含有:<dark_gray>\n\n服务器IP: " + serverip + "\n服务器Q群: " + qqgroup + "</dark_gray>\n\n<hover:show_text:'<gray>返回主界面</gray>'><click:run_command:'/bilitoper'><b><gold>返回主界面</gold></b></click></hover>")
        );
    }

    public static void runReward(Player p) {
        Bukkit.getScheduler().runTask(BiliBiliToper.plugin, () -> {
            var name = p.getName();
            commands.forEach((s -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", name));
            }));
        });
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        reloadConfig();
        loadConfig();

        ConfigManager.createFile("users");
        ConfigManager.createFile("videos");

        listenerManager.addListeners(
                new ToperUI(), new ChatInput()
        );
        listenerManager.register();

        Command.register("bilitoper", (commandSender, command, s, inside) -> {
            ToperUI.open((Player) commandSender);
            return false;
        });
        Command.register("bilireload", (commandSender, command, s, strings) -> {
            if (!commandSender.isOp()) return false;
            reloadConfig();
            loadConfig();
            commandSender.sendMessage(reloaded);
            return true;
        });

    }

}
