package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Main.Main;
import com.Game.Projectile.BeetleSpike;
import com.Game.Projectile.BeetleSpikeAvoidable;
import com.Util.Math.Vector2;

public class Cockroach extends Enemy {
    public Cockroach(int x, int y) {
        super(x, y);
        this.image = getImage("CockRoach.png");
        this.passive = false;
        this.speed = 2.5f;
        this.maxTarget = 5f;
        setMaxHealth(35);
        setScale(48, 48);
        setBounds(679, 1718, 1501, 2775);
    }

    public void AI() {
        timer += Main.dTime();
        timer2 += Main.dTime();

        Vector2 move = Vector2.magnitudeDirection(position, Main.player.position);
        position.add(move.scale(speed * 1.25f));

        if (timer2 > 1.25f) {
            timer2 = 0;
            new BeetleSpike(position);
        }

        if (timer > 1f) {
            timer = 0;
            new BeetleSpikeAvoidable(position).multiProjectile(3, 0.5f);
        }
    }

    public void passiveAI() {
        moveToAI();
    }
}
