package com.Game.Projectile;

import com.Game.Entity.Enemy.Enemy;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
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
        this.speed = speed * Settings.projLengthMultiplier;
        this.rotate = false;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);

        // If the bullet is not going to move, there is no point in spawning it in.
        if (!position.equalTo(aim)) {
            MethodHandler.projectiles.add(this);
            setCooldown(0.35f);
        }
    }

    public Projectile(Vector2 position, Vector2 aim, Projectile arrow) {
        this.position = position.clone();
        this.aim = aim.clone();
        this.damage = arrow.damage;
        this.friendly = arrow.friendly;
        this.expMultiplier = arrow.expMultiplier;
        this.speed = arrow.speed;
        this.rotate = arrow.rotate;
        this.attackStyle = arrow.attackStyle;
        this.duration = arrow.duration;
        this.image = arrow.image;
        this.scale = arrow.scale;

        initPos = position.clone();

        direction = Vector2.magnitudeDirection(position, aim).scale(speed);

        // If the bullet is not going to move, there is no point in spawning it in.
        if (!position.equalTo(aim)) {
            MethodHandler.projectiles.add(this);
            setCooldown(0.35f);
        }
    }

    protected Object clone() {

        return null;
    }

    public void setCooldown(float timer) {
        Main.player.shootTimer = timer;
    }

    public void setAim(Vector2 aim) {
        this.aim = aim;
        direction = Vector2.magnitudeDirection(position, aim).scale(speed);
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

        image = Render.getScaledImage(image, scale.x, scale.y);

        aim.add(image.getWidth() / 2, image.getHeight() / 2);

        if (rotate) {
            double radians = Math.atan(-(aim.x - position.x) / (aim.y - position.y));

            if (aim.y > position.y)
                radians += Math.PI;

            image = Render.rotateImage(image, radians);
        }
    }

    public void setScale(int scale) {
        this.scale = new Vector2(scale);
    }

    public Vector2 adjustedPosition() {
        return position.subtractClone((float) image.getWidth() / 2, (float) image.getHeight() / 2);
    }

    public Vector2 getCenter() {
        return adjustedPosition().addClone(Render.getImageSize(image).scale(0.5f));
    }

    public void projectileUpdate() {
        position.add(direction.scaleClone(speed));

        Render.drawImage(image, adjustedPosition().subtractClone(World.curWorld.offset));

        duration -= Main.dTime();

        if (duration < 0)
            destroy();

        if (friendly) {
            ArrayList<Enemy> enemies = MethodHandler.enemies;
            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
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
        switch (attackStyle) {
            case 1:
                Skills.addExperience(Skills.RANGED, (int) (damage * expMultiplier * Settings.rangedXPMultiplier));
                break;
            case 2:
                Skills.addExperience(Skills.MELEE, (int) (damage * expMultiplier * Settings.meleeXPMultiplier));
                break;
        }
    }

    public void render() {
    }

    public void update() {
    }

    protected void destroy() {
        MethodHandler.projectiles.remove(this);
    }

    public void multiShot(double degrees, float radius, int amount) {
        degrees = Math.toRadians(degrees);

        if (amount % 2 == 1) {
            double theta = Math.atan((aim.x - position.x) / (aim.y - position.y));

            if (aim.y - position.y <= 0) {
                theta += DeltaMath.pi;
            }

            Constructor projectileConstructor;

            Vector2 adjust = position.addClone(radius * Math.sin(theta), radius * Math.cos(theta));

            setAim(adjust);

            try {
                projectileConstructor = getClass().getConstructor(Vector2.class, Vector2.class, Projectile.class);
            } catch (NoSuchMethodException e) {
                System.err.println(getClass() + " does not contain a correct constructor!");
                return;
            }

            for (int i = -amount / 2; i < amount / 2 + 1; i++) {
                if (i == 0)
                    continue;

                /*
                    Point on Circle from Center = (r * sin(θ), r * cos(θ))
                    Where r = radius of circle and θ = degrees
                    NOTE: 0 degrees is located at (0, r)
                 */

                try {
                    Vector2 newAim = position.addClone(radius * Math.sin(theta + i * degrees), radius * Math.cos(theta + i * degrees));
                    projectileConstructor.newInstance(position, newAim, this);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void multiShotEnemy(double degrees, float radius, int amount) {
        degrees = Math.toRadians(degrees);

        if (amount % 2 == 1) {
            Vector2 aim = Main.player.position;

            double theta = Math.atan((aim.x - position.x) / (aim.y - position.y));

            if (aim.y - position.y <= 0) {
                theta += DeltaMath.pi;
            }

            Vector2 adjust = position.addClone(radius * Math.sin(theta), radius * Math.cos(theta));

            setAim(adjust);

            Constructor projectileConstructor;

            try {
                projectileConstructor = getClass().getConstructor(Vector2.class, Vector2.class, Projectile.class);
            } catch (NoSuchMethodException e) {
                System.err.println(getClass() + " does not contain a correct multi-shot constructor!");
                return;
            }

            for (int i = -amount / 2; i < amount / 2 + 1; i++) {
                if (i == 0)
                    continue;

                /*
                    Point on Circle from Center = (r * sin(θ), r * cos(θ))
                    Where r = radius of circle and θ = degrees
                    NOTE: 0 degrees is located at (0, r)
                 */

                try {
                    Vector2 newAim = position.addClone(radius * Math.sin(theta + i * degrees), radius * Math.cos(theta + i * degrees));
                    projectileConstructor.newInstance(position, newAim, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}