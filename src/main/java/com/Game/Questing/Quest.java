package com.Game.Questing;

import com.Game.GUI.Chatbox.ChatBox;

import java.awt.*;

public class Quest {

    public Color color = Color.ORANGE;
    public String clue, name;

    public Quest() {
        clue = "";
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public void printClue() {
        ChatBox.sendMessage(clue);
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
