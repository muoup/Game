package com.Game.Object.Utilities;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Object.UsableGameObject;

public class Anvil extends UsableGameObject {

    ItemList helmet = ItemList.empty;
    ItemList chestplate = ItemList.empty;
    ItemList leggings = ItemList.empty;
    ItemList boots = ItemList.empty;
    int id[] = new int[4];

    public Anvil(int x, int y) {
        super(x, y);
        image = getImage("anvil.png");

        options.add("Craft Helmet");
        options.add("Craft Chestplate");
        options.add("Craft Leggings");
        options.add("Craft Boots");
    }

    public void onOption(int option) {
        String id = "";
        int index = -1;

        ItemStack[] inventory = InventoryManager.inventory;
        ItemStack opt = ItemList.empty.singleStack();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.getData() != 1)
                continue;
            switch (stack.item.getItemList()) {
                case stone:
                    id = "Rock";
                    helmet = ItemList.rockHelmet;
                    chestplate = ItemList.rockChestplate;
                    leggings = ItemList.rockLeggings;
                    boots = ItemList.rockBoots;
                    opt = createOption(option);
                    break;
            }
            if (!stack.meetsRequirement() && id != "") {
                ChatBox.sendMessage(stack.item.requirement.toString());
                id = "";
                continue;
            } else if (stack.meetsRequirement() && id != "" &&
                        InventoryManager.getAmount(opt.item.getItemList()) >= opt.amount) {
                index = i;
                break;
            }
        }

        if (id == "" || index == -1) {
            ChatBox.sendMessage("You need a smelted resource for this.");
            return;
        }

        InventoryManager.setItem(index, opt.singleStack());

        index = -1;
        id = "";
    }

    private ItemStack createOption(int option) {
        switch (option) {
            case 0:
                return new ItemStack(helmet, 3);
            case 1:
                return new ItemStack(chestplate, 6);
            case 2:
                return new ItemStack(leggings, 4);
            case 3:
                return new ItemStack(boots, 2);
            default:
                return null;
        }
    }
}
