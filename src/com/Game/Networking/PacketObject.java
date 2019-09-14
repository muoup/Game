package com.Game.Networking;

import java.io.Serializable;

public class PacketObject implements Serializable {
    private String message;

    public PacketObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
