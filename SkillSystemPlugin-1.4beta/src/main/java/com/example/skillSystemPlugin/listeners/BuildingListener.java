package com.example.skillSystemPlugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import com.example.skillSystemPlugin.SkillManager;
import com.example.skillSystemPlugin.SkillType;

import java.util.Set;
import java.util.EnumSet;

public class BuildingListener implements Listener {

    private final SkillManager skillManager;

    public BuildingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    private static final Set<Material> EXCLUDED = EnumSet.of(
            Material.SCAFFOLDING, Material.TORCH, Material.ITEM_FRAME, Material.PAINTING,
            Material.END_ROD, Material.LILY_PAD, Material.CANDLE, Material.MOSS_BLOCK,
            Material.MOSS_CARPET, Material.KELP, Material.KELP_PLANT, Material.FLOWERING_AZALEA,
            Material.AZALEA, Material.SUNFLOWER // и т.д.
    );

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (EXCLUDED.contains(event.getBlockPlaced().getType())) return;

        skillManager.addXP(player, SkillType.BUILDING, 1);
    }
}
