package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Enemy;

public class SpiderRoach extends Enemy {
    public SpiderRoach(int x, int y) {
        super(x, y);
        this.image = getImage("SpiderRoach.png");
        this.maxTarget = 25.5f;
        this.name = "Spider Roach";
        setScale(128, 128);
        setMaxHealth(150);
    }

    public void AI() {

    }

    public void passiveAI() {

    }
}
