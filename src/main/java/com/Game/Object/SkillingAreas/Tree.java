package com.Game.Object.SkillingAreas;

import com.Game.Entity.Player.Player;
import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Game.Main.Main;
import com.Game.Object.GameObject;
import com.Game.Questing.QuestManager;

import java.util.Random;

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
            Main.player.changeSprite(Player.idleAnimation);
            return false;
        }

        if (Skills.getLevel(Skills.WOODCUTTING) < preset.lvlReq) {
            Main.player.changeSprite(Player.idleAnimation);
            ChatBox.sendMessage("You do not have the required woodcutting level of " + preset.lvlReq);
            return false;
        }

        if (InventoryManager.isFull()) {
            Main.player.changeSprite(Player.idleAnimation);
            ChatBox.sendMessage("You do not have any inventory space!");
            return false;
        }

        timer += Main.dTime();

        Main.player.changeSprite(Player.chopAnimation);
        drawPlayerProgressBar();

        if (timer > maxTimer) {
            timer = 0;
            woodAmount--;
            maxTimer = preset.getTimer();
            InventoryManager.addItem(preset.wood, 1);
            Skills.addExperience(Skills.WOODCUTTING, preset.getXp());

            // If the 'Bird Watching' Quest is underway, there is a chance that the player collects
            // a bird that is needed in order to finish the quest.
            if (QuestManager.getData(0) == 1) {
                // The chance that the player collects the bird is a 1 in 16 chance.
                // This should only take around 2 or 3 minutes to complete hopefully.

                // TODO: Instead of having the tree drop the bird, implement bird's nests into the game to drop them instead.
                // Making the birds nest drop the parrot bird will introduce the player to the idea of a bird's nest
                // without having to explain it which is quite handy.
                if (new Random().nextInt(15) == 5 && InventoryManager.itemCount(ItemList.parrotBird) == 0) {
                    InventoryManager.addItem(ItemList.parrotBird, 1);
                }
            }

            if (woodAmount == 0) {
                rpTimer = preset.getTimer() * 2;
                image = getImage("toppled_" + preset.imageName);
            }
        }

        return true;
    }

    public void loseFocus() {
        timer = 0;
        maxTimer = preset.getTimer();
    }
}