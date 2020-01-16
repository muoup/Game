package com.Game.GUI.Questing;

import com.Game.GUI.GUI;
import com.Game.GUI.Questing.Quests.TestQuest;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class QuestManager {

    public static final Color LOCKED = Color.DARK_GRAY;
    public static final Color INCOMPLETE = Color.RED;
    public static final Color IN_PROGRESS = Color.YELLOW;
    public static final Color HAND_IN = Color.BLUE;
    public static final Color COMPLETE = Color.GREEN;

    public static Quest[] questList = {
        new TestQuest(0, "Testing Quest")
    };

    public static void init() {

    }

    public static void render() {
        Render.setFont(Settings.itemFont);
        Render.setColor(Color.LIGHT_GRAY);
        Render.drawBorderedBounds(GUI.GuiPos, GUI.GUIEnd());

        for (int i = 0; i < questList.length; i++) {
            Quest curQuest = questList[i];
            Render.setColor(curQuest.getColor());

            Render.drawText(curQuest.name, GUI.GuiPos.addClone(32, 16 + Render.getStringHeight() * (i + 1) * 1.25f));
        }
    }

    public static void setStatus(int id, int status) {
        questList[id].setStatus(status);
    }
}
