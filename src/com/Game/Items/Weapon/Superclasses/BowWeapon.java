package com.Game.Items.Weapon.Superclasses;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.ItemRequirement;
import com.Game.Items.ItemSets;
import com.Game.Items.Weapon.Weapon;

/**
 * The superclass for bow weapons. Extend if creating a new bow item.
 */
public class BowWeapon extends Weapon {
    public BowWeapon(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        this.itemSet = ItemSets.arrows;
        this.equipStatus = AccessoriesManager.WEAPON_SLOT;
    }

    // TODO: Find an algorithm to make a damage and accuracy from weapon tier.
    /**
     * Sets the weapon's damage using its tier, if the weapon is special, this method
     * is not required.
     * @param tier The tier of the weapon
     */
    public void setWeaponTier(int tier) {
        // For now this just 0.3 times the tier (including 1)
        weaponDamage = (float) (Math.floor(tier / 10) + 1) * 0.3f;
        requirement = new ItemRequirement(0, tier);
    }
}
