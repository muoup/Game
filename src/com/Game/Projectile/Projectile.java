package com.Game.Projectile;

import com.Game.Main.Main;
import com.Util.Math.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
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

        index = Main.projectiles.size();
        Main.projectiles.add(this);
    }

    public void setImage(String root) {
        try {
            this.image = ImageIO.read(getClass().getResource(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void projectileUpdate(Graphics g, int index) {
        position.add(direction);
        g.drawImage(image, (int) position.x, (int) position.y, (int) scale.x, (int) scale.y, null);
        if (position.x + scale.x > Main.frame.getWidth()
                || position.x - scale.x < 0
                || position.y + scale.y > Main.frame.getHeight()
                || position.y - scale.y < 0)
            destroy(index);

        render(g);
        update();
    }

    public void render(Graphics g) {}

    public void update() {}

    public void destroy(int index) {
        Main.projectiles.remove(index);
    }
}
