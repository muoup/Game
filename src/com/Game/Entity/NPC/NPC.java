package com.Game.Entity.NPC;

import com.Game.Entity.Player.Player;
import com.Game.Main.Main;

import java.awt.*;

public class NPC {

    // NOTE: I am not sure if the id is going to be of any use, but is it is good to have it nonetheless.
    public int id;
    public int x;
    public int y;

    // Static variables -- just references of objects that are going to be used for cleaner code;
    protected Player player = Main.main.player;

    public NPC(int id, int x, int y, Image image) {

    }

    public void update(Graphics g) {

    }

    public void onInteract() {

    }

    public void move() {

    }
}
