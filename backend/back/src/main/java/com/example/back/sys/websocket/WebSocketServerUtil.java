package com.example.back.sys.websocket;

import com.example.back.sys.entity.Player;
import com.example.back.sys.service.IPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket/{playerName}")
@Slf4j
public class WebSocketServerUtil {

    private Session session;
    private static CopyOnWriteArraySet<WebSocketServerUtil > webSocketSet = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<Long,WebSocketServerUtil > webSocketMap  = new ConcurrentHashMap<>();
    private Long uId = null;
    @Autowired
    private IPlayerService playerService;

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketServerUtil.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("playerName") String playerName){

        playerService = applicationContext.getBean(IPlayerService.class);

        Player player = playerService.findByPlayerName(playerName);
        playerService.setLogin(player.getPlayerID());

        uId = Long.valueOf(player.getPlayerID());
        this.session = session;
        if(webSocketMap .containsKey(uId)){
            webSocketMap .remove(uId);
            webSocketMap .put(uId,this);
        }else{
            webSocketMap .put(uId,this);
            webSocketSet.add(this);
        }

        log.info("【websocket消息】有新的连接，总数：{}",webSocketMap.size());
    }

    @OnClose
    public void onClose(){
        if(webSocketMap.containsKey(uId)){
            webSocketMap.remove(uId);
            //从set中删除
            webSocketSet.remove(this);
        }
        log.info("【websocket消息】连接断开，总数：{}",webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("【websocket消息】收到客户端发来的消息：{}",message);
    }

    public void sendMessage(String message){
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,Long uId) throws Exception {
        //log.info("发送消息到:"+uId+"，报文:"+message);

        if(webSocketMap.containsKey(uId)){
            webSocketMap.get(uId).sendMessage(message);
        }else{
            log.error("用户"+uId+",不在线！");
            throw new Exception("连接已关闭，请刷新页面后重试");
        }

    }
}
