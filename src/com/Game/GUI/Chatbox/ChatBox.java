package com.Game.GUI.Chatbox;

import com.Game.GUI.GUI;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class ChatBox {
    public static ArrayList<Message> messages;

    public static Font textFont;

    public static int maxMessages = 20;
    public static int scroll = 0;
    public static int maxScroll = 3000;

    private static Vector2 size = Vector2.zero();
    private static float height = 0;
    private static Vector2 dPos = Vector2.zero();

    private static Vector2 initMouse = Vector2.zero();
    private static int initScroll = 0;

    private static boolean moveBar = false;
    protected static Vector2 bSize;
    protected static Vector2 gSize;

    private static boolean onStartup = true;

    public static void init() {
        messages = new ArrayList();
        textFont = new Font("TimesRoman", Font.PLAIN, 24);
    }

    public static void startUp() {
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
        sendMessage("Welcome to the Server!");
    }

    public static void render() {
        if (onStartup) {
            startUp();
            onStartup = false;
        }

        Render.setFont(textFont);
        Vector2 v1 = new Vector2(0, Settings.curResolution().y - bSize.y - 35f);

        Render.setColor(new Color(175, 129, 34));
        Render.drawBorderedRect(v1, gSize);

        updateBar();
        renderBar();

        updateText();
        renderText();
    }

    public static void renderBar() {
        Render.setColor(new Color(40, 38, 34));
        Render.drawBorderedRect(dPos, bSize);

        Render.setColor(inBar() && Input.GetMouse(1) ? new Color(181, 160, 108) :
                new Color(181, 151, 57));
        Render.drawBorderedRect(dPos.addClone(0, scroll), new Vector2(size.x * 0.125f, height));
    }

    public static void updateBar() {
        if (inBar() && Input.GetMouse(1)) {
            if (initMouse.isZero()) {
                initMouse = Input.mousePosition.clone();
                initScroll = scroll;
            }

            moveBar = true;
        }

        if (!Input.GetMouse(1) && !initMouse.isZero()) {
            initMouse = Vector2.zero();
            moveBar = false;
        }

        if (moveBar)
            scroll = initScroll + (int) (Input.mousePosition.y - initMouse.y);

        if (scroll < 0)
            scroll = 0;

        if (scroll > bSize.y - height)
            scroll = (int) (bSize.y - height);
    }

    public static void renderText() {
        // For the sake of simplicity for now, assume all text is one line.
        // TODO: Allow for the ChatBox to handle multi-lined messages.
        Render.setFont(textFont);
        int textHeight = (int) (Render.getStringHeight() * 1.25);

        int truncTxt = (int) DeltaMath.roundTo((float) Math.ceil(scroll / maxScroll), textHeight) / 2;

        // The starting offset of the text.
        int remTrunc = scroll % textHeight;

        // The starting index of the message that will appear on screen.
        int start = truncTxt * messages.size();

        Render.setColor(Color.BLACK);

        for (int i = start; i < start + Math.min(gSize.y / textHeight, messages.size()); i++) {
            if (i >= messages.size())
                break;

            Vector2 textPos = new Vector2(gSize.x / 20, Settings.curResolution().y - gSize.y + (i - start) * textHeight - remTrunc);
            Render.drawText(messages.get(i).message, textPos);
        }
    }

    public static void updateText() {

    }

    private static boolean inBar() {
        return Input.mousePosition.compareTo(dPos.addClone(0, scroll)) == 1 && dPos.addClone(size.x * 0.125f, height + scroll).compareTo(Input.mousePosition) == 1;
    }

    public static void update() {
        size = GUI.GUIEnd().subtractClone(GUI.GuiPos);
        dPos = new Vector2(size.x * 3.375f, Settings.curResolution().y - size.y * 1.25f);
        bSize = new Vector2(size.x * 0.125f, size.y * 1.25f - 35f);
        gSize = new Vector2(size.x * 3.5f, size.y * 1.25f - 35f);
        height = (float) Math.min(gSize.y, Math.pow(gSize.y, 2) / maxScroll);
    }

    public static void sendMessage(String message) {
        String fmsg = formatMSG(message);
        Message msg = new Message(fmsg, Color.BLACK);

        if (Main.graphics == null)
            System.err.println("The graphics component is null!");

        if (messages.size() > maxMessages)
            messages.remove(0);

        Render.setFont(textFont);

        messages.add(msg);
        maxScroll = Render.getStringHeight() * messages.size();
    }

    private static String formatMSG(String message) {
        String fMsg = message;
        for (int i = 1; i < message.length() / 40; i++) {
            fMsg = fMsg.substring(0, i * 40) + "\n" + fMsg.substring(i * 40 + 1);
        }

        return fMsg;
    }
}
