package com.Game.World;

import com.Game.Entity.Enemy.Underground.BabySpider;
import com.Game.Entity.Enemy.Underground.Cockroach;
import com.Game.Object.AreaTeleporter.CaveEntrance;

public class Underground extends World {
    public Underground() {
        initImage("underground.png");
    }

    public void initWorld() {
        new BabySpider(3902, 507);
        new BabySpider(3639, 419);
        new BabySpider(3639, 653);
        new BabySpider(4115, 404);

        new Cockroach(1100, 2286);

        new CaveEntrance(726, 172, CaveEntrance.TeleType.caveExit);
    }
}
