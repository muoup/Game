package com.Game.Projectile;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

public class HomingProjectile extends Projectile {

    public boolean avoidable;

    public HomingProjectile(Vector2 position, float damage, float speed, float expMultiplier, boolean friendly) {
        super(position, Vector2.zero(), damage, speed, expMultiplier, friendly);
    }

    @Override
    public void projectileUpdate() {
        Vector2 target = Main.player.position.clone();
        position.add(Vector2.magnitudeDirection(position, target).scale(speed));

        Render.drawImage(image, position.subtractClone(World.curWorld.offset));

        if (avoidable) {
            duration -= Main.dTime();

            if (duration < 0)
                destroy();
        } else {
            speed += Main.dTime();
        }

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
    }
}
