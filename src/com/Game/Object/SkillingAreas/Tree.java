package com.Game.Object.SkillingAreas;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.Items.RawResource.Log;
import com.Game.Main.Main;
import com.Game.Object.GameObject;

public class Tree extends GameObject {

    public Tree(int x, int y) {
        super(x, y);

        image = getImage("tree.png");
        maxDistance = 150f;
    }

    public boolean onInteract() {
        timer += 1 / Main.fps;

        drawProgressBar();

        if (timer > maxTimer) {
            timer = 0;
            InventoryManager.addItem(Item.wood, 1);
        }

        return true;
    }
}
