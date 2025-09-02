package com.example.ServerClientApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class ClientApiController {

    @GetMapping("/clients")
    public Set<String> getConnectedClients() {
        return ClientRegistry.getClientSockets().keySet();
    }
}
