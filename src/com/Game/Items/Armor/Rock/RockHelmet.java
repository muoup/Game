package com.Game.Items.Armor.Rock;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;

public class RockHelmet extends Item {
    public RockHelmet(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.HELMET_SLOT;
        this.armor = 7.5f;
    }
}
