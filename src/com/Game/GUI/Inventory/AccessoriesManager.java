package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

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
        setSlot(new ItemStack(accessories[slot].getItem(),amount + accessories[slot].getAmount()), slot);
    }

    public static void setSlot(ItemStack item, int slot) {
        if (accessories[slot].getID() == item.getID()) {
            int maxAmount = accessories[slot].getMaxAmount() - accessories[slot].getAmount();
            if (maxAmount > item.getAmount())
                maxAmount = item.getAmount();

            Main.sendPacket("09" + slot + ":" + item.getID() + ":" + maxAmount + ":" + Main.player.name);
            accessories[slot].setAmount(maxAmount);
        } else {
            float prev = accessories[slot].getArmor();
            accessories[slot] = item;
            Main.sendPacket("09" + slot + ":" + item.getID() + ":" + item.getAmount() + ":" + Main.player.name);
            armor -= prev;
            armor += accessories[slot].getArmor();
        }
    }

    public static void clientSetItem(int slot, int id, int amount) {
        accessories[slot] = new ItemStack(ItemList.values()[id], amount);
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
                    drawBox(x, y);

                    break;
                case AMMO_SLOT:
                    drawBox(x, y);

                    break;
                case HELMET_SLOT:
                    drawBox(x, y);

                    break;
                case NECKLACE_SLOT:
                    drawBox(x, y);

                    break;
                case CHESTPLATE_SLOT:
                    drawBox(x, y);

                    break;
                case LEGGING_SLOT:
                    drawBox(x, y);

                    break;
                case BOOT_SLOT:
                    drawBox(x, y);

                    break;
            }

            Vector2 rectPos = GUI.getGridPosition(x, y);

            if (accessories[i].getID() != 0) {
                Render.drawImage(Render.getScaledImage(accessories[i].getImage(), GUI.invSize), rectPos);
            }
        }

        Render.drawRectOutline(GUI.GuiPos.addClone(2, 2), GUI.GUIEnd().subtractClone(2, 2));
    }


    public static void drawBox(int x, int y) {
        Vector2 rectPos = GUI.getGridPosition(x, y);

        Render.setColor(Color.LIGHT_GRAY);
        Render.drawRectangle(rectPos, Vector2.identity(GUI.IntBoxSize));

        Render.setColor(Color.BLACK);
        Render.drawRectOutline(rectPos, Vector2.identity(GUI.IntBoxSize));

        ItemStack cur = accessories[x + y * 4];

        String amount = InventoryManager.formatAmount(cur.getAmount());

        Render.setFont(Settings.itemFont);

        if (cur.getMaxAmount() > 1)
            Render.drawText(amount, rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(amount) - 4, GUI.IntBoxSize - 4)));
    }

    public static void update() {
        if (GUI.inGUI()) {
            if (Input.GetMouse(1)) {
                Vector2 deltaMouse = Input.mousePosition.subtract(GUI.GuiPos);

                int x = (int) deltaMouse.x / GUI.IntBoxSize;
                int y = (int) deltaMouse.y / GUI.IntBoxSize;

                int index = x + y * 4;

                ItemStack stack = accessories[index].clone();

                InventoryManager.addItem(stack.getItem(), stack.getAmount());

                setSlot(Item.emptyStack(), index);
            }
        }
    }
}