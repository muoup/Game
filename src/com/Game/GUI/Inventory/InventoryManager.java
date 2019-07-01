package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Main.Main;
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

        if (GUI.inGUI() && !RightClick.render && RightClick.coolDown <= 0) {
            if (Input.GetMouse(1)) {
                Vector2 deltaMouse = Input.mousePosition.subtract(GUI.mainPos);

                int x = (int) deltaMouse.x / GUI.inputSize;
                int y = (int) deltaMouse.y / GUI.inputSize;

                int index = x + y * 4;

                inventory[index].item.OnClick(index);
            }
        }
    }

    public static void render() {
        Render.setColor(new Color(255, 138, 4));

        Render.drawRectangle(GUI.mainPos, GUI.GUIEnd().subtract(GUI.mainPos));

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {
                Vector2 rectPos = GUI.mainPos.addClone(x * GUI.inputSize, y * GUI.inputSize);

                ItemStack stack = inventory[x + y * 4];

                if (stack.getID() != 0 && stack.getAmount() > 0) {
                    Render.drawImage(stack.item.image.getScaledInstance(GUI.inputSize, GUI.inputSize, 0), rectPos);

                    if (stack.getMaxAmount() > 1) {
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

                        Render.setFont(Settings.itemFont);

                        Render.drawText(text,
                                rectPos.addClone(new Vector2(GUI.inputSize - Settings.sWidth(text) - 4, GUI.inputSize - 4)));
                    }
                }

                Render.drawRectOutline(rectPos, new Vector2(GUI.inputSize, GUI.inputSize));
            }
        }
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
}
