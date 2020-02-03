package com.Game.Items.Ammo;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;
import com.Game.Projectile.Arrow;
import com.Game.Projectile.Projectile;
import com.Util.Math.Vector2;

public class ArrowItem extends Item {

    public ArrowItem(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
        this.equipStatus = AccessoriesManager.AMMO_SLOT;
    }

    public Projectile createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
        return new Arrow(position, direction, 1.7f * damageMultiplier, 2.5f, expMultiplier, true);
    }
}
