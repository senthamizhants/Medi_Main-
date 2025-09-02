package com.example.ServerClientApp;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRegistry {

    private static final Map<String, Socket> clientSockets = new ConcurrentHashMap<>();
    private static final Map<String, DataOutputStream> clientOutputs = new ConcurrentHashMap<>();

    public static void addClient(String ip, Socket socket, DataOutputStream out) {
        clientSockets.put(ip, socket);
        clientOutputs.put(ip, out);
    }

    public static void removeClient(String ip) {
        clientSockets.remove(ip);
        clientOutputs.remove(ip);
    }
    
    public static DataOutputStream getOutputStream(String ip) {
        return clientOutputs.get(ip);
    }

    public static Socket getSocket(String ip) {
        return clientSockets.get(ip);
    }

    public static Map<String, Socket> getClientSockets() {
        return clientSockets;
    }

    public static Map<String, DataOutputStream> getClientOutputs() {
        return clientOutputs;
    }

    public static Set<String> getConnectedClientIPs() {
        return clientSockets.keySet();
    }
}
