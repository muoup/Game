package com.Game.Projectile;

import com.Game.GUI.Skills.Skills;
import com.Util.Math.Vector2;

/**
 * Default Projectile When No Weapon is Equipped
 */
public class Fist extends Projectile {

    public Fist(Vector2 position, Vector2 aim) {
        super(position, aim, 0.1f + Skills.getLevel(Skills.RANGED) * 0.01f, 1.15f, 1, true);
        this.rotate = true;
        this.duration = 0.75f;
        this.attackStyle = 1;
        this.scale = Vector2.identity(128);
        setImage("fist.png");
    }
}
