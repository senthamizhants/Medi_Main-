package com.example.ServerClientApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ServerClientController {
    private static volatile boolean clientConnected = false;
    private Socket serverSideSocket;
    private Socket clientSocket;
    private DataOutputStream serverOut;
    private DataInputStream serverIn;
    private DataOutputStream clientOut;
    private DataInputStream clientIn;

    //private final Map<String, Socket> clientSockets = Collections.synchronizedMap(new HashMap<>());
    //private final Map<String, DataOutputStream> clientOutputs = Collections.synchronizedMap(new HashMap<>());
    //private final Map<Socket, DataOutputStream> clientMap = Collections.synchronizedMap(new HashMap<>());

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port > 0 && port <= 65535;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/config")
    public String showConfigPage(Model model) {
        model.addAttribute("mode", "server");
        return "config";
    }

    @PostMapping("/server/test-connection")
    public String testServer(@RequestParam Map<String, String> params, Model model) {
        String ip = params.get("ip");
        String port = params.get("port");

        if (isEmpty(ip) || isEmpty(port) || !isValidPort(port)) {
            model.addAttribute("error", "IP and valid Port required.");
            model.addAttribute("mode", "server");
            model.addAllAttributes(params);
            return "config";
        }

        model.addAttribute("message", "Listening...");
        model.addAllAttributes(params);
        model.addAttribute("mode", "server");

        // 🗺️ Global maps for tracking clients
        Map<String, Socket> clientSockets = new ConcurrentHashMap<>();
        Map<String, DataOutputStream> clientOutputs = new ConcurrentHashMap<>();

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.bind(new InetSocketAddress(ip, Integer.parseInt(port)));
                System.out.println("🟢 Server started on " + ip + ":" + port);

                while (true) {
                    Socket socket = serverSocket.accept();
                    String clientIP = socket.getInetAddress().getHostAddress();
                    System.out.println("✅ Client connected from: " + clientIP);

                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    ClientRegistry.addClient(clientIP, socket, out);

                    // Start new thread to handle this client using our handler
                    Thread clientHandler = new Thread(
                    	    new ServerSocketHandler(socket)
                    	);
                    	clientHandler.start();
                }
            } catch (IOException e) {
                System.err.println("❌ Server error: " + e.getMessage());
            }
        }).start();

        // Sleep to allow thread startup feedback
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (clientConnected) {
            model.addAttribute("message", "✅ Client connected!");
            clientConnected = false;
        }
        return "config";
    }

    @PostMapping("/client/test-connection")
    public String testClient(@RequestParam Map<String, String> params, Model model) {
        String host = params.get("clientHostIp");
        String port = params.get("clientPort");

        if (isEmpty(host) || isEmpty(port) || !isValidPort(port)) {
            model.addAttribute("error", "Host IP and valid Port required.");
            model.addAttribute("mode", "client");
            model.addAllAttributes(params);
            return "config";
        }
        
        try {
            clientSocket = new Socket(host, Integer.parseInt(port));
            clientOut = new DataOutputStream(clientSocket.getOutputStream());
            clientIn = new DataInputStream(clientSocket.getInputStream());

            model.addAttribute("message", "✅ Connected to server.");

            // ✅ Start receiving messages
            new Thread(() -> {
                try {
                    while (true) {
                        String response = clientIn.readUTF();  // match server's writeUTF
                        System.out.println("📥 Received from server: " + describeControlChars(response));
                    }
                } catch (IOException e) {
                    System.err.println("❌ Client receive error: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            model.addAttribute("error", "❌ Connection failed: " + e.getMessage());
        }

        model.addAttribute("mode", "client");
        model.addAllAttributes(params);
        return "config";
    }

    private String describeControlChars(String s) {
        StringBuilder out = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case 0x00 -> out.append("<NUL>");
                case 0x01 -> out.append("<SOH>");
                case 0x02 -> out.append("<STX>");
                case 0x03 -> out.append("<ETX>");
                case 0x04 -> out.append("<EOT>");
                case 0x05 -> out.append("<ENQ>");
                case 0x06 -> out.append("<ACK>");
                case 0x0D -> out.append("<CR>");
                case 0x0A -> out.append("<LF>");
                default -> {
                    if (Character.isISOControl(c)) {
                        out.append(String.format("<0x%02X>", (int)c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        return out.toString();
    }
  
    @PostMapping("/client/send-files")
    public String sendFromClient(@RequestParam("filePaths") String filePaths, Model model) {
        model.addAttribute("mode", "client");

        if (clientOut == null || clientSocket == null || clientSocket.isClosed()) {
            model.addAttribute("error", "❌ No active connection.");
            return "config";
        }

        try {
            String[] files = filePaths.split(",");
            System.out.println("📤 [Client] Sending command: SEND_FILE");
            clientOut.writeUTF("SEND_FILE");
            clientOut.writeInt(files.length);

            for (String raw : files) {
                Path path = Paths.get(raw.trim());
                if (!Files.exists(path)) {
                    System.out.println("⚠️ [Client] Skipping non-existent: " + path);
                    continue;
                }

                String name = path.getFileName().toString();
                byte[] bytes = Files.readAllBytes(path);

                System.out.println("📤 [Client] Sending file: " + name + " (" + bytes.length + " bytes)");

                clientOut.writeUTF(name);
                clientOut.writeLong(bytes.length);
                clientOut.write(bytes);
                clientOut.flush();
            }

            model.addAttribute("message", "✅ Files sent to server.");
        } catch (IOException e) {
            System.err.println("❌ [Client] Sending files failed: " + e.getMessage());
            model.addAttribute("error", "❌ Sending failed: " + e.getMessage());
        }

        return "config";
    }

    private void receiveFiles(DataInputStream dis, String prefix) throws IOException {
        int fileCount = dis.readInt();
        for (int i = 0; i < fileCount; i++) {
            String name = dis.readUTF();
            long length = dis.readLong();
            Path path = Paths.get(prefix + name);
            try (OutputStream out = Files.newOutputStream(path)) {
                byte[] buf = new byte[4096];
                long rem = length;
                while (rem > 0) {
                    int read = dis.read(buf, 0, (int) Math.min(buf.length, rem));
                    if (read == -1) break;
                    out.write(buf, 0, read);
                    rem -= read;
                }
            }
            System.out.println("✅ Server received: " + name);
        }
    }

    private void sendFiles(DataOutputStream dos, List<String> files) throws IOException {
        dos.writeInt(files.size());
        for (String raw : files) {
            Path path = Paths.get(raw.trim());
            if (!Files.exists(path)) continue;

            String name = path.getFileName().toString();
            byte[] bytes = Files.readAllBytes(path);

            dos.writeUTF(name);
            dos.writeLong(bytes.length);
            dos.write(bytes);
            dos.flush();

            System.out.println("📤 Server sent: " + name);
        }
    }
    
    @PostMapping("/server/ping-client")
    public String pingClient(@RequestParam("ClientIp") String ip, Model model) {
        if (isEmpty(ip)) {
            model.addAttribute("error", "Client IP is required for ping.");
        } else {
            try {
                boolean success = InetAddress.getByName(ip).isReachable(2000);
                model.addAttribute("message", success ? "Ping Success" : "Ping Failed");
            } catch (IOException e) {
                model.addAttribute("error", "Ping error: " + e.getMessage());
            }
        }
        model.addAttribute("mode", "server");
        model.addAttribute("clientIp", ip);
        return "config";
    }

    @PostMapping("/client/ping-server")
    public String pingServer(@RequestParam("pingIp") String ip, Model model) {
        if (isEmpty(ip)) {
            model.addAttribute("error", "Server IP is required for ping.");
        } else {
            try {
                boolean success = InetAddress.getByName(ip).isReachable(2000);
                model.addAttribute("message", success ? "Ping Success" : "Ping Failed");
            } catch (IOException e) {
                model.addAttribute("error", "Ping error: " + e.getMessage());
            }
        }
        model.addAttribute("mode", "client");
        model.addAttribute("pingIp", ip);
        return "config";
    }

    @PostMapping("/server/save-config")
    public String saveServer(@RequestParam Map<String, String> params, Model model) {
        String ip = params.get("ip");
        String port = params.get("port");
        String machineId = params.get("machineId");
        String machineName = params.get("serverName");
        String username = params.get("dbUser");
        String password = params.get("dbPass");
        String dbType = params.get("dbType");

        if (isEmpty(ip) || isEmpty(port) || isEmpty(machineId) || isEmpty(machineName)) {
            model.addAttribute("error", "Missing required fields to save.");
        } else {
            String content = new Date() + ": Server Saved -> " + String.join(", ",
                    ip, port, machineId, machineName, username, password, dbType);
            try {
                Files.write(
                    Paths.get("server_config.txt"),
                    (content + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,                    
                    StandardOpenOption.APPEND
                );
                model.addAttribute("message", "Server configuration saved.");
               
            } catch (IOException e) {
                model.addAttribute("error", "Failed to save config: " + e.getMessage());
            }
        }

        model.addAttribute("mode", "server");
        model.addAllAttributes(params);
        return "config";
    }

    @PostMapping("/client/save-config")
    public String saveClient(@RequestParam Map<String, String> params, Model model) {
        String host = params.get("clientHostIp");
        String port = params.get("clientPort");
        String machineId = params.get("clientMachineId");
        String machineName = params.get("clientMachineName");
        String username = params.get("clientUser");
        String password = params.get("clientPass");
        String dbType = params.get("clientDbType");

        if (isEmpty(host) || isEmpty(port) || isEmpty(machineId) || isEmpty(machineName)) {
            model.addAttribute("error", "Missing required fields to save.");
        } else {
            String content = new Date() + ": Client Saved -> " + String.join(", ",
                    host, port, machineId, machineName, username, password, dbType);
            try {
                Files.write(
                    Paths.get("client_config.txt"),
                    (content + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
                );
                model.addAttribute("message", "Client configuration saved.");
            } catch (IOException e) {
                model.addAttribute("error", "Failed to save config: " + e.getMessage());
            }
        }

        model.addAttribute("mode", "client");
        model.addAllAttributes(params);
        return "config";
    }
    
    @PostMapping("/client/send-data")
    public String sendDataFromClient(@RequestParam("filePaths") String inputText,
                                     @RequestParam Map<String, String> params,
                                     Model model) {
        model.addAttribute("mode", "client");

        if (clientSocket == null || clientSocket.isClosed()) {
            model.addAttribute("error", "❌ Client not connected to server.");
            return "config";
        }

        try {
            // Check if input is comma-separated list of actual files
            String[] entries = inputText.split(",");
            List<File> validFiles = new ArrayList<>();

            for (String entry : entries) {
                File f = new File(entry.trim());
                if (f.exists() && f.isFile()) {
                    validFiles.add(f);
                }
            }

            if (!validFiles.isEmpty()) {
                // ========== SEND FILES ==========
                clientOut.writeUTF("SEND_FILE");
                clientOut.writeInt(validFiles.size());

                for (File f : validFiles) {
                    clientOut.writeUTF(f.getName());
                    clientOut.writeLong(f.length());

                    byte[] fileBytes = Files.readAllBytes(f.toPath());
                    clientOut.write(fileBytes);
                }

                clientOut.flush();
                model.addAttribute("message", "✅ File(s) sent to server.");

            } else {
                // ========== SEND MESSAGE ==========
                clientOut.writeUTF("SEND");
                clientOut.writeUTF(inputText);  // this is the actual message
                clientOut.flush();
                model.addAttribute("message", "✅ Message sent to server.");
            }

        } catch (IOException e) {
            System.err.println("❌ [Client] Error: " + e.getMessage());
            model.addAttribute("error", "❌ Error sending: " + e.getMessage());
        }

        model.addAllAttributes(params);
        return "config";
    }

    
   /* private void handleClient(Socket socket, String clientIp) {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            while (true) {
                String command = in.readUTF();
                if ("SEND".equals(command)) {
                    receiveFiles(in, "received_from_" + clientIp + "_");
                } else if ("MSG".equals(command)) {
                    String msg = in.readUTF();
                    System.out.println("📨 Message from " + clientIp + ": " + msg);
                } else if ("EXIT".equals(command)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Client " + clientIp + " disconnected.");
        } finally {
            clientSockets.remove(clientIp);
            clientOutputs.remove(clientIp);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }*/
     
    @GetMapping("/server")
    public String showServerPage(Model model) {
        // Assuming you're storing this map somewhere globally or injected
    	Set<String> connectedClients = ClientRegistry.getConnectedClientIPs();  // or wherever your map is
        model.addAttribute("connectedClients", connectedClients);
        return "server"; // your HTML or JSP page name
    }
    
 /*  @PostMapping("/server/send-message")
    public String sendToClient(@RequestParam("clientMessage") String message,
                               @RequestParam("selectedClientIp") String clientIp,
                               @RequestParam Map<String, String> params,
                               Model model) {
        model.addAttribute("mode", "server");

        if (clientIp == null || clientIp.isEmpty()) {
            model.addAttribute("error", "⚠️ No client selected.");
            return "config";
        }

        try {
            DataOutputStream out = ClientRegistry.getOutputStream(clientIp);

            if (out == null) {
                model.addAttribute("error", "❌ Could not find connection for client: " + clientIp);
                return "config";
            }

            if (message != null && !message.trim().isEmpty()) {
                System.out.println("📤 [Server] Sending to " + clientIp + ": " + message);

                out.writeUTF("SEND");       // command
                out.writeUTF(message);      // actual message
                out.flush();

                model.addAttribute("message", "✅ Message sent to client " + clientIp);
            } else {
                model.addAttribute("error", "⚠️ Cannot send empty message.");
            }

        } catch (IOException e) {
            System.err.println("❌ Error sending to client " + clientIp + ": " + e.getMessage());
            model.addAttribute("error", "❌ Failed to send message: " + e.getMessage());
        }

        model.addAllAttributes(params);
        return "config";
    }*/

     
    @PostMapping("/client/send-message")
    public String sendMessageFromClient(@RequestParam("clientMessage") String message,
                                        @RequestParam Map<String, String> params,
                                        Model model) {
        model.addAttribute("mode", "client");

        if (clientSocket == null || clientSocket.isClosed()) {
            model.addAttribute("error", "❌ Client not connected to server.");
            return "config";
        }

        try {
            if (message != null && !message.trim().isEmpty()) {
                System.out.println("📤 [Client] Sending command: SEND");
                clientOut.writeUTF("SEND");

                System.out.println("📤 [Client] Sending message: " + message);     
                clientOut.writeUTF(message);     // actual message second
                clientOut.flush();

                model.addAttribute("message", "✅ Message sent to server.");
            } else {
                model.addAttribute("error", "⚠️ Cannot send empty message.");
            }
        } catch (IOException e) {
            System.err.println("❌ [Client] Error sending message: " + e.getMessage());
            model.addAttribute("error", "❌ Error sending message: " + e.getMessage());
        }

        model.addAllAttributes(params);
        return "config";
    }

	public String performConnectionTest(String ip, int port) {
		// TODO Auto-generated method stub
		return null;
	}
		
	private void log(String tag, String msg) {
	    System.out.println("[" + tag + "] " + msg);
	}		
	
	@PostMapping("/server/send-file")
	public String sendToClient(@RequestParam("filePaths") String filePaths,
	                           @RequestParam("selectedClient") String clientIP,
	                           @RequestParam Map<String, String> params,
	                           Model model) {
	    model.addAttribute("mode", "server");

	    if (filePaths == null || filePaths.trim().isEmpty()) {
	        model.addAttribute("error", "⚠️ No file path or message provided.");
	        return "config";
	    }

	    if (clientIP == null || clientIP.isEmpty()) {
	        model.addAttribute("error", "⚠️ No client selected.");
	        return "config";
	    }

	    DataOutputStream out = ClientRegistry.getOutputStream(clientIP);
	    if (out == null) {
	        model.addAttribute("error", "❌ Client not connected: " + clientIP);
	        return "config";
	    }

	    String[] paths = filePaths.split(",");

	    try {
	        for (String path : paths) {
	            path = path.trim();
	            File file = new File(path);

	            if (file.exists() && file.isFile()) {
	                // 🔁 Send as file
	                out.writeUTF("SEND_FILE");
	                out.writeInt(1); // one file

	                out.writeUTF(file.getName());
	                out.writeLong(file.length());

	                byte[] fileData = Files.readAllBytes(file.toPath());
	                out.write(fileData);
	                out.flush();

	                System.out.println("📤 [Server] Sent file: " + file.getName() + " to " + clientIP);
	            } else {
	                // 💬 Send as plain text message
	                out.writeUTF("SEND");
	                out.writeUTF(path); // actual message
	                out.flush();

	                System.out.println("💬 [Server] Sent message: \"" + path + "\" to " + clientIP);
	            }
	        }

	        model.addAttribute("message", "✅ File(s) or message(s) sent to client: " + clientIP);

	    } catch (IOException e) {
	        e.printStackTrace();
	        model.addAttribute("error", "❌ Error sending to client: " + e.getMessage());
	    }

	    model.addAllAttributes(params);
	    return "config";
	}

	
}