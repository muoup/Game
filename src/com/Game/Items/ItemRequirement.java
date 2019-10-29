package com.Game.Items;

import com.Game.GUI.Skills.Skills;

public class ItemRequirement {
    private int skill;
    private int level;

    public ItemRequirement(int skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    public int getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public static ItemRequirement none() {
        return new ItemRequirement(0, 0);
    }

    public String toString() {
        return "You require level " + level + " " + Skills.skillNames[skill] + " to wield this weapon.";
    }

    public boolean meetsRequirement() {
        return Skills.getLevel(skill) >= level;
    }
}