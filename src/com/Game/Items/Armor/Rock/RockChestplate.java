package com.Game.Items.Armor.Rock;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;

public class RockChestplate extends Item {
    public RockChestplate(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.CHESTPLATE_SLOT;
        this.armor = 20f;
    }
}
