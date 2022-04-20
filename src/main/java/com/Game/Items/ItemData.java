package com.Game.Items;

import com.Game.Entity.Player;
import com.Game.Main.Main;
import com.Util.Other.Sprite;

import java.awt.image.BufferedImage;

public class ItemData {

    public int id, amount, sellValue;
    public Sprite image;
    public String name;
    public String examineText;
    public String[] rcOptions;

    public ItemData() {
        this.rcOptions = new String[0];
    }

    public ItemData(ItemData data) {
        this.id = data.id;
        this.amount = data.amount;
        this.image = data.image;
        this.name = data.name;
        this.rcOptions = data.rcOptions;
        this.examineText = data.examineText;
        this.sellValue = data.sellValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setExamineText(String text) {
        this.examineText = text;
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

    public void setRcOptions(String rcOptions) {
        this.rcOptions = rcOptions.split("\n");
    }

    public boolean notEmpty() {
        return amount > 0 && image != null;
    }

    public void onRightClick(int index, String optionText) {
        Main.sendPacket("rc" + Player.name + ";" + index + ";" + optionText);
    }

    public Sprite getSprite() {
        return image;
    }

    public void interpretPacketData(String[] index) {
        try {
            setName(index[1]);
            setAmount(Integer.parseInt(index[2]));
            setImage(Sprite.identifierSprite(index[3]));
            setRcOptions(index[4]);
            setExamineText(index[5]);
            setSellValue(Integer.parseInt(index[6]));
        } catch (Exception e) {
            System.err.println("Invalid item sent!");
            StringBuilder errorBuilder = new StringBuilder();

            for (String string : index)
                errorBuilder.append(string + ";");

            System.err.println(errorBuilder);
            e.printStackTrace();
        }
    }

    public static ItemData getFromPacketData(String index) {
        ItemData item = new ItemData();
        item.interpretPacketData(index.split(";"));

        return item;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void setSellValue(int sellValue) {
        this.sellValue = sellValue;
    }

    public int getSellValue() {
        return sellValue;
    }
}
