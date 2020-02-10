package com.Game.Items;

import com.Game.Main.Main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemStack {

    public Item item;
    public int amount;
    public int maxAmount;
    public int data;
    public String name;
    public ArrayList<String> options = new ArrayList<String>();
    public ItemRequirement requirement = ItemRequirement.none();
    public BufferedImage image;
    public int equipStatus;
    public float armor = 0;

    public ItemStack(Item item, int amount, int data) {
        this.item = item;
        this.amount = amount;
        this.maxAmount = item.maxStack;
        name = item.name;

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

    public static ItemStack empty() {
        return new ItemStack(ItemList.empty, 0);
    }

    public void setImage(String imageName) {
        this.image = Main.getImage("Items/" + imageName);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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

    public boolean meetsRequirement() {
        return item.requirement.meetsRequirement();
    }

    public String getOptionText(int index) {
        return item.getOptionText(index, data, this);
    }

    public ItemStack singleStack() {
        return item.getItemList().singleStack();
    }

    public ItemList getItemList() {
        return item.getItemList();
    }

    public boolean compareTo(ItemStack stack) {
        return stack.getID() == getID() && stack.getData() == stack.getData();
    }

    public boolean equivalent(ItemStack stack) {
        return stack.getID() == getID() && stack.getData() == getData();
    }

    public int getPrice() {
        return getItemList().getPrice();
    }

    public float getMoveSpeed() {
        return getItemList().getMoveSpeed();
    }

    public float getDamageMultiplier() {
        return getItemList().getDamageMulti();
    }
}
