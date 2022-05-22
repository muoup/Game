package com.Game.GUI.Inventory;

import com.Game.GUI.Banking.BankingHandler;
import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemData;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.DataShortcut;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemDrag {

    enum DragState {
        none, inventoryClick, bankClick, draggingInventory, draggingBank
    }

    public static ItemData itemDrag; // The item that is being dragged by the mouse, after the mouse is released, set that state space to the item stack.
    private static BufferedImage itemDragImage = null;

    private static int inventoryIndex = -1; // The space that the item left when being dragged
    private static int bankIndex = -1;

    private static int selectedIndex = -1;

    private static Vector2 initMousePos = Vector2.zero();

    private static boolean mouse = false;
    private static boolean mouseDown = false;
    private static boolean click = true;

    private static DragState state = DragState.none;

    public static void init() {
        itemDrag = new ItemData();
    }

    // Old and no longer used, but kept for reference
    public static void renderOLD() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        if (itemDrag != null && mouse && !click) {
            // When the player attempts to drag the item, draw the image.
            InventoryManager.useIndex = -1;
            Vector2 rectPos = Input.mousePosition.subtractClone(itemDragImage.getWidth() / 2, itemDragImage.getWidth() / 2);

            Render.drawImage(itemDragImage, rectPos);

            if (itemDrag.getAmount() > 1) {
                String text = GUI.formatAmount(itemDrag.getAmount());

                Render.setFont(Settings.itemFont);
                Render.setColor(Color.BLACK);

                Render.drawText(text,
                        rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4)));
            }
        } else if (itemDrag.notEmpty() && !mouse && inCorrectBounds() && !click) {
            if (state == DragState.draggingInventory) {
                int index = getInventoryIndex();

                // Swap the two spaces in case there is already an item in the state slot.
                InventoryManager.swapSlots(inventoryIndex, index);

                resetVariables();
            } else if (state == DragState.draggingBank) {
                int index = getBankIndex();

                BankingHandler.swapSlots(bankIndex, index);

                resetVariables();
            }
        } else if (itemDrag.notEmpty() && !mouse && !inCorrectBounds() && !click) {
            // When the player attempts to drag the item outside the state
            resetVariables();
        } else if (itemDrag.notEmpty() && !mouse && click && RightClick.coolDown <= 0) {
            // When the player clicks on an item
            if (state == DragState.inventoryClick) {
                InventoryManager.leftClick(inventoryIndex);
            } else if (state == DragState.bankClick) {
                BankingHandler.leftClick(bankIndex);
            }
            resetVariables();
        } else if (inventoryIndex != -1 && !mouse) {
            resetVariables();
        }
    }

    // Old and no longer used, but kept for reference
    public static void updateOLD() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        mouse = Input.GetMouse(1);

        // If the mouse is in the inventory and there is no selected item, select that item.
        if (mouse && GUI.inGUI() && inventoryIndex == -1) {
            inventoryIndex = getInventoryIndex();
            itemDrag = InventoryManager.getStack(inventoryIndex);
            itemDragImage = Render.getScaledImage(itemDrag.getImage(), GUI.invSize);
            initMousePos = Input.mousePosition.clone();
            state = DragState.inventoryClick;
        } else if (mouse && GUI.inBank() && bankIndex == -1) {
            bankIndex = getBankIndex();

            if (bankIndex > BankingHandler.items.size() - 1) {
                bankIndex = -1;
                return;
            }

            itemDrag = BankingHandler.getItem(bankIndex);
            itemDragImage = Render.getScaledImage(itemDrag.getImage(), BankingHandler.getItemScale());
            initMousePos = Input.mousePosition.clone();
            state = DragState.bankClick;

            if (bankIndex < 0 || bankIndex > BankingHandler.items.size() - 1)
                resetVariables();
        }

        // If an item is selected and the mouse tries to drag it, drag the item.
        if (state != DragState.none && Vector2.distance(initMousePos, Input.mousePosition) > 16) {
            click = false;

            switch (state) {
                case inventoryClick:
                    InventoryManager.draggedIndex = inventoryIndex;
                    state = DragState.draggingInventory;
                    break;
                case bankClick:
                    BankingHandler.draggedIndex = bankIndex;
                    state = DragState.draggingBank;
                    break;
            }
        }
    }

    public static void render() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        if (itemDrag != null && itemDrag.notEmpty()) {
            Vector2 itemDragPos = Input.mousePosition.addClone(-itemDragImage.getWidth() / 2);
            Render.drawImage(itemDragImage, itemDragPos);

            if (itemDrag.amount > 1) {
                String text = GUI.formatAmount(itemDrag.getAmount());

                Render.setFont(Settings.itemFont);
                Render.setColor(Color.BLACK);

                Render.drawText(text,
                        itemDragPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4)));
            }
        }
    }

    public static void update() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        mouse = Input.GetMouse(1);
        mouseDown = Input.GetMouseDown(1);

        // If the user tries to select an item in a GUI/Inventory, and they are allowed to do so, select that item.
        if (mouseDown && selectedIndex == -1) {
            if (GUI.inGUI()) {
                selectedIndex = getInventoryIndex();

                if (selectedIndex > InventoryManager.inventory.length - 1)
                    selectedIndex = -1;

                state = DragState.inventoryClick;
            } else if (GUI.inBank()) {
                selectedIndex = getBankIndex();

                if (selectedIndex > BankingHandler.items.size() - 1)
                    selectedIndex = -1;

                state = DragState.bankClick;
            }

            initMousePos = Input.mousePosition.clone();
        }

        if (selectedIndex == -1) {
            if (state != DragState.none)
                resetVariables();

            return;
        }

        // If the player drags an item outside the item area, begin to drag the item
        if (Vector2.distance(initMousePos, Input.mousePosition) > 16) {
            switch (state) {
                case inventoryClick:
                    InventoryManager.draggedIndex = inventoryIndex;
                    state = DragState.draggingInventory;
                    itemDrag = InventoryManager.getStack(selectedIndex);
                    itemDragImage = Render.getScaledImage(itemDrag.getImage(), GUI.invSize);
                    break;
                case bankClick:
                    BankingHandler.draggedIndex = bankIndex;
                    state = DragState.draggingBank;
                    itemDrag = BankingHandler.getItem(selectedIndex);
                    itemDragImage = Render.getScaledImage(itemDrag.getImage(), BankingHandler.getItemScale());
                    break;
            }
        }

        // If an item has been clicked/dragged, and the mouse is released, either swap items or click the item
        if (!mouse) {
            if (inCorrectBounds()) {
                int index;

                switch (state) {
                    case draggingInventory:
                        index = getInventoryIndex();
                        InventoryManager.swapSlots(selectedIndex, index);
                        break;
                    case draggingBank:
                        index = getBankIndex();
                        BankingHandler.swapSlots(selectedIndex, index);
                        break;
                    case inventoryClick:
                        InventoryManager.leftClick(selectedIndex);
                        break;
                    case bankClick:
                        BankingHandler.leftClick(selectedIndex);
                        break;
                }
            }

            resetVariables();
        }
    }

    private static void resetVariables() {
        click = true;
        itemDrag = new ItemData();
        inventoryIndex = -1;
        bankIndex = -1;
        selectedIndex = -1;
        state = DragState.none;
        InventoryManager.draggedIndex = -1;
        BankingHandler.draggedIndex = -1;
    }

    private static int getInventoryIndex() {
        Vector2 mousePos = Input.mousePosition.subtractClone(GUI.GuiPos);

        int x = (int) mousePos.x / GUI.intBoxSize;
        int y = (int) mousePos.y / GUI.intBoxSize;

        return x + y * 4;
    }

    private static int getBankIndex() {
        Vector2 mousePos = Input.mousePosition.subtractClone(BankingHandler.getBeginPos());

        int x = (int) mousePos.x / (BankingHandler.padding + GUI.intBoxSize);
        int y = (int) mousePos.y / (BankingHandler.padding + GUI.intBoxSize);

        if (x > BankingHandler.maxRow - 1)
            return Integer.MAX_VALUE;

        int ret = x + y * BankingHandler.maxRow;

        return ret;
    }

    private static boolean inCorrectBounds() {
        return (GUI.inGUI() && DataShortcut.multiCompare(state, DragState.draggingInventory, DragState.inventoryClick))
                || (GUI.inBank() && DataShortcut.multiCompare(state, DragState.draggingBank, DragState.bankClick));
    }

    public static int invDragIndex() {
        return (state == DragState.draggingInventory) ? selectedIndex : -1;
    }

    public static int bankDragIndex() {
        return (state == DragState.draggingBank) ? selectedIndex : -1;
    }
}