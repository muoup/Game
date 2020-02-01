package com.Game.GUI.Skills;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.SkillPopup.SkillPopup;
import com.Game.Main.Main;
import com.Util.Other.Settings;

public class Skills {
    public static int RANGED = 0;
    public static int MELEE = 1;
    public static int FISHING = 2;
    public static int WOODCUTTING = 3;
    public static int FLETCHING = 4;
    public static int MINING = 5;
    public static int SMITHING = 6;

    public static int skillAmt;

    private static float[] exp;
    private static int[] lvl;
    public static String[] skillNames = {
            "Archery",
            "Attack",
            "Fishing",
            "Woodcutting",
            "Fletching",
            "Mining",
            "Smithing"
    };
    private static boolean[] levelUp;

    public static void initExperience() {
        skillAmt = SkillsManager.skillImageNames.length;
        exp = new float[skillAmt];
        lvl = new int[skillAmt];
        levelUp = new boolean[skillAmt];

        for (int i = 0; i < lvl.length; i++) {
            deltaLevel(i, false);
        }
    }

    public static void addExperience(int skill, float amount) {
        if (getLevel(skill) >= Settings.lvlMax) {
            ChatBox.sendMessage("You have reached the maximum level. This amount will be increased in the future.");
            return;
        }

        exp[skill] += amount;
        Main.sendPacket("07" + skill + ":" + amount + ":" + Main.player.name);
        deltaLevel(skill, true);
        new SkillPopup(skill, amount);
    }

    public static void setExperience(int skill, float amount) {
        exp[skill] = amount;
        deltaLevel(skill, false);
    }

    public static void setLevel(int skill, int level) {
        addExperience(skill, levelToExp(level) - exp[skill]);
    }

    private static void deltaLevel(int skill, boolean lvlUp) {
        int level = expToLevel(exp[skill]);
        int initLevel = getLevel(skill);

        if (level > lvl[skill]) {
            levelUp[skill] = true;
            lvl[skill] = level;
        }

        if (lvlUp && initLevel < level) {
            ChatBox.sendMessage("Congratulations, you have reached level " + lvl[skill] + " " + Skills.skillNames[skill] + ".");
        }
    }

    public static int expToLevel(float exp) {
        int result = 0;
        int i = 0;

        while (result - 1 < exp) {
            i++;
            result += (int) (Math.pow(1.085, i - 1) * 150);
        }

        return Math.max(i, 1);
    }

    public static int levelToExp(int lvl) {
        int result = 0;

        for (int i = 2; i <= lvl; i++) {
            result = (int) ((Math.pow(1.085, i - 2) * 150) + result);
        }

        return result;
    }

    public static float getExperience(int skill) {
        return exp[skill];
    }

    public static int getLevel(int skill) {
        return Math.max(1, lvl[skill]);
    }

    public static boolean isLevelUp(int skill) {
        return levelUp[skill];
    }
}