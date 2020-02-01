package com.Game.Questing;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.Main.Main;

import java.awt.*;

public class Quest {
    protected final int id;
    protected int data;
    protected int completion;
    public String name;

    protected final int LOCKED = -1;
    protected final int INCOMPLETE = 0;
    protected final int INPROGRESS = 1;
    protected final int HANDIN = 2;
    protected final int COMPLETE = 3;

    public final int completeData;

    public Quest(int id, String name, int completeData) {
        this.id = id;
        this.completion = INCOMPLETE;
        this.name = name;
        this.data = 0;
        this.completeData = completeData;
    }

    public Color getColor() {
        switch (completion) {
            case LOCKED:
                return QuestManager.LOCKED;
            case INCOMPLETE:
                return QuestManager.INCOMPLETE;
            case INPROGRESS:
                return QuestManager.IN_PROGRESS;
            case HANDIN:
                return QuestManager.HAND_IN;
            case COMPLETE:
                return QuestManager.COMPLETE;
            default:
                System.err.println(name + " has an invalid completion id of " + id);
        }
        return Color.LIGHT_GRAY;
    }

    public boolean isComplete() {
        return data == completeData;
    }

    protected void parseData() {
    }

    public void setStatus(int status) {
        this.completion = status;
    }

    public void setData(int data) {
        this.data = data;
        Main.sendPacket("57" + id + ":" + data + ":" + Main.player.name);
        parseData();
    }

    public void printClue() {
        ChatBox.sendMessage("No quest message has been implemented, dev is lazy lmao.");
    }
}
