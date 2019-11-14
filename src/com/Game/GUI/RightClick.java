package com.Game.GUI;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryDrag;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Object.GameObject;
import com.Game.Object.UsableGameObject;
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
    public static State state = State.none;
    public static int groundItem;
    public static UsableGameObject object;
    public static ArrayList<String> options;
    public static float percentBox;
    private static float maxWidth = 0;
    public static float maxMultiplier = 1.1f;
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
            Vector2 newPos = deltaDraw.addClone(0, percentBox * y);

            Render.setColor(Color.LIGHT_GRAY);
            Render.drawRectangle(newPos, new Vector2(maxWidth, percentBox));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(newPos, new Vector2(maxWidth, percentBox));
            Render.drawText(options.get(y), newPos.addClone(maxWidth * (maxMultiplier - 1), percentBox * 0.65f));
        }
    }

    public static void update() {
        if (!render)
            state = null;

        if (Input.GetMouseDown(3)) {
            groundItem = 0;
            state = (GUI.inGUI()) ? State.inventory : State.none;

            if (state == State.inventory && GUI.curMain == 0)
                inventoryRightClick();

            if (state == State.none)
                groundRightClick();
        }

        if (render && !onPopup()) {
            coolDown = 0.1f;
            render = false;
        }

        if (render && Input.GetMouse(1)) {
            // Select Right Click Option
            if (state == State.inventory) {
                Vector2 deltaDraw = draw.subtractClone(GUI.GuiPos);

                int xx = (int) deltaDraw.x / GUI.IntBoxSize;
                int yy = (int) deltaDraw.y / GUI.IntBoxSize;

                ItemStack item = InventoryManager.inventory[xx + yy * 4];

                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;

                item.getItem().RightClickIdentities(xx + yy * 4, option);

                RightClick.coolDown = 0.1f;
                render = false;
            } else if (state == State.groundItem) {
                if (MethodHandler.groundItems.size() - 1 < groundItem) {
                    render = false;
                    return;
                }

                if (Vector2.distance(Main.player.position, MethodHandler.groundItems.get(groundItem).position) > 512) {
                    render = false;
                    return;
                }

                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;

                GroundItem ground = MethodHandler.groundItems.get(groundItem);

                if (option > ground.stack.size() - 1)
                    return;

                ItemStack selected = ground.stack.get(option);

                int amount = selected.getAmount();
                int removed = 0;

                if (selected.getEquipmentStatus() != -1) {
                    ItemStack accessory = AccessoriesManager.getSlot(selected.equipStatus);

                    if (accessory.getID() == selected.getID()) {
                        int maxAdd = accessory.getMaxAmount() - accessory.getAmount();
                        int add = (amount > maxAdd) ? maxAdd : amount;

                        AccessoriesManager.addAmount(selected.getEquipmentStatus(), add);
                        amount -= add;

                        removed += add;
                    }
                }

                removed += InventoryManager.addItem(selected.getItem(), amount);

                MethodHandler.groundItems.get(groundItem).stack.get(option).amount -= removed;
            } else if (state == State.object) {
                if (Vector2.distance(Main.player.position, object.position) > 512) {
                    render = false;
                    return;
                }

                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;

                if (option > options.size() - 1)
                    return;

                object.onOption(option);
            }
        }
    }

    private static void inventoryRightClick() {
        if (InventoryDrag.itemDrag.getID() != 0)
            return;

        maxWidth = 0;

        state = State.inventory;

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

        if (item.getEquipmentStatus() != -1)
            options.add("Equip");

        options.addAll(item.options);

        for (String s : options) {
            if (Render.getStringWidth(s) > maxWidth)
                maxWidth = Render.getStringWidth(s);
        }

        maxWidth = Math.max(Settings.sWidth("Examine"), maxWidth) * maxMultiplier;

        if (deltaDraw.x + maxWidth > Settings.curResolution().x * 0.9f)
            deltaDraw.x -= maxWidth;

        options.add("Drop");
        options.add("Examine");
    }

    private static void groundRightClick() {
        GroundItem hover = GroundItem.mouseOver();
        UsableGameObject object = GameObject.mouseOver();

        if (hover == null && object != null) {
            objectRightClick(object);
            return;
        } else if (hover == null)
            return;

        state = State.groundItem;

        groundItem = MethodHandler.groundItems.indexOf(hover);

        maxWidth = 0;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        options.clear();

        Render.setFont(Settings.itemFont);

        for (ItemStack item : hover.stack) {
            String content = item.getAmount() + " " + item.getItem().name;
            if (item.getAmount() > 1)
                content += "s";

            if (Settings.sWidth(content) > maxWidth)
                maxWidth = Settings.sWidth(content);

            options.add(content);
        }

        if (maxWidth * 1.1f + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth * 1.1f;
    }

    private static void objectRightClick(UsableGameObject object) {
        RightClick.object = object;

        if (object == null)
            return;

        state = State.object;

        maxWidth = 0;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        maxWidth = 0;

        object.onRightClick();

        options.clear();
        options.addAll(object.options);

        for (String s : options) {
            maxWidth = Math.max(maxWidth, Render.getStringWidth(s));
        }

        Render.setFont(Settings.itemFont);

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

enum State {
    none, inventory, groundItem, object
}