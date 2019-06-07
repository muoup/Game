package com.Game.Main;

import com.Game.listener.Input;
import com.Util.Other.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Menu {

    public boolean tempFullscreen = false;

    public enum MenuState {
        PauseMenu,
        VideoSettings,
        AudioSettings
    }

    int[] stateLengths = {
        4,
        4,
        4
    };

    public int curSelected;
    public MenuState state;

    public Menu() {
        curSelected = 0;
        state = MenuState.PauseMenu;
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, Settings.fontSize));

        switch (state) {
            case PauseMenu:
                tempFullscreen = Settings.fullScreen;

                String[] options1 = {
                        ((curSelected == 0) ? "> " : "") + "Video Settings",
                        ((curSelected == 1) ? "> " : "") + "Audio Settings",
                        ((curSelected == 2) ? "> " : "") + "Return to Game",
                        ((curSelected == 3) ? "> " : "") + "Exit Game"
                };

                drawOptions(options1, g, Main.frame, new int[0]);

                if (Input.GetKeyDown(KeyEvent.VK_ENTER)) {
                    switch (curSelected) {
                        case 0:
                            state = MenuState.VideoSettings;
                            break;
                        case 1:
                            state = MenuState.AudioSettings;
                            break;
                        case 2:
                            Settings.pause = false;
                            break;
                        case 3:
                            System.exit(0);
                            break;
                    }
                }

                break;

            case VideoSettings:

                String[] options2 =  {
                        ((curSelected == 0) ? "> " : "") + "Screen Resolution: " + Settings.curResolution().toString(),
                        ((curSelected == 1) ? "> " : "") + "Show FPS: " + Settings.showFPS,
                        ((curSelected == 2) ? "> " : "") + "Fullscreen: " + tempFullscreen,
                        ((curSelected == 3) ? "> " : "") + "Apply Changes"
                };

                if (Settings.fullScreen)
                    options2[0] = ((curSelected == 0) ? "> " : "") + "Screen Resolution: " + Main.screenSize.width + ", " + Main.screenSize.height;

                int[] grayed = {
                        (Settings.fullScreen) ? 0 : -1
                };

                drawOptions(options2, g, Main.frame, grayed);

                boolean left = Input.GetKeyDown(KeyEvent.VK_LEFT);
                boolean right = Input.GetKeyDown(KeyEvent.VK_RIGHT);
                boolean enter = Input.GetKeyDown(KeyEvent.VK_ENTER);

                if (left && curSelected == 0 && !Settings.fullScreen) {
                    Settings.resolutionIndex--;
                }
                if (right && curSelected == 0 && !Settings.fullScreen) {
                    Settings.resolutionIndex++;
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
                    Main.main.updateFrame();
                    state = MenuState.PauseMenu;
                }

                break;

            case AudioSettings:

                break;
        }
    }

    public void drawOptions(String[] options, Graphics g, JFrame window, int[] grayedOut) {
        for (int i = 0; i < options.length; i++) {
            g.setColor(Color.BLACK);

            for (int x : grayedOut)
                if (i == x)
                    g.setColor(Color.GRAY);

            g.drawString(options[i], window.getWidth() / 2
                            - Settings.sWidth(options[i], g) / 2,
                    window.getHeight() / 2 - Settings.fontSize * options.length + i * Settings.fontSize * 2);
        }
    }

    public void update() {
        if (Input.GetKeyDown(KeyEvent.VK_DOWN)) {
            curSelected++;
        }

        if (Input.GetKeyDown(KeyEvent.VK_UP)) {
            curSelected--;
        }

        curSelected %= stateLengths[state.ordinal()];
    }
}

