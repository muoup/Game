package com.Game.Main;

import com.Game.Entity.NPC.NPC;
import com.Game.Entity.Player.Player;
import com.Game.GUI.TextBox;
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

    public Menu settings;
    public Player player;

    public static ArrayList<Projectile> projectiles;
    public static ArrayList<NPC> npcs;

    public MethodHandler() {
        main = Main.main;
        projectiles = new ArrayList();
        npcs = new ArrayList();
    }

    public void update() {
        TextBox.handleTextbox();

        if (Input.GetKeyDown(KeyEvent.VK_ESCAPE)) {
            if (Settings.pause && TextBox.noText())
                Main.settings.state = Menu.MenuState.PauseMenu;

            Settings.pause = !Settings.pause;
        }

        if (Input.GetKeyDown(KeyEvent.VK_F1))
            Settings.showFPS = !Settings.showFPS;

        if (Settings.pause)
            return;

        player.update();
    }

    public void render() {
        if (!Settings.pause) {

            World.curWorld.renderWorld();
            for (int i = 0; i < projectiles.size(); i++)
                projectiles.get(i).projectileUpdate(i);

            for (int i = 0; i < npcs.size(); i++)
                npcs.get(i).update();


            player.render();

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
