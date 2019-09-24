package com.Game.Items.RawResource;

import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;

public class Log extends Item {
    public Log(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        options.add("Craft Bow");
        options.add("Craft Arrow Shafts");
    }


    public void ClickIdentities(int index) {
        replaceInventory(index, new ItemStack(ItemList.bow, 1));
    }

    public void OnRightClick(int index, int option) {
        switch (option) {
            case 1:
                // Craft Arrows
                replaceInventory(index, new ItemStack(ItemList.arrowShaft, 15));
                break;
        }
    }
}
