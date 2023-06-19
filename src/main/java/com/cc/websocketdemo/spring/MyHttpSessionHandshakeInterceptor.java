package com.cc.websocketdemo.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author cc
 * @date 2023/6/17-16:49
 */
@Component
@Slf4j
public class MyHttpSessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info(request.getRemoteAddress().toString()+":开始握手");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        //完成握手后，http协议升级为websocket协议
        log.info(request.getRemoteAddress().toString()+":握手完成");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
