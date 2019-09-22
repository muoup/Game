package com.Game.World;

import com.Game.Entity.Enemy.BabyRockEnemy;
import com.Game.Entity.Enemy.RockEnemy;
import com.Game.Entity.Player.Player;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
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
        items.add(new ItemStack(ItemList.arrow, 5));

        // Forest Area
        new Tree(1655, 1875, Tree.wood);
        new Tree(1870, 1805, Tree.wood);
        new Tree(2057, 1834, Tree.wood);
        new Tree(1975, 1982, Tree.wood);
        new Tree(1788, 2072, Tree.wood);
        new Tree(1587, 2126, Tree.wood);
        new Tree(2225, 2049, Tree.wood);
        new Tree(2070, 2163, Tree.wood);
        new Tree(1649, 2349, Tree.wood);
        new Tree(2273, 1869, Tree.wood);
        new Tree(2191, 1728, Tree.wood);
        new Tree(1984, 1704, Tree.wood);
        new Tree(1726, 1744, Tree.wood);
        new Tree(1509, 1976, Tree.wood);
        new Tree(1400, 2237, Tree.wood);
        new Tree(2082, 2378, Tree.wood);
        new Tree(2393, 1792, Tree.wood);
        new Tree(2448, 2030, Tree.wood);
        new Tree(1854, 2309, Tree.maple);
        new Tree(2316, 2244, Tree.maple);
        new Tree(2053, 2581, Tree.maple);
        new Tree(1690, 2565, Tree.maple);
        new Tree(2386, 2493, Tree.maple);

        // Rock Enemies
        new BabyRockEnemy(3296, 4320);
        new BabyRockEnemy(3468, 4273);
        new BabyRockEnemy(3597, 4402);
        new BabyRockEnemy(3456, 4492);
        new BabyRockEnemy(3244, 4611);
        new BabyRockEnemy(3559, 4632);

        new RockEnemy(4430, 4547);
        new RockEnemy(4430, 4724);
        new RockEnemy(4430, 4951);

        // Beach Area
        new FishingArea(885, 5595, FishingArea.clownFish);
        new FishingArea(971, 5697, FishingArea.clownFish);

        new FishingArea(1179, 5939, FishingArea.blueFish);
        new FishingArea(1250, 6024, FishingArea.blueFish);
    }
}