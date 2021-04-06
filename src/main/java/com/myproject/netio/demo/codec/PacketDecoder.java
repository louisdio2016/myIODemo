package com.myproject.netio.demo.codec;

import com.myproject.netio.demo.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ByteToMessageDecoder:将二进制数据转换为java对象
 * 采用ByteToMessageDecoder可以自动释放ByteBuf，避免内存泄露
 */
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        out.add(PacketCodeC.INSTANCE.decode(in));//list中添加解码后的对象，即可向后面的handler传递
    }
}
