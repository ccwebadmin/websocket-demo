package com.cc.websocketdemo.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpoint;

/**
 * @author cc
 * @date 2023/6/17-16:28
 */
@Configuration
public class WebSockConfig {

    /**
     * 将依赖包里的一些类注入进来为一个bean
     * @return
     */

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
