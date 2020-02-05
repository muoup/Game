package com.Game.World;

import com.Game.Entity.Enemy.Chicken;
import com.Game.Entity.NPC.BirdWatcher;
import com.Game.Entity.NPC.Fisher;
import com.Game.Object.AreaTeleporter.CaveEntrance;
import com.Game.Object.AreaTeleporter.InvisibleTeleporter;
import com.Game.Object.SkillingAreas.*;
import com.Game.Object.Utilities.Anvil;
import com.Game.Object.Utilities.Furnace;
import com.Game.Object.Utilities.StorageChest;

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

        // Rocks
        new MiningRock(3496, 4520, RockType.stone);
        new MiningRock(3668, 4473, RockType.stone);
        new MiningRock(3797, 4602, RockType.stone);
        new MiningRock(3656, 4692, RockType.copper);
        new MiningRock(3444, 4811, RockType.tin);
//        new MiningRock(3759, 4832);

        new Furnace(5042, 4382);
        new Anvil(5042, 4658);

        // Chickens
        new Chicken(4394, 2874);
        new Chicken(4223, 2764);
        new Chicken(4223, 2993);
        new Chicken(4532, 2993);
        new Chicken(4532, 2696);

        new StorageChest(3038, 3395);

        // Beach Area
        new FishingArea(885, 5595, FishingPreset.clownFish);
        new FishingArea(971, 5697, FishingPreset.clownFish);

        new FishingArea(1179, 5939, FishingPreset.blueFish);
        new FishingArea(1250, 6024, FishingPreset.blueFish);

        new FishingArea(5306, 1678, FishingPreset.seaWeed);
        new FishingArea(5103, 1604, FishingPreset.seaWeed);

        new CaveEntrance(5150, 3534, CaveEntrance.TeleType.caveEntrance); // Teleporter to underground area.

        // Quest NPC
        new BirdWatcher(2024, 2881);

        // Fisher
        new Fisher(1301, 5497);

        // Teleporter to Chess Dungeon
        new InvisibleTeleporter(5507, 1643, 135, 125, 2);

        // Teleporter to Tribal Lands
        new InvisibleTeleporter(500, 4100,1652, 3098, 3);
    }
}