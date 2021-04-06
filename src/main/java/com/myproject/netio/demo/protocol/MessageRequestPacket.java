package com.myproject.netio.demo.protocol;

import static com.myproject.netio.demo.protocol.Command.MESSAGE_REQUEST;

public class MessageRequestPacket extends Packet {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
