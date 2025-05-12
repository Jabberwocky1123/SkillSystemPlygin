package com.example.skillSystemPlugin.listeners;

import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillEffects;
import com.example.skillSystemPlugin.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatDamageListener implements Listener {

    private final SkillManager skillManager;

    public CombatDamageListener(SkillManager manager) {
        this.skillManager = manager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        int level = skillManager.getLevel(player, SkillType.COMBAT);
        if (level >= 10) {
            double chance = (level / 10) * 0.01; // 1% каждые 10 лвл
            if (Math.random() < chance) {
                event.setDamage(event.getDamage() * 2);
                player.sendMessage("§cВы нанесли двойной урон!");
            }
        }
    }

}

