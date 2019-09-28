package com.Game.Object.AreaTeleporter;

public class CaveEntrance extends Teleporter {
    public CaveEntrance(int x, int y) {
        super(x, y, 77, 77, 1);
        this.image = getImage("caveEntrance.png");
        this.maxDistance = 100f;
    }
}
