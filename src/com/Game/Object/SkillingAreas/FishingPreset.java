package com.Game.Object.SkillingAreas;

import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Util.Math.DeltaMath;

public enum FishingPreset {

    clownFish(35, 1.5f, 6.5f,
            1, ItemList.clownfish),
    blueFish(65, 5f, 12.5f,
            10, ItemList.bluefish);

    float xp, minTimer, maxTimer;
    int lvlReq;
    ItemList fish;

    FishingPreset(float xp, float minTimer, float maxTimer, int lvlReq, ItemList fish) {
        this.xp = xp;
        this.minTimer = minTimer;
        this.maxTimer = maxTimer;
        this.fish = fish;
        this.lvlReq = lvlReq;
    }

    public float getTimer() {
        return DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (Skills.getLevel(Skills.FISHING) - lvlReq));
    }

    public float getXp() {
        return xp;
    }
}
