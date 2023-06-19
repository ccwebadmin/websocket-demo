package com.cc.websocketdemo.java;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cc
 * @date 2023/6/10-16:27
 */

/**
 * 监听websocket地址
 */
@Component
@ServerEndpoint("/ws")
@Slf4j
public class WsServerEndpoint {

    /**
     * 线程安全 存session信息
     */
    public static Map<String, Session> map = new ConcurrentHashMap<>();


    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        map.put(session.getId(), session);
        log.info("建立websocket连接");
    }

    /**
     * 收到客户端信息
     *
     * @param text
     * @return
     */
    @OnMessage
    public String OnMessage(String text) {
        log.info("接收信息：" + text);
        return "服务器返回";
    }

    /**
     * 客户端关闭
     * @param session
     */
    @OnClose
    public void OnClose(Session session) {
        map.remove(session.getId());
        log.info("websocket 已关闭");
    }

    /**
     * spring 提供的定时任务实现
     * 每隔两秒向客户端发消息
     *
     * @return
     */
    @Scheduled(fixedRate = 2000)
    public void checkHits() throws IOException {

        for (String key : map.keySet()) {
            map.get(key).getBasicRemote().sendText("心跳");
        }
    }
}
