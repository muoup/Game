package com.Game.Main;

import com.Game.Entity.NPC.NPC;
import com.Game.Entity.Player.Player;
import com.Game.Projectile.Projectile;
import com.Game.listener.Input;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MethodHandler {

    public Main main;

    public Menu settings;
    public Player player;

    public ArrayList<Projectile> projectiles;
    public ArrayList<NPC> npcs;

    public MethodHandler() {
        main = Main.main;
        projectiles = new ArrayList();
        npcs = new ArrayList();
    }

    public void update() {
        if (Input.GetKeyDown(KeyEvent.VK_ESCAPE))
            Settings.pause = !Settings.pause;

        if (Input.GetKeyDown(KeyEvent.VK_F1))
            Settings.showFPS = !Settings.showFPS;

        if (Settings.pause)
            return;

        player.update();
    }

    public void render(Graphics g) {
        // Wipe screen every tick


        if (!Settings.pause) {
            for (int i = 0; i < projectiles.size(); i++)
                projectiles.get(i).projectileUpdate(g, i);

            for (int i = 0; i < npcs.size(); i++)
                npcs.get(i).update(g);

            player.render(g);

            settings.curSelected = 0;
            settings.state = Menu.MenuState.PauseMenu;
        } else {
            // Enter Pause Menu
            settings.render(g);
            settings.update();
        }

        if (Settings.showFPS) {
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.BLACK);
            g.drawString("FPS: " + (int) Main.fps, 15, 25);
        }
    }
}
