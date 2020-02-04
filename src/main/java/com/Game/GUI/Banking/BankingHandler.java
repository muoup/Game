package com.Game.GUI.Banking;

import com.Game.GUI.Chatbox.ChatBox;
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
        int amount = BankingGUI.itemStacks.get(index).getAmount();
        removeItem(index, amount);
    }

    public static void removeItem(int index, int amount) {
        if (index < 0 || index >= BankingGUI.itemStacks.size()) {
            ChatBox.sendMessage(index + " is out of the range of the bank, it is not a valid item.");
            return;
        }

        if (amount > BankingGUI.itemStacks.get(index).getAmount()) {
            ChatBox.sendMessage(amount + " is greater than the amount in the bank, please try a valid amount.");
            return;
        }

        ItemStack stackClone = BankingGUI.itemStacks.get(index).getStack().clone();
        stackClone.amount = amount;

        InventoryManager.addItem(stackClone);

        update(index);
    }

    private static void update(int index) {
        if (BankingHandler.getAmount(index) == 0) {
            BankingGUI.itemStacks.remove(index);
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

    public static void setAmount(int index, int amount) {
        BankingGUI.itemStacks.get(index).getStack().amount = amount;

        // TODO: Server-saving implementation
    }

    public static int getAmount(int index) {
        return BankingGUI.itemStacks.get(index).getStack().amount;
    }
}