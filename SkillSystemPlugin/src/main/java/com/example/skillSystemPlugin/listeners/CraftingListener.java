package com.example.skillSystemPlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillType;

public class CraftingListener implements Listener {

    private final SkillManager skillManager;

    public CraftingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        skillManager.addXP(player, SkillType.CRAFTING, 1);
    }
}

