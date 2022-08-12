package com.Game.World;

import com.Game.Entity.Enemy;
import com.Game.Entity.NPC;
import com.Game.Entity.Player;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Networking.PlayerObject;
import com.Game.Object.GameObject;
import com.Game.Object.GroundItem;
import com.Game.Projectile.Projectile;
import com.Util.Math.DeltaMath;
import com.Util.Math.Rect2;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;
import com.Util.Other.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class World {

    private static final int miniMapSize = 1024;
    private static final int miniMapScale = 256;

    public static Vector2 size;
    public static Vector2 offset;

    public static BufferedImage worldImage;
    public static BufferedImage colWorld;

    public static Rect2 minimap;

    public static ArrayList<Runnable> todo = new ArrayList<>();

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

        MethodHandler.npcs.clear();
        MethodHandler.projectiles.clear();
        MethodHandler.objects.clear();
        MethodHandler.groundItems.clear();
        MethodHandler.enemies.clear();

        minimap = new Rect2(Settings.screenSize().x - miniMapScale - 40, 40,
                miniMapScale, miniMapScale);

        Player.handleOffset();
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
        Vector2 scale = new Vector2(Settings.screenSize().x / Settings.worldScale, Settings.screenSize().y / Settings.worldScale);

        if (start.x + scale.x > worldImage.getWidth() || start.y + scale.y > worldImage.getHeight()) {
            return;
        }

        BufferedImage subImage = worldImage.getSubimage((int) start.x, (int) start.y, (int) scale.x, (int) scale.y);

        BufferedImage imageMap = Render.getScaledImage(subImage, Settings.screenSize());

        Render.drawImage(imageMap, Vector2.zero());
    }

    public static void renderMiniMap() {
        if (worldImage == null)
            return;

        Vector2 pos = getPos();

        BufferedImage imageMap = worldImage.getSubimage((int) pos.x / Settings.worldScale, (int) pos.y / Settings.worldScale,
                miniMapSize * 2 / Settings.worldScale, miniMapSize * 2 / Settings.worldScale);
        imageMap = Render.getScaledImage(imageMap, minimap.getWidth(), minimap.getHeight());

        Render.setColor(Color.BLACK);

        Render.drawRectangle(minimap.getX() - 4, minimap.getY() - 4, minimap.getWidth() + 8, minimap.getHeight() + 8);
        Render.drawImage(imageMap, minimap.getX(), minimap.getY());

        todo.forEach(Runnable::run);
        todo.clear();
    }

    public static Vector2 getPos() {
        Vector2 offset = World.offset.addClone(Settings.screenSize().scaleClone(0.5f));

        return new Vector2(DeltaMath.minpositive(offset.x - miniMapSize, worldImage.getWidth() * Settings.worldScale - miniMapSize * 2),
                DeltaMath.minpositive(offset.y - miniMapSize, worldImage.getHeight() * Settings.worldScale - miniMapSize * 2));
    }

    public static void renderEnemy(Vector2 position) {
        Vector2 pos = getPos();
        Rect2 render = new Rect2(worldPosToMiniMap(pos), miniMapScale, miniMapScale);
        Rect2 enemy = new Rect2(worldPosToMiniMap(position.addClone(-2)), 4, 4);

        if (render.overlaps(enemy)) {
            todo.add(() -> {
                Render.setColor(Color.RED);
                Render.drawCircleWithinBounds(enemy.getX(), enemy.getY(), 4, 4, minimap);
            });
        }
    }

    public static void renderObject(Vector2 position, BufferedImage image, Vector2 scale) {
        Vector2 imageScale = scale.scaleClone(1 / wtmConst());

        if (imageScale.x < 1 || imageScale.y < 1)
            return;

        BufferedImage imageMap = Render.getScaledImage(image, imageScale);
        Vector2 pos = getPos();
        Rect2 render = new Rect2(worldPosToMiniMap(pos), miniMapScale, miniMapScale);
        Rect2 enemy = new Rect2(worldPosToMiniMap(position.addClone(imageScale.scaleClone(0.5f))), imageScale);

        if (render.overlaps(enemy)) {
            todo.add(() -> Render.drawImageWithinBounds(imageMap, enemy.getX(), enemy.getY(), minimap));
        }
    }

    public static Vector2 worldPosToMiniMap(Vector2 position) {
        Vector2 pos = getPos();
        return new Vector2((position.x - pos.x) / wtmConst() + minimap.getX(),
                (position.y - pos.y)  / wtmConst() + minimap.getY());
    }

    public static Vector2 worldPosToMiniMap(float x, float y) {
        return worldPosToMiniMap(new Vector2(x, y));
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

    public static void spawnNPC(String message) {
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

        new NPC(position, image);
    }

    public static void spawnEnemy(String message) {
        String[] parts = message.split(";");

        Vector2 position = Vector2.fromString(parts[0]);
        Sprite image = Sprite.identifierSprite(parts[1]);
        int token = Integer.parseInt(parts[2]);
        String[] healths = parts[3].split(" ");
        String name = parts[4];

        float health = Float.parseFloat(healths[0]);
        float maxHealth = Float.parseFloat(healths[1]);

        for (GameObject object : MethodHandler.objects) {
            if (Vector2.distance(object.position, position) < 2) {
                MethodHandler.objects.remove(object);
                break;
            }
        }

        new Enemy(position, image, token, health, maxHealth, name);
    }

    public static void updateEnemy(String message) {
        String[] parts = message.split(";");

        int token = Integer.parseInt(parts[0]);
        String variable = parts[1];
        String[] values = parts[2].split(" ");

        Enemy enemy = findEnemy(token);

        if (enemy == null) {
            System.err.println("Command: " + variable);
            System.err.println("Enemy is null with token: " + token);
            System.err.println(message);
            return;
        }

        if (!variable.equalsIgnoreCase("Enabled") && !enemy.enabled) {
            System.err.println("mhm mhm mhm: " + token);
            System.err.println("bad variable: " + variable);
            System.err.println(message);
            return;
        }

        try {
            switch (variable) {
                case "MoveTo":
                    Vector2 position = new Vector2(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
                    Vector2 moveTo = new Vector2(Float.parseFloat(values[2]), Float.parseFloat(values[3]));

                    enemy.setMoveTo(position, moveTo, Float.parseFloat(values[4]));
                    break;
                case "Health":
                    enemy.health = Float.parseFloat(values[0]);
                    break;
                case "MaxHealth":
                    enemy.maxHealth = Float.parseFloat(values[0]);
                    break;
                case "Enabled":
                    enemy.enabled = Boolean.parseBoolean(values[0]);
                    break;
                case "Position":
                    enemy.position = Vector2.fromString(parts[2]);
                    break;
            }
        } catch (Exception e){
            System.err.println("Error Enemy Change Packet: " + message);
            e.printStackTrace();
        }
    }

    public static Enemy findEnemy(int token) {
        for (Enemy enemy : MethodHandler.enemies) {
            if (enemy.token == token) {
                return enemy;
            }
        }

        return null;
    }

    public static void spawnGroundItems(String message) {
        GroundItem groundItem = getItem(message);

        MethodHandler.groundItems.add(groundItem);
    }

    public static void updateGroundItem(String message) {
        GroundItem groundItem = getItem(message);

        for (GroundItem item : MethodHandler.groundItems) {
            if (item.randomToken == groundItem.randomToken) {
                item.set(groundItem);
                return;
            }
        }
    }

    public static GroundItem getItem(String message) {
        String[] parts = message.split("=items>");

        String[] info = parts[0].split(";");
        String[] items = parts[1].split("=>");

        Vector2 position = Vector2.fromString(info[0]);
        int token = Integer.parseInt(info[1]);

        GroundItem groundItem = new GroundItem(position);

        for (String item : items) {
            groundItem.addStack(ItemData.getFromPacketData(item));
        }

        groundItem.randomToken = token;

        return groundItem;
    }

    public static void removeGroundItem(String message) {
        int token = Integer.parseInt(message);

        for (GroundItem item : MethodHandler.groundItems) {
            if (item.randomToken == token) {
                MethodHandler.groundItems.remove(item);
                return;
            }
        }
    }

    public static void removePlayer(String message) {
        for (PlayerObject player : MethodHandler.playerConnections) {
            if (player.getUsername().equalsIgnoreCase(message)) {
                MethodHandler.playerConnections.remove(player);
                return;
            }
        }
    }

    public static void chickenCheck(String message) {
        String[] parts = message.split(";");
        Vector2 position = Vector2.fromString(parts[0]);
        Vector2 moveTo = Vector2.fromString(parts[1]);
        Vector2 movement = Vector2.fromString(parts[2]);

        for (Enemy enemy : MethodHandler.enemies) {
            /* Debug */ System.out.println("Distance: " + Vector2.distance(enemy.position, position));
            /* Debug */ System.out.println("Movement Error: " + Vector2.distance(enemy.movement, movement));
            /* Debug */ System.out.println("MoveTo Error: " + Vector2.distance(enemy.moveTo, moveTo));
        }
    }

    public static void projectilePonderance(String message) {
        Vector2 position = Vector2.fromString(message);

        for (Projectile projectile : MethodHandler.projectiles) {
            /* Debug */ System.out.println("Projectile Distance: " + Vector2.distance(projectile.position, position));
        }
    }

    public static float wtmConst() {
        return Settings.worldScale * miniMapSize / miniMapScale;
    }
}
