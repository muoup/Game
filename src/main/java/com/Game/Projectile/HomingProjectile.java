package com.Game.Projectile;

import com.Game.Entity.Player;
import com.Game.Main.Main;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

public class HomingProjectile extends Projectile {

    public boolean avoidable;

    public HomingProjectile(Vector2 position, float speed) {
        super(position, Vector2.zero(), speed);
    }

    @Override
    public void renderProjectile() {
        Vector2 target = Player.position.clone();
        position.add(Vector2.magnitudeDirection(position, target).scale(speed));

        Render.drawImage(image, position.subtractClone(World.offset));

        speed += Main.dTime();
    }
}
