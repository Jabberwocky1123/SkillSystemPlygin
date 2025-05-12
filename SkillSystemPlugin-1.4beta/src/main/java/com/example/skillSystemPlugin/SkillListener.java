package com.example.skillSystemPlugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.block.BlockBreakEvent;

public class SkillListener implements Listener {

    private final SkillManager skillManager;

    public SkillListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType().toString().contains("LOG")) {
            skillManager.addXP(player, SkillType.WOODCUTTING, 1);
        } else if (block.getType().toString().contains("STONE") || block.getType() == Material.COAL_ORE) {
            skillManager.addXP(player, SkillType.MINING, 1);
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            skillManager.addXP(killer, SkillType.COMBAT, 1);
        }
    }
}