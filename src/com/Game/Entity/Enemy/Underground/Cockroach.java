package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Items.EntityDropTable;
import com.Game.Items.ItemList;
import com.Game.Main.Main;
import com.Game.Projectile.BeetleSpike;
import com.Game.Projectile.BeetleSpikeAvoidable;
import com.Game.World.GroundItem;

public class Cockroach extends Enemy {
    public float timer2 = 0;

    public Cockroach(int x, int y) {
        super(x, y);
        this.maxTarget = 7.5f;
        this.passive = false;
        this.image = getImage("CockRoach.png");
        this.speed = 1.25f;
        this.name = "Cockroach";
        this.respawnTimer = 8.0f;

        setMaxHealth(25);
        setScale(64, 64);
        setBounds(3577, 2793, 4159, 3355);
    }

    public void update() {
        if (!withinBounds())
            targetTimer = 0;
    }

    public void AI() {
        timer += Main.dTime();
        timer2 += Main.dTime();

        if (range() > 256f)
            moveToPlayer();

        if (timer > 0.65f) {
            new BeetleSpikeAvoidable(position, Main.player.position).multiShotEnemy(20, 256, 5);
            timer = 0;
        }

        if (timer2 > 1.35f) {
            new BeetleSpike(position);
            timer2 = 0;
        }
    }

    public void passiveAI() {
        moveToAI();
    }

    public void handleDrops() {
        EntityDropTable table = new EntityDropTable();

        table.add(ItemList.arrow, 5, 1);
        table.add(ItemList.arrow, 5, 0.25);
        table.add(ItemList.bronzeDagger, 1, 0.50);
        table.add(ItemList.stoneDagger, 1, 0.25);
        table.add(ItemList.ironDagger, 1, 0.075);

        GroundItem.createGroundItem(position, table.determineOutput());
    }
}
