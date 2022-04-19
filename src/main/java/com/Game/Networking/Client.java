package com.Game.Networking;

import com.Game.Entity.Player;
import com.Game.GUI.Banking.BankingHandler;
import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUI;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.GUI.TextBox;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Game.Main.MenuHandler;
import com.Game.Main.MethodHandler;
import com.Game.Projectile.HomingProjectile;
import com.Game.Projectile.Projectile;
import com.Game.Questing.QuestManager;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;
import com.Util.Other.SoundHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;

public class Client {

    public enum Error {
        NONE, INVALID_HOST, SOCKET_EXCEPTION
    }

    private String ipAddress;
    private int port;
    private boolean listening = false;
    private byte[] dataBuffer = new byte[4096];
    private static final String clientVersion = "0.0.2a";

    private Error errorCode = Error.NONE;

    private InetAddress serverAddress;

    private DatagramSocket socket;

    private Thread listenerThread;

    public Client(String host) {
        String[] parts = host.split(":");

        if (parts.length != 2) {
            errorCode = Error.INVALID_HOST;
            return;
        }

        this.ipAddress = parts[0];

        try {
            this.port = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            errorCode = Error.INVALID_HOST;
            return;
        }
    }

    public Client(String host, int port) {
        this.ipAddress = host;
        this.port = port;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void listen() {
        while (listening) {
            DatagramPacket packet = new DatagramPacket(dataBuffer, dataBuffer.length);

            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            process(packet);
        }
    }

    private void process(DatagramPacket packet) {
        byte[] data = packet.getData();
        String content = new String(data);
        String start = content.substring(0, 2);
        String message = content.substring(2).trim();
        String[] index;

        switch (start) {
            case "00":
                index = message.split(":");
                Settings.questCount = Integer.parseInt(index[0]);
                QuestManager.init();
                // index[1] is the skill count if ever needed.
                break;
            case "02":
                index = message.split(":");
                if (message.charAt(0) == 'p') {
                    JOptionPane.showMessageDialog(null, "That player is already logged in. Wait a second and retry again.");
                    return;
                } else if (message.charAt(0) == 'v') {
                    JOptionPane.showMessageDialog(null, "You are running the wrong client version! Please restart your client.");
                    return;
                }
                boolean c = message.charAt(1) == 'c';
                if (message.charAt(0) == 'l') {
                    if (c) {
                        joinServer(index[1]);
                    } else {
                        JOptionPane.showMessageDialog(null, "The username and password is not recognized.");
                    }
                } else if (message.charAt(0) == 'r') {
                    if (c) {
                        joinServer(index[1]);
                    } else {
                        JOptionPane.showMessageDialog(null, "That username already exists!");
                    }
                } else {
                    System.err.println("An unexpected login message occured.");
                }
                break;
            case "04":
                takeSkillData(message.split(":"));
                break;
            case "07":
                takeQuestData(message.split(":"));
                break;
            case "12":
                String[] parts = message.split(":");
                MethodHandler.playerConnections.add(new PlayerObject(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2]));
                break;
            case "13":
            case "55":
            case "me":
                // Message is received
                ChatBox.sendMessage(message);
                break;
            case "te":
                TextBox.addText(message);
                break;
            case "ch":
                TextBox.setChoices(message);
                break;
            case "15":
                String[] movement = message.split(":");
                for (PlayerObject o : MethodHandler.playerConnections) {
                    if (o.getUsername().equals(movement[0].trim())) {
                        o.setPos(Integer.parseInt(movement[1]), Integer.parseInt(movement[2]));
                        o.setImage(Player.getAnimation(movement[4]));
                        break;
                    }
                }
                break;
            case "76":
                send("76" + Player.name);
                break;
            case "66":
                for (PlayerObject o : MethodHandler.playerConnections) {
                    if (o.getUsername().equals(message)) {
                        MethodHandler.playerConnections.remove(o);
                        break;
                    }
                }
                break;
            case "99":
                Main.logout();
                break;
            case "wc": // World Change
                World.setWorld(message);
                break;
            case "pl": // Remove Player
                World.removePlayer(message);
                break;
            case "ob": // Spawn Objects
                World.spawnObject(message);
                break;
            case "ns": // NPC Spawn
                World.spawnNPC(message);
                break;
            case "ne": // New Enemy
                World.spawnEnemy(message);
                break;
            case "eu": // Enemy Movement
                World.updateEnemy(message);
                break;
            case "gi": // Ground Item(s)
                World.spawnGroundItems(message);
                break;
            case "gu": // Grounditem Update
                World.updateGroundItem(message);
                break;
            case "gr": // Grounditem Remove
                World.removeGroundItem(message);
                break;
            case "ou": // Object Update
                World.updateObject(message);
                break;
            case "cc": // Perform the Chicken Check
                World.chickenCheck(message);
                break;
            case "pp": // Perform the Projectile Ponderance
                World.projectilePonderance(message);
                break;
            case "uu": // User Uxamination
                Player.playerTest(message);
                break;
            case "pc": // Position Change
                index = message.split(";");
                Player.position = new Vector2(Integer.parseInt(index[0]), Integer.parseInt(index[1]));
                break;
            case "in": // Inventory Change
                index = message.split(";");
                ItemData inventory = InventoryManager.inventory[Integer.parseInt(index[0])];
                inventory.interpretPacketData(index);
                break;
            case "ac": // Accessory Change
                index = message.split(";");
                ItemData accessory = AccessoriesManager.accessories[Integer.parseInt(index[0])];
                accessory.interpretPacketData(index);
                break;
            case "oi": // Object Interaction
                Player.interactionFinish = Long.parseLong(message);
                Player.interactionStart = System.currentTimeMillis();
                break;
            case "lf": // Lose Focus
                Player.interactionFinish = 0;
                Player.interactionStart = 0;
                break;
            case "ui": // User Interface
                GUI.openGUI(message);
                break;
            case "bc": // Bank Change
                BankingHandler.handleChange(message);
                break;
            case "sk": // Set Skill
                Skills.handleChange(message);
                break;
            case "ps": // Projectile Spawn
                Projectile.spawn(message);
                break;
            case "hs": // Homing Projectile Spawn
                HomingProjectile.spawn(message);
                break;
            case "pd": // Projectile Destroy
                Projectile.destroy(Integer.parseInt(message));
                break;
            case "ph": // Player Health
                index = message.split(";");
                Player.health = Float.parseFloat(index[0]);
                Player.maxHealth = Float.parseFloat(index[1]);
                break;
            case "so": // Sound
                SoundHandler.playSound(message);
                break;
            case "sc":
                System.out.println(message);
                Player.changeSprite(message);
                break;
            default:
                System.err.println("Unhandled server input: " + start + "\n" + content);
        }

        dataBuffer = new byte[4096];
    }

