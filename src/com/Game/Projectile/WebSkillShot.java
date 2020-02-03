package com.Game.Projectile;

import com.Util.Math.Vector2;

public class WebSkillShot extends Projectile {
    public WebSkillShot(Vector2 position, Vector2 aim) {
        super(position, aim, 12, 2.25f, 0, false);
        this.duration = 2.5f;
        this.rotate = false;
        setScale(18);
        setImage("web.png");
    }

    public WebSkillShot(Vector2 position, Vector2 aim, Projectile arrow) {
        super(position, aim, arrow);
    }
}
