package com.Game.Entity.NPC;

import com.Game.Main.Main;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class NPC {

    // NOTE: I am not sure if the id is going to be of any use, but is it is good to have it nonetheless.
    public int id;

    protected BufferedImage image;
    public Vector2 position;

    public NPC(int id, int x, int y) {
        this.id = id;

        position = new Vector2(x, y);
        image = null;

        Main.methods.npcs.add(this);
    }

    public void update() {
        Vector2 offset = World.curWorld.offset;

        if (Input.GetKeyDown(KeyEvent.VK_E) && Vector2.distance(Main.player.pos, position) < 150) {
            onInteract();
        }

        if (position.compareTo(offset.subtractClone(Render.getDimensions(image))) == 1
                && offset.addClone(Settings.curResolution()).compareTo(position) == 1) {
            Render.drawImage(image, position.subtractClone(World.curWorld.offset));
        }

        move();
    }

    // When the npc is being interacted with, do so-and-so
    public void onInteract() {

    }

    // Update the npc's position with a movement AI
    public void move() {

    }
}
