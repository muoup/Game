package com.Game.GUI.Skills;

import com.Game.GUI.SkillPopup.SkillPopup;

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
            "Life Points",
            "Fishing",
            "Woodcutting",
            "Fletching",
            "Mining",
            "Smithing"
    };
    public static void initExperience() {
        skillAmt = SkillsManager.skillImageNames.length;
        exp = new float[skillAmt];
        lvl = new int[skillAmt];
    }

    public static void setExperience(int skill, float amount, boolean popup) {
        float initialXP = exp[skill];

        exp[skill] = amount;

        if (popup) {
            new SkillPopup(skill, (float) (Math.floor(exp[skill]) - Math.floor(initialXP)));
        }
    }

    public static void setLevel(int skill, int level) {
        lvl[skill] = level;
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

    public static void handleChange(String message) {
        String[] args = message.split(";");

        int skill = Integer.parseInt(args[0]);
        int level = Integer.parseInt(args[1]);
        float xp = Float.parseFloat(args[2]);
        String popup = args[3];

        setExperience(skill, xp, popup.equals("p"));
        setLevel(skill, level);
    }
}