package com.cc.websocketdemo.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket处理程序，监听连接前，连接中，连接后
 *
 * @author cc
 * @date 2023/6/19-10:41
 */
@Component
@Slf4j
public class MyWsHandler extends AbstractWebSocketHandler {
    private static Map<String, SessionBean> sessionBeanMap;

    //线程安全的integer
    private static AtomicInteger atomicInteger;

    //群聊信息 线程安全
    private static StringBuffer stringBuffer;

    static {
        sessionBeanMap = new ConcurrentHashMap<>();
        atomicInteger = new AtomicInteger(0);
        stringBuffer = new StringBuffer();
    }

    //连接建立
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        //getAndIncrement 自增 并返回自增的值
        SessionBean sessionBean = new SessionBean(session, atomicInteger.getAndIncrement());
        sessionBeanMap.put(session.getId(), sessionBean);
        log.info(sessionBean.getMakerId() + ":建立了链接");
        stringBuffer.append(sessionBeanMap.get(session.getId()).getMakerId() + "：进入了群聊<br>");
        sendMsssage();
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    /**
     * 收到消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.info(sessionBeanMap.get(session.getId()).getMakerId() + ":" + message.getPayload());
        stringBuffer.append(sessionBeanMap.get(session.getId()).getMakerId() + "：" + message.getPayload()+"<br>");
        sendMsssage();
    }

    /**
     * 传输过程异常
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        if (session.isOpen()) {
            session.close();
        }
        sessionBeanMap.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info(sessionBeanMap.get(session.getId()).getMakerId() + ":断开了链接");
        stringBuffer.append(sessionBeanMap.get(session.getId()).getMakerId() + "：离开了群聊<br>");
        sessionBeanMap.remove(session.getId());
        sendMsssage();
    }

    //给客户端群发消息
    public void sendMsssage() throws IOException {
        for (String id : sessionBeanMap.keySet()) {
            sessionBeanMap.get(id).getSession().sendMessage(new TextMessage(stringBuffer.toString()));
        }
    }

    /**
     * spring 提供的定时任务实现
     * 每隔两秒向客户端发消息
     *
     * @return
     */
//    @Scheduled(fixedRate = 2000)
//    public void checkHits() throws IOException {
//
//        for (String key : sessionBeanMap.keySet()) {
//            sessionBeanMap.get(key).getSession().sendMessage(new TextMessage("心跳"));
//        }
//    }
}
