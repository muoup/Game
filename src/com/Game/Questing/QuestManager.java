package com.Game.Questing;

import com.Game.GUI.GUI;
import com.Game.Questing.Quests.BirdWatchingQuest;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class QuestManager {

    public static final Color LOCKED = Color.DARK_GRAY;
    public static final Color INCOMPLETE = Color.RED;
    public static final Color IN_PROGRESS = Color.YELLOW;
    public static final Color HAND_IN = Color.BLUE;
    public static final Color COMPLETE = Color.GREEN.darker();

    public static Quest[] questList = {
        new BirdWatchingQuest(0, "Bird Watching")
    };

    public static void init() {

    }

    public static void render() {
        Render.setColor(Color.LIGHT_GRAY);
        Render.drawBorderedBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);
        Render.setFont(Settings.questFont.deriveFont(Font.BOLD, 24));
        Render.drawText("Quests:", GUI.GuiPos.addClone(32, 8 + Render.getStringHeight()));
        Render.setFont(Settings.questFont);

        for (int i = 0; i < questList.length; i++) {
            Quest curQuest = questList[i];
            Render.setColor(curQuest.getColor());

            Render.drawText(curQuest.name, GUI.GuiPos.addClone(32, 24 + Render.getStringHeight() * (i + 2.5f)));
        }
    }

    public static void update() {
        if (!GUI.inGUI() || !Input.GetMouseDown(1))
            return;

        Vector2 pos = GUI.GuiPos.addClone(32, 24 + Render.getStringHeight() * 2.5f);
        Vector2 mouse = Input.mousePosition;

        if (mouse.x > pos.x && mouse.x < GUI.GUIEnd().x - 64) {
            int index = (int) ((mouse.y - pos.y) / (Render.getStringHeight() * 2.5f));

            if (index < questList.length && index >= 0)
                questList[index].printClue();
        }
    }

    public static void setStatus(int id, int status) {
        questList[id].setStatus(status);
    }

    public static void setData(int id, int data) {
        questList[id].setData(data);
    }

    public static int getData(int id) {
        return questList[id].data;
    }
}
