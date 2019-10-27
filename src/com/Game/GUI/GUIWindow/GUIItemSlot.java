package com.Game.GUI.GUIWindow;

import com.Game.Items.Item;
import com.Game.Items.ItemStack;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;

/**
 * Represents a single item render on a GUI. Add one of these to a GUIWindow
 * to render an ItemStack;
 */
public class GUIItemSlot extends GUIElement {
    private ItemStack stack;
    private Vector2 position;
    private Vector2 renderSize;
    private boolean bordered = false;
    private boolean renderItem = true;
    private boolean draggable = false;

    public GUIItemSlot(ItemStack stack, Vector2 position, Vector2 renderSize) {
        this.stack = stack;
        this.position = position;
        this.renderSize = renderSize;
    }

    public GUIItemSlot(Item item, Vector2 position, Vector2 renderSize) {
        this.stack = new ItemStack(item, -1);
        this.position = position;
        this.renderSize = renderSize;
    }

    public GUIItemSlot(ItemStack item, Vector2 position) {
        this.stack = item;
        this.position = position;
        this.renderSize = Render.getImageSize(stack.getImage());
    }

    public void setBordered(boolean bordered) {
        this.bordered = bordered;
    }

    public void setEnabled(boolean enabled) {
        this.renderItem = enabled;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getID() {
        return stack.getID();
    }

    public Image getImage() {
        return stack.getImage();
    }

    public int getAmount() {
        return stack.getAmount();
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public void setAmount(int amount) {
        stack.amount = amount;
    }

    public void addAmount(int amount) {
        setAmount(amount + getAmount());
    }

    public void render() {
        if (bordered) {
            Render.drawBorderedRect(position, renderSize);
        }

        if (renderItem)
            Render.drawImage(Render.getScaledImage(stack.getImage(), renderSize), position);
    }

    public void update() {

    }
}
