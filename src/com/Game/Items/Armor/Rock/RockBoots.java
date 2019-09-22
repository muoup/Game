package com.Game.Items.Armor.Rock;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;

public class RockBoots extends Item {
    public RockBoots(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.BOOT_SLOT;
        this.armor = 5f;
    }
}
