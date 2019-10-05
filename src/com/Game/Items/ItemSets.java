package com.Game.Items;

public enum ItemSets {
    none(new int[0]),
    arrows(new int[]{
        3, // Arrow
        14 // Rock Arrow
    });

    public int[] items;

    ItemSets(int[] items) {
        this.items = items;
    }
}
