package com.Game.Projectile;

import com.Util.Math.Vector2;

public class RockPellet extends Projectile {
    public RockPellet(Vector2 position, Vector2 aim, float damage, float speed, float expMultiplier, boolean friendly) {
        super(position, aim, damage, speed, expMultiplier, friendly);
        this.duration = 2.5f;
        this.scale = Vector2.identity(32);
        setImage("rockPellet.png");
    }
}
