package com.Game.World;

import com.Game.Entity.Enemy.Underground.BabySpider;
import com.Game.Entity.Enemy.Underground.BigSpider;
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

        new BigSpider(878, 2133);
        new BigSpider(1215, 2448);
        new BigSpider(934, 2448);
        new BigSpider(1281, 2174);

        new Cockroach(3652, 2874);
        new Cockroach(3821, 3024);
        new Cockroach(3706, 3185);
        new Cockroach(3971, 3214);
        new Cockroach(3837, 3365);

        new CaveEntrance(726, 172, CaveEntrance.TeleType.caveExit);
    }
}
