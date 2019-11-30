package com.Game.Projectile;

import com.Game.Main.Main;
import com.Util.Math.Vector2;

public class BeetleSpikeAvoidable extends Projectile {
    public BeetleSpikeAvoidable(Vector2 position) {
        super(position, Main.player.position, 2.5f, 2.5f, -1, false);
        setImage("beetle_spike2.png");
        this.rotate = true;
        this.duration = 5.5f;
    }
}
