package com.Game.World;

import com.Game.Entity.Enemy.TestBoss;
import com.Game.Entity.NPC.TestNPC;
import com.Game.Entity.Player.Player;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.MethodHandler;
import com.Game.Object.SkillingAreas.FishingArea;
import com.Game.Object.SkillingAreas.Tree;

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

        new Tree(1767, 1197);
        new Tree(1664, 1296);
        new Tree(1555, 5242);

        new TestBoss(1430, 5040);

        new FishingArea(885, 5595, FishingArea.clownFish);
        new FishingArea(971, 5697, FishingArea.clownFish);

        new FishingArea(1179, 5939, FishingArea.blueFish);
        new FishingArea(1250, 6024, FishingArea.blueFish);
    }
}