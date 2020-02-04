package com.Game.Entity.NPC;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.GUI.TextBox;
import com.Game.Items.ItemList;
import com.Game.Main.Main;
import com.Game.Questing.QuestManager;

public class BirdWatcher extends NPC {

    public BirdWatcher(int x, int y) {
        super(0, x, y);

        image = Main.main.getImageFromRoot("testNPC.png");
    }

    public void onInteract() {
        switch (QuestManager.getData(0)) {
            case 0:
                TextBox.setChoice(() -> questStart(), () -> TextBox.clearTextBox(),
                        "I would like to help.", "Maybe later.",
                        "I need your help. Would you mind lending me a helping hand?");
                break;
            case 1:
                if (InventoryManager.itemCount(ItemList.parrotBird) >= 1)
                    onBird();
                else
                    TextBox.setText("It does not seem that you have gotten a bird, please come back when you have.");
                break;
            case 2:
                TextBox.setText("Thank you very much traveller, you will forever have my thanks!");
        }
    }

    private void onBird() {
        if (InventoryManager.isFull()) {
            TextBox.setText("It appears that you do not have any inventory space, please come back and try again.");
            return;
        }

        InventoryManager.removeItem(ItemList.parrotBird, 1);
        InventoryManager.addItem(ItemList.gold, 1000);
        Skills.addExperience(Skills.WOODCUTTING, 1000);
        TextBox.setText("Thank you very much, I will always be grateful for your deed.");
        ChatBox.sendMessage("For helping the Bird Watcher, you have recieved 1000 Coins and 500 Woodcutting experience!");
        QuestManager.setData(0, 2);
    }

    public void questStart() {
        TextBox.setText("Fantastic! I've always come by here to look at the birds"
                + "and I have always wanted one for myself, would you be able"
                + "to get one for me?");
        QuestManager.setData(0, 1);
    }

    public void move() {

    }
}
