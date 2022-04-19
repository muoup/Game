package com.Game.GUI.Chatbox;

import com.Game.Entity.Player;
import com.Game.Main.Main;

public class Commands {
    public static void onCommand(Message message) {
        Main.sendPacket("cm" + Player.name + ";" + message.rawMessage);
    }
}
