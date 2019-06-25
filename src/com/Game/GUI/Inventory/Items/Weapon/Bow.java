package com.Game.GUI.Inventory.Items.Weapon;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Projectile.Bullet;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;

public class Bow extends Item {
    public Bow() {
        super(2, "bow.png", "Get ready for the power of my bow!", 1);

        this.equipStatus = AccessoriesManager.WEAPON_SLOT;
    }

    public void OnClick(int index) {
        System.out.println("OnClick");
        equipItem(index, AccessoriesManager.WEAPON_SLOT);
    }

    public void createProjectile(Vector2 position, Vector2 direction) {
        new Bullet(position, direction, new Vector2(8, 8), 10, 4, true);
    }
}
