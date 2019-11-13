package com.Game.Items.RawResource.Log;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemRequirement;
import com.Game.Items.ItemStack;

public class Log extends Item {
    protected ItemList bow;
    protected int arrowShaft = 15;

    public Log(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        setTier(1);
    }

    public void setTier(int tier) {
        requirement = new ItemRequirement(Skills.FLETCHING, tier);
    }

    public void ClickIdentities(int index) {
        // Craft Bow
        if (requirement.meetsRequirement()) {
            replaceInventory(index, new ItemStack((bow == null) ? ItemList.bow : bow, 1, 0));
            Skills.addExperience(Skills.FLETCHING, 25 * (1 + requirement.getLevel() / 5));
        } else {
            ChatBox.sendMessage(requirement.toString());
        }
    }

    public void OnRightClick(int index, int option) {
        switch (option) {
            case 1:
                // Craft Arrow Shafts
                if (requirement.meetsRequirement()) {
                    replaceInventory(index, new ItemStack(ItemList.arrowShaft, arrowShaft));
                    Skills.addExperience(Skills.FLETCHING, 20 * (1 + requirement.getLevel() / 5));
                } else {
                    ChatBox.sendMessage(requirement.toString());
                }
                break;
        }
    }

    public void setData(ItemStack stack) {
        stack.options.clear();
        stack.options.add("Craft Bow");
        stack.options.add("Craft Arrow Shafts");
    }

    public String getOptionText(int i, int data, ItemStack stack) {
        return "Craft " + stack.name + " Bow";
    }
}
