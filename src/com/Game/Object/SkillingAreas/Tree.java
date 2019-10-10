package com.Game.Object.SkillingAreas;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Game.Main.Main;
import com.Game.Object.GameObject;
import com.Util.Math.DeltaMath;

public class Tree extends GameObject {

    TreePreset preset;
    int woodAmount;
    float rpTimer;

    public static TreePreset wood = new TreePreset(0);
    public static TreePreset oak = new TreePreset(3);
    public static TreePreset maple = new TreePreset(5);

    public Tree(int x, int y, TreePreset preset) {
        super(x, y);

        image = getImage(preset.treeImage);
        maxDistance = 64f;
        woodAmount = preset.getWoodAmount();
        this.preset = preset;
    }

    public void update() {
        if (woodAmount == 0) {
            rpTimer -= Main.dTime();

            if (rpTimer <= 0) {
                woodAmount = preset.getWoodAmount();
                image = getImage(preset.treeImage);
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
                image = getImage("toppled_" + preset.treeImage);
            }
        }

        return true;
    }
}

class TreePreset {

    float xp, minTimer, maxTimer;
    int minWood, maxWood, lvlReq;
    ItemList wood;
    String treeImage;

    public TreePreset(int index) {
        switch(index) {
            case 0:
                // Regular Tree
                xp = 45;
                minTimer = 5.0f; //5.0f
                maxTimer = 12.5f; //12.5f
                minWood = 2;
                maxWood = 9;
                lvlReq = 1;
                wood = ItemList.wood;
                treeImage = "tree.png";
                break;
            case 3:
                // Oak Tree
                xp = 55;
                minTimer = 5.5f;
                maxTimer = 13f;
                minWood = 1;
                maxWood = 12;
                lvlReq = 10;
                wood = ItemList.wood;
                treeImage = "oakTree.png";
                break;
            case 4:
                // Maple Tree
                xp = 65;
                minTimer = 6.5f;
                maxTimer = 14f;
                minWood = 1;
                maxWood = 7;
                lvlReq = 10;
                wood = ItemList.mapleLog;
                treeImage = "mapleTree.png";
                break;
        }
    }

    public float getTimer() {
        return DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (Skills.getLevel(Skills.FISHING) - lvlReq));
    }

    public float getXp() {
        return xp;
    }

    public int getWoodAmount() {
        return Math.round(DeltaMath.range(minWood, maxWood));
    }
}