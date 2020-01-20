package com.Game.Entity.NPC;

import com.Game.GUI.TextBox;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class NPC {

    // NOTE: Use id for comparing NPCs rather than comparing the objects themselves.
    public int id;

    protected BufferedImage image;
    public Vector2 position;

    public NPC(int id, int x, int y) {
        this.id = id;

        position = new Vector2(x, y);
        image = null;

        MethodHandler.npcs.add(this);
    }

    public void update() {
        if (image == null)
            return;

        if (Render.onScreen(position, image)) {
            Render.drawImage(image, position.subtractClone(World.curWorld.offset));
            if (Input.GetKeyDown(KeyEvent.VK_E)
                    && Vector2.distance(Main.player.position, position) < 150
                    && !Settings.paused()
                    && TextBox.noText()) {
                onInteract();
            }
        }

        move();
    }

    // When the npc is being interacted with, do so-and-so
    public void onInteract() {

    }

    // Update the NPC's position with a movement AI
    public void move() {

    }
}
