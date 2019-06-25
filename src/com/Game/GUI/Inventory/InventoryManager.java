package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.listener.Input;
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
    }

    public static void update() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].amount <= 0 && inventory[i].getID() != -1) {
                inventory[i] = Item.emptyStack();
            }
        }

        if (Input.mousePosition.compareTo(GUI.mainPos) == 1 &&
            GUI.mainPos.addClone(4 * GUI.select, 5 * GUI.select).compareTo(Input.mousePosition) == 1) {
            if (Input.GetMouse(1)) {
                Vector2 deltaMouse = Input.mousePosition.subtract(GUI.mainPos);

                int x = (int) deltaMouse.x / GUI.select;
                int y = (int) deltaMouse.y / GUI.select;

                int index = x + y * x;

                inventory[index].item.OnClick(index);
            }
        }
    }

    public static void render() {
        Render.setColor(new Color(255, 138, 4));

        Render.drawRectangle(GUI.mainPos, new Vector2(4 * GUI.inputSize, 5f * GUI.inputSize));

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {
                Vector2 rectPos = GUI.mainPos.addClone(x * GUI.inputSize, y * GUI.inputSize);

                ItemStack stack = inventory[x + y * 4];

                if (stack.getID() != 0 && stack.getAmount() > 0) {
                    Render.drawImage(stack.item.image.getScaledInstance(GUI.inputSize, GUI.inputSize, 0), rectPos);

                    if (stack.getAmount() > 1) {
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

                        Render.setFont(new Font("Arial", 0, (int) rectPos.x / 55));

                        Render.drawText(text,
                                rectPos.addClone(new Vector2(GUI.inputSize - Settings.sWidth(text) - 4, GUI.inputSize - 4)));
                    }
                }

                Render.drawRectOutline(rectPos, new Vector2(GUI.inputSize, GUI.inputSize));
            }
        }
    }

    public static boolean addItem(Item item, int amount) {
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

            if (inventory[i].item.id == 0) {
                if (add > item.maxStack) {
                    add = item.maxStack;
                }

                inventory[i] = new ItemStack(item, add);

                amount -= add;
            }

            if (add == 0)
                return true;
        }
        return false;
    }
}
