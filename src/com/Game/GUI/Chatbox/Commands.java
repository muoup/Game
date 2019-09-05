package com.Game.GUI.Chatbox;

import com.Game.Main.Main;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;

import java.util.ArrayList;

public class Commands {
    public static void onCommand(Message message) {
        String msg = message.rawMessage;
        String command;
        String[] parameters;

        for (int i = 2; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                command = msg.substring(2, i);
                parameters = getParameters(msg.substring(i + 1));
                runCommand(command.trim(), parameters);
                return;
            } else if (i == msg.length() - 1) {
                command = msg.substring(2);
                runCommand(command.trim(), null);
                return;
            }
        }
    }

    private static void runCommand(String command, String[] parameters) {
        System.out.println("COMMAND: " + command);

        if (parameters == null) {
            parameters = new String[0];
        }

        switch(command.toLowerCase()) {
            case "pos":
                ChatBox.sendMessage(Main.player.position.toString());
                break;
            case "tp":
                if (parameters.length == 2) {
                    Vector2 tp = new Vector2(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
                    ChatBox.sendMessage("Teleported to: " + tp.toString());
                    Main.player.position = tp.clone();
                } else {
                    ChatBox.sendMessage("Correct parameters are (x, y)");
                }
                break;
            case "tpmouse":
                ChatBox.sendMessage("Teleported.");
                Vector2 tp = Input.mousePosition.add(World.curWorld.offset);
                break;
            case "resetworld":
                World.curWorld.initWorld();
                break;
            default:
                ChatBox.sendMessage("That is not a valid command, please check your spelling and try again.");
        }
    }

    private static String[] getParameters(String rawParams) {
        ArrayList<String> parameters = new ArrayList<String>();

        int b = 0;

        for (int i = 0; i < rawParams.length(); i++) {
            if (rawParams.charAt(i) == ' ') {
                parameters.add(rawParams.substring(b, i).trim());
                b = i + 1;
            }
            if (i == rawParams.length() - 1) {
                parameters.add(rawParams.substring(b).trim());
            }
        }

        return parameters.toArray(new String[0]);
    }
}
