package com.Game.Items;

import com.Game.Main.Main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemStack {

    public Item item;
    public int amount;
    public int maxAmount;
    public int data;
    public ArrayList<String> options = new ArrayList<String>();
    public ItemRequirement requirement = ItemRequirement.none();
    public BufferedImage image;
    public int equipStatus;
    public float armor;

    public ItemStack(Item item, int amount, int data) {
        this.item = item;
        this.amount = amount;
        this.maxAmount = item.maxStack;

        setData(data);
    }

    public ItemStack(Item item, int amount) {
        this(item, amount, 0);
    }

    public ItemStack(ItemList item, int amount) {
        this(item.item, amount, 0);
    }

    public ItemStack(ItemList item, int amount, int data) {
        this(item.item, amount, data);
    }

    public void setImage(String imageName) {
        this.image = Main.getImage("Items/" + imageName);
    }

    public void setEquipStatus(int equipStatus) {
        this.equipStatus = equipStatus;
    }

    public void setRequirement(ItemRequirement requirement) {
        this.requirement = requirement;
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
        return armor;
    }

    public int getEquipmentStatus() {
        return equipStatus;
    }

    public ItemStack clone() {
        return new ItemStack(item, amount, data);
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
        return image;
    }

    public void setData(int data) {
        this.data = data;
        options.clear();
        takeData();
        item.setData(this);
    }

    public void takeData() {
        this.image = item.image;
        this.armor = item.armor;
        this.equipStatus = item.equipStatus;
        this.requirement = item.requirement;
    }
}
