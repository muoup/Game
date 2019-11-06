package com.Game.Object.AreaTeleporter;

public class InvisibleTeleporter extends Teleporter {
    public InvisibleTeleporter(int x, int y, int tx, int ty, int sub) {
        super(x, y, tx, ty, sub);
        this.maxDistance = 32;
    }
}
