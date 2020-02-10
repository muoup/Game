package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;

public class AccessoriesManager {

    public static ItemStack[] accessories = new ItemStack[23];

    public static final int WEAPON_SLOT = 0;
    public static final int AMMO_SLOT = 4;

    public static final int HELMET_SLOT = 3;
    public static final int NECKLACE_SLOT = 7;
    public static final int CHESTPLATE_SLOT = 11;
    public static final int LEGGING_SLOT = 15;
    public static final int BOOT_SLOT = 19;

    public static float armor = 0;

    public static float damageMultiplier = 0;
    public static float moveSpeedMultiplier = 0;

    public static void init() {
        for (int i = 0; i < accessories.length; i++) {
            accessories[i] = Item.emptyStack();
        }
    }

    public static ItemStack getSlot(int slot) {
        return accessories[slot];
    }

    public static void handleInventory() {
        for (int i = 0; i < accessories.length; i++) {
            if (accessories[i].getAmount() <= 0 && accessories[i].getID() != 0) {
                accessories[i] = Item.emptyStack();
            }
        }
    }

    public static void addAmount(int slot, int amount) {
        setSlot(new ItemStack(accessories[slot].getItem(), amount + accessories[slot].getAmount()), slot);
    }

    public static void setSlot(ItemStack item, int slot) {
        int stackSet = Math.min(item.getAmount(), item.getMaxAmount());

        accessories[slot] = new ItemStack(item.getItemList(), stackSet, item.getData());

        Main.sendPacket("09" + slot + ":" + item.getID() + ":" + item.getAmount() + ":" + item.getData() + ":" + Main.player.name);

        calculateStats();
    }

    public static void calculateStats() {
        armor = 0;
        damageMultiplier = 0;
        moveSpeedMultiplier = 0;

        for (ItemStack item : accessories) {
            armor += item.getArmor();
            damageMultiplier += item.getDamageMultiplier();
            moveSpeedMultiplier += item.getMoveSpeed();
        }
    }

    public static void clientSetItem(int slot, int id, int amount, int data) {
        accessories[slot] = new ItemStack(ItemList.values()[id], amount);
        accessories[slot].setData(data);
    }

    public static void render() {
        Render.setColor(Color.BLACK);
        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(new Color(154, 154, 154));
        Render.drawBounds(GUI.GuiPos.addClone(2, 2), GUI.GUIEnd().subtractClone(2, 2));

        for (int i = 0; i < accessories.length; i++) {
            int x = i % 4;
            int y = i / 4;


            switch (i) {
                // Draw highlight images of each under when they are made:
                case WEAPON_SLOT:
                case AMMO_SLOT:
                case HELMET_SLOT:
                case NECKLACE_SLOT:
                case CHESTPLATE_SLOT:
                case LEGGING_SLOT:
                case BOOT_SLOT:
                    GUI.drawItem(x, y, accessories[i]);
                    break;

            }

            Vector2 rectPos = GUI.getGridPosition(x, y);

            if (accessories[i].getID() != 0) {
                Render.drawImage(Render.getScaledImage(accessories[i].getImage(), GUI.invSize), rectPos);
            }
        }
    }

    public static void update() {
        if (GUI.inGUI()) {
            if (Input.GetMouse(1)) {
                Vector2 deltaMouse = Input.mousePosition.subtract(GUI.GuiPos);

                int x = (int) deltaMouse.x / GUI.IntBoxSize;
                int y = (int) deltaMouse.y / GUI.IntBoxSize;

                int index = x + y * 4;

                ItemStack stack = accessories[index].clone();

                InventoryManager.addItem(stack.getItem(), stack.getAmount(), stack.getData());

                setSlot(Item.emptyStack(), index);
            }
        }
    }
}