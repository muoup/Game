package com.Game.World;

import com.Game.Main.Main;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.image.BufferedImage;

public class World {

    private static World[] worlds = {
            new MainWorld() // Normal Overworld
    };

    public static World curWorld = worlds[0];

    public Vector2 size;
    public Vector2 offset;

    public BufferedImage worldImage;
    public BufferedImage colWorld;

    public World() {
        offset = Vector2.zero();
        size = Vector2.zero();
    }

    // Set the world size for initialization of a world
    public void setSize(int x, int y) {
        size = new Vector2(x, y);
    }

    // Change the offset of the world, for moving the player or scrolling the screen.
    public void changeOffset(Vector2 change) {
        offset.add(change);
    }

    public static void changeWorld(int worldIndex) {
        curWorld = worlds[worldIndex];
        curWorld.initWorld();
    }

    // Set the offset of world, possibly for teleportation.
    public void setOffset(Vector2 set) {
        offset = set;
    }

    // Void for instantiating npcs, items, etc.
    public void initWorld() {

    }

    public void initImage(String name) {
        worldImage = Main.main.getImageFromRoot(name);
        colWorld = Main.main.getImageFromRoot("col_" + name);

        this.size = new Vector2(worldImage.getWidth(), worldImage.getHeight());
    }

    // Render the buffered image of the world, this will be called in the methods.java generally
    public void renderWorld() {
        BufferedImage subImage = worldImage.getSubimage((int) World.curWorld.offset.x / Settings.worldScale, (int)World.curWorld.offset.y / Settings.worldScale,
                (int) Settings.curResolution().x / Settings.worldScale, (int)Settings.curResolution().y / Settings.worldScale);

        Render.drawImage(subImage.getScaledInstance((int) Settings.curResolution().x, (int) Settings.curResolution().y, 0), Vector2.zero());
    }
}
