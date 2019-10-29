package com.Game.World;

import com.Game.Entity.Enemy.BabyRockEnemy;
import com.Game.Entity.Enemy.Chicken;
import com.Game.Entity.Enemy.RockEnemy;
import com.Game.Object.AreaTeleporter.CaveEntrance;
import com.Game.Object.SkillingAreas.FishingArea;
import com.Game.Object.SkillingAreas.Tree;
import com.Game.Object.SkillingAreas.TreePreset;

public class MainWorld extends World {
    public MainWorld() {
        initImage("worldTest.png");
    }

    public void initWorld() {
        // Forest Area
        new Tree(1655, 1875, TreePreset.tree);
        new Tree(1870, 1805, TreePreset.tree);
        new Tree(2057, 1834, TreePreset.tree);
        new Tree(1975, 1982, TreePreset.tree);
        new Tree(1788, 2072, TreePreset.tree);
        new Tree(1587, 2126, TreePreset.tree);
        new Tree(2225, 2049, TreePreset.tree);
        new Tree(2070, 2163, TreePreset.tree);
        new Tree(1649, 2349, TreePreset.tree);
        new Tree(2273, 1869, TreePreset.tree);
        new Tree(2191, 1728, TreePreset.tree);
        new Tree(1984, 1704, TreePreset.tree);
        new Tree(1726, 1744, TreePreset.tree);
        new Tree(1509, 1976, TreePreset.tree);
        new Tree(1400, 2237, TreePreset.tree);
        new Tree(2082, 2378, TreePreset.tree);
        new Tree(2393, 1792, TreePreset.tree);
        new Tree(2448, 2030, TreePreset.tree);
        new Tree(1854, 2309, TreePreset.ash);
        new Tree(2316, 2244, TreePreset.pine);
        new Tree(2053, 2581, TreePreset.oakTree);
        new Tree(1690, 2565, TreePreset.spruce);
        new Tree(2386, 2493, TreePreset.mapleTree);

        // Rock Enemies
        new BabyRockEnemy(3496, 4520);
        new BabyRockEnemy(3668, 4473);
        new BabyRockEnemy(3797, 4602);
        new BabyRockEnemy(3656, 4692);
        new BabyRockEnemy(3444, 4811);
        new BabyRockEnemy(3759, 4832);

        new RockEnemy(4430, 4547);
        new RockEnemy(4430, 4724);
        new RockEnemy(4430, 4951);

        new Chicken(4394, 2874);
        new Chicken(4223, 2764);
        new Chicken(4223, 2993);
        new Chicken(4532, 2993);
        new Chicken(4532, 2696);

        // Beach Area
        new FishingArea(885, 5595, FishingArea.clownFish);
        new FishingArea(971, 5697, FishingArea.clownFish);

        new FishingArea(1179, 5939, FishingArea.blueFish);
        new FishingArea(1250, 6024, FishingArea.blueFish);

        new CaveEntrance(5188, 3568, CaveEntrance.TeleType.caveEntrance); // Teleporter to underground area.
    }
}