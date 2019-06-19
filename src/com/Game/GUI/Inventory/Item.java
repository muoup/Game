package com.Game.GUI.Inventory;

import com.Game.Main.Main;

import java.awt.*;

public class Item {

    public static Item empty = new Item(-1, "/", "/", -1);
    public static Item wood = new Item(0, "wood.png", "A nice piece of wood.", 10);

    public int id;
    public Image image;
    public String examineText;
    public int maxStack;

    public Item(int id, String imageName, String examineText, int maxStack) {
        this.id = id;
        this.image = (!imageName.equals("/")) ? Main.main.getImageFromRoot("items/" + imageName) : null;
        this.examineText = examineText;
        this.maxStack = maxStack;
    }
}

