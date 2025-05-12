package com.example.skillSystemPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class SkillDataListener implements Listener {

    private final SkillManager skillManager;

    public SkillDataListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        skillManager.saveData(event.getPlayer().getUniqueId());
    }
}

