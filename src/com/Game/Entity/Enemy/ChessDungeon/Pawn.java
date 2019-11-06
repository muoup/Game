package com.Game.Entity.Enemy.ChessDungeon;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Main.Main;
import com.Game.Projectile.ChessProjectile;

public class Pawn extends Enemy {
    public Pawn(int x, int y) {
        super(x, y);
        setMaxHealth(35);
        this.image = getImage("/ChessDungeon/pawn.png");
        this.name = "Chessboard Pawn";
        this.id = 35;
        this.respawnTimer = 5.0f;
        this.maxTarget = 4.5f;
        this.timer = 0;
        this.passive = false;
        setScale(96, 96);
    }

    public void AI() {
        timer += Main.dTime();
        timer2 += Main.dTime();

        if (timer2 > 0.55f) {
            new ChessProjectile(position, Main.player.position,  22.5f);

            timer2 = 0;
        }
    }

    public void passiveAI() {
        if (timer > 0.1f) {
            timer -= Main.dTime();
        } else if (timer < -0.1f) {
            timer += Main.dTime();
        } else if (timer != 0) {
            timer = 0;
        }
    }

    public void update() {
        position = spawnPosition.addClone(0, 48f + (float) Math.sin((timer - 3.14f / 2f) * 5) * 48f);
    }
}
