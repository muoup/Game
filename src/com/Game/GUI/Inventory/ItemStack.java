package com.Game.GUI.Inventory;

import java.awt.image.BufferedImage;

public class ItemStack {

    private Item item;
    private int amount;
    private int maxAmount;

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
        this.maxAmount = item.maxStack;
    }

    public ItemStack(ItemList item, int amount) {
        this(item.item, amount);
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

    public String getPlural() {
        return item.getPlural();
    }

    public ItemStack clone() {
        return new ItemStack(item, amount);
    }

    public String toString() {
        return item.name + " " + amount;
    }

    public int getWorth() {
        return item.worth * amount;
    }

    public Item getItem() {
        return item;
    }

    public BufferedImage getImage() {
        return item.image;
    }

    public void setItem(ItemList item) {
        this.item = item.item;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        setAmount(this.amount + amount);
    }
}
