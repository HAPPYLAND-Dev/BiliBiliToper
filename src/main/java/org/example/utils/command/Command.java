package org.example.utils.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

public abstract class Command implements CommandExecutor {
    private final String name;
    private final PluginCommand command;

    public Command(String name) {
        this.name = name;

        PluginCommand pluginCommand = Bukkit.getPluginCommand(name);
        if (pluginCommand == null) {
            throw new RuntimeException("Cannot find plugin command '" + name + "'.");
        }

        this.command = pluginCommand;
        pluginCommand.setExecutor(this);
    }

    public static boolean register(String name, CommandExecutor executor) {
        PluginCommand pluginCommand = Bukkit.getPluginCommand(name);
        if (pluginCommand == null) {
            return false;
        }

        pluginCommand.setExecutor(executor);
        return true;
    }

    public String getName() {
        return name;
    }

    public PluginCommand getCommand() {
        return command;
    }
}
