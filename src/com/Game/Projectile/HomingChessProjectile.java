package com.Game.Projectile;

import com.Util.Math.Vector2;

public class HomingChessProjectile extends HomingProjectile {
    public HomingChessProjectile(Vector2 position, float damage) {
        super(position, damage, 2.5f, 0f, false);
        setImage("chess_pellet.png");
    }
}
