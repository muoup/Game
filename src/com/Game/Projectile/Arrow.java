package com.Game.Projectile;

import com.Util.Math.Vector2;

public class Arrow extends Projectile {
    public Arrow(Vector2 position, Vector2 aim, float damage, float speed, float expMultiplier, boolean friendly) {
        super(position, aim, damage, speed, expMultiplier, friendly);
        this.rotate = true;
        this.duration = 1.5f;
        this.attackStyle = 1;
        setScale(16);
        setImage("arrow.png");
        setCooldown(0.65f);
    }

    /*
        This should only be called from multiShotEnemy()
     */
    public Arrow(Vector2 position, Vector2 aim, Projectile arrow) {
        super(position, aim, arrow);
    }
}
