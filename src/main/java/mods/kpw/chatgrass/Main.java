package mods.kpw.chatgrass;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {

    private final Random random = new Random();

    @Override
    public void onEnable() {
        getLogger().info("ChatGrassプラグインが有効になりました。");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatGrassプラグインが無効になりました。");
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        String message = event.getMessage();
        int wCount = countW(message);

        if (wCount > 0) {
            Player player = event.getPlayer();
            // Define a radius for grass generation based on wCount
            // For now, a simple radius, will be configurable later
            int radius = Math.min(wCount, 5); 

            for (int i = 0; i < wCount; i++) {
                // Generate random offsets within the radius
                int xOffset = random.nextInt(radius * 2 + 1) - radius;
                int zOffset = random.nextInt(radius * 2 + 1) - radius;
                // yOffset can be around player's y-level, or slightly above/below
                // For simplicity, let's try to place grass at player's y-level or one block above
                int yOffset = random.nextInt(2); // 0 or 1

                Block targetBlock = player.getLocation().add(xOffset, yOffset, zOffset).getBlock();
                Block blockBelow = targetBlock.getRelative(0, -1, 0);

                // Check if the target block is air and the block below is suitable for grass
                if (targetBlock.getType() == Material.AIR &&
                    (blockBelow.getType() == Material.DIRT || blockBelow.getType() == Material.GRASS_BLOCK)) {

                    // Randomly choose between SHORT_GRASS and TALL_GRASS
                    Material grassType = random.nextBoolean() ? Material.SHORT_GRASS : Material.TALL_GRASS;
                    targetBlock.setType(grassType);
                    getLogger().info(player.getName() + "がチャットで「ｗ」を" + wCount + "個入力したため、ランダムな位置に草を生成しました。");
                }
            }
        }
    }

    private int countW(String message) {
        int count = 0;
        for (char c : message.toCharArray()) {
            if (c == 'ｗ' || c == 'w') { // 全角と半角の'w'をカウント
                count++;
            }
        }
        return count;
    }
}