package com.Game.Items;

import java.awt.image.BufferedImage;

public class ItemStack {

    private Item item;
    private int amount;
    private int maxAmount;
    private int data;

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
        this.maxAmount = item.maxStack;
        this.data = 0;
    }

    public ItemStack(Item item, int amount, int data) {
        this.item = item;
        this.amount = amount;
        this.maxAmount = item.maxStack;
        this.data = data;

        item.onDataSet(data);
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

    public int getData() {
        return data;
    }

    public String getPlural() {
        return item.getPlural();
    }

    public float getArmor() {
        return item.armor;
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

    public void setData(int data) {
        this.data = data;
        item.onDataSet(data);
    }
}
