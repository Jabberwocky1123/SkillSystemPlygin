package com.example.skillSystemPlugin;

public enum SkillType {
    WOODCUTTING("Лесоруб"),
    MINING("Шахтёр"),
    COMBAT("Боец"),
    VITALITY("Выносливость"),
    WANDERER("Путешественник"),
    INTELLECT("Интеллект"),
    CRAFTING("Ремесло"),
    COOKING("Кулинария"),
    BUILDING("Строительство");

    private final String displayName;

    SkillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}