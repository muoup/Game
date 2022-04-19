package com.Game.Main;

import com.Game.Entity.Enemy;
import com.Game.Entity.NPC;
import com.Game.Entity.Player;
import com.Game.GUI.GUI;
import com.Game.GUI.SkillPopup.SkillPopup;
import com.Game.GUI.TextBox;
import com.Game.Networking.PlayerObject;
import com.Game.Object.GameObject;
import com.Game.Object.GroundItem;
import com.Game.Projectile.Projectile;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MethodHandler {

    public Main main;

    public MenuHandler settings;

    public static ArrayList<NPC> npcs;
    public static ArrayList<GameObject> objects;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<GroundItem> groundItems;
    public static ArrayList<Projectile> projectiles;
    public static ArrayList<PlayerObject> playerConnections;
    public static ArrayList<SkillPopup> skillPopups;

    private static ArrayList[] lists;

    private static int fpsTotal = 0;
    private static int fpsAmount = 0;

    public MethodHandler() {
        main = Main.main;
        npcs = new ArrayList();
        objects = new ArrayList();
        enemies = new ArrayList();
        groundItems = new ArrayList();
        projectiles = new ArrayList();
        playerConnections = new ArrayList();
        skillPopups = new ArrayList();
    }

    public boolean canRender() {
        return !Settings.paused() || MenuHandler.getState() == MenuHandler.MenuState.TextBoxPause;
    }

    public void update() {
        if (Input.GetKeyDown(KeyEvent.VK_ESCAPE)) {
            if (!Settings.paused() && TextBox.noText())
                MenuHandler.setState(MenuHandler.MenuState.PauseMenu);
            else if (Settings.paused())
                Settings.disablePause();
        }

        if (Input.GetKeyDown(KeyEvent.VK_F1))
            Settings.showFPS = !Settings.showFPS;

        if (Settings.paused())
            return;

        Player.update();

        GUI.update();
    }

    public void render() {
        if (canRender()) {
            World.renderWorld();

            /*
             * These used to be foreach but kept returning ConcurrentModificationErrors
             * it doesn't seem worth it to fight it so they are now iterators.
             * This may lead to some items not being updated on a certain frame
             * but that should hopefully not have too large of an impact.
             */
            for (int i = 0; i < projectiles.size(); i++) {
                Projectile p = projectiles.get(i);
                p.renderProjectile();
            }
            for (int i = 0; i < playerConnections.size(); i++) {
                PlayerObject playerObject = playerConnections.get(i);
                playerObject.render();
            }
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);
                object.renderObject();
            }
            for (int i = 0; i < npcs.size(); i++) {
                npcs.get(i).render();
            }
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                // TODO: i dunno
                enemy.renderEnemy();
            }
            for (int i = 0; i < groundItems.size(); i++) {
                GroundItem groundItem = groundItems.get(i);
                groundItem.renderStack();
            }

            Player.render();

            GUI.render();
            World.renderMiniMap();

            for (int i = 0; i < skillPopups.size(); i++) {
                SkillPopup popup = skillPopups.get(i);
                popup.render();
                popup.update();
            }

            TextBox.handleTextbox();
            TextBox.handleButtonPress();

            settings.curSelected = 0;
        } else {
            // Enter Pause Menu
            settings.render();
            settings.update();
        }

        if (Settings.showFPS) {
            fpsTotal += Main.fps;
            fpsAmount++;

            Render.setFont(new Font("Arial", Font.BOLD, 16));
            Render.setColor(Color.BLACK);
            Render.drawText("FPS: " + (int) Main.fps + "     Average FPS: " + (fpsTotal / fpsAmount), 15, 25);
        }
    }
}
