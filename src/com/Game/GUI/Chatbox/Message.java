package com.Game.GUI.Chatbox;

import java.awt.*;

public class Message {
    // I had a ton of comments for this file but my laptop died so it was lost.
    // Considering I am going to be the only person reading this, it really is not too big of a deal.
    public String message;
    public Color color;

    public Message(String message, Color color) {
        this.message = message;
        this.color = color;
    }

    public String toString() {
        return message + " " + color.toString();
    }
}
