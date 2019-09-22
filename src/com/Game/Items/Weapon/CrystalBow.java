package com.Game.Items.Weapon;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;
import com.Game.Items.ItemSets;
import com.Util.Math.Vector2;

public class CrystalBow extends Item {

    public CrystalBow(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        this.equipStatus = AccessoriesManager.WEAPON_SLOT;
    }

    public void useWeapon(Vector2 position, Vector2 direction) {
        shoot(ItemSets.arrows, position, direction, 1.5f, 1.5f);
    }
}
