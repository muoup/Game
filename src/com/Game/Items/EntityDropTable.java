package com.Game.Items;

import com.Util.Math.DeltaMath;

import java.util.ArrayList;

public class EntityDropTable {
    public ArrayList<ItemStack> stacks;
    public ArrayList<Double> chance;

    public EntityDropTable() {
        stacks = new ArrayList<ItemStack>();
        chance = new ArrayList<Double>();
    }

    public void add(ItemList item, int amount, double chance) {
        stacks.add(new ItemStack(item, amount));
        this.chance.add(chance);
    }

    public ArrayList<ItemStack> determineOutput() {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            double percent = chance.get(i);

            if (DeltaMath.range(0, 1) <= percent) {
                drops.add(stack);
            }
        }

        return drops;
    }
}
