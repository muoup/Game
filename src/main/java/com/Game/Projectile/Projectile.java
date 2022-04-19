package com.Game.Projectile;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Sprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Projectile {
    public Vector2 position;
    protected Vector2 initPos;
    protected Vector2 aim;
    protected Vector2 direction;
    protected Vector2 scale;

    protected boolean rotate;
    protected boolean friendly;
    protected boolean homing = false;

    protected float speed;

    protected int randomToken;

    protected BufferedImage image;

    public Projectile(Vector2 position, boolean friendly, Vector2 direction, float speed, BufferedImage image) {
        this(position, friendly, speed, image);
        this.direction = direction;
        this.aim = direction.clone();

        MethodHandler.projectiles.add(this);
    }

    public Projectile(Vector2 position, boolean friendly, float speed, BufferedImage image) {
        this.position = position.clone();
        this.speed = speed;
        this.friendly = friendly;
        this.image = image;

        initPos = position.clone();
    }

    public static void spawn(String message) {
        // 4493.0 2606.0;token;4387.0 2388.875;speed;true;randomToken
        String[] contents = message.split(";");

        Vector2 position = Vector2.fromString(contents[0]);
        Vector2 direction = Vector2.fromString(contents[2]);

        Sprite image = Sprite.identifierSprite(contents[1]);

        float speed = Float.parseFloat(contents[3]);
        boolean friendly = Boolean.parseBoolean(contents[4]);

        new Projectile(position, friendly, direction, speed, image.getImage()).randomToken = Integer.parseInt(contents[5]);
    }

    public static void destroy(int id) {
        ArrayList<Projectile> projectiles = MethodHandler.projectiles;
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            if (p.randomToken == id) {
                MethodHandler.projectiles.remove(i);
                return;
            }
        }
    }

    public void setScale(int scale) {
        this.scale = new Vector2(scale);
        this.position.subtractClone(scale / 2, scale / 2);
    }

    public Vector2 adjustedPosition() {
        return position.subtractClone((float) image.getWidth() / 2, (float) image.getHeight() / 2);
    }

    public void renderProjectile() {
        Vector2 movement = direction.scaleClone(speed * (float) Main.dTime());

        position.add(movement);

        Render.drawImage(image, adjustedPosition().subtractClone(World.offset));

        render();
        update();
    }

    public void render() {
    }

    public void update() {
    }

    protected void destroy() {
        MethodHandler.projectiles.remove(this);
    }
}