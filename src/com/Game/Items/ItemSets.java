package com.Game.Items;

public class ItemSets {
    public static ItemSets none;
    public static ItemSets arrows;

    public int[] items;

    ItemSets(int[] items) {
        this.items = items;
    }

    public static void init() {
        none = new ItemSets(new int[0]);
        arrows = new ItemSets(new int[]{
                ItemList.arrow.ordinal(),
                ItemList.rockarrow.ordinal()
        });
        System.out.println(ItemList.arrow.ordinal());
    }
}
