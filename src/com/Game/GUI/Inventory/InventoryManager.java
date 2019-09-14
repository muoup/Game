package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Main.Main;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class InventoryManager {

    public static ItemStack[] inventory = new ItemStack[20];

    public static void init() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ItemStack(Item.empty, 0);
        }

        InventoryDrag.init();

        InventoryManager.addItem(Item.bow, 1);
        InventoryManager.addItem(Item.arrow, 300);
    }

    public static boolean isFull() {
        for (ItemStack i : inventory) {
            if (i.item.id == 0)
                return false;
        }

        return true;
    }

    public static void handleInventory() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].amount <= 0 && inventory[i].getID() != -1) {
                inventory[i] = Item.emptyStack();
            }
        }
    }

    public static void update() {
        if (RightClick.coolDown > 0)
            RightClick.coolDown -= 1 / Main.fps;

        InventoryDrag.update();
    }

    public static void removeItem(int index, int amount) {
        inventory[index].amount -= amount;
    }

    public static void render() {
        Render.setColor(new Color(255, 138, 4));

        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {
                Vector2 rectPos = GUI.getGridPosition(x, y);

                ItemStack stack = inventory[x + y * 4];

                if (stack.getID() != 0 && stack.getAmount() > 0) {
                    Render.drawImage(Render.getScaledImage(stack.item.image, GUI.invSize), rectPos);

                    if (stack.getMaxAmount() > 1) {
                        Render.setFont(Settings.itemFont);

                        String text = formatAmount(stack.amount);

                        Render.drawText(text,
                                rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4)));
                    }
                }

                Render.drawRectOutline(rectPos, GUI.invSize);
            }
        }

        InventoryDrag.render();
    }

    public static int addItem(Item item, int amount) {
        int amt = amount;
        int add = amount;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;

            if (inventory[i].item.id == item.id && inventory[i].amount < item.maxStack) {
                if (add > item.maxStack - inventory[i].amount) {
                    add = item.maxStack - inventory[i].amount;
                }

                inventory[i].amount += add;

                amount -= add;
            }
        }

        if (add == 0)
            return amt;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;

            if (inventory[i].item.id == 0) {
                if (add > item.maxStack) {
                    add = item.maxStack;
                }

                inventory[i] = new ItemStack(item, add);

                amount -= add;
            }

            if (add == 0)
                return amt;
        }

        return amt - add;
    }

    public static int addItem(ItemStack stack) {
        return addItem(stack.item, stack.amount);
    }

    public static String formatAmount(int amount) {
        String text;

        if (amount >= 1000000000) {
            return amount / 1000000000 + "b";
        } else if (amount >= 1000000) {
            return amount / 1000000 + "m";
        } else if (amount >= 1000) {
            return amount / 1000 + "k";
        } else {
            return amount + "";
        }
    }
}
