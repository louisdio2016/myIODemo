package com.myproject.netio.demo;

import com.myproject.netio.demo.codec.PacketDecoder;
import com.myproject.netio.demo.codec.PacketEncoder;
import com.myproject.netio.demo.handler.ClientHandler;
import com.myproject.netio.demo.handler.LoginResponseHandler;
import com.myproject.netio.demo.handler.MessageResponseHandler;
import com.myproject.netio.demo.protocol.MessageRequestPacket;
import com.myproject.netio.demo.protocol.PacketCodeC;
import com.myproject.netio.demo.protocol.Spliter;
import com.myproject.netio.demo.utils.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
//                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(new ClientHandler());//向逻辑处理链中添加逻辑处理器
//                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));//长度域拆包器
                        ch.pipeline().addLast(new Spliter());//自定义拆包器，继承长度域拆包器，增加了对其他协议的过滤
                        ch.pipeline().addLast(new PacketDecoder());//解析二进制文件
                        ch.pipeline().addLast(new LoginResponseHandler());//1.连接成功后发送登录请求;2.获取服务端发送的登录响应
                        ch.pipeline().addLast(new MessageResponseHandler());//1.连接成功后发送登录请求;2.获取服务端发送的登录响应
                        ch.pipeline().addLast(new PacketEncoder());//1.连接成功后发送登录请求;2.获取服务端发送的登录响应
                    }
                });

//        Channel channel = bootstrap.connect("127.0.0.1", 1000).channel();
//
//        while (true) {
//            channel.writeAndFlush(new Date() + ": hello world!");
//            Thread.sleep(2000);
//        }
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {//链接成功后获取命令行中的输入，写到服务端
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);//开启新的线程接收控制台输入
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    /**
     * 获取命令行
     * @param channel
     */
    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {//检查中断标志是否为true，被中断后不再执行
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
//                    ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channel.alloc(), packet);
                    channel.writeAndFlush(packet);//自动进行二进制编码
                }
            }
        }).start();
    }
}
