package com.example.skillSystemPlugin.listeners;

import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class WandererListener implements Listener {

    private final SkillManager skillManager;
    private final Map<UUID, Double> movedBlocks = new HashMap<>();
    private final Random random = new Random();

    public WandererListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        double moved = movedBlocks.getOrDefault(uuid, 0.0) + event.getFrom().distance(event.getTo());
        if (moved >= 150) {
            movedBlocks.put(uuid, 0.0);
            skillManager.addXP(player, SkillType.WANDERER, 1);
        } else {
            movedBlocks.put(uuid, moved);
        }

        int level = skillManager.getLevel(player, SkillType.WANDERER);
        if (level >= 5 && random.nextDouble() < 0.25) {
            if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1)); // 5 секунд, уровень 2
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        int level = skillManager.getLevel(player, SkillType.WANDERER);

        if (level >= 30) {
            event.setFoodLevel(event.getFoodLevel() + 1);
        } else if (level >= 15) {
            event.setFoodLevel(event.getFoodLevel() + 0);
        }
    }
}
