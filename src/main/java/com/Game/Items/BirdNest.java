package com.Game.Items;

import com.Game.Items.Consumables.Loot.LootGenerator;

public class BirdNest extends LootGenerator {
    public BirdNest(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        // Crescendo of gold amounts. Probably a little too generous.
        table.add(ItemList.gold, 125, .75f);
        table.add(ItemList.gold, 500, .50f);
        table.add(ItemList.gold, 750, .25f);
        table.add(ItemList.gold, 2500, .5);

        // I don't know if this is at all balanced but it seems like a decent idea.
        table.add(ItemList.crystalBow, 1, .0025);
    }
}
