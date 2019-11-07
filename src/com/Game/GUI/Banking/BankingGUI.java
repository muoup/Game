package com.Game.GUI.Banking;

import com.Game.GUI.GUIWindow.GUIItemSlot;
import com.Game.GUI.GUIWindow.GUILibrary;
import com.Game.GUI.GUIWindow.GUIWindow;
import com.Game.Items.ItemStack;
import com.Util.Math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class BankingGUI extends GUIWindow {
    // TODO: Save bank to server after full client-side implementation
    // TODO: Determine positions for bank item slots to make them render correctly

    /**
     * GUIItemSlots currently being rendered within the bank.
     */
    private ArrayList<GUIItemSlot> itemStacks;

    public BankingGUI() {
        itemStacks = new ArrayList<GUIItemSlot>();

        setColor(new Color(166, 134, 76));
        setPosition(5, 5);
        setSize(65, 40);
    }

    private void addItemSlot(ItemStack stack) {
        itemStacks.add(new GUIItemSlot(stack, Vector2.zero()));
    }

    /**
     * When the player attempts to deposit an item into their bank, call this method
     * @param stack The ItemStack that they are attempting to insert.
     */
    public static void addItem(ItemStack stack) {
        GUILibrary.bankingGUI.inputItem(stack);
    }

    public void inputItem(ItemStack stack) {
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
        drawItems(itemStacks, new Vector2(25), getSize().subtractClone(25, 25), 5);
    }
}
