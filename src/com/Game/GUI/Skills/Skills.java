package com.Game.GUI.Skills;

import com.Util.Math.DeltaMath;

public class Skills {
    public static int RANGED = 0;

    public static int skillAmt;

    private static int[] exp;
    private static int[] lvl;
    private static boolean[] levelUp;

    public static void initExperience() {
        skillAmt = SkillsManager.skillImageNames.length;
        exp = new int[skillAmt];
        lvl = new int[skillAmt];
        levelUp = new boolean[skillAmt];

        for (int i = 0; i < lvl.length; i++) {
            deltaLevel(i);
        }
    }

    public static void addExperience(int skill, int amount) {
        exp[skill] += amount;
        deltaLevel(skill);
    }

    private static void deltaLevel(int skill) {
        int level = expToLevel(exp[skill]);

        if (level > lvl[skill]) {
            levelUp[skill] = true;
            lvl[skill] = level;
        }
    }

    public static int expToLevel(int exp) {
        int result = 0;
        int i = 0;

        while (result < exp) {
            i++;
            result = (int)((Math.pow(1.085, i - 1) * 150) + result);
        }

        return Math.max(i, 1);
    }

    public static int levelToExp(int lvl) {
        int result = 0;

        for (int i = 2; i <= lvl; i++) {
            result = (int)((Math.pow(1.085, i - 2) * 150) + result);
        }

        return result;
    }
    
    public static int getExperience(int skill) {
        return exp[skill];
    }

    public static int getLevel(int skill) {
        return lvl[skill];
    }

    public static boolean isLevelUp(int skill) {
        return levelUp[skill];
    }
}