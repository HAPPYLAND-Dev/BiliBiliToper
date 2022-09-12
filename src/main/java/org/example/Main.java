package org.example;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.utils.command.Command;
import org.example.utils.manager.ListenerManager;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    public static ListenerManager listenerManager = new ListenerManager();
    private static Economy econ = null;

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onEnable() {
        plugin = this;
        setupEconomy();

//        listenerManager.addListeners(
//                /*Your event*/
//        );
//        listenerManager.register();

//        Command.register("example", (commandSender, command, s, inside) -> {
//            /*your way*/
//            return false;
//        });



    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
    }

}
