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

    private float speed = 1.5f;
    private float maxDistance = 128f;
    private float piecePercent = 5f;
    private float timer = 0f;

    public RockEnemy(int x, int y) {
        super(x, y);
        this.maxHealth = 250f;
        this.id = 1;
        this.image = getImage("rock.png");
        this.respawnTimer = 7.5f;
        this.maxTarget = 10f;
        this.name = "Bigger Rock";
    }

    public void AI() {
        timer += Main.dTime();

        if (Vector2.distance(Main.player.position, position) > maxDistance) {
            position.add(Vector2.magnitudeDirection(position, getPlayer()).scale(speed));
        }

        if (timer > 0.65f) {
            new RockPellet(position, Main.player.position, 25, speed * 2.5f, 0, false);
            new RockPellet(position, Main.player.position.addClone(new Vector2(32, 0)), 25, speed * 2.5f, 0, false);
            new RockPellet(position, Main.player.position.addClone(new Vector2(64, 0)), 25, speed * 2.5f, 0, false);
            new RockPellet(position, Main.player.position.addClone(new Vector2(-32, 0)), 25, speed * 2.5f, 0, false);
            new RockPellet(position, Main.player.position.addClone(new Vector2(-64, 0)), 25, speed * 2.5f, 0, false);
            timer = 0;
        }
    }

    public void handleDrops() {
        ArrayList stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(ItemList.rockArrow, (int) DeltaMath.range(2, 20)));
        float rand = DeltaMath.range(0, 100);
        while (rand <= piecePercent) {
            stacks.add(new ItemStack(ItemList.values()[Math.round(DeltaMath.range(10, 13))], 1));

            rand = DeltaMath.range(0, 100);
        }

        GroundItem.createGroundItem(position, stacks);
    }
}
