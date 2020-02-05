package com.Game.GUI.Banking;

import com.Game.GUI.GUI;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemStack;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class BankingHandler {
    private static ArrayList<ItemStack> items = new ArrayList();

    private static int padding = 16;

    private static final Vector2 beginPos = Settings.curResolution().scale(0.25f);
    private static final Vector2 size = Settings.curResolution().scale(0.5f);
    private static final int maxRow = (int) ((size.x - padding) / (padding + GUI.IntBoxSize));

    public static void render() {
        // Draw basic stuff for every shop such as background and items.
        Render.setColor(new Color(182, 124, 45));
        Render.drawBorderedRect(beginPos, size);

        for (int i = 0; i < items.size(); i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            String text = GUI.formatAmount(items.get(i).getAmount());

            Vector2 imageScale = new Vector2(GUI.IntBoxSize);

            Vector2 rectPos = beginPos.addClone(padding + (padding + GUI.IntBoxSize) * x, padding + (padding + GUI.IntBoxSize) * y);
            Vector2 textPos = rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(rectPos, imageScale);
            Render.drawImage(Render.getScaledImage(items.get(i).getImage(), imageScale), rectPos);

            Render.setColor(Color.BLACK);
            Render.setFont(Settings.itemFont);
            Render.drawText(text, textPos.addClone(1, 0));
        }

        Render.setColor(Color.RED);
        Render.drawBorderedRect(beginPos.addClone(size.x - GUI.IntBoxSize / 2, 0), new Vector2(GUI.IntBoxSize / 2));
    }

    public static void update() {
        if (Input.GetMouseDown(1)) {
            Vector2 rectBounds = beginPos.addClone(size.x - GUI.IntBoxSize / 2, 0);
            if (Input.mouseInBounds(rectBounds, rectBounds.addClone(GUI.IntBoxSize / 2))) {
                GUI.disableBankInterface();
                return;
            }
        }

        if (!Input.GetMouseDown(3))
            return;

        for (int i = 0; i < items.size(); i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            Vector2 imageScale = new Vector2(GUI.IntBoxSize);

            Vector2 pos = beginPos.addClone(padding + (padding + GUI.IntBoxSize) * x, padding + (padding + GUI.IntBoxSize) * y);
            Vector2 pos2 = pos.addClone(imageScale);

            if (Input.mousePosition.greaterThan(pos) && pos2.greaterThan(Input.mousePosition)) {
                RightClick.customRightClick((int option) -> rightClickOption(option),
                        "Withdraw 1", "Withdraw 10", "Withdraw 50", "Withdraw 100");

                break;
            }
        }
    }

    public static void addItem(ItemStack stack) {
        for (ItemStack bankStack : items) {
            if (bankStack.equivalent(stack)) {
                bankStack.amount += stack.amount;
                return;
            }
        }

        items.add(stack);
    }

    public static void rightClickOption(int option) {

    }

    public static void withdrawItem(int index, int amount) {
        ItemStack bankItem = items.get(index);
        amount = Math.min(amount, bankItem.getAmount());

        bankItem.amount -= amount; // Removes the withdrawal from the bank
        InventoryManager.addItem(bankItem.getItemList(), amount, bankItem.getData()); // Adds it to the inventory.

        if (bankItem.getAmount() == 0) {
            items.remove(index);
        }
    }

    public static void depositInventory(int invIndex, int amount) {
        ItemStack inventory = InventoryManager.getStack(invIndex).clone();
        inventory.amount = amount;

        addItem(inventory);
        InventoryManager.removeItem(inventory.getItemList(), amount, inventory.getData());
    }
}
