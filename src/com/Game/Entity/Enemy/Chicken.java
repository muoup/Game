package com.Game.Entity.Enemy;

import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.World.GroundItem;
import com.Util.Math.DeltaMath;

import java.util.ArrayList;

public class Chicken extends Enemy {
    public Chicken(int x, int y) {
        super(x, y);
        this.id = 3;
        this.image = getImage("chicken.png");
        this.respawnTimer = 5.0f;
        this.targetTimer = 2f;
        this.name = "Chicken";
        this.speed = 2.25f;
        this.maxRadius = 200f;
        setMaxHealth(10);
        setBounds(4133, 2641,
                    4650, 3100);
        System.out.println(b1 + " " + b2);
    }

    public void passiveAI() {
        moveToAI();
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
