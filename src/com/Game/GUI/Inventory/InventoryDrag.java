package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.image.BufferedImage;

public class InventoryDrag {
    public static ItemStack itemDrag; // The item that is being dragged by the mouse, after the mouse is released, set that inventory space to the item stack.
    private static int inventoryIndex = -1; // The space that the item left when being dragged
    private static Vector2 initMousePos = Vector2.zero();
    private static boolean drag = false;
    private static boolean click = true;
    private static BufferedImage itemDragImage = null;
    private static float timer = 0f;

    public static void init() {
        itemDrag = Item.emptyStack();
    }

    public static void render() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        if (itemDrag.getID() != 0 && drag && !click) { // If the itemstack is not empty, render it by the mouse
            Vector2 rectPos = Input.mousePosition.subtractClone(itemDragImage.getWidth() / 2, itemDragImage.getWidth() / 2);

            Render.drawImage(itemDragImage, rectPos);

            if (itemDrag.getAmount() > 1) {

                String text = InventoryManager.formatAmount(itemDrag.getAmount());

                Render.setFont(Settings.itemFont);

                Render.drawText(text,
                        rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4)));
            }

        } else if (itemDrag.getID() != 0 && !drag && GUI.inGUI() && !click) {
            int index = getInventoryIndex();

            // Swap the two spaces in case there is already an item in the inventory slot.
            InventoryManager.setItem(inventoryIndex, InventoryManager.getStack(index).clone());
            InventoryManager.setItem(index, itemDrag.clone());

            resetVariables();
        } else if (itemDrag.getID() != 0 && !drag && !GUI.inGUI() && !click) {
            InventoryManager.inventory[inventoryIndex] = itemDrag.clone();
            itemDrag = Item.emptyStack();
            click = true;

            resetVariables();
        } else if (itemDrag.getID() != 0 && !drag && click) {
            InventoryManager.inventory[inventoryIndex] = itemDrag.clone();
            InventoryManager.inventory[inventoryIndex].getItem().ClickIdentities(inventoryIndex);

            resetVariables();
        }
    }

    private static void resetVariables() {
        click = true;
        itemDrag = Item.emptyStack();
        inventoryIndex = -1;
        timer = 0f;
    }

    public static void update() {
        if (RightClick.render)
            return;

        drag = (!drag) ? Input.GetMouseDown(1) : true;

        if (!Input.GetMouse(1))
            drag = false;

        if (drag && GUI.inGUI() && inventoryIndex == -1) {
            inventoryIndex = getInventoryIndex();
            itemDrag = InventoryManager.inventory[inventoryIndex];
            itemDragImage = Render.getScaledImage(itemDrag.getItem().image, GUI.invSize);
            initMousePos = Input.mousePosition.clone();
        }

        if (itemDrag.getID() != 0 && (timer > 0.1f || Vector2.distance(initMousePos, Input.mousePosition) > 16)) {
            click = false;
            InventoryManager.setItem(inventoryIndex, Item.emptyStack());
        }

        if (inventoryIndex != -1) {
            timer += 1 / Main.fps;
        }
    }

    private static int getInventoryIndex() {
        Vector2 mousePos = Input.mousePosition.subtractClone(GUI.GuiPos);

        int x = (int) mousePos.x / GUI.IntBoxSize;
        int y = (int) mousePos.y / GUI.IntBoxSize;

        return x + y * 4;
    }
}
