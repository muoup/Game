package com.Game.Projectile;

import com.Util.Math.Vector2;

public class BeetleSpike extends HomingProjectile {
    public BeetleSpike(Vector2 position) {
        super(position, 2.5f, 2.5f, -1, false);
        setImage("beetle_spike.png");
        this.rotate = true;
    }
}
