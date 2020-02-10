package com.Game.World;

import com.Game.Entity.NPC.Kanuna;
import com.Game.Object.AreaTeleporter.InvisibleTeleporter;

public class Tropics extends World {
    public Tropics() {
        initImage("tribal_lands.png");
    }

    protected void initWorld() {
        // Teleporter to Main Land
        new InvisibleTeleporter(1652, 3098, 500, 4100, 0);

        new Kanuna(0, 880, 1900);
    }
}
