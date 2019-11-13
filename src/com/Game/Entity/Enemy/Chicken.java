package com.Game.Entity.Enemy;

import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.World.GroundItem;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;

import java.util.ArrayList;

public class Chicken extends Enemy {
    private Vector2 moveTo;
    private Vector2 movement;
    private float maxRadius = 140;
    private float speed = 1f;

    public Chicken(int x, int y) {
        super(x, y);
        this.id = 3;
        this.image = getImage("chicken.png");
        this.respawnTimer = 5.0f;
        this.targetTimer = 2.5f;
        this.name = "Chicken";
        setMaxHealth(10);
        setMoveTo();
    }

    public void passiveAI() {
        if (moveTo == null)
            return;

        if (Vector2.distance(position, moveTo) < 32) {
            setMoveTo();
        } else {
            position.add(movement.scaleClone(speed));
        }
    }

    public void setMoveTo() {
        moveTo = spawnPosition.addClone(DeltaMath.range(-maxRadius, maxRadius), DeltaMath.range(-maxRadius, maxRadius));
        movement = Vector2.magnitudeDirection(position, moveTo);
    }

    public void onRespawn() {
        setMoveTo();
    }

    public void handleDrops() {
        ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
        stack.add(new ItemStack(ItemList.feather, (int) DeltaMath.range(4, 9)));
        GroundItem.createGroundItem(position, stack);
    }
}
