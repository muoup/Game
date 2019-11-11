package com.Game.Items;

import com.Util.Math.DeltaMath;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Drop Tables are used to generate an ArrayList of item drops with the inputted data.
 * Add use setItems() and setDrops() with varargs to set items and itemDropChance.
 * And use getItemDrops() to get a random set of items from DropTable
 */
public class DropTable {
    public ArrayList<ItemStack> items;
    public ArrayList<Float> itemDropChance;

    public DropTable() {
        items = new ArrayList();
        itemDropChance = new ArrayList();
    }

    /**
     * If the drop table is only used for one ItemList and one amount, just use this
     * @param item The one item in the drop table.
     */
    public DropTable(ItemList item) {
        items = new ArrayList();
        itemDropChance = new ArrayList();

        setItems(item);
        setDropChances(100);
    }

    /**
     * Varargs are just a list of any amount of parameters of [ItemLists]
     * Just keep listing them like you would with printf or String.format
     * @param itemList VarArgs list of items
     */
    public void setItems(ItemStack... itemList) {
        items.addAll(Arrays.asList(itemList));
    }

    /**
     * Varargs are just a list of any amount of parameters of [ItemLists]
     * Just keep listing them like you would with printf or String.format
     * @param itemList VarArgs list of items
     */
    public void setItems(ItemList... itemList) {
        for (ItemList item : itemList) {
            items.add(new ItemStack(item, 1));
        }
    }

    /**
     * Varargs are just a list of any amount of parameters of [ItemLists]
     * Just keep listing them like you would with printf or String.format
     * @param dropChances VarArgs of drop chances (out of 100)
     */
    public void setDropChances(Float... dropChances) {
        itemDropChance.addAll(Arrays.asList(dropChances));
    }

    /**
     * Varargs are just a list of any amount of parameters of [ItemLists]
     * Just keep listing them like you would with printf or String.format
     * @param dropChances VarArgs of drop chances (out of 100)
     */
    public void setDropChances(Integer... dropChances) {
        for (Integer i : dropChances) {
            itemDropChance.add(i.floatValue());
        }
    }

    /**
     * May print an error if the {@link #setDropChances(Float...)} and {@link #setItems(ItemStack...)}
     * are not the same length.
     * @return ArrayList of drops from the drop table inputted.
     */
    public ArrayList<ItemStack> getDrop() {
        ArrayList<ItemStack> stack = new ArrayList<ItemStack>();

        if (items.size() != itemDropChance.size()) {
            System.err.println("Drop Table failed because the item list and item drop chance lists are not the same length.");
            return stack;
        }

        for (int i = 0; i < items.size(); i++) {
            float random = DeltaMath.range(0, 100);

            if (random <= itemDropChance.get(i)) {
                stack.add(items.get(i));
            }
        }

        return stack;
    }
}
