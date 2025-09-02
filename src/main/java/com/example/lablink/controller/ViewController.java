package com.example.lablink.controller;

import com.example.lablink.dto.ClientForm;
import com.example.lablink.dto.ServerForm;
import com.example.lablink.domain.ProtocolType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/tcpip")
    public String index(Model model) {
        model.addAttribute("serverForm", new ServerForm());
        model.addAttribute("clientForm", new ClientForm());
        model.addAttribute("protocolTypes", ProtocolType.values());
        return "index";
    }
}
