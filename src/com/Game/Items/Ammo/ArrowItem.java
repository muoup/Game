package com.Game.Items.Ammo;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;
import com.Game.Projectile.Arrow;
import com.Util.Math.Vector2;

public class ArrowItem extends Item {

    public ArrowItem(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.AMMO_SLOT;
    }

    public void createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
        new Arrow(position, direction, 10 * damageMultiplier, 2f, expMultiplier, true);
    }
}
