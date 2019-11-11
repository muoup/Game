package com.Game.Object.SkillingAreas;

import com.Game.Items.DropTable;
import com.Game.Items.ItemList;

public enum RockType {
    /**
     * Stone rocks cannot lose their material because they are only stone.
     * Because it only checks if a rock has 0 stone left, -1 will mean infinite
     */
    stone("empty_rock.png", 1, 15, 0.5f, 2.5f,
            -1, -1, ItemList.stone);

    public DropTable drops;
    public String imageName;
    public float minTime, maxTime, xp;
    public int minRocks, maxRocks, level;
    private Runnable initMethod;

    RockType(String imageName, int level, float xp, float minTime, float maxTime, int minRocks, int maxRocks, ItemList rock) {
        this.imageName = imageName;
        this.level = level;
        this.xp = xp;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.minRocks = minRocks;
        this.maxRocks = maxRocks;

        drops = new DropTable(rock);
    }

    /**
     * If the rock has a diverse DropTable, call an init method to add the items and their chances.
     * @param initMethod
     */
    RockType(String imageName, int level, float xp, float minTime, float maxTime, int minRocks, int maxRocks, Runnable initMethod) {
        this.imageName = imageName;
        this.level = level;
        this.xp = xp;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.minRocks = minRocks;
        this.maxRocks = maxRocks;
        this.initMethod = initMethod;

        initMethod.run();
    }
}
