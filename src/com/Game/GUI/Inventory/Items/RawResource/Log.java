package com.Game.GUI.Inventory.Items.RawResource;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.Main;

public class Log extends Item {
    public Log(int id, String imageName, String name, String examineText, int maxStack) {
        super(id, imageName, name, examineText, maxStack);

        this.options = new String[] {
            "Craft Bow",
            "Craft Arrows"
        };
    }

    public void OnClick(int index) {
        replaceInventory(index, new ItemStack(Item.bow, 1));
    }

    public void OnRightClick(int index, int option) {
        switch (option) {
            case 1:
                // Craft Arrows
                replaceInventory(index, new ItemStack(Item.arrow, 15));
                break;
        }
    }
}
