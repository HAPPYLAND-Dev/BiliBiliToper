package org.example.utils.tools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookTip {

    public static void openBook(Player p, String s) {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setAuthor("HAPPYLAND Studio");
        bookMeta.setTitle("Server Message");
        bookMeta.addPage(IString.addColor(s));
        itemStack.setItemMeta(bookMeta);
        p.openBook(itemStack);
    }

}
