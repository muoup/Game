package com.Game.Object;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;

public class Tree extends GameObject {

    public Tree(int x, int y) {
        super(x, y);

        id = 0;
    }

    public void onInteract() {
        InventoryManager.addItem(Item.wood, 1);
    }
}
