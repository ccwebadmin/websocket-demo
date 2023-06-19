package com.cc.websocketdemo.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * 配置类
 * 启用webSocket配置
 *
 * @author cc
 * @date 2023/6/19-15:02
 */
@Configuration
@EnableWebSocket
public class MyWsConfig implements WebSocketConfigurer {

    @Resource
    MyWsHandler myWsHandler;

    @Resource
    MyHttpSessionHandshakeInterceptor myHttpSessionHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWsHandler, "/ws1").addInterceptors(myHttpSessionHandshakeInterceptor).setAllowedOrigins("*");
    }
}
