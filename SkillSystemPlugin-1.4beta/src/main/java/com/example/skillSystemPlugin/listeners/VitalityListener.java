package com.example.skillSystemPlugin.listeners;

import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class VitalityListener implements Listener {

    private final SkillManager skillManager;
    private final Random random = new Random();

    public VitalityListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        int level = skillManager.getLevel(player, SkillType.VITALITY);

        int damageAsXP = (int) event.getFinalDamage();
        if (damageAsXP > 0) {
            skillManager.addXP(player, SkillType.VITALITY, damageAsXP);
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && level >= 15) {
            if (random.nextDouble() < 0.25) {
                player.sendMessage("§bYou landed confidently and avoided the fall damage.!");
                event.setCancelled(true);
                return;
            }
        }

        if (level >= 5) {
            int reductionSteps = level / 5;
            double reductionPercent = 0.05 * reductionSteps;
            double reducedDamage = event.getDamage() * (1 - reductionPercent);
            event.setDamage(reducedDamage);
        }
    }
}