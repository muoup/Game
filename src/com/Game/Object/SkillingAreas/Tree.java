package com.Game.Object.SkillingAreas;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Object.GameObject;

public class Tree extends GameObject {

    TreePreset preset;
    int woodAmount;
    float rpTimer;

    public Tree(int x, int y, TreePreset preset) {
        super(x, y);

        this.preset = preset;
        image = getImage(preset.imageName);
        maxDistance = 64f;
        woodAmount = preset.getWoodAmount();
    }

    public void update() {
        if (woodAmount == 0) {
            rpTimer -= Main.dTime();

            if (rpTimer <= 0) {
                woodAmount = preset.getWoodAmount();
                image = getImage(preset.imageName);
            }
        }

    }

    public boolean onInteract() {
        if (woodAmount == 0) {
            return false;
        }

        if (Skills.getLevel(Skills.WOODCUTTING) < preset.lvlReq) {
            ChatBox.sendMessage("You do not have the required woodcutting level of " + preset.lvlReq);
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
            woodAmount--;
            maxTimer = preset.getTimer();
            InventoryManager.addItem(preset.wood, 1);
            Skills.addExperience(Skills.WOODCUTTING, preset.getXp());

            if (woodAmount == 0) {
                rpTimer = preset.getTimer() * 2;
                image = getImage("toppled_" + preset.imageName);
            }
        }

        return true;
    }
}