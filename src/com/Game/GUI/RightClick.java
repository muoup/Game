package com.Game.GUI;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
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

public class RightClick {
    public static Vector2 draw = Vector2.zero();
    public static Vector2 deltaDraw = Vector2.zero();
    public static boolean render = false;
    public static boolean inventory = false;
    public static int groundItem;
    public static ArrayList<String> options;
    public static float percentBox;
    private static float maxWidth = 0;
    public static float coolDown = 0;

    public static void init() {
        options = new ArrayList<String>();
        percentBox = Settings.curResolution().y * 0.03f;
    }

    public static void render() {
        if (!render)
            return;

        Render.setFont(new Font("Arial", Font.BOLD, 14));

        for (int y = 0; y < options.size(); y++) {
            float dy = percentBox * y;
            Vector2 newPos = deltaDraw.addClone(0, percentBox * y);

            Render.setColor(Color.LIGHT_GRAY);
            Render.drawRectangle(newPos, new Vector2(maxWidth * 1.1f, percentBox));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(newPos, new Vector2(maxWidth * 1.1f, percentBox));
            Render.drawText(options.get(y), newPos.addClone(maxWidth * 0.05f, percentBox * 0.65f));
        }
    }
    public static void update() {
        if (Input.GetMouseDown(3)) {
            groundItem = 0;
            inventory = GUI.inGUI();

            if (inventory && GUI.curMain == 0)
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
                Vector2 deltaDraw = draw.subtractClone(GUI.GuiPos);

                int xx = (int) deltaDraw.x / GUI.IntBoxSize;
                int yy = (int) deltaDraw.y / GUI.IntBoxSize;

                ItemStack item = InventoryManager.inventory[xx + yy * 4];

                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;

                item.item.RightClickIdentities(xx + yy * 4, option);

                RightClick.coolDown = 0.1f;
                render = false;
            } else {
                if (MethodHandler.groundItems.size() == 0)
                    return;

                if (Vector2.distance(Main.player.position, MethodHandler.groundItems.get(groundItem).position) > 512) {
                    render = false;
                    return;
                }

                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;

                ItemStack selected = MethodHandler.groundItems.get(groundItem).stack.get(option);

                int amount = selected.amount;
                int removed = 0;

                if (selected.item.equipStatus != -1) {
                    ItemStack accessory = AccessoriesManager.getSlot(selected.item.equipStatus);

                    if (accessory.getID() == selected.getID()) {
                        int maxAdd = accessory.getMaxAmount() - accessory.getAmount();
                        int add = (amount > maxAdd) ? maxAdd : amount;

                        AccessoriesManager.getSlot(selected.item.equipStatus).amount += add;
                        amount -= add;

                        removed += add;
                    }
                }

                removed += InventoryManager.addItem(selected.item, amount);

                MethodHandler.groundItems.get(groundItem).stack.get(option).amount -= removed;
            }
        }
    }

    private static void inventoryRightClick() {
        maxWidth = 0;

        Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);

        int xx = (int) deltaMouse.x / GUI.IntBoxSize;
        int yy = (int) deltaMouse.y / GUI.IntBoxSize;

        ItemStack item = InventoryManager.inventory[xx + yy * 4];

        if (item.getID() == 0 || item.getAmount() == 0)
            return;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        options.clear();

        Render.setFont(Settings.itemFont);

        if (item.item.equipStatus != -1)
            options.add("Equip");

        for (String s : item.item.options) {
            if (Settings.sWidth(s) > maxWidth)
                maxWidth = Settings.sWidth(s);

            options.add(s);
        }

        if (maxWidth * 1.1f + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth * 1.1f;

        options.add("Drop");
        options.add("Examine");

        if (maxWidth == 0)
            maxWidth = Settings.sWidth("Examine");
    }

    private static void groundRightClick() {
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
            String content = item.getAmount() + " " + item.item.name;
            if (item.getAmount() > 1)
                content += "s";

            if (Settings.sWidth(content) > maxWidth)
                maxWidth = Settings.sWidth(content);

            options.add(content);
        }

        if (maxWidth * 1.1f + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth * 1.1f;
    }

    public static boolean onPopup() {
        Vector2 top = deltaDraw.subtractClone(15f, 15f);
        Vector2 bottom = deltaDraw.addClone(maxWidth + 15f, percentBox * options.size() + 15f);

        if (Input.mousePosition.compareTo(top) == 1 && Input.mousePosition.compareTo(bottom) == -1)
            return true;

        return false;
    }
}
