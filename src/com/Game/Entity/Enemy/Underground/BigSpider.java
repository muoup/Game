package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Enemy;

public class BigSpider extends Enemy {
    public BigSpider(int x, int y) {
        super(x, y);
        this.image = getImage("bigSpider.png");
        this.name = "Spiderling";
        this.passive = false;
        this.speed = 1.45f;
        setScale(64, 64);
        setMaxHealth(35);
    }
}
