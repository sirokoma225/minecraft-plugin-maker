package mods.kpw.my_example_plugin;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("WWWGrass プラグインが有効になりました。");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("WWWGrass プラグインが無効になりました。");
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        // メッセージが "ｗｗｗｗ" を含んでいるかチェック
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        getLogger().info("チャットメッセージ受信: " + message); // デバッグログ追加

        if (message.contains("ｗｗｗｗ") || message.toLowerCase().contains("wwww")) {
            Player player = event.getPlayer();
            getLogger().info(player.getName() + " が 'ｗｗｗｗ' をチャットしました。"); // デバッグログ追加

            // ブロックの変更は同期処理で行う
            Bukkit.getScheduler().runTask(this, () -> {
                // プレイヤーの足元のブロック
                Block ground = player.getLocation().subtract(0, 1, 0).getBlock();
                // プレイヤーの足（体）がある場所のブロック
                Block feet = player.getLocation().getBlock();

                // 足元のブロックが固体のブロックの場合
                if (ground.getType().isSolid()) {
                    
                    // 地面を草ブロックに上書きする
                    ground.setType(Material.GRASS_BLOCK);
                    
                    // プレイヤーの足の位置に草を植える（もしそこが空気なら）
                    if (feet.getType() == Material.AIR) {
                        feet.setType(Material.SHORT_GRASS);
                    }

                    player.sendMessage("草を生やしました！");
                    getLogger().info(player.getName() + " の足元に草を生やしました。");

                } else {
                    player.sendMessage("固い地面の上でないと草を生やせません。");
                    getLogger().info(player.getName() + " は固い地面の上にいませんでした。");
                }
            });
        }
    }
}
