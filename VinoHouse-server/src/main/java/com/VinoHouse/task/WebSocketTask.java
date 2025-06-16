package com.VinoHouse.task;

import com.VinoHouse.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebSocketTask {

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 通过 WebSocket 每隔 5 秒向客户端发送消息
     */
    //@Scheduled(cron = "0/5 * * * * ?")  // 要注释掉，不然“来单提醒”会一直响
    public void sendMessageToClient() {
        webSocketServer.sendToAllClient("这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }
}
