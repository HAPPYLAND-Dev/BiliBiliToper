package me.xiaozhangup.bilibilitoper;

import me.xiaozhangup.bilibilitoper.bilibiliapi.BGetter;
import me.xiaozhangup.bilibilitoper.utils.command.Command;
import me.xiaozhangup.bilibilitoper.utils.manager.ConfigManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.xiaozhangup.bilibilitoper.utils.manager.ListenerManager;

public class BiliBiliToper extends JavaPlugin {

    public static Plugin plugin;
    public static ListenerManager listenerManager = new ListenerManager();
    public static MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        reloadConfig();

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



    }

}
