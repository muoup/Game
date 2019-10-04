package com.Game.Items.Weapon.Bows;

import com.Game.Items.Weapon.Superclasses.BowWeapon;

public class SpruceBow extends BowWeapon {
    public SpruceBow(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        setWeaponTier(40);
    }
}