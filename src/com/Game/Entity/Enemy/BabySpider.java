package com.Game.Entity.Enemy;

import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.Projectile.WebProjectile;
import com.Game.World.GroundItem;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;

import java.util.ArrayList;

public class BabySpider extends Enemy {
    private float passiveTimer, shotTimer, speed, maxDistance;
    private Vector2 walkTo = Vector2.zero();
    private boolean walking;

    public BabySpider(int x, int y) {
        super(x, y);
        this.image = getImage("babySpider.png");
        this.maxHealth = 25;
        this.speed = 2.5f;
        this.maxDistance = 125f;
        this.shotTimer = DeltaMath.range(0, 0.0525f);
        this.respawnTimer = 7.5f;
        this.maxTarget = 7.5f;
        this.name = "Spider";
        passiveTimer = DeltaMath.range(0.25f, 0.75f);
    }

    public void AI() {
        if (maxDistance < Vector2.distance(position, getPlayer())) {
            position.add(Vector2.magnitudeDirection(position, getPlayer()).scale(speed));
        }

        shotTimer -= Main.dTime();

        if (shotTimer < 0) {
            shotTimer = DeltaMath.range(0.25f, 1.25f);
            new WebProjectile(position, 12.5f, (speed / shotTimer) * 2.25f, shotTimer * 2.5f);
        }
    }

    public void passiveAI() {
        if (!walking)
            passiveTimer -= Main.dTime();
        else {
            position.add(Vector2.magnitudeDirection(position, walkTo).scale(speed));
        }

        if (passiveTimer < 0) {
            walking = true;
            passiveTimer = DeltaMath.range(0.25f, 0.75f);
            walkTo = spawnPosition.addClone(DeltaMath.range(0, 60), DeltaMath.range(0, 60));
        }

        if (Vector2.distance(position, walkTo) < 12.5f) {
            walking = false;
        }
    }

    public void handleDrops() {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(ItemList.stringItem, 1));
        new GroundItem(position, drops);
    }
}
