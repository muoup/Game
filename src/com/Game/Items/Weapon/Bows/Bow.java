package com.Game.Items.Weapon.Bows;

import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Items.Weapon.Superclasses.BowWeapon;

public class Bow extends BowWeapon {
    public Bow(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        setWeaponTier(1);
    }

    public void OnClick(int index) {
        if (getData(index) == 0) {
            combine(index, ItemList.bowString, 1, 1);
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
