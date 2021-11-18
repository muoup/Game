package com.Game.Entity;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {

    public int id = 0;
    public Vector2 position;
    public Vector2 spawnPosition;
    protected Vector2 moveTo;
    protected Vector2 movement;

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

    public float maxRadius = 0;
    protected float speed = 0;

    private boolean useBounds = false;
    protected Vector2 b1;
    protected Vector2 b2;

    public BufferedImage image;

    public String name = getClass() + " NAME NEEDS TO BE CHANGED";
    public boolean temp = false;

    public Enemy(int x, int y) {
        position = new Vector2(x, y);
        spawnPosition = new Vector2(x, y);

        MethodHandler.enemies.add(this);
    }

    public static void createTemporary(Enemy enemy) {
        enemy.temp = true;
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
                setMoveTo();
                onRespawn();
            }

            return;
        }

        determineActive();
        renderEnemy();

        if (target)
            AI();

        if (!target || (target && passive)) {
            if (health < maxHealth && !target)
                health += maxHealth / 10 * Main.dTime();
            if (health >= maxHealth && !target)
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
        return Player.position;
    }

    public boolean withinBounds() {
        if (b1 == null || b2 == null) {
            System.err.println(getClass() + " does not contain a definition for its boundaries!");
            return false;
        }

        return position.compareTo(b1) != -1 && position.compareTo(b2) != 1;
    }

    public void moveToPlayer() {
        position.add(Vector2.magnitudeDirection(position, getPlayer()).scale(speed));
    }

    public void renderEnemy() {
        Vector2 deltaPosition = position.subtractClone(World.offset);

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
        SoundHandler.playSound("enemy_hit.wav");

        if (health <= 0) {
            enabled = false;
            SoundHandler.playSound("default_death.wav");
            handleDrops();
            if (temp)
                MethodHandler.enemies.remove(this);
        }
    }

    public void setBounds(float x, float y, float x2, float y2) {
        b1 = new Vector2(x, y);
        b2 = new Vector2(x2, y2);
        useBounds = true;
    }

    public void moveToAI() {
        if (moveTo == null) {
            setMoveTo();
            return;
        }

        if (b1.greaterThan(moveTo) || moveTo.greaterThan(b2))
            setMoveTo();

        if (Vector2.distance(position, moveTo) < 32) {
            setMoveTo();
        } else {
            position.add(movement.scaleClone(speed));
        }
    }

    public void setMoveTo() {
        if (!useBounds)
            moveTo = spawnPosition.addClone(DeltaMath.range(-maxRadius, maxRadius), DeltaMath.range(-maxRadius, maxRadius));
        else
            moveTo = new Vector2(DeltaMath.range(b1.x, b2.x),
                    DeltaMath.range(b1.y, b2.y));

        movement = Vector2.magnitudeDirection(position, moveTo);

    }

    public void target() {
        target = true;
        targetTimer = maxTarget;
    }

    public float range() {
        return Vector2.distance(position, Player.position);
    }

    public static BufferedImage getImage(String name) {
        return Main.main.getImageFromRoot("Entities/Enemies/" + name);
    }

    public void AI() {

    }

    public void handleDrops() {

    }
}
