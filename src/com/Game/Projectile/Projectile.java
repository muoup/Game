package com.Game.Projectile;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
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
    protected float duration;
    private int index;
    public BufferedImage image;
    private boolean friendly;

    public Projectile(Vector2 position, Vector2 aim, Vector2 scale, float damage, float speed, boolean friendly) {
        this.position = position.clone();
        this.aim = aim.clone();
        this.scale = scale;
        this.damage = damage;
        this.friendly = friendly;

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
        Render.drawImage(image, position.subtractClone(World.curWorld.offset));

        duration -= 1 / Main.fps;

        if (duration < 0)
            destroy(index);

        if (friendly) {
            for (Enemy e : MethodHandler.enemies) {
                if (!e.enabled)
                    continue;

                if (Vector2.distance(e.position, position) < e.image.getWidth()) {
                    e.damage(damage);

                    destroy(index);
                }
            }
        } else {
            if (Vector2.distance(Main.player.position, position) < 2) {
                Main.player.damage(damage);

                destroy(index);
            }
        }

        render();
        update();
    }

    public void render() {}

    public void update() {}

    public void destroy(int index) {
        Main.methods.projectiles.remove(index);
    }
}
