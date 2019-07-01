package com.Game.GUI.Inventory.Items.Ammo;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.Item;
import com.Game.Projectile.Bullet;
import com.Util.Math.Vector2;

public class Arrow extends Item {

    public Arrow(int id, String imageName, String name, String examineText, int maxStack) {
        super(id, imageName, name, examineText, maxStack);
        this.options = new String[] {
            "Equip"
        };
    }

    public void OnClick(int index) {
        equipItem(index, AccessoriesManager.AMMO_SLOT);
    }

    public void createProjectile(Vector2 position, Vector2 direction, float damageMultiplier) {
        new Bullet(position, direction, new Vector2(8, 8), 10, 12.5f * damageMultiplier, true);
    }
}
