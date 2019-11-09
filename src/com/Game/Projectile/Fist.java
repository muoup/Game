package com.Game.Projectile;

import com.Game.GUI.Skills.Skills;
import com.Util.Math.Vector2;

/**
 * Default Projectile When No Weapon is Equipped
 */
public class Fist extends Projectile {

    public Fist(Vector2 position, Vector2 aim) {
        super(position, aim, 2.5f + Skills.getLevel(Skills.RANGED) * 0.035f, 2.5f, 1, true);
        this.rotate = true;
        this.duration = 0.5f;
        this.attackStyle = 1;
        setScale(16);
        setImage("fist.png");
        setCooldown(0.5f);
    }
}
