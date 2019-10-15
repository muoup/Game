package com.Game.GUI.Chatbox;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.SkillPopup.SkillPopup;
import com.Game.Items.ItemList;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Object.SkillingAreas.Tree;
import com.Game.Object.SkillingAreas.TreePreset;
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
        boolean nullParam = false;

        if (parameters == null) {
            parameters = new String[0];
            nullParam = true;
        }

        switch(command.toLowerCase()) {
            case "pos":
                ChatBox.sendMessage(Main.player.position.toString());
                break;
            case "tp":
                if (parameters.length == 2 || parameters.length == 3) {
                    Vector2 tp = new Vector2(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
                    ChatBox.sendMessage("Teleported to: " + tp.toString());
                    if (parameters.length == 3) {
                        World.changeWorld(Integer.parseInt(parameters[2].trim()));
                        ChatBox.sendMessage("Change Subworld");
                    }
                    Main.player.position = tp.clone();
                } else {
                    ChatBox.sendMessage("Correct parameters are (x, y) or (x, y, subWorld)");
                }
                break;
            case "tpmouse":
                ChatBox.sendMessage("Teleported.");
                Vector2 tp = Input.mousePosition.add(World.curWorld.offset);
                break;
            case "resetworld":
                World.curWorld.resetWorld();
                break;
            case "newtree":
                if (!nullParam) {
                    int x = (int) Main.player.position.x;
                    int y = (int) Main.player.position.y;
                    String type = parameters[0];

                    switch(type) {
                        case "log":
                            new Tree(x, y, TreePreset.tree);
                            System.out.println("new Tree(" + x + ", " + y + ", Tree.log);");
                            break;
                        case "maple":
                            new Tree(x, y, TreePreset.mapleTree);
                            System.out.println("new Tree(" + x + ", " + y + ", Tree.maple);");
                            break;
                        default:
                            ChatBox.sendMessage("That is not a valid tree type!");
                    }
                }
                break;
            case "setskill":
                if (parameters.length >= 2) {
                    int skill = Integer.parseInt(parameters[0]);
                    int level = Integer.parseInt(parameters[1]);
                    Skills.setLevel(skill, level);
                }
                break;
            case "disconnect":
                Main.client.disconnect();
                break;
            case "item":
                if (parameters.length >= 2) {
                    int item = Integer.parseInt(parameters[0]);
                    int amount = Integer.parseInt(parameters[1]);

                    InventoryManager.addItem(ItemList.values()[item], amount);
                }
                break;
            case "testpopup":
                new SkillPopup(1, 1);
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
