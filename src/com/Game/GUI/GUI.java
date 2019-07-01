package com.Game.GUI;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GUI {

    public static float coolDown = 0f;
    public static Vector2 mainPos;
    public static int inputSize;
    public static Vector2 mainOffset;
    public static Vector2 res;
    public static Image[] inventoryOptions;
    public static String[] invImgNames = {
        "backpack.png",
        "accessories.png"
    };
    public static int select;
    public static Vector2 categorySize;
    public static Vector2 below;
    public static int curMain = 0;

    public static Vector2 GUIEnd() {
        return mainPos.addClone(4 * inputSize, 5 * inputSize);
    }

    public static boolean inGUI() {
        return Input.mousePosition.compareTo(GUI.mainPos) == 1 && GUI.GUIEnd().compareTo(Input.mousePosition) == 1;
    }

    public static void init() {
        res = Settings.curResolution();
        inventoryOptions = new Image[invImgNames.length];
        inputSize = (int) (res.x * 0.05f);
        select = (int) (inputSize * 0.75);
        categorySize = Vector2.identity(select);
        mainOffset = new Vector2(inputSize * 0.5f, inputSize * 1.5f);
        mainPos = res.subtractClone(new Vector2(inputSize * 4f, inputSize * 5.5f)).subtractClone(mainOffset);
        below = mainPos.addClone(0,  inputSize * 5);
        Settings.itemFont = new Font("Arial", Font.PLAIN, (int) Settings.curResolution().x / 75);

        for (int i = 0; i < invImgNames.length; i++) {
            inventoryOptions[i] = Main.getImage("/GUI/" + invImgNames[i]).getScaledInstance(select, select, 0);
        }

        InventoryManager.init();
        AccessoriesManager.init();
        RightClick.init();
    }

    public static void render() {
        Vector2 offset = below.clone();
        Vector2 change = new Vector2(select, 0);


        for (int i = 0; i < inventoryOptions.length; i++) {
            Image render = inventoryOptions[i];

            Render.setColor(Color.LIGHT_GRAY);

            Render.drawRectangle(offset, categorySize);

            Render.drawImage(render, offset);

            Render.setColor(Color.BLACK);

            Render.drawRectOutline(offset, categorySize);

            offset.add(change);
        }

        if (coolDown > 0) {
            coolDown -= 1 / Main.fps;
        }

        InventoryManager.handleInventory();
        AccessoriesManager.handleInventory();

        switch (curMain) {
            case 0:
                InventoryManager.render();
                InventoryManager.update();
                RightClick.update();
                RightClick.render();
                break;
            case 1:
                AccessoriesManager.render();
                AccessoriesManager.update();
                break;
            default:
                System.err.println("There is something wrong.");
                break;
        }
    }

    public static void update() {
        Vector2 end = below.addClone(new Vector2(inputSize * inventoryOptions.length, inputSize));

        if (Input.mousePosition.compareTo(below) == 1 && end.compareTo(Input.mousePosition) == 1 && Input.GetMouse(1)) {
            Vector2 mouseOffset = Input.mousePosition.subtractClone(below);

            curMain = (int) mouseOffset.x / (int) categorySize.x;

            coolDown = 0.2f;
        }
    }
}
