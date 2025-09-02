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
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerRunner implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ServerRunner.class);

    private final ConnectionConfig cfg;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public ServerRunner(ConnectionConfig cfg) {
        this.cfg = cfg;
    }

    public void stop() {
        running.set(false);
    }

    private Protocol protocol() {
        if (cfg.getProtocol() == ProtocolType.ASTM) return new AstmProtocol();
        return new RawProtocol();
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(cfg.getPort())) {
            log.info("Server [{}:{}] listening... protocol={}", cfg.getHost(), cfg.getPort(), cfg.getProtocol());
            while (running.get()) {
                try (Socket client = server.accept()) {
                    client.setSoTimeout(15000);
                    InputStream in = client.getInputStream();
                    OutputStream out = client.getOutputStream();
                    Protocol p = protocol();
                    boolean ok = p.handshake(in, out, true);
                    log.info("Handshake {} at {}", ok ? "OK" : "FAILED", Instant.now());
                    // Echo loop (demo): read and echo back
                    if (ok) {
                        byte[] buf = new byte[2048];
                        int n;
                        while ((n = in.read(buf)) != -1) {
                            out.write(buf, 0, n); // echo back
                            out.flush();
                        }
                    }
                } catch (Exception ex) {
                    log.warn("Client session ended: {}", ex.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Server error: {}", e.getMessage(), e);
        }
        log.info("Server stopped for cfg {}", cfg.getId());
    }
}
