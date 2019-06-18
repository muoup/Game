package com.Game.GUI.Inventory;

public class ItemStack {

    public Item item;
    public int amount;
    private int maxAmount;

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
        this.maxAmount = item.maxStack;
    }

    public int getID() {
        return item.id;
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }
}
