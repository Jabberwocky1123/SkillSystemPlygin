package com.example.skillSystemPlugin;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class SkillEffects {

    private static final Random random = new Random();

    public static void applyLevelUpEffect(Player player, SkillType skill, int level) {
        // Здесь можно добавить дополнительные визуальные или звуковые эффекты при апе уровня
        player.sendMessage("§bВы достигли §e" + level + " §bуровня навыка §a" + skill.name() + "§b!");
    }

    public static void handleMiningEffect(Player player, ItemStack tool, int skillLevel) {
        if (skillLevel >= 10 && tool != null && tool.getType().name().endsWith("_PICKAXE")) {
            if (random.nextDouble() <= 0.005) { // 0.5%
                // Не терять прочность — просто восстанавливаем 1
                short durability = tool.getDurability();
                tool.setDurability((short) Math.max(durability - 1, 0));
            }
        }

        // Haste эффект от WOODCUTTING или MINING
        double hasteChance = 0.001 * skillLevel; // 0.1% на 1 уровень
        if (random.nextDouble() <= hasteChance) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 60, 2)); // Haste III на 5 сек
        }
    }

    public static void handleCombatEffect(Player player, EntityDamageByEntityEvent event, int combatLevel) {
        // Шанс двойного урона с 10 уровня
        if (combatLevel >= 10) {
            double critChance = 0.01 + ((combatLevel / 10) - 1) * 0.01;
            if (random.nextDouble() <= critChance) {
                event.setDamage(event.getDamage() * 2);
                player.sendMessage("§cВы нанесли §lдвойной урон!");
            }
        }
    }

    public static boolean shouldDoubleLoot(int combatLevel) {
        if (combatLevel < 5) return false;
        double chance = 0.02 + ((combatLevel - 5) / 5) * 0.001; // от 2% до ...
        return random.nextDouble() <= chance;
    }
}