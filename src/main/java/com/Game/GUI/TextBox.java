package com.Game.GUI;

import com.Game.Main.Main;
import com.Game.Main.MenuHandler;
import com.Game.listener.Input;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class TextBox {
    private static ArrayList<String> textBoxes = new ArrayList();
    private static Choice[] choices = new Choice[0];

    private static boolean firstFrameLeave = false;
    private static double counter = 0;
    private static int choiceIndex = -1;

    private static int stringLength;
    private static float buttonWidth;
    private static float buttonPadding;

    public static void setText(String text) {
        text = formatString(text);
        textBoxes.add(text);
        MenuHandler.setState(MenuHandler.MenuState.TextBoxPause);
        firstFrameLeave = true;
    }

    public static void setText(String... text) {
        for (int i = 0; i < text.length; i++) {
            text[i] = formatString(text[i]);
        }
        textBoxes.addAll(Arrays.asList(text));
        MenuHandler.setState(MenuHandler.MenuState.TextBoxPause);
        firstFrameLeave = true;
    }

    public static void setChoice(Runnable run, Runnable run2, String... strings) {
        if (strings.length < 2)
            return;

        choices = new Choice[]{
                new Choice(run, strings[0]),
                new Choice(run2, strings[1])
        };

        for (int i = 2; i < strings.length; i++)
            setText(strings[i]);

        choiceIndex = textBoxes.size() - 1;
    }

    public static boolean noText() {
        return textBoxes.size() == 0 &&
                choices.length == 0;
    }

    public static void clearTextBox() {
        choices = new Choice[0];
        textBoxes.clear();
    }

    public static void handleTextbox() {
        if (noText())
            return;

        if (firstFrameLeave) {
            firstFrameLeave = false;
            return;
        }

        counter += Settings.scrollSpeed * Main.dTime();

        if (textBoxes.size() != 0) {
            String currentText = textBoxes.get(0);

            String text = textBoxes.get(0);

            Render.setColor(new Color(173, 108, 61));
            Render.drawBorderedRect(Settings.curResolution().x * 0.25f, Settings.curResolution().y * 0.60f,
                    Settings.curResolution().x * 0.5f, Settings.curResolution().y * 0.35f);

            if (counter >= text.length() * 2) {
                counter = text.length() * 2;

                if (Input.GetKeyDown(KeyEvent.VK_E) && choiceIndex != 0) {
                    next();
                }

            } else if (Input.GetKeyDown(KeyEvent.VK_E)) {
                counter = text.length() * 2;
            }

            if (counter / 2 < text.length())
                if (currentText.charAt((int) counter / 2) == '/' && currentText.charAt(1 + (int) counter / 2) == 'n')
                    counter += 4;

            Render.setColor(Color.BLACK);
            Render.setFont(Settings.npcFont);

            String[] split = text.substring(0, (int) counter / 2).split("/n");
            String[] trueSplit = text.split("/n");

            int amount = trueSplit.length;
            float height = Settings.curResolution().y * ((choices.length == 0) ? 0.8f : 0.74f)
                    - Render.getStringHeight() * 1.1f * amount * 0.5f;

            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                String ts = trueSplit[i];

                Render.drawText(s, Settings.curResolution().x / 2 - Settings.sWidth(ts) / 2,
                        height + Render.getStringHeight() * i * 1.1f);
            }
        }

        if (choices.length == 0 || choiceIndex != 0)
            return;

        Render.setFont(Settings.npcFont.deriveFont(1, 22));

        stringLength = Math.max(Render.getStringWidth(choices[0].string), Render.getStringWidth(choices[1].string));
        buttonWidth = stringLength * 1.25f;
        buttonPadding = ((Settings.curResolution().x / 2) - buttonWidth * choices.length) / (choices.length + 1);

        for (int i = 0; i < choices.length; i++) {
            String buttonText = choices[i].string;
            float rx = Settings.curResolution().x * 0.25f + buttonPadding * (i + 1) + buttonWidth * i;
            float tx = rx + buttonWidth / 2 - Render.getStringWidth(buttonText) / 2;

            Render.setColor(Color.GRAY);
            Render.drawBorderedRect(rx, Settings.curResolution().y * 0.8f, buttonWidth, Render.getStringHeight() * 1.5f);

            Render.setColor(Color.BLACK);
            Render.drawText(buttonText, tx, Settings.curResolution().y * 0.8f + Render.getStringHeight());
        }
    }

    private static void next() {
        if (textBoxes.isEmpty())
            return;
        textBoxes.remove(0);
        choiceIndex--;
        counter = 0;

        if (choiceIndex == -1)
            choices = new Choice[0];

        if (textBoxes.size() == 0) {
            textBoxes.clear();
            Settings.disablePause();
        }
    }

    public static void handleButtonPress() {
        // If there are no buttons to press, there is no way this method is going to be used, so discard the callzn.
        // Also, if the player is not pressing left-click, then they cannot be clicking a button.
        if (choiceIndex != 0 || !Input.GetMouseDown(1))
            return;

        float my = Input.mousePosition.y;
        float ry = Settings.curResolution().y * 0.8f;

        if (my > ry && my < ry + Render.getStringHeight() * 1.5f) {
            // The mouse cursor is within the height range for the button, this means that it is possible
            // the player is attempting to press the button.

            // Also, variable declarations happen in waves because it means that if the mouse cursor is not
            // in the height range, the computer does not need to handle memory from the next variables.
            // I think this is slightly more efficient, and may help performance in large batches.
            float mx = Input.mousePosition.x;
            float rx = Settings.curResolution().x;

            if (mx > rx * 0.25f && mx < rx * 0.75f) {
                // Now that we know that the cursor is within the rectangle range for the choices, it
                // is now time to determine which button the player is clicking, if any.
                for (int i = 0; i < choices.length; i++) {
                    float recX = Settings.curResolution().x * 0.25f + buttonPadding * (i + 1) + buttonWidth * i;
                    float diff = mx - recX;

                    if (diff > 0 && diff < buttonWidth) {
                        choices[i].run.run();
                        next();
                        return;
                    }
                }
            }
        }
    }

    public static String formatString(String message) {
        String fMsg = message;
        float maxWidth = Settings.curResolution().x * 0.4f;
        int bottom = 0;

        Render.setFont(Settings.npcFont);

        int counter = 1;
        while (counter < fMsg.length()) {
            counter++;

            if (Render.getStringWidth(fMsg.substring(bottom, counter)) > maxWidth && counter < fMsg.length()) {
                for (int i = counter; i >= bottom; i--) {
                    if (fMsg.charAt(i) == ' ') {
                        fMsg = fMsg.substring(0, i) + "/n" + fMsg.substring(i + 1);
                        bottom = i + 2;
                        counter += 2;
                        break;
                    } else if (i == bottom) {
                        int e = counter - 2;
                        fMsg = fMsg.substring(0, e) + "-/n" + fMsg.substring(e);
                        counter += 3;
                        bottom = e + 3;
                        break;
                    }
                }
            }
        }

        return fMsg;
    }
}