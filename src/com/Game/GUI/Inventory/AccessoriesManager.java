package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
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
            if (accessories[i].amount <= 0 && accessories[i].getID() != 0) {
                accessories[i] = Item.emptyStack();
            }
        }
    }

    public static void setSlot(ItemStack item, int slot) {
        if (accessories[slot].getID() == item.getID()) {
            int maxAmount = accessories[slot].getMaxAmount() - accessories[slot].getAmount();
            if (maxAmount > item.amount)
                maxAmount = item.amount;

            accessories[slot].amount += maxAmount;
        } else {
            accessories[slot] = item.clone();
        }
    }
    public static void render() {
        Render.setColor(Color.BLACK);
        Render.drawRectangle(GUI.mainPos, GUI.GUIEnd().subtractClone(GUI.mainPos));

        Render.setColor(new Color(154, 154, 154));
        Render.drawRectangle(GUI.mainPos.addClone(2, 2), GUI.GUIEnd().subtractClone(GUI.mainPos));

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

            Vector2 rectPos = GUI.mainPos.addClone(x * GUI.inputSize, y * GUI.inputSize);

            if (accessories[i].getID() != 0) {
                Render.drawImage(accessories[i].item.image.getScaledInstance(GUI.inputSize, GUI.inputSize, 0), rectPos);
            }
        }
    }

    public static void drawBox(int x, int y) {
        Vector2 rectPos = GUI.mainPos.addClone(new Vector2(GUI.inputSize * x, GUI.inputSize * y));

        Render.setColor(Color.LIGHT_GRAY);
        Render.drawRectangle(rectPos, Vector2.identity(GUI.inputSize));

        Render.setColor(Color.BLACK);
        Render.drawRectOutline(rectPos, Vector2.identity(GUI.inputSize));

        ItemStack cur = accessories[x + y * 4];

        String amount = Integer.toString(cur.getAmount());

        Render.setFont(Settings.itemFont);

        if (cur.getMaxAmount() > 1)
            Render.drawText(amount, rectPos.addClone(new Vector2(GUI.inputSize - Settings.sWidth(amount) - 4, GUI.inputSize - 4)));
    }

    public static void update() {
        if (Input.mousePosition.compareTo(GUI.mainPos) == 1) {
            if (Input.GetMouse(1)) {
                Vector2 deltaMouse = Input.mousePosition.subtract(GUI.mainPos);

                int x = (int) deltaMouse.x / GUI.inputSize;
                int y = (int) deltaMouse.y / GUI.inputSize;

                int index = x + y * 4;

                ItemStack stack = accessories[index].clone();

                InventoryManager.addItem(stack.item, stack.amount);

                setSlot(Item.emptyStack(), index);
            }
        }
    }
}