package com.Game.GUI.Inventory.Items.RawResource;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemList;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.Main;

public class Log extends Item {
    public Log(int id, String imageName, String name, String examineText, int maxStack) {
        super(id, imageName, name, examineText, maxStack);

        options.add("Craft Bow");
        options.add("Craft Arrows");
    }

    public void ClickIdentities(int index) {
        replaceInventory(index, new ItemStack(ItemList.bow, 1));
    }

    public void OnRightClick(int index, int option) {
        switch (option) {
            case 1:
                // Craft Arrows
                replaceInventory(index, new ItemStack(ItemList.arrow, 15));
                break;
        }
    }
}
