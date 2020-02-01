package com.Game.Entity.Enemy.ChessDungeon;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Items.DropTable;
import com.Game.Items.ItemList;
import com.Game.Main.Main;
import com.Game.Projectile.PawnProjectile;
import com.Game.World.GroundItem;

public class Pawn extends Enemy {
    public Pawn(int x, int y, boolean white) {
        super(x, y);
        setMaxHealth(35);
        this.image = getImage("ChessDungeon/" + (white ? "" : "black_") + "pawn.png");
        this.name = "Chessboard Pawn";
        this.id = 35;
        this.respawnTimer = 25.0f;
        this.maxTarget = 4.5f;
        this.timer = 0.0f;
        this.passive = false;
        setScale(96, 96);
    }

    public void AI() {
        timer += Main.dTime();
        timer2 += Main.dTime();

        if (timer2 > 0.65f) {
            new PawnProjectile(position, Main.player.position, 25.5f);
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

    public void handleDrops() {
        DropTable table = new DropTable();
        table.add(ItemList.gold, 500, 0.75);
        table.add(ItemList.goldDagger, 1, 0.5);
        table.add(ItemList.stone, 5, 0.5);
        GroundItem.createGroundItem(position, table.determineOutput());
    }
}
