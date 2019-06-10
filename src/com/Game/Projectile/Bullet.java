package com.Game.Projectile;

import com.Util.Math.Vector2;

import java.awt.*;

public class Bullet extends Projectile {

    public Bullet(Vector2 position, Vector2 aim, Vector2 scale, float damage, float speed) {
        super(position, aim, scale, damage, speed);
        setImage("/res/images/bullet.png");
    }

    public void render(Graphics g) {

    }

    public void update() {

    }
}
