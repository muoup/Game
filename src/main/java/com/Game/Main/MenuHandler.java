package com.Game.Main;

import com.Game.GUI.RightClick;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuHandler {

    public boolean tempFullscreen = false;
    private int pseudoResIndex = Settings.resolutionIndex;

    public enum MenuState {
        NoPause,
        TextBoxPause,
        PauseMenu,
        VideoSettings,
        AudioSettings,
        MiscPause
    }

    int[] stateLengths = {
            0,
            0,
            4,
            2,
            2
    };

    public int curSelected;
    public MenuState state;

    public MenuHandler() {
        curSelected = 0;
        state = MenuState.NoPause;
    }

    public static MenuState getState() {
        return Main.menu.state;
    }

    public static void setState(MenuState state) {
        Main.menu.state = state;
    }

    public void render() {
        if (state == MenuState.TextBoxPause || state == MenuState.NoPause)
            return;

        Vector2 center = Settings.curResolution().scaleClone(0.5f);

        Render.setColor(new Color(64, 64, 64));
        Render.drawRectangle(Vector2.zero(), Settings.curResolution());

        Render.setColor(Color.GRAY);
        Render.drawRectangle(center.scaleClone(0.33f), center.scaleClone(1.33f));

        Render.setColor(Color.LIGHT_GRAY);
        Render.drawRectangle(center.scaleClone(0.33f).addClone(12f), center.scale(1.33f).addClone(-24));

        Render.setColor(Color.BLACK);
        Render.setFont(new Font("Arial", Font.BOLD, Settings.fontSize));

        boolean left, right, enter;

        switch (state) {
            case PauseMenu:
                tempFullscreen = Settings.fullScreen;

                String[] options1 = {
                        ((curSelected == 0) ? "> " : "  ") + "Video Settings",
                        ((curSelected == 1) ? "> " : "  ") + "Audio Settings",
                        ((curSelected == 2) ? "> " : "  ") + "Return to Game",
                        ((curSelected == 3) ? "> " : "  ") + "Exit Game"
                };

                drawOptions(options1, Main.frame, new int[0]);

                if (Input.GetKeyDown(KeyEvent.VK_ENTER)) {
                    switch (curSelected) {
                        case 0:
                            state = MenuState.VideoSettings;
                            break;
                        case 1:
                            state = MenuState.AudioSettings;
                            break;
                        case 2:
                            Settings.disablePause();
                            break;
                        case 3:
                            Main.client.disconnect();
                            System.exit(0);
                            break;
                    }
                    curSelected = 0;
                }

                break;

            case VideoSettings:
                String[] options2 = {
                        ((curSelected == 0) ? "> " : "  ") + "Screen Resolution: " + Settings.resolutions[pseudoResIndex].toString(),
                        ((curSelected == 1) ? "> " : "  ") + "Show FPS: " + Settings.showFPS,
                        ((curSelected == 2) ? "> " : "  ") + "Fullscreen: " + tempFullscreen,
                        ((curSelected == 3) ? "> " : "  ") + "Apply Changes"
                };

                if (Settings.fullScreen)
                    options2[0] = ((curSelected == 0) ? "> " : "") + "Screen Resolution: " + Main.screenSize.width + ", " + Main.screenSize.height;

                int[] grayed = {
                        (Settings.fullScreen) ? 0 : -1
                };

                drawOptions(options2, Main.frame, grayed);

                left = Input.GetKeyDown(KeyEvent.VK_LEFT);
                right = Input.GetKeyDown(KeyEvent.VK_RIGHT);
                enter = Input.GetKeyDown(KeyEvent.VK_ENTER);

                if (left && curSelected == 0 && !Settings.fullScreen) {
                    pseudoResIndex--;
                }
                if (right && curSelected == 0 && !Settings.fullScreen) {
                    pseudoResIndex++;
                }

                if (Settings.resolutionIndex == -1 && !Settings.fullScreen)
                    Settings.resolutionIndex = 2;

                Settings.resolutionIndex %= Settings.resolutions.length;

                if ((left || right || enter) && curSelected == 1)
                    Settings.showFPS = !Settings.showFPS;

                if ((left || right || enter) && curSelected == 2)
                    tempFullscreen = !tempFullscreen;

                if (enter && curSelected == 3) {
                    Settings.fullScreen = tempFullscreen;
                    Settings.resolutionIndex = pseudoResIndex;
                    Main.main.updateFrame();
                    state = MenuState.PauseMenu;
                }

                break;

            case AudioSettings:
                String[] options3 = {
                        ((curSelected == 0) ? "> " : "  ") + "Volume: " + Settings.volume,
                        ((curSelected == 1) ? "> " : "  ") + "Apply Changes"
                };

                drawOptions(options3, Main.frame, null);

                left = Input.GetKeyDown(KeyEvent.VK_LEFT);
                right = Input.GetKeyDown(KeyEvent.VK_RIGHT);
                enter = Input.GetKeyDown(KeyEvent.VK_ENTER);

                if (curSelected == 0)
                    if (right && Settings.volume < 100)
                        Settings.volume += 10;
                    else if (left && Settings.volume > 0)
                        Settings.volume -= 10;
                if (curSelected == 1 && enter)
                    state = MenuState.PauseMenu;

                break;
        }
    }

    public void drawOptions(String[] options, JFrame window, int[] grayedOut) {
        for (int i = 0; i < options.length; i++) {
            Render.setColor(Color.BLACK);

            if (grayedOut != null)
                for (int x : grayedOut)
                    if (i == x)
                        Render.setColor(Color.GRAY);

            Render.drawText(options[i], window.getWidth() / 2
                            - Settings.sWidth(options[i]) / 2,
                    (int) (window.getHeight() / 2 - Settings.fontSize / 1.5 * options.length + i * Settings.fontSize * 2));
        }
    }

    public void update() {
        if (state == MenuState.TextBoxPause || RightClick.render || RightClick.coolDown > 0)
            return;

        if (stateLengths[state.ordinal()] == 0)
            return;

        if (Input.GetKeyDown(KeyEvent.VK_DOWN)) {
            curSelected++;
        }

        if (Input.GetKeyDown(KeyEvent.VK_UP)) {
            curSelected--;
        }

        if (curSelected <= 0) {
            curSelected = stateLengths[state.ordinal()];
        }

        curSelected %= stateLengths[state.ordinal()];
    }
}

