package com.Game.Object.Utilities;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;
import com.Game.Object.UsableGameObject;
import com.Util.Other.Settings;

import java.util.Timer;

public class Furnace extends UsableGameObject {
    int craftAmount = -1;
    int craftOption = -1;

    private Runnable command;
    private Timer repeat;

    public Furnace(int x, int y) {
        super(x, y);
        image = getImage("Furnace.png");
        repeat = new Timer();
        setScale(64, 84);
    }

    public void onRightClick() {
        options.clear();

        int stone = InventoryManager.getAmount(ItemList.stone, 0);
        int copper = InventoryManager.getAmount(ItemList.copperOre, 0);
        int tin = InventoryManager.getAmount(ItemList.tinOre, 0);
        int iron = InventoryManager.getAmount(ItemList.ironOre, 0);
        int gold = InventoryManager.getAmount(ItemList.goldOre, 0);

        if (stone >= 1)
            options.add("Smelt Stone");

        if (copper >= 1 && tin >= 1)
            options.add("Smelt Bronze");
        else if ((copper >= 1 || tin >= 1))
            options.add("You must have both copper and tin to create bronze.");

        if (iron >= 1)
            options.add("Smelt Iron");

        if (gold >= 1)
            options.add("Smelt Stone");

        if (options.size() == 0)
            options.add("You need raw ores to smelt.");
    }

    public void loseFocus() {
        command = null;
        repeat.cancel();
        repeat.purge();
    }

    public void onOption(int option) {
        String action = options.get(option);
        boolean completeAction = true;
        switch (action) {
            case "Smelt Stone":
                command = () -> craft(ItemList.stone, 1, 10);
                break;

            case "Smelt Bronze":
                command = () -> smeltBronze();
                break;

            case "Smelt Iron":
                command = () -> craft(ItemList.ironOre, 1, 50);
                break;

            case "Smelt Gold":
                command = () -> craft(ItemList.goldOre, 1, 65);
                break;

            default:
                completeAction = false;
                break;
        }

        if (completeAction)
            command.run();
    }

    private void smeltBronze() {
        int slot = InventoryManager.getIndexFirst(ItemList.copperOre, 0, ItemList.tinOre, 0);

        if (slot == -1) {
            repeat.cancel();
            repeat.purge();
            return;
        }

        ItemList remove = (InventoryManager.getItem(slot).getItemList() == ItemList.copperOre) ?
                ItemList.tinOre : ItemList.copperOre;

        InventoryManager.setItem(slot, new ItemStack(ItemList.copperOre, 1, 1));
        InventoryManager.removeItem(remove, 1);
        Skills.addExperience(Skills.SMITHING, 45);

        repeat.schedule(Settings.wrap(command), 1250);
    }

    public void craft(ItemList ore, int data, int experience) {
        int slot = InventoryManager.getIndex(ore.singleStack());

        if (slot == -1) {
            repeat.cancel();
            repeat.purge();
            return;
        }

        InventoryManager.setData(slot, data);
        Skills.addExperience(Skills.SMITHING, experience);

        repeat.schedule(Settings.wrap(command), 2500);
    }
}
