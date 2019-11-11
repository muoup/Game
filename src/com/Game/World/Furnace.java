package com.Game.World;

import com.Game.GUI.Inventory.InventoryManager;
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

        if (InventoryManager.getAmount(ItemList.stone, 0) > 1) {
            options.add("Smelt Stone");
        }
    }

    public void onOption(int option) {
        String action = options.get(option);
        
        if (action.equals("Smelt Stone")) {
            InventoryManager.findStack(ItemList.stone, 0).setData(1);
        }
    }
}
