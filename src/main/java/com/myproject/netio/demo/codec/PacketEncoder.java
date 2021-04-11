package com.myproject.netio.demo.codec;

import com.myproject.netio.demo.protocol.Packet;
import com.myproject.netio.demo.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 继承MessageToByteEncoder后，只要将对象写入到out中即可
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        System.out.println("PacketEncoder.encode     packet:"+packet.toString());
        PacketCodeC.INSTANCE.encode(out, packet);
    }
}
