package com.Game.Projectile;

import com.Game.GUI.Skills.Skills;
import com.Util.Math.Vector2;

/**
 * Default Projectile When No Weapon is Equipped
 */
public class Fist extends Projectile {

    public Fist(Vector2 position, Vector2 aim) {
        super(position, aim, 0.25f + Skills.getLevel(Skills.RANGED) * 0.05f, 2.5f, 1, true);
        this.rotate = true;
        this.duration = 0.2f;
        this.attackStyle = 1;
        this.scale = Vector2.identity(32);
        setImage("fist.png");
        setCooldown(0.5f);
    }
}
