package com.Game.Items.Weapon;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.Item;
import com.Game.Items.ItemSets;
import com.Game.Items.ItemStack;
import com.Game.Projectile.Projectile;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;

public class Weapon extends Item {
    protected Projectile projectile;
    protected ItemSets itemSet;
    protected float weaponDamage;

    public Weapon(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        this.itemSet = ItemSets.none;
        this.equipStatus = AccessoriesManager.WEAPON_SLOT;
    }

    public void useWeapon(Vector2 position, Vector2 direction) {
        shoot(itemSet, position, direction, 1f, expMultiplier);
    }

    public float getMultiplier() {
        return 1f;
    }

    public void shoot(ItemSets acceptable, Vector2 position, Vector2 direction, float damage, float expMultiplier) {
        ItemStack stack = AccessoriesManager.getSlot(AccessoriesManager.AMMO_SLOT);

        if (stack.getAmount() <= 0 || stack.getID() == 0)
            return;

        for (int i : acceptable.items) {
            if (stack.getID() == i) {
                stack.getItem().createProjectile(position, direction, dmgMultiplier(damage) * getMultiplier(), expMultiplier);
                stack.amount--;
                break;
            }
        }
    }

    // TODO: Implement Weapon Accuracy
    public float dmgMultiplier(float damage) {
        return DeltaMath.range(weaponDamage * 0.9f, weaponDamage * 1.1f);
    }
}
