package com.Game.Main;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Entity.NPC.NPC;
import com.Game.Entity.Player.Player;
import com.Game.GUI.GUI;
import com.Game.GUI.TextBox;
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

    public static ArrayList<GroundItem> removeGround;
    public static ArrayList<Projectile> removeProjectiles;

    public MethodHandler() {
        main = Main.main;
        npcs = new ArrayList();
        objects = new ArrayList();
        enemies = new ArrayList();
        groundItems = new ArrayList();
        removeGround = new ArrayList();
        projectiles = new ArrayList();
        removeProjectiles = new ArrayList();
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

    public void render() {
        if (!Settings.pause) {
            World.curWorld.renderWorld();

            for (GroundItem groundItem : groundItems) groundItem.updateStack();
            for (Projectile p : projectiles) p.projectileUpdate();

            player.render();

            for (Enemy enemy : enemies) enemy.updateEnemy();
            for (GameObject object : objects) object.updateObject();
            for (NPC npc : npcs) npc.update();

            GUI.render();

            if (!removeGround.isEmpty()) {
                groundItems.removeAll(removeGround);
                removeGround.clear();
            }

            if (!removeProjectiles.isEmpty()) {
                projectiles.removeAll(removeProjectiles);
                removeProjectiles.clear();
            }

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
