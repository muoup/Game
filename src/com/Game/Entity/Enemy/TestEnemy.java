package com.Game.Entity.Enemy;

import com.Game.Main.Main;
import com.Game.Projectile.Bullet;
import com.Util.Math.Vector2;

public class TestEnemy extends Enemy {

    private float timer = 0;
    private float maxTimer = 2f;

    private float speed = 2f;
    private float maxDistance = 250f;

    public TestEnemy(int x, int y) {
        super(x, y);
        id = 0;
        maxHealth = 100f;
        respawnTimer = 1f;
        image = getImage("testEnemy.png");
        health = maxHealth;
    }

    public void AI() {
        if (Vector2.distance(position, Main.player.position) > maxDistance) {
            position.add(Vector2.magnitudeDirection(position, Main.player.position).scale(speed));
        } else {
            timer += 1 / Main.fps;

            if (timer > maxTimer) {
                timer = 0;
                new Bullet(position, Main.player.position, 5f, 2f, 0, false);
            }
        }
    }
}
