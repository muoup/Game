package com.Game.Projectile;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.GUI.Skills.Skills;
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
    protected Vector2 initPos;
    protected Vector2 aim;
    protected Vector2 direction;
    protected Vector2 scale;
    protected float expMultiplier;
    protected float duration;
    protected float speed;
    protected boolean rotate;

    private float damage;
    public BufferedImage image;
    private boolean friendly;
    public int attackStyle;

    public Projectile(Vector2 position, Vector2 aim, float damage, float speed, float expMultiplier, boolean friendly) {
        this.position = position.clone();
        this.aim = aim.clone();
        this.damage = damage;
        this.friendly = friendly;
        this.expMultiplier = expMultiplier;
        this.speed = speed;
        this.rotate = false;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);

        Player.projectiles.add(this);
    }

    public void setImage(String root) {
        try {
            this.image = ImageIO.read(getClass().getResource("/res/images/Projectiles/" + root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void projectileUpdate() {
        position.add(direction.scaleClone(speed));
        BufferedImage bullet = image;//Render.getScaledImage(image, scale);

        if (rotate) {
            Vector2 p = initPos;
            Vector2 m = aim;
            double radians = Math.atan(-(m.x - p.x)/(m.y - p.y));

            if (m.y > p.y)
                radians += Math.PI;

            bullet = Render.rotateImage(bullet, radians);
        }

        Render.drawImage(bullet, position.subtractClone(World.curWorld.offset));

        duration -= 1 / Main.fps;

        if (duration < 0)
            destroy();

        if (friendly) {
            for (Enemy e : MethodHandler.enemies) {
                if (!e.enabled)
                    continue;

                if (Vector2.distance(e.position, position) < e.image.getWidth()) {
                    e.damage(damage);
                    onHit(e, damage);
                    destroy();
                }
            }
        } else {
            if (Vector2.distance(Main.player.position, position) < 16) {
                Main.player.damage(damage);
                destroy();
            }
        }

        render();
        update();
    }

    private void onHit(Enemy enemy, float damage) {
        Skills.addExperience(Skills.RANGED, (int) (damage * expMultiplier));
    }

    public void render() {}

    public void update() {}

    private void destroy() {
        Player.removeProj.add(this);
    }
}
