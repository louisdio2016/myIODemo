package com.myproject.netio.demo.handler;

import com.myproject.netio.demo.protocol.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1.获取ByteBuf

        //2.解码成java对象

        //3.处理登录校验

        //4.构建登录相应对象

        //5.编码成ByteBuf

        //6.写入客户端

        ByteBuf requestByteBuf = (ByteBuf) msg;
        System.out.println("服务端接收到客户端数据");
        System.out.println("正在解析...");
        // 解码
        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);



        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            System.out.println("收到客户端登录请求");
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());

            // 登录校验
            if (valid(loginRequestPacket)) {
                // 校验成功
                loginResponsePacket.setSuccess(true);
                System.out.println("成功登录");
            } else {
                // 校验失败
                System.out.println("登录失败");
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
            }

            // 编码
//            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
//            ctx.channel().writeAndFlush(responseByteBuf);
        }else if(packet instanceof MessageRequestPacket){
            // 处理消息
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
//            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
//            ctx.channel().writeAndFlush(responseByteBuf);
        }

    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
