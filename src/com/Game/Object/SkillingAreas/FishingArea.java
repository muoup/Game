package com.Game.Object.SkillingAreas;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.ItemList;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Object.GameObject;
import com.Util.Math.DeltaMath;

import java.awt.image.BufferedImage;

public class FishingArea extends GameObject {

    public static FishingPreset clownFish = new FishingPreset(0);
    public static FishingPreset blueFish = new FishingPreset(1);

    private FishingPreset preset;

    public FishingArea(int x, int y, FishingPreset preset) {
        super(x, y);

        image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        maxDistance = 128f;
        this.preset = preset;
    }

    public boolean onInteract() {
        if (Skills.getLevel(Skills.FISHING) < preset.lvlReq) {
            ChatBox.sendMessage("You do not have the required fishing level of " + preset.lvlReq);
            return false;
        }

        if (InventoryManager.isFull()) {
            ChatBox.sendMessage("You do not have any inventory space!");
            return false;
        }

        timer += Main.dTime();

        drawPlayerProgressBar();

        if (timer > maxTimer) {
            timer = 0;
            maxTimer = preset.getTimer();
            InventoryManager.addItem(preset.fish, 1);
            Skills.addExperience(Skills.FISHING, preset.getXp());
        }

        return true;
    }
}

class FishingPreset {
    float minTimer, maxTimer, xp;
    int lvlReq;
    ItemList fish;

    public FishingPreset(int index) {
        switch(index) {
            case 0: // Clown Fish
                fish = ItemList.clownfish;
                minTimer = 1.5f;
                maxTimer = 6.5f;
                xp = 15f;
                lvlReq = 1;
                break;
            case 1: // Blue Fish
                fish = ItemList.bluefish;
                minTimer = 2.5f;
                maxTimer = 7.5f;
                xp = 35f;
                lvlReq = 10;
                break;
        }
    }

    public float getTimer() {
        return DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (Skills.getLevel(Skills.FISHING) - lvlReq));
    }

    public ItemList getFish() {
        return fish;
    }

    public float getXp() {
        return xp;
    }
}