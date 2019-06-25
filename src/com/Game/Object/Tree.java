package com.Game.Object;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.Items.RawResource.Log;
import com.Game.Main.Main;

public class Tree extends GameObject {

    public Tree(int x, int y) {
        super(x, y);

        id = 0;
    }

    public void onInteract() {
        timer += 1 / Main.fps;

        drawProgressBar();

        if (timer > maxTimer) {
            timer = 0;
            InventoryManager.addItem(Item.wood, 1);
        }
    }
}
