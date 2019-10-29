package com.Game.Object.SkillingAreas;

import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Util.Math.DeltaMath;

public enum TreePreset {

    tree("tree.png", 40, 5f, 12.5f,
            2, 9, 1, ItemList.log),
    ash("ash_tree.png", 60, 5f, 12.5f,
            2, 9, 10, ItemList.ashLog),
    pine("pine_tree.png", 80, 5.5f, 13,
            1, 12, 20, ItemList.pineLog),
    oakTree("oak_tree.png", 120, 5.5f, 13,
            1, 12, 30, ItemList.oakLog),
    spruce("spruce_tree.png", 65, 6.5f, 14,
            1, 7, 140, ItemList.spruceLog),
    mapleTree("maple_tree.png", 65, 6.5f, 14,
            1, 7, 180, ItemList.mapleLog);

    float xp, minTimer, maxTimer;
    int minWood, maxWood, lvlReq;
    ItemList wood;
    String imageName;

    TreePreset(String imageName, float xp, float minTimer, float maxTimer, int minWood, int maxWood, int lvlReq, ItemList wood) {
        this.imageName = imageName;
        this.xp = xp;
        this.minTimer = minTimer;
        this.maxTimer = maxTimer;
        this.minWood = minWood;
        this.maxWood = maxWood;
        this.lvlReq = lvlReq;
        this.wood = wood;
    }

    public float getTimer() {
        return DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (Skills.getLevel(Skills.FISHING) - lvlReq));
    }

    public float getXp() {
        return xp;
    }

    public int getWoodAmount() {
        return Math.round(DeltaMath.range(minWood, maxWood));
    }
}
