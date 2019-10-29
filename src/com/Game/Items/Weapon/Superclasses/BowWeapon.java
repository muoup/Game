package com.Game.Items.Weapon.Superclasses;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Game.Items.ItemRequirement;
import com.Game.Items.ItemSets;
import com.Game.Items.ItemStack;
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
        expMultiplier = 1 + 0.125f * tier;
    }

    public void OnClick(int index) {
        if (getData(index) == 0) {
            combine(index, ItemList.bowString, 1, 1);
            Skills.addExperience(Skills.FLETCHING, 30 * (1 + requirement.getLevel() / 5));
        }
    }

    public void setData(ItemStack stack) {
        if (stack.getData() == 0) {
            stack.options.add("Craft Bow");
            stack.setImage("unstrung_" + imageName);
            stack.setEquipStatus(-1);
        } else {
            stack.setImage(imageName);
            stack.setEquipStatus(equipStatus);
        }
    }
}
