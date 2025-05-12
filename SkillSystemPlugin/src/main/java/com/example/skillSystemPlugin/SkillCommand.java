package com.example.skillSystemPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillCommand implements CommandExecutor {

    private final SkillManager skillManager;

    public SkillCommand(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cТолько игрок может открыть GUI.");
                return true;
            }
            new SkillGUI(skillManager).open(player);
            return true;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
            String targetName = args[1];
            String skillName = args[2];
            String levelStr = args[3];

            Player target = Bukkit.getPlayerExact(targetName);
            if (target == null) {
                sender.sendMessage("§cИгрок '" + targetName + "' не найден.");
                return true;
            }

            try {
                SkillType skill = SkillType.valueOf(skillName.toUpperCase());
                int level = Integer.parseInt(levelStr);

                skillManager.setLevel(target, skill, level);
                sender.sendMessage("§aУстановлен уровень " + level + " для навыка " + skill.name() + " у игрока " + target.getName());
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cНеверный навык или уровень. Проверь ввод.");
            }

            return true;
        }

        sender.sendMessage("§cИспользование: /skills set <ник> <навык> <уровень>");
        return true;
    }
}
