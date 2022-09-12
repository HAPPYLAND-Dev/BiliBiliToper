package me.xiaozhangup.bilibilitoper.utils.items;

import me.xiaozhangup.bilibilitoper.utils.tools.IString;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class IBuilder {

    public static ItemStack getBorder(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor("&7"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack buildItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack buildItem(Material material, String name, String... lores) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor(name));
        List<String> lore = new ArrayList<>();
        for (String s : lores) {
            lore.add(IString.addColor(s));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack buildItems(Material material, String name, Integer i, String... lores) {
        ItemStack itemStack = new ItemStack(material, i);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor(name));
        List<String> lore = new ArrayList<>();
        for (String s : lores) {
            lore.add(IString.addColor(s));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack buildItem(Material material, String name, Integer i, String... lores) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(IString.addColor(name));
        List<String> lore = new ArrayList<>();
        for (String s : lores) {
            lore.add(IString.addColor(s));
        }
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(i);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
