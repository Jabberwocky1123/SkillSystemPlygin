package com.example.skillSystemPlugin;

import com.example.skillSystemPlugin.SkillEffects;
import com.example.skillSystemPlugin.SkillType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SkillManager {

    private final Map<UUID, Map<SkillType, Integer>> xp = new HashMap<>();
    private final Map<UUID, Map<SkillType, Integer>> levels = new HashMap<>();
    private final Map<SkillType, Integer> maxLevels = new EnumMap<>(SkillType.class);

    private final File dataFile;
    private FileConfiguration dataConfig;
    private final JavaPlugin plugin;

    public SkillManager(JavaPlugin plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "data.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        initMaxLevels();
        loadData();
    }

    private void initMaxLevels() {
        maxLevels.put(SkillType.WOODCUTTING, 50);
        maxLevels.put(SkillType.MINING, 80);
        maxLevels.put(SkillType.COMBAT, 120);
        maxLevels.put(SkillType.VITALITY, 30);
        maxLevels.put(SkillType.WANDERER, 40);
        maxLevels.put(SkillType.INTELLECT, 20);
        maxLevels.put(SkillType.CRAFTING, 20);
        maxLevels.put(SkillType.COOKING, 5);
        maxLevels.put(SkillType.BUILDING, 10);
    }

    public void setLevel(Player player, SkillType skill, int level) {
        UUID uuid = player.getUniqueId();
        levels.putIfAbsent(uuid, new EnumMap<>(SkillType.class));
        xp.putIfAbsent(uuid, new EnumMap<>(SkillType.class));
        levels.get(uuid).put(skill, level);
        xp.get(uuid).put(skill, 0);
    }

    public void addXP(Player player, SkillType skill, int amount) {
        UUID uuid = player.getUniqueId();
        xp.putIfAbsent(uuid, new EnumMap<>(SkillType.class));
        levels.putIfAbsent(uuid, new EnumMap<>(SkillType.class));

        Map<SkillType, Integer> playerXP = xp.get(uuid);
        Map<SkillType, Integer> playerLevels = levels.get(uuid);

        int currentXP = playerXP.getOrDefault(skill, 0) + amount;
        int currentLevel = playerLevels.getOrDefault(skill, 1);
        int levelsGained = 0;
        int maxLevel = maxLevels.getOrDefault(skill, 100);
        int xpToLevel = getXPToLevel(skill, currentLevel);

        int maxLevelUps = 10;

        while (currentXP >= xpToLevel && currentLevel < maxLevel && levelsGained < maxLevelUps) {
            currentXP -= xpToLevel;
            currentLevel++;
            levelsGained++;

            if (currentLevel % 5 == 0) {
                SkillEffects.applyLevelUpEffect(player, skill, currentLevel);
            }

            xpToLevel = getXPToLevel(skill, currentLevel);
        }

        if (currentLevel >= maxLevel) {
            currentXP = 0;
        }

        playerXP.put(skill, currentXP);
        playerLevels.put(skill, currentLevel);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent("§eНавык §6" + skill.name() + ": §f" + currentXP + "/" + xpToLevel));

        if (levelsGained > 0) {
            player.sendMessage("§aНавык §e" + skill.name() + " §aповышен на §e" + levelsGained +
                    " §aуровней! Новый уровень: §e" + currentLevel);
        }
    }

    public int getXPToLevel(SkillType skill, int level) {
        switch (skill) {
            case WOODCUTTING: return (int) (50 + (level - 1) * 0.5);
            case MINING: return (int) (30 + (level - 1) * 0.5);
            case COMBAT: return (int) (30 + (level - 1) * 1.0);
            case VITALITY: return (int) (20 * Math.pow(0.5, level - 1));
            case WANDERER: return (int) (40 * Math.pow(0.5, level - 1));
            case INTELLECT: return (int) (20 * Math.pow(0.5, level - 1));
            case CRAFTING: return (int) (100 * Math.pow(0.5, level - 1));
            case COOKING: return (int) (50 * Math.pow(0.5, level - 1));
            case BUILDING: return (int) (150 * Math.pow(0.5, level - 1));
            default: return 100;
        }
    }

    public int getLevel(Player player, SkillType skill) {
        return levels.getOrDefault(player.getUniqueId(), new EnumMap<>(SkillType.class)).getOrDefault(skill, 1);
    }

    public int getXP(Player player, SkillType skill) {
        return xp.getOrDefault(player.getUniqueId(), new EnumMap<>(SkillType.class)).getOrDefault(skill, 0);
    }

    public void saveData(UUID uuid) {
        for (SkillType skill : SkillType.values()) {
            dataConfig.set(uuid + ".xp." + skill.name(), xp.get(uuid).getOrDefault(skill, 0));
            dataConfig.set(uuid + ".level." + skill.name(), levels.get(uuid).getOrDefault(skill, 1));
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        for (UUID uuid : xp.keySet()) {
            saveData(uuid);
        }
    }

    public void loadData() {
        for (String uuidStr : dataConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            Map<SkillType, Integer> xpMap = new EnumMap<>(SkillType.class);
            Map<SkillType, Integer> levelMap = new EnumMap<>(SkillType.class);
            for (SkillType skill : SkillType.values()) {
                xpMap.put(skill, dataConfig.getInt(uuidStr + ".xp." + skill.name(), 0));
                levelMap.put(skill, dataConfig.getInt(uuidStr + ".level." + skill.name(), 1));
            }
            xp.put(uuid, xpMap);
            levels.put(uuid, levelMap);
        }
    }
}