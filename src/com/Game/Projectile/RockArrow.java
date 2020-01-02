package com.Game.Projectile;

import com.Util.Math.Vector2;

public class RockArrow extends Projectile {
    public RockArrow(Vector2 position, Vector2 aim, float damage, float speed, float expMultiplier, boolean friendly) {
        super(position, aim, damage, speed, expMultiplier, friendly);
        this.rotate = true;
        this.duration = 1.25f;
        this.attackStyle = 1;
        setScale(16);
        setImage("rock_arrow.png");
        setCooldown(0.4f);
    }

    public RockArrow(Vector2 position, Vector2 aim, Projectile projectile) {
        super(position, aim, projectile.damage, projectile.speed, projectile.expMultiplier, projectile.friendly);
        this.rotate = true;
        this.duration = 1.25f;
        this.attackStyle = 1;
        setScale(16);
        setImage("rock_arrow.png");
        setCooldown(0.4f);
    }
}
