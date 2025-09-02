package com.example.ServerClientApp;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerSocketHandler implements Runnable {
    private final Socket clientSocket;
    private final ASTMMessageHandler astmHandler = new ASTMMessageHandler();

    public ServerSocketHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        String clientIP = clientSocket.getInetAddress().getHostAddress();

        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

            ClientRegistry.addClient(clientIP, clientSocket, output);
            System.out.println("✅ Client connected: " + clientIP);

            while (true) {
                try {
                    String command = input.readUTF(); // read command
                    switch (command) {
                        case "SEND" -> {
                            String message = input.readUTF();
                            System.out.println("💬 Message from " + clientIP + ": " + message);
                            if (isASTMMessage(message)) {
                                astmHandler.processIncomingASTM(message, output, clientIP);
                            } else {
                                output.writeUTF("Echo from Server: " + message);
                            }
                        }

                        case "SEND_FILE" -> {
                            int fileCount = input.readInt();
                            System.out.println("📦 Receiving " + fileCount + " file(s) from " + clientIP);

                            for (int i = 0; i < fileCount; i++) {
                                String filename = input.readUTF();
                                long fileSize = input.readLong();
                                byte[] fileData = new byte[(int) fileSize];
                                input.readFully(fileData);

                                Path savePath = Paths.get("server_received_" + filename);
                                Files.write(savePath, fileData);
                                System.out.println("📥 Received file: " + filename + " (" + fileSize + " bytes)");
                            }
                        }

                        default -> {
                            System.out.println("❓ Unknown command from " + clientIP + ": " + command);
                        }
                    }
                } catch (EOFException eof) {
                    System.out.println("🔌 Client disconnected: " + clientIP);
                    break;
                } catch (IOException e) {
                    System.err.println("❌ Error with client " + clientIP + ": " + e.getMessage());
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Connection error with client " + clientIP + ": " + e.getMessage());
        } finally {
            ClientRegistry.removeClient(clientIP);
        }
    }

    private boolean isASTMMessage(String msg) {
        return msg.contains("" + (char) 0x02) && msg.contains("" + (char) 0x03);
    }
}
