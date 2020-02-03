package com.Game.Projectile;

import com.Util.Math.Vector2;

public class BeetleSpikeAvoidable extends Projectile {
    public BeetleSpikeAvoidable(Vector2 position, Vector2 aim) {
        super(position, aim, 35f, 2.5f, -1, false);
        this.rotate = false;
        this.duration = 5.5f;
        setScale(16);
        setImage("beetle_spike2.png");
    }

    public BeetleSpikeAvoidable(Vector2 position, Vector2 aim, Projectile arrow) {
        super(position, aim, arrow);
    }
}
