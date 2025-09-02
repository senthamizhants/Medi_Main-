package com.example.lablink.service.runner;

import com.example.lablink.domain.ConnectionConfig;
import com.example.lablink.domain.ProtocolType;
import com.example.lablink.service.protocol.AstmProtocol;
import com.example.lablink.service.protocol.Protocol;
import com.example.lablink.service.protocol.RawProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientRunner implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientRunner.class);

    private final ConnectionConfig cfg;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public ClientRunner(ConnectionConfig cfg) {
        this.cfg = cfg;
    }

    public void stop() { running.set(false); }

    private Protocol protocol() {
        if (cfg.getProtocol() == ProtocolType.ASTM) return new AstmProtocol();
        return new RawProtocol();
    }

    @Override
    public void run() {
        while (running.get()) {
            try (Socket socket = new Socket(cfg.getHost(), cfg.getPort())) {
                socket.setSoTimeout(15000);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                Protocol p = protocol();
                boolean ok = p.handshake(in, out, false);
                log.info("Handshake {} at {}", ok ? "OK" : "FAILED", Instant.now());
                if (!ok) {
                    Thread.sleep(2000);
                    continue;
                }
                // Demo send alive message periodically
                int counter = 0;
                while (running.get()) {
                    String msg = "PING-" + counter++ + "\n";
                    p.send(out, msg);
                    // Try to read echo (if server echoes)
                    byte[] buf = new byte[2048];
                    int n = in.read(buf);
                    if (n == -1) break;
                    String reply = new String(buf, 0, n);
                    log.debug("Received: {}", reply.trim());
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                log.warn("Client connection error: {}", e.getMessage());
                try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        log.info("Client stopped for cfg {}", cfg.getId());
    }
}
