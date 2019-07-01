package com.Game.GUI;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.GroundItem;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RightClick {
    public static Vector2 draw = Vector2.zero();
    public static Vector2 deltaDraw = Vector2.zero();
    public static boolean render = false;
    public static boolean inventory = false;
    public static int groundItem;
    public static ArrayList<String> options;
    public static float percentBox = 0.03f;
    private static float maxWidth = 0;
    public static float coolDown = 0;

    public static void init() {
        options = new ArrayList<String>();
    }

    public static void render() {
        if (!render)
            return;

        Render.setFont(new Font("Arial", Font.BOLD, 14));

        for (int y = 0; y < options.size(); y++) {
            float dy = Settings.curResolution().y * percentBox * y;
            Vector2 newPos = deltaDraw.addClone(0, Settings.curResolution().y * percentBox * y);

            Render.setColor(Color.LIGHT_GRAY);
            Render.drawRectangle(newPos, new Vector2(maxWidth * 1.1f, Settings.curResolution().y * percentBox));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(newPos, new Vector2(maxWidth * 1.1f, Settings.curResolution().y * percentBox));
            Render.drawText(options.get(y), newPos.addClone(maxWidth * 0.05f, Settings.curResolution().y * percentBox * 0.65f));
        }
    }

    public static void update() {
        if (Input.GetMouseDown(3)) {
            groundItem = 0;
            inventory = GUI.inGUI();

            if (inventory)
                inventoryRightClick();

            if (!inventory)
                groundRightClick();
        }

        if (render && !onPopup()) {
            coolDown = 0.1f;
            render = false;
        }
        if (render && Input.GetMouseDown(1)) {
            // Select Right Click Option
            if (inventory) {
                Vector2 deltaDraw = draw.subtractClone(GUI.mainPos);

                int xx = (int) deltaDraw.x / GUI.inputSize;
                int yy = (int) deltaDraw.y / GUI.inputSize;

                ItemStack item = InventoryManager.inventory[xx + yy * 4];

                int option = (int) Input.mousePosition.subtract(draw).y / (int) (Settings.curResolution().y * percentBox);

                item.item.RightClickIdentities(xx + yy * 4, option);

                RightClick.coolDown = 0.1f;
                render = false;
            } else {
                if (Vector2.distance(Main.player.position, MethodHandler.groundItems.get(groundItem).position) > 512) {
                    render = false;
                    return;
                }

                int option = (int) Input.mousePosition.subtract(draw).y / (int) (Settings.curResolution().y * percentBox);

                ItemStack selected = MethodHandler.groundItems.get(groundItem).stack.get(option);

                int removed = InventoryManager.addItem(selected.item, selected.amount);

                MethodHandler.groundItems.get(groundItem).stack.get(option).amount -= removed;
            }
        }
    }

    public static void inventoryRightClick() {
        maxWidth = 0;

        Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.mainPos);

        int xx = (int) deltaMouse.x / GUI.inputSize;
        int yy = (int) deltaMouse.y / GUI.inputSize;

        ItemStack item = InventoryManager.inventory[xx + yy * 4];

        if (item.getID() == 0 || item.getAmount() == 0)
            return;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        options.clear();

        Render.setFont(Settings.itemFont);

        for (String s : item.item.options) {
            if (Settings.sWidth(s) > maxWidth)
                maxWidth = Settings.sWidth(s);

            options.add(s);
        }

        if (maxWidth * 1.1f + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth * 1.1f;

        options.add("Drop");
        options.add("Examine");
    }

    public static void groundRightClick() {
        GroundItem hover = GroundItem.mouseOver();

        if (hover == null)
            return;

        groundItem = MethodHandler.groundItems.indexOf(hover);

        maxWidth = 0;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        options.clear();

        Render.setFont(Settings.itemFont);

        for (ItemStack item : hover.stack) {
            String content = item.item.name + " > " + item.getAmount();

            if (Settings.sWidth(content) > maxWidth)
                maxWidth = Settings.sWidth(content);

            options.add(content);
        }

        if (maxWidth * 1.1f + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth * 1.1f;
    }

    public static boolean onPopup() {
        Vector2 top = deltaDraw.subtractClone(15f, 15f);
        Vector2 bottom = deltaDraw.addClone(maxWidth + 15f, Settings.curResolution().y * percentBox * options.size() + 15f);

        if (Input.mousePosition.compareTo(top) == 1 && Input.mousePosition.compareTo(bottom) == -1)
            return true;

        return false;
    }
}
