package com.example.skillSystemPlugin.listeners;

import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillSystemPlugin;
import com.example.skillSystemPlugin.SkillType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.EnumSet;
import java.util.Set;

public class CombatListener implements Listener {

    private final SkillManager skillManager;
    private final FileConfiguration config;

    private static final Set<EntityType> PEACEFUL_MOBS = EnumSet.of(
            EntityType.COW, EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN,
            EntityType.RABBIT, EntityType.VILLAGER, EntityType.HORSE, EntityType.SQUID,
            EntityType.STRIDER, EntityType.SNIFFER, EntityType.CAT, EntityType.WANDERING_TRADER,
            EntityType.OCELOT
    );

    public CombatListener(SkillManager skillManager) {
        this.skillManager = skillManager;
        this.config = SkillSystemPlugin.getInstance().getConfig();
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity().getKiller() instanceof Player)) return;

        Player killer = event.getEntity().getKiller();
        EntityType type = event.getEntity().getType();

        double baseXP = config.getDouble("combat-xp." + type.name().toLowerCase(),
                PEACEFUL_MOBS.contains(type)
                        ? config.getDouble("combat-xp.peaceful", 0.1)
                        : config.getDouble("combat-xp.default", 1.0));

        int currentLevel = skillManager.getLevel(killer, SkillType.COMBAT);
        double requiredXP = skillManager.getXPToLevel(SkillType.COMBAT, currentLevel);

        int finalXP = (int) (requiredXP * (baseXP / 100.0));

        if (finalXP > 0) {
            skillManager.addXP(killer, SkillType.COMBAT, finalXP);
        }
    }
}