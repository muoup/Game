package com.Game.Projectile;

import com.Util.Math.Vector2;

public class Arrow extends Projectile {

    public Arrow(Vector2 position, Vector2 aim, float damage, float speed, float expMultiplier, boolean friendly) {
        super(position, aim,damage, speed, expMultiplier, friendly);
        this.rotate = true;
        this.duration = 1.25f;
        this.attackStyle = 1;
        this.scale = Vector2.identity(4);
        setImage("arrow.png");
    }
}