    public boolean connect(String username, String password, int connectionCode) {
        try {
            serverAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            errorCode = Error.INVALID_HOST;
            return false;
        }

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            errorCode = Error.SOCKET_EXCEPTION;
            return false;
        }
        send(Main.connectionCode + username + ":" + password + ":" + connectionCode + ":" + clientVersion);
        listening = true;

        listenerThread = new Thread(this::listen);
        listenerThread.start();
        return true;
    }

    public void takeSkillData(String[] index) {
        // 0 - Username; 1-? -> skills;
        Player.name = index[0];

        for (int i = 1; i < index.length; i++) {
            Skills.setExperience(i - 1, Float.parseFloat(index[i]), false);
        }
    }

    public void takeAccData(String[] index) {
        for (int i = 1; i < index.length; i++) {
            String[] itemData = index[i].split(" ");
            AccessoriesManager.clientSetItem(i - 1,
                    itemData[0], itemData[1], itemData[2]);
        }
    }

    private void takeQuestData(String[] index) {
        int id = Integer.parseInt(index[1]);

        QuestManager.setName(id, index[2]);
        QuestManager.setColor(id, new Color(Integer.parseInt(index[3])));
        QuestManager.setClue(id, index[4]);
    }

    public void joinServer(String username) {
        ChatBox.tag = "[" + Player.name + "]: ";

        Main.isConnected = true;
    }

    public void disconnect() {
        send("55" + Player.name);
        Settings.disablePause();
        MenuHandler.setState(MenuHandler.MenuState.NoPause);
        AccessoriesManager.init();
        ChatBox.messages.clear();
        Main.logout();
    }

    public void send(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        send(message.getBytes());
    }

    public Error getErrorCode() {
        return errorCode;
    }
}
