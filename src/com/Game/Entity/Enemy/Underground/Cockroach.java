package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Main.Main;
import com.Game.Projectile.BeetleSpike;
import com.Game.Projectile.BeetleSpikeAvoidable;

public class Cockroach extends Enemy {
    public float timer2 = 0;

    public Cockroach(int x, int y) {
        super(x, y);
        this.maxTarget = 7.5f;
        this.passive = false;
        this.image = getImage("CockRoach.png");
        this.speed = 1.25f;
        this.name = "Cockroach";
        this.respawnTimer = 8.0f;

        setMaxHealth(25);
        setScale(64, 64);
        setBounds(683, 1762, 1508, 2894);
    }

    public void update() {
        if (!withinBounds())
            loseTarget();
    }

    public void AI() {
        timer += Main.dTime();
        timer2 += Main.dTime();

        if (range() > 256f)
            moveToPlayer();

        if (timer > 0.65f) {
            new BeetleSpikeAvoidable(position, Main.player.position).multiShotEnemy(20, 256, 5);
            timer = 0;
        }

        if (timer2 > 1.35f) {
            new BeetleSpike(position);
            timer2 = 0;
        }
    }

    public void passiveAI() {
        moveToAI();
    }
}
