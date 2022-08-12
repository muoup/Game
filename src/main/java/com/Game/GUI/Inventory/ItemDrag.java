package com.Game.GUI.Inventory;

import com.Game.GUI.Banking.BankingHandler;
import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemData;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.DataShortcut;
import com.Util.Other.Render;

import java.awt.*;
import java.awt.event.KeyEvent;
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

    private static DragState state = DragState.none;

    public static void init() {
        itemDrag = new ItemData();
    }

    public static void render() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        if (itemDrag != null && itemDrag.notEmpty()) {
            Vector2 itemDragPos = Input.mousePosition.addClone(-itemDragImage.getWidth() / 2);
            GUI.drawItem(itemDragPos, itemDrag);
        }

        if (state == DragState.draggingBank) {
            double index = getBankIndex();

            if (index >= BankingHandler.items.size())
                return;

            Render.setColor(Color.YELLOW);

            if (index % 1 == 0) {
                int i = (int) index;
                int x = i % BankingHandler.maxRow;
                int y = i / BankingHandler.maxRow;

                Vector2 itemPos = BankingHandler.getItemPos(x, y);

                Render.drawRectOutline(itemPos, GUI.invSize);
            } else {
                int i = (int) index;
                int x = i % BankingHandler.maxRow + 1;
                int y = i / BankingHandler.maxRow;

                Vector2 itemPos = BankingHandler.getItemPos(x, y).subtractClone(BankingHandler.padding / 2, 0);

                Render.drawRectangle(itemPos, new Vector2(1, GUI.intBoxSize));
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

                if (selectedIndex > InventoryManager.inventory.length - 1
                        || !InventoryManager.getStack(selectedIndex).notEmpty())
                    selectedIndex = -1;

                state = DragState.inventoryClick ;
            } else if (GUI.inBank()) {
                double sel = getBankIndex();

                if (sel % 1 != 0)
                    return;

                selectedIndex = (int) sel;

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
                    if (GUI.curMain != 0)
                        break;

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
                double index;

                switch (state) {
                    case draggingInventory:
                        if (GUI.curMain != 0)
                            return;

                        index = getInventoryIndex();
                        InventoryManager.swapSlots(selectedIndex, (int) index);
                        break;
                    case draggingBank:
                        index = getBankIndex();

                        BankingHandler.swapSlots(selectedIndex, index);
                        break;
                    case inventoryClick:
                        if (GUI.curMain != 0)
                            return;

                        if (GUI.renderBank) {
                            BankingHandler.depositItem(selectedIndex, Input.GetKey(KeyEvent.VK_SHIFT) || Input.GetKey(KeyEvent.VK_CONTROL)
                                    ? Integer.MAX_VALUE : 1);
                        } else
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

    private static double getBankIndex() {
        Vector2 mousePos = Input.mousePosition.subtractClone(BankingHandler.getBeginPos());

        int x = (int) mousePos.x / (BankingHandler.padding + GUI.intBoxSize);
        int y = (int) mousePos.y / (BankingHandler.padding + GUI.intBoxSize);

        double ret = x + y * BankingHandler.maxRow;

        if (mousePos.x - (BankingHandler.padding + GUI.intBoxSize) * x < BankingHandler.padding)
            ret -= 0.5;
        else if (mousePos.x - (BankingHandler.padding + GUI.intBoxSize) * x > BankingHandler.padding + GUI.intBoxSize)
            ret += 0.5;

        if (ret >= BankingHandler.maxRow * (y + 1))
            return Integer.MAX_VALUE;

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