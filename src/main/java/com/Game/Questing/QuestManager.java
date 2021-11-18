package com.Game.Questing;

import com.Game.GUI.GUI;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class QuestManager {

    public static final Color COMPLETE = Color.GREEN.darker();

    public static Quest[] questList;

    public static void init() {
        questList = new Quest[Settings.questCount];

        for (int i = 0; i < questList.length; i++) {
            questList[i] = new Quest();
        }
    }

    public static void render() {
        Render.setColor(Color.LIGHT_GRAY);
        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);
        Render.drawRectOutlineBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);
        Render.setFont(Settings.questFont.deriveFont(Font.BOLD, 24));
        Render.drawText("Quests:", GUI.GuiPos.addClone(32, 8 + Render.getStringHeight()));
        Render.setFont(Settings.questFont);

        for (int i = 0; i < questList.length; i++) {
            Quest curQuest = questList[i];

            Render.setColor(curQuest.getColor());
            Render.drawText(curQuest.getName(), GUI.GuiPos.addClone(32, 24 + Render.getStringHeight() * (i + 2.5f)));
        }
    }

    public static void update() {
        if (!GUI.inGUI() || !Input.GetMouseDown(1))
            return;

        Vector2 pos = GUI.GuiPos.addClone(32, 24 + Render.getStringHeight() * 1.5f);
        Vector2 mouse = Input.mousePosition;

        if (mouse.x > pos.x && mouse.x < GUI.GUIEnd().x - 64) {
            int index = (int) ((mouse.y - pos.y) / Render.getStringHeight());

            if (index < questList.length && index >= 0)
                questList[index].printClue();
        }
    }

    public static boolean isComplete(int id) {
        return questList[id].getColor() == Color.GREEN;
    }

    public static void setName(int id, String name) {
        questList[id].setName(name);
    }

    public static void setClue(int id, String questClue) {
        questList[id].setClue(questClue);
    }

    public static void setColor(int id, Color color) {
        questList[id].setColor(color);
    }
}
