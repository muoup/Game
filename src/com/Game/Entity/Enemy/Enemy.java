package com.Game.Entity.Enemy;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {

    public int id = 0;
    public Vector2 position;
    public Vector2 spawnPosition;

    public boolean enabled = true;
    public boolean target = false;
    public boolean passive = false;

    public float respawnTimer = 0;

    public float timer = 0;
    public float timer2 = 0;

    public float maxTarget = 0;
    public float targetTimer = 0;

    public float maxHealth = 0;
    public float health = 25000f;

    public BufferedImage image;

    public String name = "NAME NEEDS TO BE CHANGED";

    public Enemy(int x, int y) {
        position = new Vector2(x, y);
        spawnPosition = new Vector2(x, y);

        MethodHandler.enemies.add(this);
    }

    public void setMaxHealth(float amount) {
        this.maxHealth = amount;
        this.health = amount;
    }

    public void setScale(int x, int y) {
        this.image = Render.getScaledImage(image, x, y);
    }

    public void updateEnemy() {
        update();

        if (!enabled) {
            timer += Main.dTime();

            if (timer > respawnTimer) {
                enabled = true;
                target = false;
                targetTimer = 0;
                health = maxHealth;
                position = spawnPosition.clone();
                timer = 0;
                onRespawn();
            }

            return;
        }

        determineActive();
        renderEnemy();

        if (target)
            AI();

        if (!target || target && passive) {
            if (health < maxHealth)
                health += maxHealth / 10 * Main.dTime();
            if (health >= maxHealth)
                health = maxHealth;
            passiveAI();
        }
    }

    public void update() {

    }

    public void passiveAI() {

    }

    public void onRespawn() {

    }

    public Vector2 getPlayer() {
        return Main.player.position;
    }

    public void renderEnemy() {
        Vector2 deltaPosition = position.subtractClone(World.curWorld.offset);

        if (image == null) {
            System.err.println(name + ": " + id + " did not load image correctly :(.");
            return;
        }

        if (Render.onScreen(position, image)) {
            Render.drawImage(image, deltaPosition.subtract(Render.getDimensions(image).scale(0.5f)));

            // Draw Health Bar
            Render.setColor(Color.RED);
            Render.drawRectangle(deltaPosition.addClone(new Vector2(0, -8)),
                    new Vector2((float) image.getWidth() * (health / maxHealth), 8));
        }
    }

    public void loseTarget() {

    }

    public void determineActive() {
        targetTimer -= Main.dTime();

        if (targetTimer <= 0 && target) {
            target = false;
            loseTarget();
        }
    }

    public void damage(float amount) {
        target();
        health -= amount;

        if (health <= 0) {
            enabled = false;
            handleDrops();
        }
    }

    public void target() {
        target = true;
        targetTimer = maxTarget;
    }

    public static BufferedImage getImage(String name) {
        return Main.main.getImageFromRoot("Entities/Enemies/" + name);
    }

    public void AI() {

    }

    public void handleDrops() {

    }
}
