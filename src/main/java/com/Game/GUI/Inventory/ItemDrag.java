package com.Game.GUI.Inventory;

import com.Game.GUI.Banking.BankingHandler;
import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemData;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
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

    private static Vector2 initMousePos = Vector2.zero();

    private static boolean drag = false;
    private static boolean click = true;

    private static DragState state = DragState.none;

    public static void init() {
        itemDrag = new ItemData();
    }

    public static void render() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        if (itemDrag != null && drag && !click) {
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
        } else if (itemDrag.notEmpty() && !drag && inCorrectBounds() && !click) {
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
        } else if (itemDrag.notEmpty() && !drag && !inCorrectBounds() && !click) {
            // When the player attempts to drag the item outside the state
            resetVariables();
        } else if (itemDrag.notEmpty() && !drag && click) {
            // When the player clicks on an item
            if (state == DragState.inventoryClick) {
                InventoryManager.leftClick(inventoryIndex);
            } else if (state == DragState.bankClick) {
                BankingHandler.leftClick(bankIndex);
            }
            resetVariables();
        } else if (inventoryIndex != -1 && !drag) {
            resetVariables();
        }
    }

    public static void update() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        drag = Input.GetMouse(1);

        // If the mouse is in the inventory and there is no selected item, select that item.
        if (drag && GUI.inGUI() && inventoryIndex == -1) {
            inventoryIndex = getInventoryIndex();
            itemDrag = InventoryManager.getStack(inventoryIndex);
            itemDragImage = Render.getScaledImage(itemDrag.getImage(), GUI.invSize);
            initMousePos = Input.mousePosition.clone();
            state = DragState.inventoryClick;
        } else if (drag && GUI.inBank() && bankIndex == -1) {
            bankIndex = getBankIndex();

            if (bankIndex > BankingHandler.items.size() - 1)
                return;

            itemDrag = BankingHandler.getItem(bankIndex);
            itemDragImage = Render.getScaledImage(itemDrag.getImage(), BankingHandler.getItemScale());
            initMousePos = Input.mousePosition.clone();
            state = DragState.bankClick;

            if (bankIndex < 0 || bankIndex > BankingHandler.items.size() - 1)
                resetVariables();
        }

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

    private static void resetVariables() {
        click = true;
        itemDrag = new ItemData();
        inventoryIndex = -1;
        bankIndex = -1;
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

        return x + y * BankingHandler.maxRow;
    }

    private static boolean inCorrectBounds() {
        return (GUI.inGUI() && state == DragState.draggingInventory) || (GUI.inBank() && state == DragState.draggingBank);
    }
}