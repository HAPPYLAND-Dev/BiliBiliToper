package me.xiaozhangup.bilibilitoper;

import me.xiaozhangup.bilibilitoper.bilibiliapi.BGetter;
import me.xiaozhangup.bilibilitoper.utils.manager.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.xiaozhangup.bilibilitoper.utils.manager.ListenerManager;

public class BiliBiliToper extends JavaPlugin {

    public static Plugin plugin;
    public static ListenerManager listenerManager = new ListenerManager();
//    private static Economy econ = null;
//
//    public static Economy getEconomy() {
//        return econ;
//    }

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info(BGetter.getVideo("BV1zU4y1r7F1").getString("title"));

        saveDefaultConfig();
        reloadConfig();

        ConfigManager.createFile("users");
        ConfigManager.createFile("videos");
//        setupEconomy();

//        listenerManager.addListeners(
//                /*Your event*/
//        );
//        listenerManager.register();

//        Command.register("example", (commandSender, command, s, inside) -> {
//            /*your way*/
//            return false;
//        });



    }

//    private void setupEconomy() {
//        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
//        if (economyProvider != null) {
//            econ = economyProvider.getProvider();
//        }
//    }

}
