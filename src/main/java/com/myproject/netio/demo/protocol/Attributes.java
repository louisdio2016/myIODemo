package com.myproject.netio.demo.protocol;

import io.netty.util.AttributeKey;

/**
 * 放入channel中的标志
 */
public interface Attributes {

    /**
     * 登录成功标志
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
