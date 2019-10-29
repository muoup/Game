package com.Game.Projectile;

import com.Util.Math.Vector2;

public class Bullet extends Projectile {

    public Bullet(Vector2 position, Vector2 aim, float damage, float speed, float expMultiplier, boolean friendly) {
        super(position, aim, damage, speed, expMultiplier, friendly);
        this.duration = 1.25f;
        this.attackStyle = 1;
        this.scale = Vector2.identity(32);
        setImage("bullet.png");
    }

    public void render() {

    }

    public void update() {

    }
}
