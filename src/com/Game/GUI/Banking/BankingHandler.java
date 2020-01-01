package com.Game.GUI.Banking;

import com.Game.GUI.GUIWindow.GUIItemSlot;
import com.Game.GUI.GUIWindow.GUILibrary;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.ItemStack;

public class BankingHandler {
    public static void printBankSpace() {
        BankingGUI.itemStacks.forEach(System.out::println);
    }

    public static void removeItem(ItemStack stack) {
        removeItem(BankingGUI.itemStacks.indexOf(stack));
    }

    public static void removeItem(int index) {
        if (index < 0 || index >= BankingGUI.itemStacks.size()) {
            System.err.println(index + " is out of the range of the bank, it is not a valid item.");
        }
    }

    public static void addItem(ItemStack stack) {
        for (GUIItemSlot iStack : BankingGUI.itemStacks) {
            if (iStack.getStack().compareTo(stack)) {
                iStack.addAmount(stack.getAmount());
                return;
            }
        }

        GUILibrary.bankingGUI.addItemSlot(stack);
    }

    public static void addInvItem(int index) {
        ItemStack stack = InventoryManager.getStack(index).clone();
        InventoryManager.setItem(index, ItemStack.empty());
        addItem(stack);
    }
}