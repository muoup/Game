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

    public void multiShot(double degrees, float radius, int amount) {
//        degrees = Math.toRadians(degrees);
//
//        if (amount % 2 == 1) {
//            // If the amount is odd, the initial bullet will be
//            // in the middle, so that can be ignored.
//
//            // For the bullet, this will be the adjusted aim so that it other
//            // bullets can have their aims based upon a correctly position aim
//            setAim(Vector2.dynamicNormalization(position, aim).scale(radius).addClone(position));
//            System.out.println(aim);
//
//            float dx = aim.x - position.x;
//            float dy = aim.y - position.y;
//
//            Constructor projectileConstructor;
//
//            try {
//                projectileConstructor = getClass().getConstructor(new Class[] {Vector2.class, Vector2.class} );
//            } catch (NoSuchMethodException e) {
//                System.err.println(getClass() + " does not contain a correct constructor!");
//                return;
//            }
//
//            for (int i = -amount / 2; i < amount / 2; i++) {
//                /*
//                    Point on Circle from Center = (r*sinθ,r*cosθ)
//                    Where r = radius of circle and θ = degrees
//                    NOTE: 0 degrees is located at (0, r)
//                 */
//
//                try {
//                    Projectile newProj = (Projectile) projectileConstructor.newInstance(position, aim);
//                    float degreeX = radius * (float) Math.sin(initialDegrees + i * degrees);
//                    float degreeY = radius * (float) Math.cos(initialDegrees + i * degrees);
//                    newProj.aim = new Vector2(degreeX, degreeY).addClone(position);
//                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
