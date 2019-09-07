package com.Game.Object.SkillingAreas;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Object.GameObject;
import com.Util.Math.DeltaMath;

import java.awt.image.BufferedImage;

public class Tree extends GameObject {

    TreePreset preset;

    public static TreePreset wood = new TreePreset(0);
    public static TreePreset maple = new TreePreset(1);

    public Tree(int x, int y, TreePreset preset) {
        super(x, y);

        image = preset.treeImage;
        maxDistance = 150f;
        this.preset = preset;
    }

    public boolean onInteract() {
        if (Skills.getLevel(Skills.FISHING) < preset.lvlReq) {
            ChatBox.sendMessage("You do not have the required woodcutting level of " + preset.lvlReq);
            return false;
        }

        if (InventoryManager.isFull()) {
            ChatBox.sendMessage("You do not have any inventory space!");
            return false;
        }

        timer += 1 / Main.fps;

        drawPlayerProgressBar();

        if (timer > maxTimer) {
            timer = 0;
            maxTimer = preset.getTimer();
            InventoryManager.addItem(preset.wood.getSingleStack());
            Skills.addExperience(Skills.FISHING, preset.getXp());
        }

        return true;
    }
}

class TreePreset {

    float xp, minTimer, maxTimer;
    int lvlReq;
    Item wood;
    BufferedImage treeImage;

    public TreePreset(int index) {
        switch(index) {
            case 0:
                // Regular Tree
                xp = 45;
                minTimer = 5.0f;
                maxTimer = 12.5f;
                lvlReq = 1;
                wood = Item.wood;
                treeImage = Tree.getImage("tree.png");
                break;
            case 1:
                xp = 65;
                minTimer = 6.5f;
                maxTimer = 14f;
                lvlReq = 10;
                wood = Item.mapleLog;
                treeImage = Tree.getImage("mapleTree.png");
                break;
        }
    }

    public float getTimer() {
        return DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (Skills.getLevel(Skills.FISHING) - lvlReq));
    }

    public float getXp() {
        return xp;
    }
}