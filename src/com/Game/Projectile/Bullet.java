package com.Game.Projectile;

import com.Util.Math.Vector2;

public class Bullet extends Projectile {

    public Bullet(Vector2 position, Vector2 aim, Vector2 scale, float damage, float speed, boolean friendly) {
        super(position, aim, scale, damage, speed, friendly);
        duration = 2.5f;
        setImage("bullet.png");
    }

    public void render() {

    }

    public void update() {

    }
}
