package com.Game.Items.Armor.Rock;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;

public class RockLeggings extends Item {
    public RockLeggings(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.LEGGING_SLOT;
        this.armor = 15f;
    }
}
