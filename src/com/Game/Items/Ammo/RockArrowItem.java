package com.Game.Items.Ammo;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;
import com.Game.Projectile.RockArrow;
import com.Util.Math.Vector2;

public class RockArrowItem extends Item {

    public RockArrowItem(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.AMMO_SLOT;
    }

    public void createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
        new RockArrow(position, direction, 2.5f * damageMultiplier, 2f, expMultiplier, true);
    }
}
