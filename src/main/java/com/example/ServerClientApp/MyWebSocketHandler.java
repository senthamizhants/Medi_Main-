package com.example.ServerClientApp;

import java.net.InetAddress;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

  
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();

        if (msg.startsWith("testServerConnection|")) {
            String[] parts = msg.split("\\|");
            String ip = parts[1];
            int port = Integer.parseInt(parts[2]);
            ServerClientController controller = new ServerClientController();
            String result = controller.performConnectionTest(ip, port);
            session.sendMessage(new TextMessage(result));
        } else if (msg.startsWith("pingClient|")) {
            String[] parts = msg.split("\\|");
            boolean reachable = InetAddress.getByName(parts[1]).isReachable(2000);
            session.sendMessage(new TextMessage(reachable ? "✅ Ping OK" : "❌ Ping Failed"));
        } else {
            session.sendMessage(new TextMessage("❓ Unknown command"));
        }
    }
}
