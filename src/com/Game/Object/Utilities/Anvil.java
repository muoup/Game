package com.Game.Object.Utilities;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Object.UsableGameObject;

import java.util.HashSet;

public class Anvil extends UsableGameObject {

    ItemList helmet = ItemList.empty;
    ItemList chestplate = ItemList.empty;
    ItemList leggings = ItemList.empty;
    ItemList boots = ItemList.empty;
    ItemList dagger = ItemList.empty;
    float experience = 0;

    public Anvil(int x, int y) {
        super(x, y);
        image = getImage("anvil.png");

        options.add("Craft Helmet");
        options.add("Craft Chestplate");
        options.add("Craft Leggings");
        options.add("Craft Boots");
        options.add("Craft Dagger");
    }

    public void onOption(int option) {
        ItemStack opt;
        HashSet<String> messages = new HashSet();
        for (ItemStack stack : InventoryManager.inventory) {
            ItemList itemList = stack.getItemList();

            switch (itemList) {
                case stone:
                    helmet = ItemList.rockHelmet;
                    chestplate = ItemList.rockChestplate;
                    leggings = ItemList.rockLeggings;
                    boots = ItemList.rockBoots;
                    dagger = ItemList.stoneDagger;
                    experience = 25f;
                    opt = createOption(option);
                    break;
                default:
                    continue;
            }

            if (InventoryManager.getAmount(itemList, 1) >= opt.amount) {
                if (stack.meetsRequirement()) {
                    craftItem(opt, itemList);
                    return;
                } else {
                    messages.add("You need level " + stack.requirement.getLevel() + " Smithing to smith with " + stack.name);
                }
            } else {
                messages.add("You need " + opt.getAmount() + " " + stack.name + " to make this piece.");
            }
        }

        messages.forEach(ChatBox::sendMessage);
    }

    private void craftItem(ItemStack option, ItemList remove) {
        InventoryManager.removeItem(new ItemStack(remove, option.getAmount(), 1));
        InventoryManager.addItem(option.singleStack());
        Skills.addExperience(Skills.SMITHING, option.getAmount() * experience);
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
            case 4:
                return new ItemStack(dagger, 3);
            default:
                return null;
        }
    }
}
