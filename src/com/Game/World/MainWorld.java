package com.Game.World;

import com.Game.Entity.Enemy.TestBoss;
import com.Game.Entity.Enemy.TestEnemy;
import com.Game.Entity.NPC.TestNPC;
import com.Game.Entity.Player.Player;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Object.GameObject;
import com.Game.Object.Tree;
import com.Util.Math.Vector2;

import java.util.ArrayList;

public class MainWorld extends World {
    public MainWorld() {
        initImage("worldTest.png");
    }

    public void initWorld() {
        MethodHandler.npcs.clear();
        MethodHandler.objects.clear();
        MethodHandler.groundItems.clear();
        Player.projectiles.clear();

        ArrayList<ItemStack> items = new ArrayList();
        items.add(new ItemStack(Item.arrow, 5));

        new TestNPC(250, 250);
        new Tree(350 , 100);
        new TestBoss(1250, 1250);
        new GroundItem(100, 100, items);
    }
}