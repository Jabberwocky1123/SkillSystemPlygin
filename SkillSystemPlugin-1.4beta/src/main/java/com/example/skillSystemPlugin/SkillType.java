package com.example.skillSystemPlugin;

public enum SkillType {
    WOODCUTTING("Lumberjack"),
    MINING("Miner"),
    COMBAT("Fighter"),
    VITALITY("Resistance"),
    WANDERER("Wanderer"),
    INTELLECT("Intellect"),
    CRAFTING("Craft"),
    COOKING("Cooking"),
    BUILDING("Building");

    private final String displayName;

    SkillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}