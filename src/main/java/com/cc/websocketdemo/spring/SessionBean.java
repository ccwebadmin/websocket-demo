package com.cc.websocketdemo.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author cc
 * @date 2023/6/19-10:44
 */
//@Data 不包含构造函数
@Data
@AllArgsConstructor
public class SessionBean {
    private WebSocketSession session;
    private Integer makerId;
}
