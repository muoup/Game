package com.Game.Main;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Entity.NPC.NPC;
import com.Game.Entity.Player.Player;
import com.Game.GUI.GUI;
import com.Game.GUI.TextBox;
import com.Game.Networking.PlayerObject;
import com.Game.Object.GameObject;
import com.Game.Projectile.Projectile;
import com.Game.World.GroundItem;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MethodHandler {

    public Main main;

    public Menu settings;
    public Player player;
    //public static boolean startUp = true;

    public static ArrayList<NPC> npcs;
    public static ArrayList<GameObject> objects;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<GroundItem> groundItems;
    public static ArrayList<Projectile> projectiles;
    public static ArrayList<PlayerObject> playerConnections;

    public static ArrayList<Object> remove;

    private static ArrayList[] lists;

    public MethodHandler() {
        main = Main.main;
        npcs = new ArrayList();
        objects = new ArrayList();
        enemies = new ArrayList();
        groundItems = new ArrayList();
        projectiles = new ArrayList();
        playerConnections = new ArrayList();
        remove = new ArrayList();
        lists = new ArrayList[]{
                npcs,
                objects,
                enemies,
                groundItems,
                projectiles
        };
    }

    public void update() {
        if (Input.GetKeyDown(KeyEvent.VK_ESCAPE)) {
            if (Settings.pause && TextBox.noText())
                Main.settings.state = Menu.MenuState.PauseMenu;

            Settings.pause = !Settings.pause;
        }

        TextBox.handleTextbox();

        if (Input.GetKeyDown(KeyEvent.VK_F1))
            Settings.showFPS = !Settings.showFPS;

        if (Settings.pause)
            return;

        player.update();

        GUI.update();

        handleRemove();
    }

    /**
     * After every tick, removes the objects from their respective ArrayLists.
     */
    private static void handleRemove() {
        for (Object o : remove)
        for (ArrayList list : lists) {
            if (list.contains(o)) {
                list.remove(o);
                remove.remove(o);
                break;
            }
        }

        objects.clear();
    }

    public void render() {
        if (!Settings.pause) {
            World.curWorld.renderWorld();

            for (GroundItem groundItem : groundItems) groundItem.updateStack();
            for (Projectile p : projectiles) p.projectileUpdate();
            for (GameObject object : objects) object.updateObject();
            for (PlayerObject playerObject : playerConnections) playerObject.tick();
            for (Enemy enemy : enemies) enemy.updateEnemy();
            for (NPC npc : npcs) npc.update();

            player.render();

            GUI.render();

            settings.curSelected = 0;
        } else {
            // Enter Pause Menu
            settings.render();
            settings.update();
        }

        if (Settings.showFPS) {
            Render.setFont(new Font("Arial", Font.BOLD, 16));
            Render.setColor(Color.BLACK);
            Render.drawText("FPS: " + (int) Main.fps, 15, 25);
        }
    }
}
