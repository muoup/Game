package com.Game.GUI.Inventory;

import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class InventoryManager {

    public static ItemStack[] inventory = new ItemStack[23];

    public static void init() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ItemStack(Item.empty, 0);
        }
    }

    public static void update() {
        for (ItemStack i : inventory) {
            if (i.amount <= 0) {
                i = new ItemStack(Item.empty, 0);
            }
        }
    }

    public static void render() {
        Vector2 startingPosition = Settings.curResolution().subtractClone(48 * 4 + 25, 48 * 5 + 45);

        Render.setColor(new Color(255, 138, 4));

        Render.drawRectangle(startingPosition, new Vector2(4 * 48f, 5 * 48f));

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {
                Vector2 rectPos = startingPosition.addClone(x * 48f, y * 48f);

                ItemStack stack = inventory[x + y * 4];

                if (stack.getID() != -1) {
                    Render.drawImage(stack.item.image, startingPosition.addClone(x * 48f, y * 48f));

                    int itext = stack.amount;
                    String text;

                    // There is no point in going beyond billion because the
                    // integer limit is 2.147 billion
                    if (itext >= 1000000000) {
                        text = itext / 1000000000 + "b";
                    } else if (itext >= 1000000) {
                        text = itext / 1000000 + "m";
                    } else if (itext >= 1000) {
                        text = itext / 1000 + "k";
                    } else {
                        text = itext + "";
                    }

                    Render.setFont(new Font("Arial", 0, 9));

                    Render.drawText(text,
                            rectPos.addClone(new Vector2(45 - Settings.sWidth(text), 45)));
                }

                Render.drawRectOutline(rectPos, new Vector2(48, 48));
            }
        }
    }

    public static boolean addItem(Item item, int amount) {
        int add = amount;

        for (int i = 0; i < inventory.length; i++) {
            System.out.println(amount);
            add = amount;

            if (add == 0)
                return true;

            if (inventory[i].item.id == item.id && inventory[i].amount < item.maxStack) {
                if (add > item.maxStack - inventory[i].amount) {
                    add = item.maxStack - inventory[i].amount;
                }

                inventory[i].amount += add;

                amount -= add;
            }

            if (inventory[i].item.id == -1) {
                if (add > item.maxStack) {
                    add = item.maxStack;
                }

                inventory[i] = new ItemStack(item, add);

                amount -= add;
            }
        }

        if (amount == 0)
            return true;

        return false;
    }
}
