package com.Game.GUI.Banking;

import com.Game.GUI.GUIWindow.GUIItemSlot;
import com.Game.GUI.GUIWindow.GUIWindow;
import com.Game.Items.ItemStack;
import com.Game.listener.Input;
import com.Util.Math.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BankingGUI extends GUIWindow {
    // TODO: Save bank to server after full client-side implementation
    // TODO: Determine positions for bank item slots to make them render correctly

    /**
     * GUIItemSlots currently being rendered within the bank.
     */
    private ArrayList<GUIItemSlot> itemStacks;

    public void init() {
        itemStacks = new ArrayList<GUIItemSlot>();

        setColor(new Color(162, 166, 56));
        setPosition(100, 100);
        setSize(1000, 800);

        for (GUIItemSlot slots : itemStacks) {
            addElement(slots);
        }
    }

    private void addItemSlot(ItemStack stack) {
        itemStacks.add(new GUIItemSlot(stack, Vector2.zero()));
    }

    /**
     * When the player attempts to deposit an item into their bank, call this method
     * @param stack The ItemStack that they are attempting to insert.
     */
    public void addItem(ItemStack stack) {
        for (GUIItemSlot slot : itemStacks) {
            if (slot.getID() == stack.getID()) {
                slot.addAmount(stack.getAmount());
                return;
            }
        }

        addItemSlot(stack);
    }

    public void update() {

    }

    public void render() {

    }
}
