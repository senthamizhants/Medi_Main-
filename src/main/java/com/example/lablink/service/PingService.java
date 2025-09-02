package com.example.lablink.service;

import com.example.lablink.dto.PingResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

@Service
public class PingService {

    public PingResponse ping(String ip) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String cmd;
            if (os.contains("win")) {
                cmd = String.format("ping -n 1 -w 2000 %s", ip);
            } else {
                cmd = String.format("ping -c 1 -W 2 %s", ip);
            }
            Instant start = Instant.now();
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line).append('\n');
            }
            int code = p.waitFor();
            long ms = Duration.between(start, Instant.now()).toMillis();
            boolean reachable = code == 0;
            String message = reachable ? "Ping success" : "Ping failed";
            return new PingResponse(reachable, message, ms);
        } catch (Exception e) {
            return new PingResponse(false, "Ping error: " + e.getMessage(), -1);
        }
    }
}
