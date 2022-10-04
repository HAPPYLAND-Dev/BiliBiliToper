package me.xiaozhangup.bilibilitoper;

import me.xiaozhangup.bilibilitoper.data.DataMaster;
import me.xiaozhangup.bilibilitoper.utils.items.IBuilder;
import me.xiaozhangup.bilibilitoper.utils.items.Skull;
import me.xiaozhangup.bilibilitoper.utils.tools.IString;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static me.xiaozhangup.bilibilitoper.BiliBiliToper.mm;

public class ToperUI implements Listener, InventoryHolder {

    public static final @NotNull Component bindacc = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 请在聊天框输入你的BiliBili用户名<yellow>或输入 cancel 取消</yellow>");
    public static final @NotNull Component noacc = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> <red>你还没有绑定一个BiliBili账号!</red> 点击<yellow><click:run_command:'/bilitoper'>此处</click></yellow>来绑定");
    public static final @NotNull Component postvideo = mm.deserialize("<dark_gray>[<color:#00a1d6>哔哩</color>]</dark_gray> 请输入你已通过稿件的<red>BV号</red><yellow>或输入 cancel 取消</yellow>");
    public static final ItemStack describe = IBuilder.buildItem(Material.BOOK, "&6使用介绍", " ", "&7点击打开查阅");
    public static final ItemStack post = IBuilder.buildItem(Material.OAK_SIGN, "&e添加一个视频", " ", "&7点击添加你上传的视频");
    private static final ItemStack board = IBuilder.getBorder(Material.GRAY_STAINED_GLASS_PANE);
    private static final Book book = Book.book(
            Component.text("BiliBiliToper"),
            Component.text("xiaozhangup"),
            mm.deserialize("<b><red>视频要求</red></b>\n\n视频必须为您亲自上传,且上传账号必须和您绑定的账号相一致才算做有效的投稿\n\n不需要各种剪辑,只需录制您的日常游戏过程即可,最好配有背景音乐\n\n<b><gold>具体要求请参阅下一页</gold></b>"),
            mm.deserialize("<b><red>描述要求</red></b>\n\n视频标题需要含有<b>空岛生存</b>\n视频分区需为<b>网络游戏</b>\n视频简介必须含有:<dark_gray>\n\n服务器IP: HAPPYLANDMC.CC\n服务器Q群: 1039979008</dark_gray>\n\n<hover:show_text:'<gray>返回主界面</gray>'><click:run_command:'/bilitoper'><b><gold>返回主界面</gold></b></click></hover>")
    );

    public static void open(Player p) {

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
        Inventory menu = Bukkit.createInventory(new ToperUI(), 27, IString.addColor("BiliBili实况宣传"));
        Bukkit.getScheduler().runTaskAsynchronously(BiliBiliToper.plugin, () -> {
            for (int i = 0; i < 27; i++) {
                menu.setItem(i, board);
            }

            menu.setItem(11, describe);
            menu.setItem(13, Skull.getSkull(p, "&f你的个人数据", " ", "&7你当前绑定的账号: &f" + DataMaster.getNick(p), "&7你发布的视频数: &f" + DataMaster.getTotalVideo(p), " ", "&e点击此处绑定账号"));
            menu.setItem(15, post);
            menu.setItem(26, IBuilder.buildItem(Material.CHEST_MINECART, "&f每发布一个视频,您将获得", BiliBiliToper.reward));

            Bukkit.getScheduler().runTask(BiliBiliToper.plugin, () -> p.openInventory(menu));
        });
    }

    public Inventory getInventory() {
        return null;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && e.getInventory().getHolder() instanceof ToperUI) {
            e.setCancelled(true);
            switch (e.getRawSlot()) {
                case 11 -> {
                    p.closeInventory();
                    p.openBook(book);
                }
                case 13 -> {
                    p.closeInventory();
                    p.sendMessage(bindacc);
                    ChatInput.state.put(p, 1);
                }
                case 15 -> {
                    p.closeInventory();
                    if (DataMaster.getNick(p).equals("无")) {
                        p.sendMessage(noacc);
                        return;
                    }
                    p.sendMessage(postvideo);
                    ChatInput.state.put(p, 2);
                }
            }
        }
    }

}
