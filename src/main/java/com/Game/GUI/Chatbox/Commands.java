package com.Game.GUI.Chatbox;

import com.Game.Entity.Player;
import com.Game.Main.Main;
import com.Util.Other.Settings;

public class Commands {
    public static void onCommand(Message message) {
        if (clientSideCommand(message))
            return;

        Main.sendPacket("cm" + Player.name + ";" + message.rawMessage);
    }

    public static boolean clientSideCommand(Message command) {
        String string = command.rawMessage.substring(2);
        String[] parts = string.split(" ");
        String commandFormat = "";

        try {
            switch (parts[0].toLowerCase()) {
                case "setamt":
                case "setamount":
                    commandFormat = "::setamount <amount>";
                    if (parts.length < 2)
                        return true;

                    int amount = Integer.parseInt(parts[1]);
                    if (amount <= 0)
                        return true;

                    Settings.customAmount = amount;
                    return true;
            }
        } catch (IllegalArgumentException e) {
            ChatBox.sendMessage("Invalid command format.");
            ChatBox.sendMessage(commandFormat);
        }

        return false;
    }
}
