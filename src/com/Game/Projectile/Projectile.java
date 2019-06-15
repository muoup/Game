package com.Game.Projectile;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Projectile {
    protected Vector2 position;
    protected Vector2 aim;
    protected Vector2 scale;
    protected Vector2 direction;

    private float damage;
    private int index;
    public BufferedImage image;

    public Projectile(Vector2 position, Vector2 aim, Vector2 scale, float damage, float speed) {
        this.position = position.clone();
        this.aim = aim.clone();
        this.scale = scale;
        this.damage = damage;

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);

        MethodHandler.projectiles.add(this);
    }

    public void setImage(String root) {
        try {
            this.image = ImageIO.read(getClass().getResource("/res/images/" + root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void projectileUpdate(int index) {
        position.add(direction);
        Render.drawImage(image, position);

        if (position.x + scale.x > Main.frame.getWidth()
                || position.x - scale.x < 0
                || position.y + scale.y > Main.frame.getHeight()
                || position.y - scale.y < 0)
            destroy(index);

        render();
        update();
    }

    public void render() {}

    public void update() {}

    public void destroy(int index) {
        Main.methods.projectiles.remove(index);
    }
}
