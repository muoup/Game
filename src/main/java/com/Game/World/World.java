package com.Game.World;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Object.GameObject;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;
import com.Util.Other.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public class World {

    private static final int miniMapSize = 512;
    private static final int miniMapScale = 256;

    public static Vector2 size;
    public static Vector2 offset;

    public static BufferedImage worldImage;
    public static BufferedImage colWorld;

    public static void changeWorld(int parseInt) {

    }

    public static boolean isNull() {
        return size == null || offset == null || worldImage == null || colWorld == null;
    }

    public static void setWorld(String message) {
        worldImage = Main.getImage("World/" + message);
        colWorld = Main.getImage("World/col_" + message.replace(".png", ".png"));

        size = new Vector2(worldImage.getWidth(), worldImage.getHeight());
        offset = Vector2.zero();
    }

    public static void init() {
       size = Vector2.zero();
       offset = Vector2.zero();
    }

    // Set the World size for initialization of a World
    public static void setSize(int x, int y) {
        size = new Vector2(x, y);
    }

    // Change the offset of the World, for moving the player or scrolling the screen.
    public static void changeOffset(Vector2 change) {
        offset.add(change);
    }

    public static int getWidth() {
        return worldImage.getWidth() * Settings.worldScale;
    }

    public static int getHeight() {
        return worldImage.getHeight() * Settings.worldScale;
    }

    // Set the offset of World, possibly for teleportation.
    public static void setOffset(Vector2 set) {
        offset = set;
    }

    // Resets all of the arrays so that new objects can be added in initWorld()
    public static void resetWorld() {
        MethodHandler.npcs.clear();
        MethodHandler.enemies.clear();
        MethodHandler.objects.clear();
        MethodHandler.groundItems.clear();
        MethodHandler.projectiles.clear();
    }

    // Render the buffered image of the World, this will be called in the methods.java generally
    public static void renderWorld() {
        Vector2 start = new Vector2(World.offset.x / Settings.worldScale, World.offset.y / Settings.worldScale);
        Vector2 scale = new Vector2(Settings.curResolution().x / Settings.worldScale, Settings.curResolution().y / Settings.worldScale);

        BufferedImage subImage = worldImage.getSubimage((int) start.x, (int) start.y, (int) scale.x, (int) scale.y);

        BufferedImage imageMap = Render.getScaledImage(subImage, Settings.curResolution());

        Render.drawImage(imageMap, Vector2.zero());
    }

    public static void renderMiniMap() {
        Vector2 screenSize = Settings.curResolution();
        Vector2 offset = World.offset.addClone(screenSize.scaleClone(0.5f));

        Vector2 pos = new Vector2(DeltaMath.maxmin(offset.x / Settings.worldScale - miniMapSize, 0, worldImage.getWidth() - miniMapSize * 2),
                DeltaMath.maxmin(offset.y / Settings.worldScale - miniMapSize, 0, worldImage.getHeight() - miniMapSize * 2));

        BufferedImage imageMap = worldImage.getSubimage((int) pos.x, (int) pos.y, Math.min(miniMapSize * 2, worldImage.getWidth()), Math.min(miniMapSize * 2, worldImage.getHeight()));
        imageMap = Render.getScaledImage(imageMap, Vector2.identity(miniMapScale));

        Render.setColor(Color.BLACK);

        Render.drawRectangle(screenSize.x - miniMapScale - 40, 40, imageMap.getWidth() + 8, imageMap.getHeight() + 8);
        Render.drawImage(imageMap, new Vector2(screenSize.x - miniMapScale - 36, 44));
    }

    public static void spawnObject(String message) {
        String[] parts = message.split(":");
        String[] pos = parts[0].split(" ");

        Vector2 position = new Vector2(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
        Sprite image = Sprite.identifierSprite(parts[1]);

        new GameObject(position, image);
    }

    public static void updateObject(String message) {
        String[] parts = message.split(":");
        String[] pos = parts[0].split(" ");

        Vector2 position = new Vector2(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
        Sprite image = Sprite.identifierSprite(parts[1]);

        for (GameObject object : MethodHandler.objects) {
            if (Vector2.distance(object.position, position) < 2) {
                MethodHandler.objects.remove(object);
                break;
            }
        }

        new GameObject(position, image);
    }
}
