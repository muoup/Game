package com.Game.GUI.Skills;

import com.Game.GUI.GUI;
import com.Game.Main.Main;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SkillsManager {

    public static BufferedImage[] skillImages;
    public static String[] skillImageNames = {
            "ranged.png",
            "melee.png",
            "lifepoints.png",
            "fishing.png",
            "woodcutting.png",
            "fletching.png",
            "mining.png",
            "smithing.png"
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
        Render.setFont(Settings.itemFont);

        Render.setColor(GUI.GUIBgColor);
        Render.drawBorderedBounds(GUI.GuiPos, GUI.GUIEnd(), 1);

        for (int i = 0; i < skillImageNames.length; i++) {
            int x = i % 4;
            int y = i / 4;

            Render.setColor(Color.BLACK);
            Render.drawRectOutlineBounds(GUI.GuiPos, GUI.GUIEnd());

            Vector2 rectPos = GUI.getGridPosition(x, y);

            Image image = Render.getScaledImage(skillImages[i], GUI.invSize);

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(rectPos, GUI.invSize);
            Render.drawImage(image, rectPos);

            String text = Integer.toString(Skills.getLevel(i));

            Render.drawText(text,
                    rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 2, GUI.intBoxSize - 2)));
        }
    }

    public static void update() {

    }
}
