package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class InventoryManager {

    public static ItemStack[] inventory = new ItemStack[20];
    public static int draggedIndex = -1;

    public static void init() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ItemStack(ItemList.empty, 0);
        }

        InventoryDrag.init();
    }

    public static void reset() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ItemStack(ItemList.empty, 0);
        }
    }

    public static boolean isFull() {
        for (ItemStack i : inventory) {
            if (i.getItem().id == 0)
                return false;
        }

        return true;
    }

    public static void handleInventory() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].getAmount() <= 0 && inventory[i].getID() != -1) {
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
        inventory[index].addAmount(-amount);
    }

    public static int itemCount(ItemList item) {
        int amount = 0;
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID())
                amount += stack.getAmount();
        }
        return amount;
    }

    public static boolean removeItem(ItemList item, int amount) {
        int remove = amount;
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.getID() == item.getID()) {
                int removeAmount = (remove > stack.getAmount()) ? stack.getAmount() : remove;
                removeItem(i, removeAmount);
                remove -= removeAmount;
                if (remove == 0)
                    return false;
            }
        }

        return true;
    }

    public static void render() {
        Render.setColor(new Color(255, 138, 4));

        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {

                Vector2 rectPos = GUI.getGridPosition(x, y);

                if (draggedIndex == x + y * 4) {
                    Render.drawRectOutline(rectPos, GUI.invSize);
                    continue;
                }

                ItemStack stack = getStack(x + y * 4);

                if (stack.getID() != 0 && stack.getAmount() > 0) {
                    Render.drawImage(Render.getScaledImage(stack.getImage(), GUI.invSize), rectPos);

                    if (stack.getAmount() > 1) {
                        Render.setFont(Settings.itemFont);
                        Render.setColor(Color.BLACK);

                        String text = formatAmount(stack.getAmount());


                        Render.drawText(text,
                                rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4)));
                    }
                }
                Render.drawRectOutline(rectPos, GUI.invSize);
            }
        }

        if (useIndex != -1) {
            Render.setColor(Color.GREEN);
            Render.drawRectOutline(GUI.getGridPosition(useIndex % 4, useIndex / 4), GUI.invSize);
        }

        InventoryDrag.render();
    }

    public static void setAmount(int slot, int amount) {
        ItemStack stack = getStack(slot);
        stack.setAmount(amount);
        setItem(slot, stack);
    }

    public static void setID(int slot, int id) {
        ItemStack stack = getStack(slot);
        stack.setItem(ItemList.values()[id]);
    }

    public static void addAmount(int slot, int amount) {
        setAmount(slot, amount + getStack(slot).getAmount());
    }

    public static int useIndex = -1;

    public static void setItem(int slot, ItemStack item) {
        inventory[slot].setItem(ItemList.values()[item.getID()]);
        inventory[slot].setAmount(item.getAmount());

        Main.sendPacket("08" + slot + ":" + item.getID() + ":" + item.getAmount() + ":" + Main.player.name);
    }

    public static void swapSlots(int slot1, int slot2) {
        ItemStack temp = getStack(slot1).clone();
        setItem(slot1, getStack(slot2).clone());
        setItem(slot2, temp);
    }

    public static void clientSetItem(int slot, int id, int amount) {
        inventory[slot] = new ItemStack(ItemList.values()[id], amount);
    }

    public static int addItem(ItemList item, int amount) {
        int amt = amount;
        int add = amount;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;

            if (inventory[i].getID() == item.item.id && inventory[i].getAmount() < item.maxStack()) {
                if (add > item.maxStack() - inventory[i].getAmount()) {
                    add = item.maxStack() - inventory[i].getAmount();
                }

                addAmount(i, add);

                amount -= add;
            }
        }

        if (amount == 0)
            return amt;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;

            if (inventory[i].getID() == 0) {
                if (add > item.maxStack()) {
                    add = item.maxStack();
                }

                setItem(i, new ItemStack(item, add));

                amount -= add;
            }

            if (amount == 0)
                return amt;
        }

        return amt - add;
    }

    public static int addItem(Item item, int amount) {
        return addItem(ItemList.values()[item.id], amount);
    }

    public static int addItem(ItemStack stack) {
        return addItem(stack.getItem(), stack.getAmount());
    }

    public static String formatAmount(int amount) {
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

    public static ItemStack getStack(int inventoryIndex) {
        return inventory[inventoryIndex];
    }

    public static Item getItem(int inventoryIndex) {
        return inventory[inventoryIndex].getItem();
    }
}