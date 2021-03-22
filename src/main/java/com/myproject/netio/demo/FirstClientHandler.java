package com.myproject.netio.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 链接成功后调用：向服务端写数据
     * @param ctx
     */
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println(new Date() + ": 客户端写出数据");
//
//        // 1. 获取数据
//        ByteBuf buffer = getByteBuf(ctx);
//
//        // 2. 写数据到服务器端
//        ctx.channel().writeAndFlush(buffer);
//    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好，闪电侠!".getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }

    /**
     * 接收到服务端的数据
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }
}
