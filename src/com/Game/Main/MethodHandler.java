package com.Game.Main;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Entity.NPC.NPC;
import com.Game.Entity.Player.Player;
import com.Game.GUI.GUI;
import com.Game.GUI.SkillPopup.*;
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

    public static ArrayList<NPC> npcs;
    public static ArrayList<GameObject> objects;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<GroundItem> groundItems;
    public static ArrayList<Projectile> projectiles;
    public static ArrayList<PlayerObject> playerConnections;
    public static ArrayList<SkillPopup> skillPopups;
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
        skillPopups = new ArrayList();
        lists = new ArrayList[]{
                npcs,
                objects,
                enemies,
                groundItems,
                projectiles,
                skillPopups
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
    }

    /**
     * After every tick, removes the objects from their respective ArrayLists.
     */
    private static void handleRemove() {
        for (int i = 0; i < remove.size(); i++) {
            Object o = remove.get(i);
            for (ArrayList list : lists) {
                if (list.contains(o)) {
                    list.remove(o);
                    remove.remove(o);
                    break;
                }
            }
        }

        remove.clear();
    }

    public void render() {
        if (!Settings.pause) {
            World.curWorld.renderWorld();

            /*
             * These used to be foreach but kept returning ConcurrentModificationErrors
             * it doesn't seem worth it to fight it so they are now iterators.
             * This may lead to some items not being updated on a certain frame
             * but that should hopefully not have too large of an impact.
             */
            for (int i = 0; i < groundItems.size(); i++) {
                GroundItem groundItem = groundItems.get(i);
                groundItem.updateStack();
            }
            for (int i = 0; i < projectiles.size(); i++) {
                Projectile p = projectiles.get(i);
                p.projectileUpdate();
            }
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);
                object.updateObject();
            }
            for (int i = 0; i < playerConnections.size(); i++) {
                PlayerObject playerObject = playerConnections.get(i);
                playerObject.tick();
            }
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.updateEnemy();
            }
            for (int i = 0; i < npcs.size(); i++) {
                NPC npc = npcs.get(i);
                npc.update();
            }

            player.render();

            GUI.render();

            for (int i = 0; i < skillPopups.size(); i++) {
                SkillPopup popup = skillPopups.get(i);
                popup.render();
                popup.update();
            }

            settings.curSelected = 0;

            handleRemove();
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
