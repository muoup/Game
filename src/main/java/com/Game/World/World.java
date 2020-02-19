package com.Game.World;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class World {

    private static final int miniMapSize = 512;
    private static final int miniMapScale = 256;

    private static World[] worlds = {
            new MainWorld(), // Normal Overworld
            new Underground(), // Underground Zone
            new ChessDungeon(), // Probably not permanent
            new Tropics() // Tribal Lands with Natives to the Island
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

    // Set the offset of world, possibly for teleportation.
    public void setOffset(Vector2 set) {
        offset = set;
    }

    /**
     * Is called every time the player attempts to interact with the GameObject
     */
    protected void initWorld() {

    }

    // Resets all of the arrays so that new objects can be added in initWorld()
    public void resetWorld() {
        MethodHandler.npcs.clear();
        MethodHandler.enemies.clear();
        MethodHandler.objects.clear();
        MethodHandler.groundItems.clear();
        MethodHandler.projectiles.clear();

        initWorld();
    }

    public void initImage(String name) {
        worldImage = Main.main.getImageFromRoot(name);
        colWorld = Main.main.getImageFromRoot("col_" + name);

        this.size = new Vector2(worldImage.getWidth(), worldImage.getHeight());
    }

    // Render the buffered image of the world, this will be called in the methods.java generally
    public void renderWorld() {
        Vector2 start = new Vector2(World.curWorld.offset.x / Settings.worldScale, World.curWorld.offset.y / Settings.worldScale);
        Vector2 scale = new Vector2(Settings.curResolution().x / Settings.worldScale, Settings.curResolution().y / Settings.worldScale);

        BufferedImage subImage = worldImage.getSubimage((int) start.x, (int) start.y, (int) scale.x, (int) scale.y);

        BufferedImage imageMap = Render.getScaledImage(subImage, Settings.curResolution());

        Render.drawImage(imageMap, Vector2.zero());
    }

    public void renderMiniMap() {
        Vector2 screenSize = Settings.curResolution();
        Vector2 offset = World.curWorld.offset.addClone(screenSize.scaleClone(0.5f));

        Vector2 pos = new Vector2(DeltaMath.maxmin(offset.x / Settings.worldScale - miniMapSize, 0, worldImage.getWidth() - miniMapSize * 2),
                DeltaMath.maxmin(offset.y / Settings.worldScale - miniMapSize, 0, worldImage.getHeight() - miniMapSize * 2));

        BufferedImage imageMap = worldImage.getSubimage((int) pos.x, (int) pos.y, Math.min(miniMapSize * 2, worldImage.getWidth()), Math.min(miniMapSize * 2, worldImage.getHeight()));
        imageMap = Render.getScaledImage(imageMap, Vector2.identity(miniMapScale));

        Render.setColor(Color.BLACK);

        Render.drawRectangle(screenSize.x - miniMapScale - 40, 40, imageMap.getWidth() + 8, imageMap.getHeight() + 8);
        Render.drawImage(imageMap, new Vector2(screenSize.x - miniMapScale - 36, 44));
    }
}
