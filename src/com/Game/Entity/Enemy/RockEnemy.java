package com.Game.Entity.Enemy;

import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.Projectile.RockPellet;
import com.Game.World.GroundItem;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;

import java.util.ArrayList;

public class RockEnemy extends Enemy {

    private float speed = 0.5f;
    private float maxDistance = 128f;
    private float piecePercent = 1.5f;

    public RockEnemy(int x, int y) {
        super(x, y);
        this.maxHealth = 250f;
        this.id = 1;
        this.image = getImage("rock.png");
        this.respawnTimer = 7.5f;
        this.maxTarget = 10f;
    }

    public void AI() {
        if (Vector2.distance(Main.player.position, position) > maxDistance) {
            position.add(Vector2.magnitudeDirection(position, getPlayer()).scale(speed));
        } else {
            new RockPellet(position, Main.player.position,
                25, speed * 2.25f, 0, false);
        }
    }

    public void handleDrops() {
        ArrayList stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(ItemList.rockarrow, (int) DeltaMath.range(40, 75)));
        float rand = DeltaMath.range(0, 100);
        while (rand <= piecePercent) {
            stacks.add(new ItemStack(ItemList.values()[Math.round(DeltaMath.range(10, 13))], 1));

            rand = DeltaMath.range(0, 100);
        }

        new GroundItem(position, stacks);
    }
}
