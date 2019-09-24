package com.Game.Items.RawResource;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.Item;
import com.Game.Items.ItemList;

public class ArrowShaft extends Item {
    public ArrowShaft(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        options.add("Combine");
    }

    public void OnClick(int index) {
        int amount = InventoryManager.itemCount(ItemList.feather);
        amount = Math.min(15, amount);
        amount = Math.min(amount, InventoryManager.getStack(index).getAmount());
        if (amount == 0) {
            ChatBox.sendMessage("You need some arrow shafts to do this.");
            return;
        }
        InventoryManager.removeItem(ItemList.feather, amount);
        InventoryManager.removeItem(ItemList.arrowShaft, amount);
        InventoryManager.addItem(ItemList.arrow, amount);
    }
}
