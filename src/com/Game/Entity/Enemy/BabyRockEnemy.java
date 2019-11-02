package com.Game.Entity.Enemy;

import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.Projectile.RockPellet;
import com.Game.World.GroundItem;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;

import java.util.ArrayList;

public class BabyRockEnemy extends Enemy {
    float piecePercent = 1f;
    private float speed = 1.25f;
    private float maxDistance = 80f;
    private float timer = 0f;

    public BabyRockEnemy(int x, int y) {
        super(x, y);
        this.image = getImage("babyRock.png");
        this.respawnTimer = 5f;
        this.maxTarget = 7.5f;
        this.name = "Baby Rock";
        setMaxHealth(100);
    }

    public void AI() {
        timer += Main.dTime();
        if (Vector2.distance(Main.player.position, position) > maxDistance) {
            position.add(Vector2.magnitudeDirection(position, getPlayer()).scale(speed));
        }

        if (timer > 0.45f) {
            new RockPellet(position, Main.player.position, 25, speed * 2.35f, 0, false);
            new RockPellet(position, Main.player.position.addClone(32, 0), 25, speed * 2.35f, 0, false);
            new RockPellet(position, Main.player.position.addClone(-32, 0), 25, speed * 2.35f, 0, false);
            timer = 0;
        }
    }

    public void handleDrops() {
        ArrayList stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(ItemList.rockArrow, (int) DeltaMath.range(1, 10)));
        float rand = DeltaMath.range(0, 100);
        while (rand <= piecePercent) {
            stacks.add(new ItemStack(ItemList.values()[Math.round(DeltaMath.range(10, 13))], 1));

            rand = DeltaMath.range(0, 100);
        }

        GroundItem.createGroundItem(position, stacks);
    }
}
