package com.Game.World;

import com.Game.Entity.Enemy.BabySpider;

public class Underground extends World {
    public Underground() {
        initImage("underground.png");
    }

    public void initWorld() {
        new BabySpider(3902, 507);
//        new BabySpider(3639, 419);
//        new BabySpider(3639, 653);
//        new BabySpider(4115, 404);
    }
}
