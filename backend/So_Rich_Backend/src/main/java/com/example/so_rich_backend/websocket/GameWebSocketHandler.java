package com.example.so_rich_backend.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏 WebSocket 处理器
 * 负责处理游戏内的实时通信：掷骰子、移动、交易、聊天等
 */
@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(GameWebSocketHandler.class);

    /**
     * roomId -> (playerId -> session)
     */
    private final Map<String, Map<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = getRoomId(session);
        String playerId = getPlayerId(session);

        roomSessions.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>()).put(playerId, session);

        log.info("玩家 {} 加入房间 {}, 当前房间人数: {}",
                playerId, roomId, roomSessions.get(roomId).size());

        // 通知房间内其他玩家
        JSONObject msg = new JSONObject();
        msg.put("type", "PLAYER_JOIN");
        msg.put("playerId", playerId);
        msg.put("playerCount", roomSessions.get(roomId).size());
        broadcastToRoom(roomId, playerId, msg);

        // 给当前玩家发送连接成功消息
        JSONObject welcome = new JSONObject();
        welcome.put("type", "CONNECTED");
        welcome.put("playerId", playerId);
        welcome.put("roomId", roomId);
        sendMessage(session, welcome);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String roomId = getRoomId(session);
        String playerId = getPlayerId(session);

        try {
            JSONObject msg = JSON.parseObject(message.getPayload());
            String type = msg.getString("type");

            log.debug("收到消息 - 房间:{}, 玩家:{}, 类型:{}", roomId, playerId, type);

            switch (type) {
                case "ROLL_DICE":
                    handleRollDice(roomId, playerId, session, msg);
                    break;
                case "BUY_PROPERTY":
                case "PAY_RENT":
                case "USE_CARD":
                case "END_TURN":
                case "CHAT":
                case "TRADE_OFFER":
                case "TRADE_RESPONSE":
                    // 默认广播给房间内所有玩家
                    msg.put("playerId", playerId);
                    broadcastToRoom(roomId, null, msg);
                    break;
                default:
                    JSONObject error = new JSONObject();
                    error.put("type", "ERROR");
                    error.put("message", "未知消息类型: " + type);
                    sendMessage(session, error);
            }
        } catch (Exception e) {
            log.error("处理消息异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理掷骰子
     */
    private void handleRollDice(String roomId, String playerId, WebSocketSession session, JSONObject msg) {
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;

        JSONObject response = new JSONObject();
        response.put("type", "DICE_RESULT");
        response.put("playerId", playerId);
        response.put("dice1", dice1);
        response.put("dice2", dice2);
        response.put("total", dice1 + dice2);
        response.put("isDouble", dice1 == dice2);

        broadcastToRoom(roomId, null, response);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomId = getRoomId(session);
        String playerId = getPlayerId(session);

        Map<String, WebSocketSession> players = roomSessions.get(roomId);
        if (players != null) {
            players.remove(playerId);
            if (players.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }

        log.info("玩家 {} 离开房间 {}", playerId, roomId);

        JSONObject msg = new JSONObject();
        msg.put("type", "PLAYER_LEAVE");
        msg.put("playerId", playerId);
        msg.put("playerCount", players == null ? 0 : players.size());
        broadcastToRoom(roomId, playerId, msg);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("WebSocket 传输错误: {}", exception.getMessage());
    }

    // ==================== 辅助方法 ====================

    /**
     * 广播消息给房间内所有玩家
     * @param excludePlayerId 排除的玩家（可选）
     */
    private void broadcastToRoom(String roomId, String excludePlayerId, JSONObject message) {
        Map<String, WebSocketSession> players = roomSessions.get(roomId);
        if (players == null) return;

        String payload = message.toJSONString();
        players.forEach((pid, wsSession) -> {
            if (excludePlayerId == null || !excludePlayerId.equals(pid)) {
                try {
                    if (wsSession.isOpen()) {
                        wsSession.sendMessage(new TextMessage(payload));
                    }
                } catch (IOException e) {
                    log.error("发送消息失败: {}", e.getMessage());
                }
            }
        });
    }

    private void sendMessage(WebSocketSession session, JSONObject message) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message.toJSONString()));
            }
        } catch (IOException e) {
            log.error("发送消息失败: {}", e.getMessage());
        }
    }

    private String getRoomId(WebSocketSession session) {
        return (String) session.getAttributes().get("roomId");
    }

    private String getPlayerId(WebSocketSession session) {
        return (String) session.getAttributes().get("playerId");
    }

    /**
     * 获取房间当前人数
     */
    public int getRoomPlayerCount(String roomId) {
        Map<String, WebSocketSession> players = roomSessions.get(roomId);
        return players == null ? 0 : players.size();
    }
}
