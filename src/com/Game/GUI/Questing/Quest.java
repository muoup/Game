package com.Game.GUI.Questing;

import java.awt.*;

public class Quest {
    protected final int id;
    protected int completion;
    public String name;

    protected final int LOCKED = -1;
    protected final int INCOMPLETE = 0;
    protected final int INPROGRESS = 1;
    protected final int HANDIN = 2;
    protected final int COMPLETE = 3;

    public Quest(int id, String name) {
        this.id = id;
        this.completion = INCOMPLETE;
        this.name = name;
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

    public void parseData(int data) {}

    public void setStatus(int status) {
        this.completion = status;
    }
}
