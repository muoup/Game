package com.Game.GUI.Chatbox;

import com.Util.Other.Render;

import java.awt.*;

public class Message {
    // I had a ton of comments for this file but my laptop died so it was lost.
    // Considering I am going to be the only person reading this, it really is not too big of a deal.
    public String message;
    public String rawMessage;
    public Color color;
    public int lines = 0;

    public Message(String message, Color color) {
        this.rawMessage = removePlayerTag(message);
        this.message = formatMSG(message);
        this.color = color;
    }

    public String toString() {
        return message + " " + color.toString();
    }

    public String formatMSG(String message) {
        String fMsg = message;
        float maxWidth = ChatBox.gSize.x * 0.90f;
        lines++;
        int bottom = 0;

        Render.setFont(ChatBox.textFont);

        int counter = 1;
        while (counter < fMsg.length()) {
            counter++;

            if (Render.getStringWidth(fMsg.substring(bottom, counter)) > maxWidth && counter < fMsg.length()) {
                for (int i = counter; i >= bottom; i--) {
                    if (fMsg.charAt(i) == ' ') {
                        fMsg = fMsg.substring(0, i) + "/n" + fMsg.substring(i + 1);
                        bottom = i + 2;
                        lines++;
                        counter += 2;
                        break;
                    } else if (i == bottom) {
                        int e = counter - 2;
                        fMsg = fMsg.substring(0, e) + "-/n" + fMsg.substring(e);
                        lines++;
                        counter += 3;
                        bottom = e + 3;
                        break;
                    }
                }
            }
        }

        return fMsg;
    }

    public String removePlayerTag(String message) {
        String ret = "";

        if (message.charAt(0) != '[')
            return message;

        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == ']') {
                ret = message.substring(i + 2);
            }
        }

        return ret;
    }

    public int getHeight() {
        Render.setFont(ChatBox.textFont);
        return lines * Render.getStringHeight();
    }

    public static int getMessageIndexLine(int line) {
        int c = 0;

        for (int i = 0; i < ChatBox.messages.size(); i++) {
            c += ChatBox.messages.get(c).lines;

            if (c >= line)
                return i;
        }

        System.err.println("That message outreaches the limit for the text box!");
        System.err.println("Message Line: " + line);

        return -1;
    }

    public static int heightToIndex(float height) {
        int t = 0;

        for (int i = 0; i < ChatBox.messages.size(); i++) {
            t += ChatBox.messages.get(i).getHeight() + ChatBox.getPadding();
            if (t > height)
                return i;
        }

        System.err.println("That height is out of bounds!");

        return -1;
    }

    public static int heightTo(int index) {
        int total = 0;

        for (int i = 0; i < index; i++) {
            total += ChatBox.messages.get(i).getHeight();
            total += ChatBox.getPadding();
        }

        return total;
    }

    public static int height() {
        return heightTo(ChatBox.messages.size());
    }

    public static int lineheight() {
        int total = 0;

        for (int i = 0; i < ChatBox.messages.size(); i++) {
            total += ChatBox.messages.get(i).lines;
        }

        return total;
    }
}
