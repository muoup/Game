package com.Game.GUI;

import com.Game.Main.Main;
import com.Game.Main.Menu;
import com.Game.listener.Input;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TextBox {
    private static ArrayList<String> textBoxes = new ArrayList();
    private static int counter = 0;

    public static void setText(String text) {
        textBoxes.add(text);
    }

    public static void setText(String[] text) {
        for (String s : text)
            textBoxes.add(s);
    }

    public static boolean noText() {
        return textBoxes.size() == 0;
    }

    public static void handleTextbox() {
        if (noText())
            return;

        Main.settings.state = Main.settings.state.SimplePause;
        Settings.pause = true;

        counter++;

        String text = textBoxes.get(0);

        Render.setColor(new Color(129, 79, 47));
        Render.drawRectangle(0,Settings.curResolution().y * 0.65f,
                Settings.curResolution().x, Settings.curResolution().y * 0.35f);

        if (counter >= text.length() * 2) {
            counter = text.length() * 2;

            if (Input.GetKeyDown(KeyEvent.VK_ENTER)) {
                textBoxes.remove(0);
                counter = 0;

                if (noText()) {
                    Main.settings.state = Menu.MenuState.PauseMenu;
                    Settings.pause = false;
                }
            }
        } else if (Input.GetKeyDown(KeyEvent.VK_ENTER)) {
            counter = text.length() * 2;
        }

        Render.setColor(Color.BLACK);
        Render.setFont(Settings.npcFont);

        Render.drawText(text.substring(0, counter / 2), Settings.curResolution().x / 2 - Settings.sWidth(text) / 2,
                Settings.curResolution().y  * 0.8f);
    }
}
