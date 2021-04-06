package com.myproject.netio.demo.protocol;

public interface Command {
    Byte LOGIN_REQUEST = 1;//登录请求
    Byte LOGIN_RESPONSE = 2;//登录响应
    Byte MESSAGE_REQUEST = 3;//消息请求
    Byte MESSAGE_RESPONSE = 4;//消息响应
}
