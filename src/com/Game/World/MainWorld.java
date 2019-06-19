package com.Game.World;

import com.Game.Entity.NPC.TestNPC;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Object.GameObject;
import com.Game.Object.Tree;
import com.Util.Math.Vector2;

public class MainWorld extends World {
    public MainWorld() {
        size = new Vector2(5000, 5000);
        worldImage = Main.main.getImageFromRoot("worldTest.png");
    }

    public void initWorld() {
        MethodHandler.npcs.clear();
        MethodHandler.objects.clear();
        MethodHandler.projectiles.clear();

        new TestNPC(0, 250, 250);
        new Tree(500, 500);
    }
}
