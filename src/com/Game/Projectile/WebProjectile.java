package com.Game.Projectile;

import com.Util.Math.Vector2;

public class WebProjectile extends HomingProjectile {
    public WebProjectile(Vector2 position, float damage, float speed, float duration) {
        super(position, damage, speed, 0, false);
        setImage("web.png");
        this.avoidable = false;
        this.scale = Vector2.identity(24);
    }
}
