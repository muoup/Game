package com.Game.Items.Armor.Rock;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;
import com.Util.Other.SpriteSheet;

public class ArmorPiece extends Item {
    public ArmorPiece(int id, ArmorType type, int tier, String name, String examineText, int maxStack, int worth) {
        super(id, SpriteSheet.armorSheet.getCell( (int) (tier / 10.0), type.ordinal()), name, examineText, maxStack, worth);
        setTier(tier);

        switch (type) {
            case helmet:
                equipStatus = AccessoriesManager.HELMET_SLOT;
                break;
            case chestplate:
                equipStatus = AccessoriesManager.CHESTPLATE_SLOT;
                break;
            case leggings:
                equipStatus = AccessoriesManager.LEGGING_SLOT;
                break;
            case boots:
                equipStatus = AccessoriesManager.BOOT_SLOT;
                break;
            default:
                System.err.println(type.name() + " is not a supported armor piece");
                break;
        }
    }

    // TODO: Armor tier calculation.
    public void setTier(int tier) {

    }
}

