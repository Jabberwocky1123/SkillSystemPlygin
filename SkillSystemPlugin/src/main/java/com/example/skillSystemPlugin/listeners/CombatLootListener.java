package com.example.skillSystemPlugin.listeners;

import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillEffects;
import com.example.skillSystemPlugin.SkillType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CombatLootListener implements Listener {

    private final SkillManager skillManager;

    public CombatLootListener(SkillManager manager) {
        this.skillManager = manager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        int level = skillManager.getLevel(killer, SkillType.COMBAT);
        if (level >= 5) {
            double chance = 0.02 + ((level - 5) / 5) * 0.001; // +0.1% каждые 5 лвл
            if (Math.random() < chance) {
                for (ItemStack drop : event.getDrops()) {
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), drop.clone());
                }
                killer.sendMessage("§eВы получили двойной лут!");
            }
        }
    }

}
