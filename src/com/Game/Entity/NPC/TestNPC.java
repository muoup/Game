package com.Game.Entity.NPC;

import com.Game.Main.Main;

public class TestNPC extends NPC {

    public TestNPC(int id, int x, int y) {
        super(id, x, y);

        image = Main.main.getImageFromRoot("testNPC.png");
    }

    public void onInteract() {
        System.out.println("Hello traveller! How may I treat you.");
    }

    public void move() {

    }
}
