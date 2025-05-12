package com.example.skillSystemPlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillType;

public class IntelligenceListener implements Listener {

    private final SkillManager skillManager;

    public IntelligenceListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onExpGain(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        int amount = event.getAmount();
        skillManager.addXP(player, SkillType.INTELLECT, amount);
    }
}

