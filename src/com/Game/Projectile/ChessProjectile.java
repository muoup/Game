package com.Game.Projectile;

import com.Util.Math.Vector2;

public class ChessProjectile extends Projectile {
    public ChessProjectile(Vector2 position, Vector2 aim, float damage) {
        super(position, aim, damage, 3.5f, 0f, false);
        setImage("chess_pellet.png");
        scale = new Vector2(16, 16);
    }
}
