package com.Game.GUI.Inventory;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.Items.Ammo.ArrowItem;
import com.Game.GUI.Inventory.Items.Consumables.Food.BlueFishFood;
import com.Game.GUI.Inventory.Items.Consumables.Food.ClownFishFood;
import com.Game.GUI.Inventory.Items.RawResource.Log;
import com.Game.GUI.Inventory.Items.RawResource.MapleLog;
import com.Game.GUI.Inventory.Items.Weapon.Bow;
import com.Game.GUI.Inventory.Items.Weapon.CrystalBow;
import com.Game.GUI.RightClick;
import com.Game.GUI.TextBox;
import com.Game.Main.Main;
import com.Util.Math.Vector2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Item {

    public static Item empty = new Item(0, "/", "/", "/", 0);
    public static Item wood = new Log(1, "wood.png", "Log", "The remnants of a tree.", 1);
    public static Item bow = new Bow(2, "bow.png", "Bow","Get ready for the power of my bow!", 1);
    public static Item arrow = new ArrowItem(3, "arrow.png", "Arrow", "Some sharp arrows!", 100000);
    public static Item crystalBow = new CrystalBow(4, "crystalBow.png", "Crystal Bow", "This is really gonna hurt.", 1);
    public static Item clownfish = new ClownFishFood(5, "clownfish.png", "Clownfish", "This looks quite tasty.", 1);
    public static Item bluefish = new BlueFishFood(6, "bluefish.png", "Bluefish", "This looks very tasty.", 1);
    public static Item mapleLog = new MapleLog(7, "maplewood.png", "Maple Log", "A sticky log, sounds useful to me.", 1);

    public static Item[] itemList = {
            empty,
            wood,
            bow,
            arrow,
            crystalBow,
            clownfish,
            bluefish,
            mapleLog
    };

    public ArrayList<String> options;

    public int equipStatus = -1;
    public int id;
    public String name;
    public BufferedImage image;
    public String examineText;
    public int maxStack;

    public Item(int id, String imageName, String name, String examineText, int maxStack) {
        this.id = id;
        this.image = (!imageName.equals("/")) ? Main.main.getImageFromRoot("items/" + imageName) : null;
        this.examineText = examineText;
        this.maxStack = maxStack;
        this.name = name;
        this.options = new ArrayList<String>();
    }

    public static ItemStack emptyStack() {
        return new ItemStack(Item.empty, 0);
    }

    public void ClickIdentities(int index) {
        if (equipStatus != -1) {
            equipItem(index, equipStatus);
        }

        OnClick(index);
    }

    public void OnClick(int index) {

    }

    public void RightClickIdentities(int index, int option) {
        if (option == 0) {
            ClickIdentities(index);
        }

        if (option == RightClick.options.size() - 1)
            ChatBox.sendMessage(examineText);

        if (option == RightClick.options.size() - 2)
            InventoryManager.inventory[index] = emptyStack();

        OnRightClick(index, option);
    }

    public void OnRightClick(int index, int option) {

    }

    public void useWeapon(Vector2 position, Vector2 direction) {

    }

    public void createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {

    }

    public void equipItem(int index, int slot) {
        ItemStack slotStack = AccessoriesManager.getSlot(slot);
        ItemStack inventoryStack = InventoryManager.inventory[index];

        if (slotStack.getID() == id) {
            int add = inventoryStack.amount;

            if (add > slotStack.getMaxAmount() - slotStack.getAmount())
                add = slotStack.getMaxAmount() - slotStack.getAmount();

            AccessoriesManager.accessories[slot].amount += add;
            InventoryManager.inventory[index].amount -= add;

        } else {
            InventoryManager.inventory[index] = AccessoriesManager.getSlot(slot);
            AccessoriesManager.setSlot(new ItemStack(this, inventoryStack.amount), slot);
        }
    }

    public void replaceInventory(int index, ItemStack item) {
        InventoryManager.inventory[index] = Item.emptyStack();
        InventoryManager.addItem(item.item, item.amount);
    }

    public void shoot(Item[] acceptable, Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
        ItemStack stack = AccessoriesManager.getSlot(AccessoriesManager.AMMO_SLOT);

        if (stack.amount <= 0 || stack.getID() == 0)
            return;

        for (Item i : acceptable) {
            if (stack.getID() == i.id) {
                stack.item.createProjectile(position, direction, damageMultiplier, expMultiplier);
                stack.amount--;
                break;
            }
        }
    }

    public ItemStack getSingleStack() {
        return new ItemStack(this, 1);
    }
}

enum ItemList {
    empty(new Item(0, "/", "/", "/", 0)),
    wood(new Log(1, "wood.png", "Log", "The remnants of a tree.", 1)),
    bow(new Bow(2, "bow.png", "Bow","Get ready for the power of my bow!", 1)),
    arrow(new ArrowItem(3, "arrow.png", "Arrow", "Some sharp arrows!", 100000)),
    crystalBow(new CrystalBow(4, "crystalBow.png", "Crystal Bow", "This is really gonna hurt.", 1)),
    clownfish(new ClownFishFood(5, "clownfish.png", "Clownfish", "This looks quite tasty.", 1)),
    bluefish(new BlueFishFood(6, "bluefish.png", "Bluefish", "This looks very tasty.", 1)),
    mapleLog(new MapleLog(7, "maplewood.png", "Maple Log", "A sticky log, sounds useful to me.", 1));

    public Item item;

    ItemList(Item item) {

    }
}