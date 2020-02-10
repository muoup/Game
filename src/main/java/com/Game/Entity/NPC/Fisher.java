package com.Game.Entity.NPC;

import com.Game.GUI.GUI;
import com.Game.GUI.Shop.Shop;

public class Fisher extends NPC {
    public Fisher(int x, int y) {
        super(1, x, y);

        setImage("fish_npc.png");
    }

    public void onInteract() {
        GUI.enableShop(Shop.fishing);
    }
}
