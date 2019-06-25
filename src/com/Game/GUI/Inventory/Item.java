package com.Game.GUI.Inventory;

import com.Game.GUI.Inventory.Items.RawResource.Log;
import com.Game.GUI.Inventory.Items.Weapon.Bow;
import com.Game.Main.Main;
import com.Util.Math.Vector2;

import java.awt.*;

public class Item {

    public static Item empty = new Item(0, "/", "/", 0);
    public static Item wood = new Log();
    public static Item bow = new Bow();

    public int equipStatus = -1;
    public int id;
    public Image image;
    public String examineText;
    public int maxStack;

    public Item(int id, String imageName, String examineText, int maxStack) {
        this.id = id;
        this.image = (!imageName.equals("/")) ? Main.main.getImageFromRoot("items/" + imageName) : null;
        this.examineText = examineText;
        this.maxStack = maxStack;
    }

    public static ItemStack emptyStack() {
        return new ItemStack(Item.empty, 0);
    }

    public void OnClick(int index) {
    }

    public void createProjectile(Vector2 position, Vector2 direction) {

    }

    public void equipItem(int index, int slot) {
        InventoryManager.inventory[index] = AccessoriesManager.getSlot(slot);
        AccessoriesManager.setSlot(new ItemStack(this, 1), slot);
    }
}

