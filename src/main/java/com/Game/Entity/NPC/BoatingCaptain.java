package com.Game.Entity.NPC;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.TextBox;
import com.Game.Items.ItemList;
import com.Game.Items.ItemSets;
import com.Game.Questing.QuestManager;

public class BoatingCaptain extends NPC {
    public BoatingCaptain(int id, int x, int y) {
        super(id, x, y);

        setImage("captain.png");
    }

    public void onInteract() {
        int temp;

        switch (QuestManager.getData(1)) {
            case 0:
                TextBox.setChoice(() -> {
                    QuestManager.setData(1, 1); TextBox.setText("Okay, can you get me ten pieces of wood for me?");}, () -> TextBox.clearTextBox(),
                            "I can help with that", "Not right now",
                        "Can you help me out? My boat is in shambles right now and you would be of great help to me.");
                break;
            case 1:
                temp = InventoryManager.itemCount(ItemSets.wood);

                if (temp >= 10) {
                    TextBox.setText("Thank you very much for your wood, this will work as a good foundation",
                            "I don't mean to bother you more, but could you also get me about fifteen pieces of string? Thank you very much traveller.");
                    QuestManager.setData(1, 2);
                    InventoryManager.removeItem(ItemSets.wood, 10);
                } else
                    TextBox.setText("It does not seem you have what I need. Could you get me ten pieces of wood.");
                break;
            case 2:
                temp = InventoryManager.itemCount(ItemList.stringItem);

                if (temp >= 15) {
                    TextBox.setText("I knew you could do it! I now have everything I need to continue on my journey. You may now use my boat free of charge.");
                    QuestManager.setData(1, 3);
                    InventoryManager.removeItem(ItemList.stringItem, 15);
                } else
                    TextBox.setText("It does not seem you have what I need. Could you get me fifteen strings of string.");
                break;
            case 3:
                TextBox.setText("Come on now, try out my boat, it is brand new.");
                break;
            case 4:
                TextBox.setText("Thank you for everything you have done. I can now pursue my dream as a captain.");
                break;
        }
    }
}
