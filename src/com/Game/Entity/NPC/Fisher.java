package com.Game.Entity.NPC;

import com.Game.GUI.GUI;
import com.Game.GUI.Shop.Shop;
import com.Game.Main.Main;

public class Fisher extends NPC {
    public Fisher(int x, int y) {
        super(1, x, y);

        image = Main.main.getImageFromRoot("fish_npc.png");
    }

    public void onInteract() {
        GUI.currentShop = Shop.fishing;
    }
}
