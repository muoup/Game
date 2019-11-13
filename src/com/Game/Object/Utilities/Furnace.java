package com.Game.Object.Utilities;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Game.Object.UsableGameObject;

public class Furnace extends UsableGameObject {
    public Furnace(int x, int y) {
        super(x, y);
        image = getImage("Furnace.png");
        setScale(64, 84);

    }

    public void onRightClick() {
        options.clear();

        if (InventoryManager.getAmount(ItemList.stone, 0) >= 1)
            options.add("Smelt Stone");

        if (options.size() == 0)
            options.add("You need raw ores to smelt.");
    }

    public void onOption(int option) {
        String action = options.get(option);
        
        if (action.equals("Smelt Stone")) {
            int slot = InventoryManager.getIndex(ItemList.stone.singleStack());
            InventoryManager.setData(slot, 1);
            Skills.addExperience(Skills.SMITHING, 10);
        }
    }
}
