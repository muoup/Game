package com.Game.Items.Weapon.Bows;

import com.Game.Items.Weapon.Superclasses.BowWeapon;

public class Bow extends BowWeapon {
    public Bow(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        setWeaponTier(1);
    }

    public void onDataSet(int data) {
        if (data == 0) {
            setImage("unstrung_" + imageName);
        } else {
            setImage(imageName);
        }
    }
}
