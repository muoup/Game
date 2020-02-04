package com.Game.Projectile;

import com.Util.Math.Vector2;

public class PawnProjectile extends Projectile {
    public PawnProjectile(Vector2 position, Vector2 aim, float damage) {
        super(position, aim, damage, 3.5f, 0f, false);
        this.duration = 10f;
        this.speed = 1.25f;
        setScale(48);
        setImage("chess_pellet.png");
    }
}
