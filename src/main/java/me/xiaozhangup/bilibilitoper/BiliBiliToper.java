package me.xiaozhangup.bilibilitoper;

import me.xiaozhangup.bilibilitoper.bilibiliapi.BGetter;
import me.xiaozhangup.bilibilitoper.utils.command.Command;
import me.xiaozhangup.bilibilitoper.utils.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.xiaozhangup.bilibilitoper.utils.manager.ListenerManager;
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

    public static void loadConfig() {
        commands = BiliBiliToper.plugin.getConfig().getStringList("commands");
        reward = BiliBiliToper.plugin.getConfig().getStringList("reward");
    }

    public static void runReward(Player p) {
        Bukkit.getScheduler().runTask(BiliBiliToper.plugin, () -> {
            var name = p.getName();
            commands.forEach((s -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", name));
            }));
        });
    }

}
