package com.Game.Items;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.RightClick;
import com.Game.Main.Main;
import com.Game.Projectile.Projectile;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Item {
    public ArrayList<String> options = new ArrayList<String>();

    public final int id; // ID of the item
    public final String name; // Name of the item, will eventually be used when the ItemStacks if hovered over.
    public final String examineText; // Text printed in ChatBox when examined
    public final int maxStack; // Maximum amount allowed in one stack in the state
    public final int worth; // How much the item is worth when sold
    public String imageName; // Name of the image (use as basic image, other data values may have different images
    public BufferedImage image; // Default image (use as basic image, other data values may have different images)
    public int equipStatus = -1; // Equipment status of an item, default if nothing is set in setData(ItemStack stack)
    public float armor = 0; // Armor amount, default if nothing is set in setData(ItemStack stack)
    public ItemRequirement requirement = ItemRequirement.none(); // Requirements to use an item.
    public float expMultiplier = 1f;

    public Item(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        this.id = id;
        this.examineText = examineText;
        this.maxStack = maxStack;
        this.name = name;
        this.worth = worth;
        this.imageName = imageName;
        this.image = (!imageName.equals("/")) ? Main.getImage("Items/" + imageName) : null;
    }

    public Item(int id, BufferedImage image, String name, String examineText, int maxStack, int worth) {
        this.id = id;
        this.examineText = examineText;
        this.maxStack = maxStack;
        this.name = name;
        this.worth = worth;
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getImage(String root) {
        return Main.getImage("Items/" + root);
    }

    public static ItemStack emptyStack() {
        return new ItemStack(ItemList.empty, 0);
    }

    public void ClickIdentities(int index) {
        if (InventoryManager.getStack(index).equipStatus != -1) {
            equipItem(index);
        } else {
            InventoryManager.useIndex = -1;
            OnClick(index);
        }
    }

    public void OnClick(int index) {
        examineItem(index);
    }

    public void examineItem(int index) {
        String text = examineText;
        text = text.replace("[amt]", Integer.toString(InventoryManager.inventory[index].amount));
        ChatBox.sendMessage(text);
    }

    public void RightClickIdentities(int index, int option) {
        if (option == 0) {
            ClickIdentities(index);
        }

        if (option == RightClick.options.size() - 1)
            examineItem(index);

        if (option == RightClick.options.size() - 2)
            InventoryManager.setItem(index, emptyStack());

        OnRightClick(index, option);
    }

    public void OnRightClick(int index, int option) {

    }

    public void setData(ItemStack stack) {

    }

    public void useWeapon(Vector2 position, Vector2 direction) {

    }

    public Projectile createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
        return null;
    }

    public void equipItem(int index) {
        ItemStack stack = InventoryManager.getStack(index);
        ItemStack slotStack = AccessoriesManager.getSlot(stack.equipStatus);

        if (!stack.requirement.meetsRequirement()) {
            ChatBox.sendMessage(stack.requirement.toString());
            return;
        }

        if (slotStack.getID() == stack.getID() && slotStack.getData() == stack.getData()) {
            int add = stack.getAmount();

            if (add > slotStack.getMaxAmount() - slotStack.getAmount())
                add = slotStack.getMaxAmount() - slotStack.getAmount();

            AccessoriesManager.addAmount(stack.getEquipmentStatus(), add);
            InventoryManager.addAmount(index, -add);

        } else {
            ItemStack invHolder = InventoryManager.getStack(index).clone();
            InventoryManager.setItem(index, AccessoriesManager.getSlot(stack.equipStatus));
            AccessoriesManager.setSlot(
                    new ItemStack(invHolder.getItemList(), invHolder.getAmount(), invHolder.getData()),
                    stack.equipStatus);
        }
    }

    public void replaceInventory(int index, ItemStack item) {
        InventoryManager.setItem(index, emptyStack());
        InventoryManager.addItem(item);
    }

    public void use(int index) {

    }

    public int getData(int index) {
        return InventoryManager.getData(index);
    }

    public boolean combine(int index, ItemList use, int maxUse, int newData) {
        int useAmount = Math.min(maxUse, InventoryManager.itemCount(use));
        if (useAmount == 0) {
            ChatBox.sendMessage("You need some " + use.item.name + " to complete this action.");
            return false;
        }

        InventoryManager.removeItem(use, useAmount);
        InventoryManager.setData(index, newData);
        return true;
    }

    public int combine(int index, ItemList use, ItemList create, int amt) {
        int amount = InventoryManager.itemCount(use);
        amount = Math.min(amt, amount);
        amount = Math.min(amount, InventoryManager.getStack(index).getAmount());
        if (amount == 0) {
            ChatBox.sendMessage("You need some " + use.item.name + " to do this.");
            return 0;
        }

        InventoryManager.removeItem(use, amount);
        InventoryManager.removeItem(getItemList(), amount);
        InventoryManager.addItem(create, amount);

        return amount;
    }

    public int combine(int index, ItemList use, ItemList create, int amt, int data) {
        int amount = InventoryManager.itemCount(use);
        amount = Math.min(amt, amount);
        amount = Math.min(amount, InventoryManager.getStack(index).getAmount());
        if (amount == 0) {
            ChatBox.sendMessage("You need some " + use.item.name + " to do this.");
            return 0;
        }

        InventoryManager.removeItem(use, amount);
        InventoryManager.removeItem(getItemList(), amount);
        int in = InventoryManager.addItem(create, amount);
        InventoryManager.setData(in, data);

        return amount;
    }

    public int convert(int amountPer, int maxCreate, ItemList create) {
        int use = InventoryManager.itemCount(getItemList());
        int make = (int) DeltaMath.maxmin(use / amountPer, 0, maxCreate);
        if (make == 0) {
            ChatBox.sendMessage("You need " + amountPer + " " + name + " to make this.");
            return 0;
        }
        InventoryManager.removeItem(getItemList(), make * amountPer);
        InventoryManager.addItem(create, make);
        return amountPer;
    }

    public ItemList getItemList() {
        return ItemList.values()[id];
    }

    public String getPlural() {
        return name + "s";
    }

    public ItemStack getSingleStack() {
        return new ItemStack(this, 1);
    }

    public ArrayList<String> createOptions(String... optionList) {
        return (ArrayList<String>) Arrays.asList(optionList);
    }

    public String getOptionText(int i, int data, ItemStack stack) {
        if (stack.options.size() == 0) {
            return "Examine " + stack.name;
        }

        return stack.options.get(i) + " " + name;
    }
}