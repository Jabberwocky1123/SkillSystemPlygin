package com.example.skillSystemPlugin.listeners;

import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillEffects;
import com.example.skillSystemPlugin.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDurabilityListener implements Listener {

    private final SkillManager skillManager;

    public ItemDurabilityListener(SkillManager manager) {
        this.skillManager = manager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool != null && tool.getType().name().endsWith("_PICKAXE")) {
            int level = skillManager.getLevel(player, SkillType.MINING);
            if (level >= 10) {
                double chance = 0.005; // 0.5%
                if (Math.random() < chance) {
                    tool.setDurability((short) (tool.getDurability() - 1)); // Восстанавливаем прочность
                    player.sendMessage("§aThe pickaxe escaped damage!");
                }
            }
        }
    }
}
