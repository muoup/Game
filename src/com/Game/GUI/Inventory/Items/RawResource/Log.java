package com.Game.GUI.Inventory.Items.RawResource;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.Main;

public class Log extends Item {
    public Log() {
        super(1, "wood.png", "The remnants of a tree.", 1);
    }

    public void OnClick(int index) {
        InventoryManager.inventory[index] = new ItemStack(Item.bow, 1);
    }
}
