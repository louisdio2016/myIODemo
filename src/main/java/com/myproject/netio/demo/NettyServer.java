package com.myproject.netio.demo;

import com.myproject.netio.demo.codec.PacketDecoder;
import com.myproject.netio.demo.codec.PacketEncoder;
import com.myproject.netio.demo.handler.LoginRequestHandler;
import com.myproject.netio.demo.handler.MessageRequestHandler;
import com.myproject.netio.demo.handler.ServerHandler;
import com.myproject.netio.demo.protocol.Spliter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {
    private static final int PORT = 8000;

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                .group(boss, worker)//boss表示监听链接线程组、worker表示处理每一条链接的数据逻辑的线程组
                .channel(NioServerSocketChannel.class)//指定IO模型
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {//需要实现的抽象方法
//                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));//长度域拆包器
                        ch.pipeline().addLast(new Spliter());//自定义拆包器，继承长度域拆包器，增加了对其他协议的过滤
                        ch.pipeline().addLast(new PacketDecoder());//解析二进制文件
                        ch.pipeline().addLast(new LoginRequestHandler());//1.接收客户端发送的登录数据，返回登录响应
                        ch.pipeline().addLast(new MessageRequestHandler());//1.接收客户端发送的登录数据，返回登录响应
                        ch.pipeline().addLast(new PacketEncoder());//1.接收客户端发送的登录数据，返回登录响应
                    }
                });
//                .bind(8000);//绑定8000端口
        bind(serverBootstrap,PORT);
    }


    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }
}
