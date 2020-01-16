package com.Game.GUI.Questing.Quests;

import com.Game.GUI.Questing.Quest;

public class TestQuest extends Quest {
    public TestQuest(int id, String name) {
        super(id, name);
    }

    public void parseID(int data) {
        if (data == 0) {
            completion = INCOMPLETE;
        } else if (data == 1) {
            completion = COMPLETE;
        }
    }
}
