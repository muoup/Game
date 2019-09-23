package com.Game.Items;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.RightClick;
import com.Game.Main.Main;
import com.Util.Math.Vector2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Item {
    public ArrayList<String> options;

    public int equipStatus = -1;
    public int id;
    public String name;
    public BufferedImage image;
    public String examineText;
    public float armor = 0;
    public int maxStack;
    public int worth;
    public ItemRequirement requirement = ItemRequirement.none();

    public Item(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        this.id = id;
        this.image = (!imageName.equals("/")) ? Main.main.getImageFromRoot("Items/" + imageName) : null;
        this.examineText = examineText;
        this.maxStack = maxStack;
        this.name = name;
        this.options = new ArrayList<String>();
        this.worth = worth;
    }

    public static ItemStack emptyStack() {
        return new ItemStack(ItemList.empty, 0);
    }

    public void ClickIdentities(int index) {
        if (equipStatus != -1) {
            equipItem(index);
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
            InventoryManager.setItem(index, emptyStack());

        OnRightClick(index, option);
    }

    public void OnRightClick(int index, int option) {

    }

    public void useWeapon(Vector2 position, Vector2 direction) {

    }

    public void createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {

    }

    public void equipItem(int index) {
        if (!requirement.meetsRequirement()) {
            ChatBox.sendMessage(requirement.toString());
            return;
        }

        ItemStack slotStack = AccessoriesManager.getSlot(equipStatus);
        ItemStack inventoryStack = InventoryManager.getStack(index);

        if (slotStack.getID() == id) {
            int add = inventoryStack.getAmount();

            if (add > slotStack.getMaxAmount() - slotStack.getAmount())
                add = slotStack.getMaxAmount() - slotStack.getAmount();

            AccessoriesManager.addAmount(equipStatus, add);
            InventoryManager.addAmount(index, -add);

        } else {
            ItemStack invHolder = InventoryManager.getStack(index).clone();
            InventoryManager.setItem(index, AccessoriesManager.getSlot(equipStatus));
            AccessoriesManager.setSlot(new ItemStack(this, invHolder.getAmount()), equipStatus);
        }
    }

    public void replaceInventory(int index, ItemStack item) {
        InventoryManager.inventory[index] = Item.emptyStack();
        InventoryManager.addItem(item.getItem(), item.getAmount());
    }

    public void shoot(ItemList[] acceptable, Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
        ItemStack stack = AccessoriesManager.getSlot(AccessoriesManager.AMMO_SLOT);

        if (stack.getAmount() <= 0 || stack.getID() == 0)
            return;

        for (ItemList i : acceptable) {
            if (stack.getID() == i.getID()) {
                stack.getItem().createProjectile(position, direction, damageMultiplier, expMultiplier);
                stack.addAmount(-1);
                break;
            }
        }
    }

    public void use(int index) {

    }

    public float getArmor() {
        return armor;
    }

    public String getPlural() {
        return name + "s";
    }

    public ItemStack getSingleStack() {
        return new ItemStack(this, 1);
    }
}