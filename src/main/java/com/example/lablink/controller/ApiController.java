package com.example.lablink.controller;

import com.example.lablink.dto.*;
import com.example.lablink.domain.*;
import com.example.lablink.repo.ConnectionConfigRepository;
import com.example.lablink.service.ConnectionManager;
import com.example.lablink.service.PingService;
import com.example.lablink.service.protocol.AstmProtocol;
import com.example.lablink.service.protocol.Protocol;
import com.example.lablink.service.protocol.RawProtocol;
import jakarta.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PingService pingService;
    private final ConnectionConfigRepository repo;
    private final ConnectionManager manager;

    public ApiController(PingService pingService, ConnectionConfigRepository repo, ConnectionManager manager) {
        this.pingService = pingService;
        this.repo = repo;
        this.manager = manager;
    }

    @PostMapping(value = "/ping", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public PingResponse ping(@RequestParam("ip") String ip) {
        return pingService.ping(ip);
    }

    @PostMapping(value = "/server/test", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> serverTest(@Valid ServerForm form, BindingResult br, HttpSession session) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(new TestResponse(false, "Validation failed for server form.", null));
        }
        StringBuilder log = new StringBuilder();
        try {
            Protocol p = form.getProtocolType() == ProtocolType.ASTM ? new AstmProtocol() : new RawProtocol();
            try (ServerSocket server = new ServerSocket(form.getPort())) {
                log.append("[").append(Instant.now()).append("] Listening on port ").append(form.getPort()).append("\n");
                // Accept one client for test
                server.setSoTimeout(15000);
                try (Socket client = server.accept()) {
                    client.setSoTimeout(10000);
                    InputStream in = client.getInputStream();
                    OutputStream out = client.getOutputStream();
                    boolean ok = p.handshake(in, out, true);
                    log.append("Handshake ").append(ok ? "OK" : "FAILED").append("\n");
                    String token = ok ? UUID.randomUUID().toString() : null;
                    if (ok) session.setAttribute("serverTestToken", token);
                    return ResponseEntity.ok(new TestResponse(ok, log.toString(), token));
                }
            }
        } catch (Exception e) {
            log.append("Error: ").append(e.getMessage());
            return ResponseEntity.ok(new TestResponse(false, log.toString(), null));
        }
    }

    @PostMapping(value = "/client/test", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> clientTest(@Valid ClientForm form, BindingResult br, HttpSession session) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(new TestResponse(false, "Validation failed for client form.", null));
        }
        StringBuilder log = new StringBuilder();
        try {
            Protocol p = form.getProtocolType() == ProtocolType.ASTM ? new AstmProtocol() : new RawProtocol();
            try (Socket socket = new Socket(form.getHostIp(), form.getPort())) {
                socket.setSoTimeout(10000);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                boolean ok = p.handshake(in, out, false);
                log.append("[").append(Instant.now()).append("] Connected to ").append(form.getHostIp()).append(":").append(form.getPort()).append("\n");
                log.append("Handshake ").append(ok ? "OK" : "FAILED").append("\n");
                String token = ok ? UUID.randomUUID().toString() : null;
                if (ok) session.setAttribute("clientTestToken", token);
                return ResponseEntity.ok(new TestResponse(ok, log.toString(), token));
            }
        } catch (Exception e) {
            log.append("Error: ").append(e.getMessage());
            return ResponseEntity.ok(new TestResponse(false, log.toString(), null));
        }
    }

    @PostMapping(value = "/server/save", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> saveServer(@Valid ServerForm form, BindingResult br, HttpSession session) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "Validation failed for server form."));
        }
        if (session.getAttribute("serverTestToken") == null) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "Run Test Connection first for the server."));
        }
        ConnectionConfig cfg = new ConnectionConfig();
        cfg.setMode(ConnectionMode.SERVER);
        cfg.setHost(form.getServerIp());
        cfg.setPort(form.getPort());
        cfg.setMachineName(form.getName());
        cfg.setMachineId(form.getMachineId());
        cfg.setDbType(form.getDatabaseType());
        cfg.setDbCredentials(form.getDbCredentials());
        cfg.setProtocol(form.getProtocolType());
        cfg.setStatus("RUNNING");
        cfg = repo.save(cfg);
        manager.start(cfg);
        session.removeAttribute("serverTestToken");
        return ResponseEntity.ok(Map.of("ok", true, "message", "Server configuration saved and started.", "id", cfg.getId()));
    }

    @PostMapping(value = "/client/save", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> saveClient(@Valid ClientForm form, BindingResult br, HttpSession session) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "Validation failed for client form."));
        }
        if (session.getAttribute("clientTestToken") == null) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "Run Test Connection first for the client."));
        }
        ConnectionConfig cfg = new ConnectionConfig();
        cfg.setMode(ConnectionMode.CLIENT);
        cfg.setHost(form.getHostIp());
        cfg.setPort(form.getPort());
        cfg.setMachineName(form.getMachineName());
        cfg.setMachineId(form.getMachineId());
        cfg.setDbType(form.getDatabaseType());
        cfg.setDbCredentials(form.getDbCredentials());
        cfg.setProtocol(form.getProtocolType());
        cfg.setStatus("RUNNING");
        cfg = repo.save(cfg);
        manager.start(cfg);
        session.removeAttribute("clientTestToken");
        return ResponseEntity.ok(Map.of("ok", true, "message", "Client configuration saved and started.", "id", cfg.getId()));
    }

    @GetMapping("/connections")
    public Map<String, Object> connections() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("runningIds", manager.list().keySet());
        resp.put("all", repo.findAll());
        return resp;
    }
}
