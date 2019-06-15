package com.Game.World;

import com.Game.Entity.NPC.TestNPC;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Util.Math.Vector2;

public class MainWorld extends World {
    public MainWorld() {
        size = new Vector2(5000, 5000);
        worldImage = Main.main.getImageFromRoot("worldTest.png");
    }

    public void initWorld() {
        MethodHandler.npcs.clear();

        MethodHandler.npcs.add(new TestNPC(0, 250, 250));
    }
}
