package com.Game.Projectile;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

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

    protected float damage;
    public BufferedImage image;
    protected boolean friendly;

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
        setCooldown(0.35f);
    }

    private Projectile(Projectile projectile) {
        this.position = projectile.position.clone();
        this.aim = projectile.aim.clone();
        this.damage = projectile.damage;
        this.friendly = projectile.friendly;
        this.expMultiplier = projectile.expMultiplier;
        this.speed = projectile.speed;
        this.rotate = projectile.rotate;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);

        Player.projectiles.add(this);

        setCooldown(0);
    }

    protected Object clone() {

        return null;
    }

    public void setCooldown(float timer) {
        Main.player.shootTimer = timer;
    }

    public void setImage(String root) {
        try {
            this.image = ImageIO.read(getClass().getResource("/res/images/Projectiles/" + root));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image == null) {
            System.err.println("Cannot find image: " + root);
            return;
        }

        if (scale == null) {
            scale = Vector2.identity(8);
        }

        position.offset(-(image.getWidth() - Main.player.image.getWidth()) / 2);
        image = Render.getScaledImage(image, scale.x, scale.y);
    }

    public void setScale(int scale) {
        this.scale = new Vector2(scale);
    }

    public Vector2 getCenter() {
        return position.addClone(Render.getImageSize(image).scale(0.5f));
    }

    public void projectileUpdate() {
        position.add(direction.scaleClone(speed));
        BufferedImage bullet = image;

        if (rotate) {
            Vector2 p = initPos;
            Vector2 m = aim;
            double radians = Math.atan(-(m.x - p.x)/(m.y - p.y));

            if (m.y > p.y)
                radians += Math.PI;

            bullet = Render.rotateImage(bullet, radians);
        }

        Render.drawImage(bullet, position.subtractClone(World.curWorld.offset));

        duration -= Main.dTime();

        if (duration < 0)
            destroy();

        if (friendly) {
            for (Enemy e : MethodHandler.enemies) {
                if (!e.enabled)
                    continue;

                if (Vector2.distance(e.position, getCenter()) < (e.image.getWidth() + image.getWidth()) / 2f) {
                    e.damage(damage);
                    onHit(e, damage);
                    destroy();
                }
            }
        } else {
            if (Vector2.distance(Main.player.position, getCenter()) < scale.x) {
                Main.player.damage(damage);
                destroy();
            }
        }

        render();
        update();
    }

    protected void onHit(Enemy enemy, float damage) {
        Skills.addExperience(Skills.RANGED, (int) (damage * expMultiplier * Settings.rangedXPMultiplier));
    }

    public void render() {}

    public void update() {}

    protected void destroy() {
        Player.removeProj.add(this);
    }

    public void multiProjectile(int amount, float degree) {
        if (amount % 2 == 1) {
            // If the amount if odd, one bullet will move towards the player
            // and the others will be evenly split between the player.
            float distance = Vector2.distance(position, aim);
            float degrees = (float) Math.acos((aim.x - position.x) / distance);
            ArrayList<Projectile> pArray = Player.projectiles;

            for (int i = -amount / 2; i < amount / 2; i++) {
                if (i == 0)
                    continue;
                float deltaDegrees = degrees - degree * i;
                float x = position.x + (float) (distance * Math.cos(deltaDegrees));
                float y = position.y + (float) (distance * Math.sin(deltaDegrees));

                Projectile newProj = null;

                try {
                    newProj = this.getClass().getDeclaredConstructor(new Class[]{Vector2.class}).newInstance(position);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                    return;
                }

                newProj.aim = new Vector2(x, y);
                newProj.direction = Vector2.magnitudeDirection(position, newProj.aim);
            }
        } else {

        }
    }
}
