package com.Game.GUI.Inventory.Items.Weapon;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.GUI.Inventory.Items.ItemSets;
import com.Game.Projectile.Bullet;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;

public class Bow extends Item {

    public Bow(int id, String imageName, String name, String examineText, int maxStack) {
        super(id, imageName, name, examineText, maxStack);

        this.equipStatus = AccessoriesManager.WEAPON_SLOT;
    }

    public void useWeapon(Vector2 position, Vector2 direction) {
        shoot(ItemSets.arrows, position, direction, 1f, 1f);
    }
}
