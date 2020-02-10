package com.Game.Questing.Quests;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.Questing.Quest;

public class AyeAyeCaptain extends Quest {
    public AyeAyeCaptain(int id, String name) {
        super(id, name, 0);
    }

    public void printClue() {
        switch (data) {
            case 0:
                ChatBox.sendMessage("That captain would like to have a word with you.");
                break;
            case 1:
                ChatBox.sendMessage("He needs about 10 logs, I'm not sure what kind, but I hope he will take whatever I find.");
                break;
            case 2:
                ChatBox.sendMessage("It seems that he needs some string for his sail. How much could he need? I think he said about 15 pieces of string.");
                break;
            case 3:
                ChatBox.sendMessage("All that is left to do is ride his ship, I wonder where it will take me.");
                break;
            case 4:
                ChatBox.sendMessage("The Captain is happy to have his ship back in shape, and now I have a way to the tribal lands.");
                break;
            default:
                ChatBox.sendMessage("You're quest is broken, please tell someone.");
                break;
        }
    }

    protected void parseData() {
        switch (data) {
            case 0:
                completion = INCOMPLETE;
                break;
            case 1:
            case 2:
            case 3:
                completion = INPROGRESS;
                break;
            case 4:
                completion = COMPLETE;
                break;
        }
    }

    public boolean isComplete() {
        return data == 4;
    }
}
