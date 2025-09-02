package com.example.ServerClientApp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ASTMMessageHandler {

    public void processIncomingASTM(String message, DataOutputStream output, String clientIP) throws IOException {
        if (isASTMMessage(message)) {
            System.out.println("🔬 ASTM Message Handling");

            // Example response for ENQ
            if (message.contains("\u0005")) {
                System.out.println("📥 Server replied: <ACK>");
                output.write("\u0006".getBytes(StandardCharsets.US_ASCII)); // ACK
                output.flush();
                return;
            }

            // Basic frame echo simulation
            if (message.startsWith("\u0002") && message.contains("\u0003")) {
                System.out.println("📤 Sending back ASTM frame");
                output.write(message.getBytes(StandardCharsets.US_ASCII)); // Echo back
                output.flush();
            }

            // You can expand with real parsing/ASTM block handling if needed
        } else {
            System.out.println("💬 Plain message received from " + clientIP + ": " + message.trim());

            String response = "🧾 Echo from server: " + message.trim();
            output.write(response.getBytes(StandardCharsets.US_ASCII));
            output.flush();
        }
    }

    private boolean isASTMMessage(String message) {
        return message.contains("\u0002") || message.contains("\u0005") || message.contains("|");
    }
}
