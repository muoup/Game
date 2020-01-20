package com.Game.Questing.Quests;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.Questing.Quest;

public class BirdWatchingQuest extends Quest {
    public BirdWatchingQuest(int id, String name) {
        super(id, name, 3);
    }

    protected void parseData() {
        if (data == 0) {
            completion = INCOMPLETE;
        } else if (data == 1) {
            completion = INPROGRESS;
        } else {
            completion = COMPLETE;
        }
    }

    public void printClue() {
        switch (data) {
            case 0:
                ChatBox.sendMessage("It looks like the Bird Watcher is in need of help, I should go help him out.");
                break;
            case 1:
                ChatBox.sendMessage("The Bird Watcher talked about a bird in the tree. I wonder how I can get one for him.");
                break;
            default:
                ChatBox.sendMessage("The Bird Watcher has gotten a pet bird thanks to your help.");
                break;
        }
    }
}
