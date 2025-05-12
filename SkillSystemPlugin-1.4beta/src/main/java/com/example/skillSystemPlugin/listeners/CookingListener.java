package com.example.skillSystemPlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillType;

public class CookingListener implements Listener {

    private final SkillManager skillManager;

    public CookingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onCook(FurnaceExtractEvent event) {
        Player player = event.getPlayer();
        int amount = event.getItemAmount();
        skillManager.addXP(player, SkillType.COOKING, amount);
    }
}
