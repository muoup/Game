package com.Game.Entity.Enemy;

import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.Main;
import com.Game.Projectile.Bullet;
import com.Game.World.GroundItem;
import com.Util.Math.Vector2;

import java.util.ArrayList;

public class TestBoss extends Enemy {

    private float speed = 2f;
    private float maxDistance = 45f;

    private float timer = 0.25f;

    public TestBoss(int x, int y) {
        super(x, y);

        this.maxHealth = 100f;
        this.id = 1;
        this.image = getImage("testBoss.png");
        this.respawnTimer = 12.5f;
        this.maxTarget = 15f;
    }

    public void AI() {
        timer -= 1 / Main.fps;
        Vector2 speed = Vector2.magnitudeDirection(position, getPlayer()).scale(2f);

        if (Vector2.distance(position, getPlayer()) > 45) {
            position.add(speed);
        }

        if (timer < 0) {
            timer = 0.25f;

            new Bullet(position, getPlayer(),16f, 5f,0, false);
        }
    }

    public void handleDrops() {
        ArrayList<ItemStack> drops = new ArrayList();
        drops.add(new ItemStack(Item.crystalBow, 1));
        drops.add(new ItemStack(Item.arrow, 250));

        new GroundItem(position, drops);
    }
}
