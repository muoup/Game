package com.Game.Entity.NPC;

import com.Game.GUI.GUI;
import com.Game.GUI.Shop.Shop;

public class Kanuna extends NPC {
    public Kanuna(int id, int x, int y) {
        super(id, x, y);
        setImage("tribalKanuna.png");
    }

    public void onInteract() {
        GUI.enableShop(Shop.kanuna);
    }
}
