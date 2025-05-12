package com.example.skillSystemPlugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemRestrictionListener implements Listener {

    private final SkillManager skillManager;

    public ItemRestrictionListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    private static class Restriction {
        final SkillType skill;
        final int level;

        Restriction(SkillType skill, int level) {
            this.skill = skill;
            this.level = level;
        }
    }

    private Restriction getRestriction(Material type) {
        return switch (type) {
            // ТОПОРЫ
            case WOODEN_AXE, STONE_AXE -> new Restriction(SkillType.WOODCUTTING, 1);
            case GOLDEN_AXE -> new Restriction(SkillType.WOODCUTTING, 16);
            case IRON_AXE -> new Restriction(SkillType.WOODCUTTING, 21);
            case DIAMOND_AXE -> new Restriction(SkillType.WOODCUTTING, 31);
            case NETHERITE_AXE -> new Restriction(SkillType.WOODCUTTING, 41);

            // КИРКИ
            case WOODEN_PICKAXE, STONE_PICKAXE -> new Restriction(SkillType.MINING, 1);
            case GOLDEN_PICKAXE -> new Restriction(SkillType.MINING, 26);
            case IRON_PICKAXE -> new Restriction(SkillType.MINING, 36);
            case DIAMOND_PICKAXE -> new Restriction(SkillType.MINING, 51);
            case NETHERITE_PICKAXE -> new Restriction(SkillType.MINING, 71);

            // МЕЧИ
            case WOODEN_SWORD, STONE_SWORD -> new Restriction(SkillType.COMBAT, 1);
            case GOLDEN_SWORD -> new Restriction(SkillType.COMBAT, 31);
            case IRON_SWORD -> new Restriction(SkillType.COMBAT, 41);
            case DIAMOND_SWORD -> new Restriction(SkillType.COMBAT, 81);
            case NETHERITE_SWORD -> new Restriction(SkillType.COMBAT, 111);

            // БРОНЯ
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS ->
                    new Restriction(SkillType.VITALITY, 1);
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS ->
                    new Restriction(SkillType.VITALITY, 6);
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS ->
                    new Restriction(SkillType.VITALITY, 11);
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS ->
                    new Restriction(SkillType.VITALITY, 16);
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS ->
                    new Restriction(SkillType.VITALITY, 26);

            // ПРОЧЕЕ
            case ENCHANTING_TABLE -> new Restriction(SkillType.INTELLECT, 5);

            default -> null;
        };
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) return;

        Restriction restriction = getRestriction(item.getType());
        if (restriction != null && skillManager.getLevel(player, restriction.skill) < restriction.level) {
            event.setCancelled(true);
            player.sendMessage("§cВы не можете использовать этот предмет. Требуется §e" +
                    restriction.level + "§c уровень навыка §e" + restriction.skill.name());
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) return;

        Restriction restriction = getRestriction(item.getType());
        if (restriction != null && skillManager.getLevel(player, restriction.skill) < restriction.level) {
            event.setCancelled(true);
            player.sendMessage("§cВы не можете атаковать с этим предметом. Требуется §e" +
                    restriction.level + "§c уровень навыка §e" + restriction.skill.name());
        }
    }
}
