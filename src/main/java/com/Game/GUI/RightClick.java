package com.Game.GUI;

import com.Game.GUI.Banking.BankingHandler;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryDrag;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Shop.Shop;
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
    public static UsableGameObject object = UsableGameObject.empty;
    public static ArrayList<String> options;
    public static float percentBox;
    private static float maxWidth = 0;
    public static float maxMultiplier = 1.1f;
    public static float coolDown = 0;
    public static RightClickRunnable run = null;

    public static void init() {
        options = new ArrayList<String>();
        percentBox = Settings.curResolution().y * 0.03f;
    }

    public static void render() {
        if (!render)
            return;

        if (options.size() == 0) {
            reset();
            return;
        }

        Render.setFont(Settings.groundFont);

        for (int y = 0; y < options.size(); y++) {
            Vector2 newPos = deltaDraw.addClone(0, percentBox * y);

            Render.setColor(Color.LIGHT_GRAY);
            Render.drawRectangle(newPos, new Vector2(maxWidth, percentBox));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(newPos, new Vector2(maxWidth, percentBox));
            Render.drawText(options.get(y), newPos.addClone(maxWidth * (maxMultiplier - 1) / 2, percentBox * 0.65f));
        }
    }

    public static void update() {
        if (!render)
            state = State.none;

        if (Input.GetMouseDown(3) && state == State.none) {
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
                reset();
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

                reset();
            } else if (state == State.object) {
                if (Vector2.distance(Main.player.position, object.position) > 512) {
                    render = false;
                    return;
                }

                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;

                if (option > options.size() - 1)
                    return;

                object.onOption(option);

                reset();
            } else if (state == State.misc) {
                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;

                if (option > options.size() - 1)
                    return;

                if (run != null)
                    run.run(option);

                reset();
            }
        }
    }

    private static void reset() {
        render = false;
        run = null;
        state = State.none;
    }

    public static void customRightClick(ArrayList<String> optionArray, RightClickRunnable run) {
        render = true;
        state = State.misc;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();
        RightClick.run = run;

        options.clear();

        Render.setFont(Settings.groundFont);

        options.addAll(optionArray);

        for (String s : options) {
            if (Render.getStringWidth(s) > maxWidth)
                maxWidth = Render.getStringWidth(s);
        }

        maxWidth *= maxMultiplier;

        if (deltaDraw.x + maxWidth > Settings.curResolution().x * 0.9f)
            deltaDraw.x -= maxWidth;
    }

    public static void customRightClick(RightClickRunnable run, String... options) {
        ArrayList<String> optionArray = new ArrayList();

        for (String o : options)
            optionArray.add(o);

        customRightClick(optionArray, run);
    }

    private static void inventoryRightClick() {
        if (GUI.currentShop != Shop.empty) {
            if (inventoryStack().getID() != 0) {
                Shop.selected = inventoryStack();
                customRightClick((int option) -> GUI.currentShop.sellOption(option), "Sell 1", "Sell 10", "Sell 50", "Sell 100", "Sell All");
            }
            return;
        }  else if (GUI.renderBank) {
            if (inventoryStack().getID() != 0) {
                BankingHandler.hover = inventoryStack();
                customRightClick((int option) -> BankingHandler.rightClickOption(option), "Deposit 1", "Deposit 10", "Deposit 50", "Deposit 100", "Deposit All");
            }
            return;
        }

        if (InventoryDrag.itemDrag.getID() != 0)
            return;

        maxWidth = 0;

        state = State.inventory;

        ItemStack item = inventoryStack();

        if (item.getID() == 0 || item.getAmount() == 0)
            return;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        options.clear();

        Render.setFont(Settings.groundFont);

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

    public static ItemStack inventoryStack() {
        Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);

        int xx = (int) deltaMouse.x / GUI.IntBoxSize;
        int yy = (int) deltaMouse.y / GUI.IntBoxSize;

        return InventoryManager.inventory[xx + yy * 4];
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

        Render.setFont(Settings.groundFont);

        for (ItemStack item : hover.stack) {
            String content = item.getAmount() + " " + item.getItem().name;
            if (item.getAmount() > 1)
                content += "s";

            if (Settings.sWidth(content) > maxWidth)
                maxWidth = Settings.sWidth(content);

            options.add(content);
        }

        maxWidth *= maxMultiplier;

        if (maxWidth + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth;
    }

    private static void objectRightClick(UsableGameObject object) {
        RightClick.object.loseFocus();
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

        maxWidth *= maxMultiplier;

        Render.setFont(Settings.groundFont);

        if (maxWidth + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth;
    }

    public static boolean onPopup() {
        Vector2 top = deltaDraw.subtractClone(15f, 15f);
        Vector2 bottom = deltaDraw.addClone(maxWidth + 15f, percentBox * options.size() + 15f);

        return Input.mousePosition.compareTo(top) == 1 && Input.mousePosition.compareTo(bottom) == -1;
    }
}

enum State {
    none, inventory, groundItem, object, misc
}