package com.Game.Items.Armor.Tribal;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;

public class AmuletOfKanuna extends Item {
    public AmuletOfKanuna(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.NECKLACE_SLOT;
        this.damageMulti = 0.06f;
    }
}
