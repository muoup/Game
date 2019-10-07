package com.Game.Object.AreaTeleporter;

public class CaveEntrance extends Teleporter {
    public enum TeleType {
        caveEntrance(726, 278, 1),
        caveExit(4423, 2991, 0);

        public final int x, y, subWorld;

        TeleType(int x, int y, int subWorld) {
            this.x = x;
            this.y = y;
            this.subWorld = subWorld;
        }
    }

    public CaveEntrance(int x, int y, TeleType teleType) {
        super(x, y, teleType.x, teleType.y, teleType.subWorld);
        this.image = getImage("caveEntrance.png");
        this.maxDistance = 50f;
    }
}
