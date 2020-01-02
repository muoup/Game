package com.Game.Projectile;

import com.Util.Math.Vector2;

public class Dagger extends Projectile {
    public Dagger(Vector2 position, Vector2 aim, float damage, float speed, float expMultiplier, boolean friendly) {
        super(position, aim, damage, speed, expMultiplier, friendly);
        this.rotate = true;
        this.duration = 0.5f;
        this.attackStyle = 2;
        setScale(16);
        setImage("dagger.png");
        setCooldown(0.35f);
    }
}
