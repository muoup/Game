package com.Game.Entity.NPC;

import com.Game.Entity.Player.Player;
import com.Game.Main.Main;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class NPC {

    // NOTE: I am not sure if the id is going to be of any use, but is it is good to have it nonetheless.
    public int id;
    public int x;
    public int y;

    private BufferedImage image;
    public Vector2 position;

    // Static variables -- just references of objects that are going to be used for cleaner code;
    protected Player player = Main.main.player;

    public NPC(int id, int x, int y, BufferedImage image) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.image = image;

        Main.methods.npcs.add(this);
    }

    public void update(Graphics g) {
        Vector2 offset = World.curWorld.offset;
        position = new Vector2(x, y);

        if (Input.GetKeyDown(KeyEvent.VK_E) && Vector2.distance(Main.main.player.pos, new Vector2(x, y)) < 16) {
            onInteract();
        }

        if (position.compareTo(offset) == 2 && offset.compareTo(position.addClone(Settings.curResolution())) == 2) {
            g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
        }
    }

    // When the npc is being interacted with, do so-and-so
    public void onInteract() {

    }

    // Update the npc's position with a movement AI
    public void move() {

    }
}
