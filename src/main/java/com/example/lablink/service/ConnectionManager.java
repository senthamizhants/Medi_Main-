package com.example.lablink.service;

import com.example.lablink.domain.ConnectionConfig;
import com.example.lablink.domain.ConnectionMode;
import com.example.lablink.service.runner.ClientRunner;
import com.example.lablink.service.runner.ServerRunner;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ConnectionManager {

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final Map<Long, Runnable> runners = new ConcurrentHashMap<>();

    public void start(ConnectionConfig cfg) {
        stop(cfg.getId());
        if (cfg.getMode() == ConnectionMode.SERVER) {
            ServerRunner runner = new ServerRunner(cfg);
            runners.put(cfg.getId(), runner);
            pool.submit(runner);
        } else {
            ClientRunner runner = new ClientRunner(cfg);
            runners.put(cfg.getId(), runner);
            pool.submit(runner);
        }
    }

    public void stop(Long id) {
        Runnable r = runners.remove(id);
        if (r instanceof ServerRunner sr) {
            sr.stop();
        } else if (r instanceof ClientRunner cr) {
            cr.stop();
        }
    }

    public Map<Long, Runnable> list() {
        return runners;
    }
}
