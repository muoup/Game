package com.Game.GUI.Banking;

import com.Game.GUI.GUIWindow.BasicGUIWindow;
import com.Game.GUI.GUI;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.RightClick;
import com.Game.GUI.Shop.Shop;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import java.util.ArrayList;

public class BankingHandler extends BasicGUIWindow {
    public static ArrayList<ItemStack> items = new ArrayList();

    private static int padding = 16;

    private static final Vector2 beginPos = Settings.curResolution().scale(0.25f);
    private static final Vector2 size = Settings.curResolution().scale(0.5f);
    private static final int maxRow = (int) ((size.x - padding) / (padding + GUI.intBoxSize));

    public static ItemStack hover = ItemStack.empty();
    public static int withdrawIndex = -1;

    public static void render() {
    }

    public static void update() {
        if (Input.GetMouseDown(1)) {
            Vector2 rectBounds = beginPos.addClone(size.x - GUI.intBoxSize / 2, 0);
            if (Input.mouseInBounds(rectBounds, rectBounds.addClone(GUI.intBoxSize / 2))) {
                GUI.disableBankInterface();
                return;
            }
        }

        if (!Input.GetMouseDown(3))
            return;

        for (int i = 0; i < items.size(); i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 pos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * x, padding + (padding + GUI.intBoxSize) * y);
            Vector2 pos2 = pos.addClone(imageScale);

            if (Input.mousePosition.greaterThan(pos) && pos2.greaterThan(Input.mousePosition)) {
                RightClick.customRightClick((int option) -> withdrawItem(option),
                        "Withdraw 1", "Withdraw 10", "Withdraw 50", "Withdraw 100");

                withdrawIndex = i;

                break;
            }
        }
    }

    private static void withdrawItem(int option) {
        withdrawItem(withdrawIndex, Math.min(Shop.amountOptions[option], items.get(withdrawIndex).getAmount()));
    }

    public static void addItem(ItemStack stack) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack bankStack = items.get(i);
            if (bankStack.equivalent(stack)) {
                bankStack.amount += stack.amount;
                sendBankSlotData(i);
                return;
            }
        }

        items.add(stack);
        sendBankCreate(stack);
    }

    public static void rightClickOption(int option) {
        depositInventory(hover, Math.min(Shop.amountOptions[option], InventoryManager.itemCount(hover.getItemList(), hover.getData())));
    }

    public static void withdrawItem(int index, int amount) {
        ItemStack bankItem = items.get(index);

        if (amount == -1 || amount > bankItem.getAmount()) {
            amount = bankItem.getAmount();
        }

        bankItem.amount -= amount; // Removes the withdrawal from the bank
        InventoryManager.addItem(bankItem.getItemList(), amount, bankItem.getData()); // Adds it to the inventory.

        if (bankItem.getAmount() == 0) {
            items.remove(index);
            sendBankRemove(index);
        } else {
            sendBankSlotData(index);
        }
    }

    public static void sendBankSlotData(int slot) {
        ItemStack bankStack = items.get(slot);
        Main.sendPacket("6e" + slot + ":" + bankStack.getID() + ":" + bankStack.getAmount() + ":" + bankStack.getData() + ":" + Main.player.name);
    }

    public static void sendBankRemove(int slot) {
        Main.sendPacket("6d" + slot + ":" + Main.player.name);
    }

    public static void sendBankCreate(ItemStack bankStack) {
        Main.sendPacket("6c" + bankStack.getID() + ":" + bankStack.getAmount() + ":" + bankStack.getData() + ":" + Main.player.name);
    }

    public static void depositInventory(ItemStack hover, int amount) {
        ItemStack inventory = hover.clone();
        if (amount == -1) {
            amount = InventoryManager.itemCount(inventory.getItemList(), inventory.getData());
        }

        inventory.amount = amount;



        addItem(inventory);
        InventoryManager.removeItem(hover.getItemList(), amount, hover.getData());
    }
}
