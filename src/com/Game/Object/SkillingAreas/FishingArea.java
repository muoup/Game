package com.Game.Object.SkillingAreas;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Object.GameObject;

import java.awt.image.BufferedImage;

public class FishingArea extends GameObject {

    private FishingPreset preset;

    public FishingArea(int x, int y, FishingPreset preset) {
        super(x, y);

        image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        maxDistance = 32f;
        this.preset = preset;
    }

    public boolean onInteract() {
        if (Skills.getLevel(Skills.FISHING) < preset.lvlReq) {
            ChatBox.sendMessage("You do not have the required fishing level of " + preset.lvlReq);
            return false;
        }

        if (InventoryManager.isFull()) {
            ChatBox.sendMessage("You do not have any state space!");
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