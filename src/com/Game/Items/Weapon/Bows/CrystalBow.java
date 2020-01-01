package com.Game.Items.Weapon.Bows;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.ItemSets;
import com.Game.Items.Weapon.Weapon;
import com.Util.Other.SpriteSheet;

/**
 * This item is not going to extend BowWeapon because it is not made from
 * logs, so it does not have an unstrung image, and also because it's damage
 * is not going to follow the tier system.
 */
public class CrystalBow extends Weapon {
    public CrystalBow(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        image = SpriteSheet.bowSheet.getCell(0, 3);
        itemSet = ItemSets.arrows;
        equipStatus = AccessoriesManager.WEAPON_SLOT;

        // This bow should be slightly better than the damage of a maple bow.
        weaponDamage = 3.75f;
    }
}
