package com.myproject.netio.demo.protocol;

public abstract class Packet {
    private Byte version = 1;
    public abstract Byte getCommand();
}
