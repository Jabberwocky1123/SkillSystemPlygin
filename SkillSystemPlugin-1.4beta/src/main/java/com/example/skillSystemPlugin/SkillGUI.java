package com.example.skillSystemPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SkillGUI {

    private final SkillManager skillManager;

    public SkillGUI(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + "Skills");

        for (SkillType skill : SkillType.values()) {
            int level = skillManager.getLevel(player, skill);
            double xp = skillManager.getXP(player, skill);
            double toNext = skillManager.getXPToLevel(skill, level);

            Material icon = getIconFor(skill, level);
            ItemStack item = new ItemStack(icon);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColor.GOLD + skill.getDisplayName() + " [" + level + "]");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Experience: " + ChatColor.YELLOW + xp + ChatColor.GRAY + "/" + ChatColor.YELLOW + toNext);
            lore.add("");

            List<String> buffs = getBuffsForSkill(skill, level);
            if (!buffs.isEmpty()) {
                lore.add(ChatColor.GREEN + "Bonuses:");
                lore.addAll(buffs);
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
            gui.addItem(item);
        }

        player.openInventory(gui);
    }

    private Material getIconFor(SkillType skill, int level) {
        return switch (skill) {
            case WOODCUTTING -> {
                if (level <= 5) yield Material.WOODEN_AXE;
                else if (level <= 15) yield Material.STONE_AXE;
                else if (level <= 20) yield Material.GOLDEN_AXE;
                else if (level <= 30) yield Material.IRON_AXE;
                else if (level <= 40) yield Material.DIAMOND_AXE;
                else yield Material.NETHERITE_AXE;
            }
            case MINING -> {
                if (level <= 10) yield Material.WOODEN_PICKAXE;
                else if (level <= 25) yield Material.STONE_PICKAXE;
                else if (level <= 35) yield Material.GOLDEN_PICKAXE;
                else if (level <= 50) yield Material.IRON_PICKAXE;
                else if (level <= 70) yield Material.DIAMOND_PICKAXE;
                else yield Material.NETHERITE_PICKAXE;
            }
            case COMBAT -> {
                if (level <= 15) yield Material.WOODEN_SWORD;
                else if (level <= 30) yield Material.STONE_SWORD;
                else if (level <= 40) yield Material.GOLDEN_SWORD;
                else if (level <= 80) yield Material.IRON_SWORD;
                else if (level <= 110) yield Material.DIAMOND_SWORD;
                else yield Material.NETHERITE_SWORD;
            }
            case VITALITY -> {
                if (level <= 5) yield Material.LEATHER_CHESTPLATE;
                else if (level <= 10) yield Material.GOLDEN_CHESTPLATE;
                else if (level <= 15) yield Material.IRON_CHESTPLATE;
                else if (level <= 25) yield Material.DIAMOND_CHESTPLATE;
                else yield Material.NETHERITE_CHESTPLATE;
            }
            case WANDERER -> {
                if (level <= 5) yield Material.LEATHER_BOOTS;
                else if (level <= 10) yield Material.GOLDEN_BOOTS;
                else if (level <= 15) yield Material.IRON_BOOTS;
                else if (level <= 25) yield Material.DIAMOND_BOOTS;
                else yield Material.NETHERITE_BOOTS;
            }
            case INTELLECT -> {
                if (level <= 5) yield Material.PAPER;
                else if (level <= 10) yield Material.BOOK;
                else if (level <= 15) yield Material.WRITABLE_BOOK;
                else yield Material.ENCHANTED_BOOK;
            }
            case CRAFTING -> {
                if (level <= 10) yield Material.STICK;
                else yield Material.DEBUG_STICK;
            }
            case COOKING -> Material.COOKED_BEEF;
            case BUILDING -> Material.SCAFFOLDING;
        };
    }

    private List<String> getBuffsForSkill(SkillType skill, int level) {
        List<String> buffs = new ArrayList<>();

        switch (skill) {
            case WOODCUTTING -> {
                buffs.add("The Chance of Haste III: §e" + String.format("%.1f", level * 0.1) + " сек.");
            }
            case MINING -> {
                buffs.add("The Chance of Haste III: §e" + String.format("%.1f", level * 0.1) + " сек.");
                if (level >= 5)
                    buffs.add("Chance of an additional drop: §e" + (level / 5 * 0.25) + "%");
                if (level >= 10)
                    buffs.add("A chance to save the pickaxe: §e" + (level / 10 * 0.5) + "%");
            }
            case COMBAT -> {
                if (level >= 5)
                    buffs.add("2x chance of damage: §e" + (level / 5 * 0.25) + "%");
                if (level >= 15)
                    buffs.add("The chance of a double drop: §e" + (level / 15 * 0.1) + "%");
            }
            case VITALITY -> {
                if (level >= 5)
                    buffs.add("Damage reduction: §e" + (level / 5 * 5) + "%");
                if (level >= 15)
                    buffs.add("A chance to ignore the fall: §e25%");
            }
            case WANDERER -> {
                if (level >= 5)
                    buffs.add("The chance of speed II: §e25%");
                if (level >= 15)
                    buffs.add("Less food consumption when running");
                if (level >= 30)
                    buffs.add("Even less food consumption when running");
            }
            case INTELLECT -> {
                if (level >= 5)
                    buffs.add("The enchantment table is available");
                if (level >= 10)
                    buffs.add("0.1% A chance not to waste experience");
                if (level >= 15)
                    buffs.add("More experience for kills");
            }
            case CRAFTING -> {
                if (level >= 5)
                    buffs.add("0.1% A chance not to waste resources");
                if (level >= 10)
                    buffs.add("+15% durability of objects");
            }
            case COOKING -> {
                if (level >= 5)
                    buffs.add("More satiety from food");
            }
            case BUILDING -> {
                if (level >= 5)
                    buffs.add("Construction on +5 blocks");
                if (level >= 10)
                    buffs.add("Construction on +10 blocks");
            }
        }

        return buffs;
    }
}