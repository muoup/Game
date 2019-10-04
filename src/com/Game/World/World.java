package com.Game.World;

import com.Game.Entity.Player.Player;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Projectile.Projectile;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class World {

    private static World[] worlds = {
            new MainWorld(), // Normal Overworld
            new Underground() // Custom Underground Zone
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

    public int getWidth() {
        return worldImage.getWidth() * Settings.worldScale;
    }

    public int getHeight() {
        return worldImage.getHeight() * Settings.worldScale;
    }

    public static void changeWorld(int worldIndex) {
        curWorld = worlds[worldIndex];
        Main.player.subWorld = worldIndex;
        curWorld.resetWorld();
        Main.player.sendMovementPacket();
    }

    public static void changeWorld(World world) {
        curWorld = world;
        curWorld.resetWorld();
        Main.player.sendMovementPacket();
    }

    // Set the offset of world, possibly for teleportation.
    public void setOffset(Vector2 set) {
        offset = set;
    }

    /**
     * Is called everytime the player attempts to interact with the GameObject
     */
    protected void initWorld() {

    }

    // Resets all of the arrays so that new objects can be added in initWorld()
    public void resetWorld() {
        MethodHandler.npcs = new ArrayList();
        MethodHandler.enemies = new ArrayList();
        MethodHandler.objects = new ArrayList();
        MethodHandler.groundItems = new ArrayList();
        Player.projectiles = new ArrayList<Projectile>();

        initWorld();
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

        Render.drawImage(Render.getScaledImage(subImage, Settings.curResolution()), Vector2.zero());
    }
}
