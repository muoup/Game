package com.Game.GUI.Inventory.Items.Weapon;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Projectile.Bullet;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;

public class CrystalBow extends Item {

    public CrystalBow(int id, String imageName, String name, String examineText, int maxStack) {
        super(id, imageName, name, examineText, maxStack);

        this.options = new String[]{
                "Equip"
        };

        this.equipStatus = AccessoriesManager.WEAPON_SLOT;
    }

    public void OnClick(int index) {
        equipItem(index, AccessoriesManager.WEAPON_SLOT);
    }

    public void useWeapon(Vector2 position, Vector2 direction) {
        ItemStack stack = AccessoriesManager.getSlot(AccessoriesManager.AMMO_SLOT);

        if (stack.amount <= 0 || stack.getID() == 0)
            return;

        int[] correctIDs = {
                3
        };

        for (int i : correctIDs) {
            if (stack.getID() == i) {
                stack.item.createProjectile(position, direction, 2.5f);
                stack.amount--;
                break;
            }
        }
    }
}
