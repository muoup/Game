package com.Game.Items;

import com.Util.Other.Sprite;

import java.awt.image.BufferedImage;

public class ItemData {

    public int id, amount;
    public Sprite image;
    public String name, examineText;
    public String[] rcOptions;

    public ItemData() {

    }

    public ItemData(ItemData data) {
        this.id = data.id;
        this.amount = data.amount;
        this.image = data.image;
        this.name = data.name;
        this.examineText = data.examineText;
        this.rcOptions = data.rcOptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamineText() {
        return examineText;
    }

    public void setExamineText(String examineText) {
        this.examineText = examineText;
    }

    public ItemData clone() {
        return new ItemData(this);
    }

    public BufferedImage getImage() {
        return image.getImage();
    }

    public void setImage(Sprite image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String[] getOptions() {
        return rcOptions;
    }

    public String getOption(int index) {
        return rcOptions[index];
    }

    public void setOption(int index, String option) {
        this.rcOptions[index] = option;
    }

    public void setRcOptions(String[] rcOptions) {
        this.rcOptions = rcOptions;
    }

    public boolean notEmpty() {
        return amount > 0 && image != null;
    }

    public void RightClickIdentities(int i, int option) {

    }

    public Sprite getSprite() {
        return image;
    }
}
