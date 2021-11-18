package com.Game.Projectile;

import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.image.BufferedImage;

public class Projectile {
    protected Vector2 position;
    protected Vector2 initPos;
    protected Vector2 aim;
    protected Vector2 direction;
    protected Vector2 scale;

    protected boolean rotate;

    protected float speed;

    public BufferedImage image;

    public Projectile(Vector2 position, Vector2 aim, float speed) {
        this.position = position.clone();
        this.aim = aim.clone();
        this.speed = speed * Settings.projLengthMultiplier;
        this.image = image;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);
    }

    protected Object clone() {
        return null;
    }

//    public void setImage(String root) {
//        try {
//            this.image = ImageIO.read(getClass().getResource("/images/Projectiles/" + root));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (image == null) {
//            System.err.println("Cannot find image: " + root);
//            return;
//        }
//
//        if (scale == null) {
//            scale = Vector2.identity(8);
//        }
//
//        image = Render.getScaledImage(image, scale.x, scale.y);
//
//        aim.add(image.getWidth() / 2, image.getHeight() / 2);
//
//        if (rotate) {
//            double radians = Math.atan(-(aim.x - position.x) / (aim.y - position.y));
//
//            if (aim.y > position.y)
//                radians += Math.PI;
//
//            image = Render.rotateImage(image, radians);
//        }
//    }

    public void setScale(int scale) {
        this.scale = new Vector2(scale);
        this.position.subtractClone(scale / 2, scale / 2);
    }

    public Vector2 adjustedPosition() {
        return position.subtractClone((float) image.getWidth() / 2, (float) image.getHeight() / 2);
    }

    public Vector2 getCenter() {
        return adjustedPosition().addClone(Render.getImageSize(image).scale(0.5f));
    }

    public void renderProjectile() {
        position.add(direction.scaleClone(speed));

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