package com.Game.GUI.Skills;

import com.Game.GUI.GUI;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class SkillsManager {

    private static BufferedImage[] skillImages;
    public static String[] skillImageNames = {
            "ranged.png"
    };

    public static void init() {
        skillImages = new BufferedImage[skillImageNames.length];

        for (int i = 0; i < skillImageNames.length; i++) {
            skillImages[i] = getImage(skillImageNames[i]);
        }
    }

    private static BufferedImage getImage(String skillImageName) {
        return Main.getImage("GUI/Skills/" + skillImageName);
    }

    public static void render() {
        Render.setColor(Color.LIGHT_GRAY);
        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        for (int i = 0; i < skillImageNames.length; i++) {
            int x = i % 4;
            int y = i / 4;

            Vector2 rectPos = GUI.getGridPosition(x, y);

            Image image = Render.getScaledImage(skillImages[i], GUI.invSize);

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(rectPos, GUI.invSize);
            Render.drawImage(image, rectPos);

            String text = Integer.toString(Skills.getLevel(i));

            Render.drawText(text,
                    rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4)));
        }
    }

    public static void update() {
        if (Input.GetKey(KeyEvent.VK_T)) {
            Skills.addExperience(Skills.RANGED, 50);
        }
    }
}
