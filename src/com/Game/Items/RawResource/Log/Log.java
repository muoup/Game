package com.Game.Items.RawResource.Log;

import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;

public class Log extends Item {
    protected ItemList bow;
    protected int arrowShaft = 15;

    public Log(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
    }

    public void ClickIdentities(int index) {
        // Craft Bow
        replaceInventory(index, new ItemStack((bow == null) ? ItemList.bow : bow, 1, 0));
    }

    public void OnRightClick(int index, int option) {
        switch (option) {
            case 1:
                // Craft Arrow Shafts
                replaceInventory(index, new ItemStack(ItemList.arrowShaft, arrowShaft));
                break;
        }
    }

    public void setData(ItemStack stack) {
        stack.options.clear();
        stack.options.add("Craft Bow");
        stack.options.add("Craft Arrow Shafts");
    }
}
