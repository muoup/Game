package com.Game.Entity;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {

    public Vector2 position;

    public int id = 0, token;
    public Vector2 moveTo;
    public Vector2 movement;

    public boolean enabled = true;

    public float maxHealth;
    public float health;
    public float speed = 0;

    public BufferedImage image;

    public String name;

    public Enemy(Vector2 position, Sprite sprite, int token, float health, float maxHealth, String name) {
        this.position = position;
        this.token = token;
        this.moveTo = position;
        this.movement = Vector2.zero();
        this.health = health;
        this.maxHealth = maxHealth;
        this.image = sprite.getImage();
        this.name = name;

        MethodHandler.enemies.add(this);
    }

    public Vector2 getPlayer() {
        return Player.position;
    }

    public void renderEnemy() {
        if (!enabled)
            return;

        Vector2 deltaPosition = position.subtractClone(World.offset);

        if (!movement.isZero() && Vector2.approximatelyEqualAngles(movement, Vector2.magnitudeDirection(position, moveTo))) {
            position.add(movement.scaleClone(speed * (float) Main.dTime()));
        }

        if (image == null) {
            System.err.println(name + ": " + id + " did not load image correctly :(.");
            return;
        }

        World.renderEnemy(position);

        if (Render.onScreen(position, image)) {
            Render.drawImage(image, deltaPosition.subtract(Render.getDimensions(image).scale(0.5f)));

            // Draw Health Bar
            Render.setColor(Color.RED);
            Render.drawRectangle(deltaPosition.addClone(new Vector2(0, -8)),
                    new Vector2((float) image.getWidth() * (health / maxHealth), 8));
        }
    }

    public void setMoveTo(Vector2 position, Vector2 moveTo, float speed) {
        this.moveTo = moveTo;
        this.position = position;
        this.speed = speed;

        movement = Vector2.magnitudeDirection(position, moveTo);
    }
}
